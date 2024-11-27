package zs.android.camerax

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import zs.android.camerax.databinding.FragmentPreviewBackBinding

class PreviewBackFragment : Fragment() {

    private lateinit var mParentActivity: PreviewActivity
    private lateinit var mDataBinding: FragmentPreviewBackBinding

    private val mCameraProviderFuture by lazy { ProcessCameraProvider.getInstance(this.requireContext()) }
    private var mCamera: Camera? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mParentActivity = context as PreviewActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_preview_back, container, false)
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        load()
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
            val cameraProvider = mCameraProviderFuture.get()
            bindPreviewBack(cameraProvider)
        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

    private fun bindPreviewBack(provider: ProcessCameraProvider) {
        mDataBinding.previewViewBack.apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE

            setOnClickListener {
                switchFlash()
            }
        }

        //方式二：
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(mDataBinding.previewViewBack.surfaceProvider)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        mCamera = provider.bindToLifecycle(this, cameraSelector, preview)
    }

    /**
     * 闪光灯
     */
    private fun switchFlash(){
        mCamera?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                it.cameraControl.enableTorch(it.cameraInfo.torchState.value == 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDataBinding.unbind()
    }
}