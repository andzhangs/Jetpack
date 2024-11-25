package com.google.play.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.play.R

/**
 * 应用内更新
 * https://developer.android.com/guide/playcore/in-app-updates/kotlin-java?hl=zh-cn
 *
 * 测试应用内更新
 * https://support.google.com/googleplay/android-developer/answer/9844679?hl=zh-Hans#zippy=%2C%E4%B8%8A%E4%BC%A0%E5%92%8C%E5%88%86%E4%BA%AB%E5%BA%94%E7%94%A8%E4%BB%A5%E4%BE%9B%E6%B5%8B%E8%AF%95%2C%E6%B7%BB%E5%8A%A0%E8%8E%B7%E6%8E%88%E6%9D%83%E4%B8%8A%E4%BC%A0%E8%80%85
 */
class UpdateActivity : AppCompatActivity() {

    private lateinit var mAppUpdateManager: AppUpdateManager
    private lateinit var mLauncher: ActivityResultLauncher<IntentSenderRequest>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        mAppUpdateManager = AppUpdateManagerFactory.create(this)
        mAppUpdateManager.registerListener(mListener)

        mLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {

            if (it.resultCode == RESULT_OK) {
                Log.i("print_logs", "update success ")
            }else{
                Log.e("print_logs", "update failed")
            }
        }

        findViewById<AppCompatButton>(R.id.acBtn_check_new_version).setOnClickListener {
            checkUpdate()
        }
    }

    private fun checkUpdate() {
        val appUpdateInfo = mAppUpdateManager.appUpdateInfo

        //检查平台是否允许指定类型的更新。
        appUpdateInfo.addOnSuccessListener { info ->
            Log.i("print_logs", "addOnSuccessListener: ")

            //此示例应用立即更新。若要应用灵活更新，请传入 AppUpdateType.FLEXIBLE。
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && (info.clientVersionStalenessDays()
                    ?: -1) >= 7 //检查更新是否已过时, 使用 clientVersionStalenessDays() 检查自 Play 商店中提供更新以来已经过了多少天
                && info.updatePriority() >= 4 //检查更新优先级是否满足要求
                && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                //Request the update.
            }

            mAppUpdateManager.startUpdateFlowForResult(
                info,
                mLauncher,
                AppUpdateOptions                            //使用 AppUpdateOptions 配置更新
                    .newBuilder(AppUpdateType.IMMEDIATE)
                    .setAllowAssetPackDeletion(true)        // 用于指定是否允许更新操作在设备存储空间有限的情况下清除资源包。此字段默认设置为 false
                    .build()
            )
        }

        appUpdateInfo.addOnCanceledListener {
            Log.w("print_logs", "addOnCanceledListener: ")
        }
        appUpdateInfo.addOnFailureListener {
            Log.e("print_logs", "addOnFailureListener: $it")
        }

        appUpdateInfo.addOnCompleteListener {
            it.result?.let { info ->
                Log.d("print_logs", "addOnCompleteListener: ${info.totalBytesToDownload()}")
            }
        }
    }

    /**
     * 监控灵活更新状态
     *
     */
    private val mListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.UNKNOWN-> {
                Log.e("print_logs", "未知错误！")
            }

            InstallStatus.PENDING-> {
                Log.i("print_logs", "下载等待中...")
            }

            InstallStatus.DOWNLOADING-> {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()
                Log.d("print_logs", "MainActivity::onStateUpdate: ")

            }

            InstallStatus.DOWNLOADED-> {        //需要重启应用以安装更新。
                Log.i("print_logs", "下载完成.")
                popupDialogForCompleteUpdate()
            }

            InstallStatus.INSTALLING-> {
                Log.i("print_logs", "安装中...")
            }

            InstallStatus.INSTALLED-> {
                Log.i("print_logs", "安装完成.")
            }

            InstallStatus.FAILED-> {
                Log.i("print_logs", "安装失败！")
            }

            InstallStatus.CANCELED-> {
                Log.i("print_logs", "已取消下载！")
            }

            else -> {
                Log.w("print_logs", "onStateUpdate: ${state.installStatus()}")
            }
        }
    }

    private fun popupDialogForCompleteUpdate(){
        AlertDialog.Builder(this)
            .setTitle("Download completed")
            .setMessage("An update has just been downloaded.")
            .setPositiveButton("Restart"){dialog,_->
                // 当您在前台调用 appUpdateManager.completeUpdate() 时，平台会显示一个用于在后台重启应用的全屏界面。平台安装完更新后，您的应用会重启并进入其主 activity。
                // 如果您改为在应用处于后台时调用 completeUpdate()，系统会在不发出提示的情况下安装更新，而不会遮挡设备界面。
                mAppUpdateManager.completeUpdate()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        mAppUpdateManager.appUpdateInfo.addOnSuccessListener {

            // 每当用户将您的应用调到前台时，请检查您的应用是否有等待安装的更新。
            // 如果您的应用有处于 DOWNLOADED 状态的更新，请提示用户安装该更新。
            // 否则，更新数据会继续占用用户的设备存储空间。
            if (it.installStatus() == InstallStatus.DOWNLOADED) {
                popupDialogForCompleteUpdate()
            }

            // 处理立即更新
            // 在您启动立即更新且用户同意开始更新后，Google Play 会在整个更新期间在应用界面顶部显示更新进度。
            // 如果用户在更新期间关闭或终止您的应用，更新应继续在后台下载并安装，无需额外得到用户确认。
            //
            // 不过，当您的应用返回到前台时，您应确认更新未在 UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS 状态下停止。
            // 如果更新在此状态下停止，请继续更新：
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                mAppUpdateManager.startUpdateFlowForResult(
                    it,
                    mLauncher,
                    AppUpdateOptions                            //使用 AppUpdateOptions 配置更新
                        .newBuilder(AppUpdateType.IMMEDIATE)
                        .setAllowAssetPackDeletion(true)        // 用于指定是否允许更新操作在设备存储空间有限的情况下清除资源包。此字段默认设置为 false
                        .build()
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mAppUpdateManager.unregisterListener(mListener)
    }
}