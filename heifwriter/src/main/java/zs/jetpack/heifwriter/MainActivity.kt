package zs.jetpack.heifwriter

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.heifwriter.HeifWriter
import java.io.File

/**
 * 使用 Android 设备上可用的编解码器，以 HEIF 格式对图像或图像集进行编码。
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var imgPath = ""
        assets.list("img")?.forEach {
            imgPath = it
        }

        Log.i("print_logs", "imgPath: $imgPath")

        findViewById<AppCompatButton>(R.id.acBtn).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    100
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            Log.i("print_logs", "MainActivity::onRequestPermissionsResult: ")
            val imgPath = "/storage/emulated/0/DCIM/Camera/20231212_143728.heic"

            if (File(imgPath).exists()) {
                val heifWriter =
                    HeifWriter.Builder(imgPath, 200, 200, HeifWriter.INPUT_MODE_BUFFER)
                        .setQuality(50)
                        .build()

                heifWriter.addBitmap(BitmapFactory.decodeFile(imgPath))
                heifWriter.start()
            }
        }
    }
}