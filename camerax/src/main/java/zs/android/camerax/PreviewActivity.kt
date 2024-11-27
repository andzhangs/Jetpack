package zs.android.camerax

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.video.ExperimentalVideo
import androidx.databinding.DataBindingUtil
import zs.android.camerax.databinding.ActivityPreviewBinding

@ExperimentalVideo
class PreviewActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityPreviewBinding
//    private val mCameraProviderFuture by lazy { ProcessCameraProvider.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {

            }
        }.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()

    }
}