package zs.android.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import zs.android.camerax.databinding.FragmentPreviewFrontBinding
import java.util.concurrent.TimeUnit

class PreviewFrontFragment : Fragment() {

    private lateinit var mParentActivity: PreviewActivity
    private lateinit var mDataBinding: FragmentPreviewFrontBinding

    private val mCameraProviderFuture by lazy { ProcessCameraProvider.getInstance(this.requireContext()) }
    private lateinit var mCameraController: LifecycleCameraController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mParentActivity = context as PreviewActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mDataBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_preview_front,
            container,
            false
        )
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        load()
    }

    private fun load() {
        mCameraProviderFuture.addListener({
            val cameraProvider = mCameraProviderFuture.get()
            bindPreviewFront(cameraProvider)
        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

    private fun bindPreviewFront(provider: ProcessCameraProvider) {
        mDataBinding.previewViewFront.apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE

            //方式一：
            controller =
                LifecycleCameraController(this@PreviewFrontFragment.requireContext()).apply {
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    setEnabledUseCases(CameraController.IMAGE_CAPTURE)
//                    setZoomRatio(0.5F)
                    bindToLifecycle(this@PreviewFrontFragment)
                }.apply {
                    mCameraController = this
                }
        }

        setZoomByScaleGesture()
    }

    /**
     * 双指缩放视图
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setZoomByScaleGesture() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio =
                    mCameraController.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
                val delta = detector.scaleFactor
                mCameraController.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(this.requireContext(), listener)
        mDataBinding.previewViewFront.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)
            singleClickFocus(v, event)
            true
        }
    }

    /**
     * 单击聚焦视图
     */
    private fun singleClickFocus(v: View, event: MotionEvent) {
        val preview = v as PreviewView
        if (event.action == MotionEvent.ACTION_DOWN) {
            val factory = preview.meteringPointFactory

            val x = event.x
            val y = event.y

            val point = factory.createPoint(x, y)
            val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                .setAutoCancelDuration(2, TimeUnit.SECONDS)
                .build()

            val rectF=RectF(x-50,y-50,x+50,y+50)

            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "singleClickFocus: $x ,$y")
            }

            mDataBinding.acIvFocus.setRectF(rectF)

            mCameraController.cameraControl?.startFocusAndMetering(action)

            v.performClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDataBinding.unbind()
    }
}