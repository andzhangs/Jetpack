package zs.android.camerax

import android.Manifest
import android.content.Intent
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.video.ExperimentalVideo
import androidx.databinding.DataBindingUtil
import zs.android.camerax.databinding.ActivityMainBinding

@ExperimentalVideo /**
 * https://developer.android.google.cn/jetpack/androidx/releases/camera?hl=zh-cn#1.4.0-alpha01
 * IdleHandler的适用场景：
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding
    private val mCameraManager: CameraManager by lazy { getSystemService(CAMERA_SERVICE) as CameraManager }
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private val cameraList = mutableListOf<CameraInfoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (!it.values.contains(false)) {
                getCameraListInfo()
            }
        }.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))

        mDataBinding.acBtnPreviewView.setOnClickListener {
            startActivity(Intent(this@MainActivity,PreviewActivity::class.java))
        }

        mDataBinding.acBtnImage.setOnClickListener {
            startActivity(Intent(this@MainActivity,ImageActivity::class.java))
        }

        mDataBinding.acBtnAnalyzer.setOnClickListener {
            startActivity(Intent(this@MainActivity,ImageAnalyzerActivity::class.java))
        }

        mDataBinding.acBtnVideo.setOnClickListener {
            startActivity(Intent(this@MainActivity,VideoActivity::class.java))
        }
    }

    //获取摄像头列表
    private fun getCameraListInfo() {
        //1、判断设备是否支持摄像头
        mCameraManager.cameraIdList.forEach {
            val characteristics = mCameraManager.getCameraCharacteristics(it)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "getCameraListInfo: $it, $facing")
            }

            val cameraInfo = CameraInfoModel().apply {
                cameraId = it
                face = "$facing，CameraId:${it}"
            }
            cameraList.add(cameraInfo)
        }
    }

//    //打开摄像头
//    @SuppressLint("MissingPermission")
//    fun openCamera(model: CameraInfoModel) {
//        model.cameraId?.let {
//            val mStateCallback = object : CameraDevice.StateCallback() {
//                override fun onOpened(camera: CameraDevice) {
//                    if (it == camera.id) {
//                        model.cameraService = camera
//                        model.state = 1
//
//                        loadSurface(model)
//                    }
//                }
//
//                override fun onDisconnected(camera: CameraDevice) {
//                    if (it == camera.id) {
//                        model.cameraService = camera
//                        model.state = 0
//                    }
//                }
//
//                override fun onError(camera: CameraDevice, error: Int) {
//                    if (it == camera.id) {
//                        model.cameraService = camera
//                        model.state = 3
//                    }
//                }
//            }
//
//            if (BuildConfig.DEBUG) {
//                Log.i("print_logs", "MainActivity::openCamera: ${model.cameraId}")
//            }
//            mCameraManager.openCamera(it, mStateCallback, mHandler)
//        }
//    }
//
//    private fun loadSurface(model: CameraInfoModel) {
//        if (BuildConfig.DEBUG) {
//            Log.i("print_logs", "MainActivity::loadLeftTopSurface: $model")
//        }
//
//        mDataBinding.tvLeftTop.surfaceTextureListener = object : SurfaceTextureListener {
//            override fun onSurfaceTextureAvailable(
//                surface: SurfaceTexture,
//                width: Int,
//                height: Int
//            ) {
//                if (BuildConfig.DEBUG) {
//                    Log.i("print_logs", "MainActivity::onSurfaceTextureAvailable: ")
//                }
//                val surfaces = Surface(mDataBinding.tvLeftTop.surfaceTexture)
//                model.cameraService?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//                    ?.also { builder ->
//                        builder.addTarget(surfaces)
//                        model.cameraService?.createCaptureSession(
//                            listOf(surfaces),
//                            object : StateCallback() {
//                                override fun onConfigured(session: CameraCaptureSession) {
//                                    session.setRepeatingRequest(builder.build(), null, mHandler)
//                                    if (BuildConfig.DEBUG) {
//                                        Log.d("print_logs", "MainActivity::onConfigured: ")
//                                    }
//                                }
//
//                                override fun onConfigureFailed(session: CameraCaptureSession) {
//                                    if (BuildConfig.DEBUG) {
//                                        Log.e("print_logs", "MainActivity::onConfigureFailed: ")
//                                    }
//                                }
//                            },
//                            mHandler
//                        )
//                    }
//            }
//
//            override fun onSurfaceTextureSizeChanged(
//                surface: SurfaceTexture,
//                width: Int,
//                height: Int
//            ) {
//                if (BuildConfig.DEBUG) {
//                    Log.i("print_logs", "MainActivity::onSurfaceTextureSizeChanged: ")
//                }
//            }
//
//            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//                if (BuildConfig.DEBUG) {
//                    Log.i("print_logs", "MainActivity::onSurfaceTextureDestroyed: ")
//                }
//                return true
//            }
//
//            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//                if (BuildConfig.DEBUG) {
//                    Log.i("print_logs", "MainActivity::onSurfaceTextureUpdated: ")
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }

}