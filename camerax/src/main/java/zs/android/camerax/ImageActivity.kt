package zs.android.camerax

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Surface
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import zs.android.camerax.databinding.ActivityImageBinding
import java.io.File
import java.util.concurrent.Executors

class ImageActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityImageBinding
    private val mCameraProviderFuture by lazy { ProcessCameraProvider.getInstance(this) }
    private val mImageCapture by lazy {
        ImageCapture.Builder()
            .setTargetRotation(Surface.ROTATION_0)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY) //提高图片拍摄的图片质量
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)  //设置闪光灯模式
            .build()
    }
    private val mExecutor by lazy { Executors.newSingleThreadExecutor() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_image)

        val cameraProvider = mCameraProviderFuture.get()

        val preview = Preview.Builder()
            .build()
        preview.setSurfaceProvider(mDataBinding.previewView.surfaceProvider)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        val camera = cameraProvider.bindToLifecycle(
            this,
            cameraSelector,
            mImageCapture,
            preview)

        val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    load()
                }
            }
        }

        mDataBinding.acBtnOk.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    load()
                }else{
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    launcher.launch(intent)
                }
            }else{
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),100)
                }else{
                    load()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==100) {
            load()
        }
    }

    private fun load(){
        val publicPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val outsidePath=getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val outputFileOptions=ImageCapture.OutputFileOptions.Builder(File(publicPath,"IMG_${System.currentTimeMillis()}.jpeg")).build()

        //此方法将拍摄的图片保存到提供的文件位置。
        mImageCapture.takePicture(outputFileOptions,mExecutor,object :ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                if (BuildConfig.DEBUG) {
                    Log.i("print_logs", "onImageSaved: ${outputFileResults.savedUri?.path}")
                }
                runOnUiThread {
                    mDataBinding.shapeIv.setImageURI(outputFileResults.savedUri)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                if (BuildConfig.DEBUG) {
                    Log.e("print_logs", "onError: $exception")
                }
            }
        })

        //此方法为拍摄的图片提供内存缓冲区。
//        mImageCapture.takePicture(mExecutor,object :OnImageCapturedCallback(){
//            override fun onCaptureSuccess(image: ImageProxy) {
//                super.onCaptureSuccess(image)
//                if (BuildConfig.DEBUG) {
//                    Log.i("print_logs", "onCaptureSuccess: $image")
//                }
//            }
//
//            override fun onError(exception: ImageCaptureException) {
//                super.onError(exception)
//                if (BuildConfig.DEBUG) {
//                    Log.e("print_logs", "onError: #exception")
//                }
//            }
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}