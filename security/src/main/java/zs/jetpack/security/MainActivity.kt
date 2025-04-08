package zs.jetpack.security

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import zs.jetpack.security.databinding.ActivityMainBinding
import java.io.File
import java.io.FileNotFoundException


/**
 * 安全地管理密钥并对文件和 sharedpreferences 进行加密。
 *
 * Jetpack 安全加密库已被废弃
 * @author zhangshuai
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    private lateinit var readWritePermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var managerFilesPermissionsLauncher: ActivityResultLauncher<Intent>

    private val mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + "/JetpackSecurity.txt"

    /**
     * setKeyScheme()和setKeyGenParameterSpec()二选一
     */
    private val mEncryptedFile :EncryptedFile by lazy {
        EncryptedFile.Builder(
            this@MainActivity,
            File(mFilePath),
            MasterKey.Builder(this@MainActivity)
//                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .setUserAuthenticationRequired(true,10 * 60)
                .setRequestStrongBoxBacked(true)
                .setKeyGenParameterSpec(
                    KeyGenParameterSpec.Builder(MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                        .build()
                )
                .build(),  // 生成主密钥
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //Android11以下版本
        readWritePermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                Log.i("print_logs", "onCreate:uri= $it")
                it?.apply {
                    encryptFile()
                }
            }

        //Android11以上版本
        managerFilesPermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        encryptFile()
                    } else {
                        Toast.makeText(this, "授权失败！1", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        mDataBinding.acBtnEncrypt.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    encryptFile()
                }else{
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    managerFilesPermissionsLauncher.launch(intent)
                }
            } else {
                readWritePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }


        mDataBinding.acBtnDecrypt.setOnClickListener {
            decryptFile()
        }
    }

    /**
     * 编码
     */
    private fun encryptFile() {
        try {
            //先读取原有内容并与新内容合并后再写入
            if (File(mFilePath).exists()) {

            }


            mEncryptedFile.openFileOutput().use {
                val content = "我是写入的数据：${System.currentTimeMillis()}".toByteArray(Charsets.UTF_8)
                it.write(content)
                it.flush()
            }
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "MainActivity::encryptFile: 加密成功！")
            }
        }catch (e:FileNotFoundException){

        } catch (e: Exception) {
            e.printStackTrace()
            if (BuildConfig.DEBUG) {
                Log.e("print_logs", "encryptFile: $e")
            }
            File(mFilePath).apply {
                if (this.exists()) {
                    this.delete()
                }
            }
            encryptFile()
            decryptFile()
        }
    }

    /**
     * 解码
     */
    private fun decryptFile() {
        try {
            mEncryptedFile.openFileInput().use {
                Log.d("print_logs", "decryptFile: ${String(it.readBytes(), Charsets.UTF_8)}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (BuildConfig.DEBUG) {
                Log.e("print_logs", "decryptFile: $e")
            }
            Toast.makeText(this, "解码失败！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}