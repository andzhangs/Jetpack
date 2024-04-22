package zs.jetpack.security

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import zs.jetpack.security.databinding.ActivityMainBinding
import java.io.File


/**
 * 安全地管理密钥并对文件和 sharedpreferences 进行加密。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    private lateinit var documentLauncher: ActivityResultLauncher<Array<String>>
    private var mFilePath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        documentLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            Log.i("print_logs", "MainActivity::onCreate:uri= $it")
            it?.apply {
                getPathByUri(this)?.also { filePath ->
                    Log.i("print_logs", "MainActivity::onCreate: $filePath")
                }

                encryptFile("/storage/emulated/0/Documents/HelloWorld.txt")
            }

        }

        mDataBinding.acBtnEncrypt.setOnClickListener {
            documentLauncher.launch(arrayOf("text/plain"))
        }
        mDataBinding.acBtnDecrypt.setOnClickListener {
            mFilePath?.apply { decryptFile(this) }
        }
    }

    /**
     * 编码
     */
    private fun encryptFile(path: String) {
        try {
            mFilePath = path
//            deleteEncryptedFile(path)
            val encryptedFile = getEncryptedFile(path)
            encryptedFile?.openFileOutput()?.use {
                it.write("我是写入的数据：${System.currentTimeMillis()}".toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 解码
     */
    private fun decryptFile(path: String) {
        try {

            val encryptedFile = getEncryptedFile(path)
            encryptedFile?.openFileInput()?.use {
                Log.i("print_logs", "decryptFile: ${String(it.readBytes(), Charsets.UTF_8)}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getEncryptedFile(path: String): EncryptedFile? =
        if (!File(path).exists()) {
            null
        } else {
            EncryptedFile.Builder(
                File(path),
                this,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
        }

    private fun deleteEncryptedFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun getPathByUri(uri: Uri): String? {
        return if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(MediaStore.MediaColumns.DATA)
                    if (columnIndex != -1) {
                        it.getString(columnIndex)
                    } else {
                        "1"
                    }
                } else {
                    "2"
                }
            } ?: run {
                "3"
            }
        } else {
            uri.path
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}