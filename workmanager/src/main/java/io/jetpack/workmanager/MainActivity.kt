package io.jetpack.workmanager

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkManager
import com.android.jetpack.workmanager.works.TestWorker
import io.jetpack.workmanager.base.BaseActivity
import io.jetpack.workmanager.databinding.ActivityMainBinding
import io.jetpack.workmanager.demo.jobs.MainJobActivity
import io.jetpack.workmanager.demo.works.WorkActivity
import io.jetpack.workmanager.utils.viewBinding
import io.jetpack.workmanager.video.MyWorkActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : BaseActivity() {

    private val mBinding by viewBinding(ActivityMainBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.acBtnGoJob.setOnClickListener {
            startActivity(Intent(this, MainJobActivity::class.java))
        }

        mBinding.acBtnGoWork.setOnClickListener {
            startActivity(Intent(this, WorkActivity::class.java))
        }

        mBinding.acBtnGoMyWork.setOnClickListener {
            startActivity(Intent(this, MyWorkActivity::class.java))
        }

        val api = fun(userName: String): String {
            return "哈哈哈 $userName"
        }

//        val aPiService = Api.APiService {
//            return@APiService "回调信息：name= $it, age= 18, sex= 男"
//        }
//
//        getService(aPiService)


        val notifyLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                MyUniqueWork.start(this)
                mCountDownTimer.start()
//            test()
//            load()
            }
        }
        mBinding.acBtnGoTest.setOnClickListener {
            notifyLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        WorkManager.getInstance(this).getWorkInfosByTagLiveData(MyUniqueWork::class.java.simpleName)
            .observe(this) {
                it.forEach { workInfo ->
                    if (BuildConfig.DEBUG) {
                        Log.i("print_logs", "MyUniqueWorker状态: ${workInfo.state}")
                    }
                }
            }


        TestWorker.setWorkListener(this)


    }

    private fun test() {
        for (i in 1..3) {
//            TestWorker.start(this)
            MyUniqueWork.start(this)
        }

//        lifecycleScope.launchWhenCreated {
//            delay(6000L)
//            TestWorker.stop(this@MainActivity)
//        }
    }

//    private fun getService(service: Api.APiService) {
//        Log.i("print_logs", "MainActivity::onCreate: ${service.getUser("你好")}")
//    }

    private fun load() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "MainActivity::load: start")
            }
            delay(2000L)
            if (BuildConfig.DEBUG) {
                Log.d("print_logs", "MainActivity::load: end")
            }

            cancel("取消了")

            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "MainActivity::load: ....")
            }
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            if (BuildConfig.DEBUG) {
//                Log.i("print_logs", "MainActivity::load: start")
//            }
//            delay(2000L)
//            if (BuildConfig.DEBUG) {
//                Log.d("print_logs", "MainActivity::load: end")
//            }
//
//            cancel("取消了")
//
//            if (BuildConfig.DEBUG) {
//                Log.i("print_logs", "MainActivity::load: ....")
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
//        mCountDownTimer.start()
    }

    override fun onStop() {
        super.onStop()
//        mCountDownTimer.cancel()
    }

    //24小时倒计时
    private val mCountDownTimer = object : CountDownTimer(15 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val hours = (millisUntilFinished / (1000 * 60 * 60)).toInt()
            val minutes = ((millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)).toInt()
            val seconds = ((millisUntilFinished % (1000 * 60)) / 1000).toInt()

            mBinding.acTvTimer.text = String.format(Locale.CHINA, "%02d:%02d:%02d", hours, minutes, seconds)
        }

        override fun onFinish() {
            mBinding.acTvTimer.text = "倒计时结束."
        }
    }

    override fun onDestroy() {
        mCountDownTimer.cancel()
        super.onDestroy()
    }
}
