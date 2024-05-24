package zs.android.camerax

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.video.ExperimentalVideo
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import zs.android.camerax.databinding.ActivityPreviewBinding

@ExperimentalVideo class PreviewActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityPreviewBinding
    private val mCameraProviderFuture by lazy { ProcessCameraProvider.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                load()
            }
        }.launch(Manifest.permission.CAMERA)
    }

    /**
     * CameraController 在单个类中提供大多数 CameraX 核心功能。
     * 它只需少量设置代码，并且可自动处理相机初始化、用例管理、目标旋转、点按对焦、双指张合缩放等操作。
     *
     * CameraController 的默认 UseCase 为 Preview、ImageCapture 和 ImageAnalysis。
     * 如需关闭 ImageCapture 或 ImageAnalysis，或者开启 VideoCapture，请使用 setEnabledUseCases() 方法。
     */
    private fun load() {
        mCameraProviderFuture.addListener({
            bindPreview()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview() {
        val cameraProvider = mCameraProviderFuture.get()

        mDataBinding.previewView.apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE

            //方式一：
//            controller = LifecycleCameraController(this@PreviewActivity).apply {
//                bindToLifecycle(this@PreviewActivity)
//                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA// DEFAULT_BACK_CAMERA
//                setEnabledUseCases(CameraController.IMAGE_CAPTURE)
//                setZoomRatio(0.5F)
//            }
        }

        //方式二：
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(mDataBinding.previewView.surfaceProvider)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()

    }
}