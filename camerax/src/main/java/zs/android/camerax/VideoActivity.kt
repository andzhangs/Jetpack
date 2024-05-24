package zs.android.camerax

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.ExperimentalVideo
import androidx.camera.view.video.OnVideoSavedCallback
import androidx.camera.view.video.OutputFileOptions
import androidx.camera.view.video.OutputFileResults
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import zs.android.camerax.databinding.ActivityVideoBinding
import zs.android.camerax.databinding.ItemQualityLayoutBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalVideo @ExperimentalCamera2Interop
class VideoActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityVideoBinding


    private val mCameraProvider by lazy { ProcessCameraProvider.getInstance(this).get() }
    private val mList by lazy {
        val cameraInfo = mCameraProvider.availableCameraInfos.filter {
            Camera2CameraInfo.from(it)
                .getCameraCharacteristic(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_BACK
        }
        val supportQualities = QualitySelector.getSupportedQualities(cameraInfo[0])

        val filteredQualities = arrayListOf(
            Quality.FHD,
            Quality.HD,
            Quality.SD
        ).filter { supportQualities.contains(it) }

        filteredQualities.map {
            VideoModel(
                qualityLevel = it.qualityToString(),
                quality = it,
                selected = false
            )
        }
    }

    private val mExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private var mRecording: Recording? = null
    private var mVideoCapture: VideoCapture<Recorder>? = null
    private var mPreview: Preview? = null
    private var mLastPosition = -1

//    private val mQualitySelector by lazy {
//        QualitySelector.fromOrderedList(
//            listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
//            FallbackStrategy.higherQualityThan(Quality.HD)
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_video)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    load()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                load()
            } else {
                val intent =
                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.parse("package:$packageName")
                    }
                launcher.launch(intent)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                )
            } else {
                load()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 || requestCode == 200) {
            load()
        }
    }

    private fun load() {
        if (ActivityCompat.checkSelfPermission(
                this@VideoActivity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            return
        }

        with(mDataBinding.recyclerView) {
            layoutManager = LinearLayoutManager(this@VideoActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }


        mDataBinding.acBtnPause.setOnClickListener {
            mRecording?.pause()
        }

        mDataBinding.acBtnResume.setOnClickListener {
            mRecording?.resume()
        }

        mDataBinding.acBtnStop.setOnClickListener {
            mRecording?.stop()
        }
    }

    private val mAdapter = object : RecyclerView.Adapter<QualityViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualityViewHolder {
            val itemBinding = DataBindingUtil.inflate<ItemQualityLayoutBinding>(
                LayoutInflater.from(this@VideoActivity),
                R.layout.item_quality_layout,
                parent,
                false
            )
            return QualityViewHolder(itemBinding)
        }

        override fun getItemCount(): Int = mList.size

        override fun onBindViewHolder(holder: QualityViewHolder, position: Int) {
            holder.bind(position, mList[position])
        }
    }


    private inner class QualityViewHolder(private val itemBinding: ItemQualityLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int, data: VideoModel) {
            itemBinding.data = data
            itemBinding.acTvQuality.apply {
                text = data.qualityLevel
                setOnClickListener {
                    //销毁上一个录制
                    if (mPreview != null && mVideoCapture != null) {

                        mCameraProvider.unbind(mPreview, mVideoCapture)

                        mRecording?.close()
                        mRecording = null

                        mPreview = null
                        mVideoCapture = null
                    }

                    //更新UI
                    if (mLastPosition != position) {
                        if (mLastPosition != -1) {
                            //上一个未选中
                            mList[mLastPosition].selected = false
                            mAdapter.notifyItemChanged(mLastPosition)

                            //选中当前
                            mList[position].selected = true
                            mAdapter.notifyItemChanged(position)
                        } else {
                            //选中当前
                            mList[position].selected = true
                            mAdapter.notifyItemChanged(position)
                        }
                        start(data)
                    }

                    mLastPosition = position

                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun start(model: VideoModel) {

        val qualitySelector = QualitySelector.from(model.quality)

        val recorder = Recorder.Builder()
            .setExecutor(mExecutor)
            .setQualitySelector(qualitySelector)
            .build()

        mVideoCapture = VideoCapture.withOutput(recorder)

        mPreview = Preview.Builder().build()
        mPreview?.setSurfaceProvider(mDataBinding.previewView.surfaceProvider)

        try {
            mCameraProvider.bindToLifecycle(
                this@VideoActivity,
                CameraSelector.DEFAULT_FRONT_CAMERA,
                mPreview,
                mVideoCapture
            )

            val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(System.currentTimeMillis())
            val fileName = "CameraX-recording-${model.qualityLevel}-${timeStamp}.mp4"
            val contentValues = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
            }

            val mediaStoreOutput = MediaStoreOutputOptions.Builder(this@VideoActivity.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).setContentValues(contentValues).build()

            mDataBinding.acTvState.text = "录制中..."

//            mRecording = mVideoCapture
//                ?.output
//                ?.prepareRecording(this@VideoActivity, mediaStoreOutput)
//                ?.withAudioEnabled()
//                ?.start(ContextCompat.getMainExecutor(this@VideoActivity)) { t ->
//                    t?.also {
////                                if (BuildConfig.DEBUG) {
////                                    Log.i("print_logs", "fileSizeLimit：${it.outputOptions.fileSizeLimit}")
////                                    Log.i("print_logs", "location：${it.outputOptions.location?.toString()}")
////                                    Log.i("print_logs", "numBytesRecorded：${it.recordingStats.numBytesRecorded}")
////                                    Log.i("print_logs", "recordedDurationNanos：${it.recordingStats.recordedDurationNanos}")
////                                    Log.i("print_logs", "audioState：${it.recordingStats.audioStats.audioState}")
////                                    Log.i("print_logs", "hasAudio：${it.recordingStats.audioStats.hasAudio()}")
////                                }
//                        when (it) {
//                            is VideoRecordEvent.Start -> {
//                                if (BuildConfig.DEBUG) {
//                                    Log.i("print_logs", "VideoRecordEvent.Start: ")
//                                }
//                                mDataBinding.acTvState.text = "录制中..."
//                            }
//
//                            is VideoRecordEvent.Pause -> {
//                                if (BuildConfig.DEBUG) {
//                                    Log.w("print_logs", "VideoRecordEvent.Pause: ")
//                                }
//                                mDataBinding.acTvState.text = "暂停"
//                            }
//
//                            is VideoRecordEvent.Resume -> {
//                                if (BuildConfig.DEBUG) {
//                                    Log.i("print_logs", "VideoRecordEvent.Resume: ")
//                                }
//                                mDataBinding.acTvState.text = "录制中..."
//                            }
//
//                            is VideoRecordEvent.Finalize -> {
//                                if (BuildConfig.DEBUG) {
//                                    Log.v("print_logs", "VideoRecordEvent.Finalize: ")
//                                }
//                                mDataBinding.acTvState.text = "停止"
//                            }
//
//                            is VideoRecordEvent.Status -> {
//                                if (BuildConfig.DEBUG) {
//                                    Log.d("print_logs", "VideoRecordEvent.Status: ")
//                                }
//                            }
//
//                            else -> {
//                                if (BuildConfig.DEBUG) {
//                                    Log.e("print_logs", "VideoRecordEvent: ")
//                                }
//                            }
//                        }
//                    }
//                }


            val cameraSelector=CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()


            val controller=LifecycleCameraController(this).also {
                it.bindToLifecycle(this)
                it.setEnabledUseCases(CameraController.VIDEO_CAPTURE)
                it.cameraSelector= CameraSelector.DEFAULT_BACK_CAMERA

                val publicPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

                val fileOptions= OutputFileOptions.builder(File(publicPath,fileName)).build()

                it.startRecording(fileOptions,ContextCompat.getMainExecutor(this),object :OnVideoSavedCallback{
                    override fun onVideoSaved(outputFileResults: OutputFileResults) {
                        if (BuildConfig.DEBUG) {
                            Log.i("print_logs", "onVideoSaved: ${outputFileResults.savedUri}")
                        }
                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                        if (BuildConfig.DEBUG) {
                            Log.e("print_logs", "onError: $videoCaptureError, $message, $cause")
                        }
                    }
                })
            }

            mDataBinding.previewView.controller=controller


        } catch (e: Exception) {
            e.printStackTrace()
            if (BuildConfig.DEBUG) {
                Log.e("print_logs", "start: $e")
            }
            mDataBinding.acTvState.text = "停止"
        }
    }

    fun Quality.qualityToString(): String {
        return when (this) {
            Quality.UHD -> "UHD-2160P"
            Quality.FHD -> "FHD-1080P"
            Quality.HD -> "HD-720P"
            Quality.SD -> "SD-480P"
            else -> throw IllegalArgumentException()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCameraProvider.unbindAll()
        mExecutor.shutdown()
        mDataBinding.unbind()
    }
}