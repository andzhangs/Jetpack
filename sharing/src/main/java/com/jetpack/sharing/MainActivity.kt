package com.jetpack.sharing

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ShareCompat


class MainActivity : AppCompatActivity() {

    private val description = "来自ShareCompat的图片分享"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val launcherPicture =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
                it?.also { uris ->
                    shareImage(uris)
                }
            }

        val launcherVideo =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
                it?.also { uris ->
                    shareVideo(uris)
                }
            }

        //分享文本
        findViewById<AppCompatButton>(R.id.acBtn_sharing_text).setOnClickListener {
            shareText()
        }

        //分享图片
        findViewById<AppCompatButton>(R.id.acBtn_sharing_image).setOnClickListener {
            launcherPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        //分享视频
        findViewById<AppCompatButton>(R.id.acBtn_sharing_video).setOnClickListener {
            launcherVideo.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
        }


        val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.i("print_logs", "MainActivity::onCreate: ${it.resultCode}")
            if (it.resultCode == RESULT_OK) {
                val msg=it.data?.getStringExtra(PickActivity.KEY_RESULT)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        //分享文件
        findViewById<AppCompatButton>(R.id.acBtn_pick).setOnClickListener {
            startActivityForResult.launch(Intent(this,PickActivity::class.java))
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareText() {
        val shareCompatIntent = ShareCompat.IntentBuilder(this)
            .setText("我是来自分享的内容")
            .setType(ShareConfig.MimeType.TEXT.get())
            .setSubject("来自Sharing的图片Subject")
            .setChooserTitle("来自ShareCompat的文字分享")
            .startChooser()  //方式一
        //方式二
//            .createChooserIntent()
//            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
//        if (shareCompatIntent.resolveActivity(this.packageManager) != null) {
//            startActivity(shareCompatIntent)
//        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareImage(uris: List<Uri>) {
        ShareCompat.IntentBuilder(this).apply {
            uris.forEach { uri ->
                ShareConfig.setUriQueries(this@MainActivity, uri)
                addStream(uri)
            }
        }.setText(description)
            .setType(ShareConfig.MimeType.IMAGE.get())
            .setSubject("来自Sharing的图片Subject")
            .setChooserTitle("选择图片")
            .startChooser()


//        val shareCompatIntent2 = ShareCompat.IntentBuilder(this).apply {
//            uris.forEach { uri ->
//                ShareConfig.setUriQueries(this@MainActivity, uri)
//                addStream(uri)
//            }
//
//        }.setText(description)
//            .setType(ShareConfig.MimeType.IMAGE.get())
//            .setSubject("来自Sharing的图片Subject")
//            .setChooserTitle("选择图片")
//            .createChooserIntent()
//            .addCategory(Intent.CATEGORY_DEFAULT)
//            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        if (shareCompatIntent2.resolveActivity(this.packageManager) != null) {
//            startActivity(shareCompatIntent2)
//        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareVideo(uris: List<Uri>) {
        val shareCompatIntent = ShareCompat.IntentBuilder(this)
            .apply {
                uris.forEach { uri ->
                    ShareConfig.setUriQueries(this@MainActivity, uri)
                    addStream(uri)
                }
            }
            .setText(description)
            .setType(ShareConfig.MimeType.VIDEO.get())
            .setSubject("来自Sharing的视频Subject")
            .setChooserTitle("选择视频")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (shareCompatIntent.resolveActivity(this.packageManager) != null) {
            startActivity(shareCompatIntent)
        }
    }
}