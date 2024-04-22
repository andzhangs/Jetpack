package com.jetpack.sharing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ShareCompat

class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        val tvSubject = findViewById<AppCompatTextView>(R.id.acTv_subject)
        val tvText = findViewById<AppCompatTextView>(R.id.acTv_share_get_text)
        val imageView = findViewById<AppCompatImageView>(R.id.acTv_share_get_img)
        val videoView = findViewById<VideoView>(R.id.video_view)


        ShareCompat.IntentReader(this).let { reader ->

            tvSubject.text = reader.subject

            when (reader.type) {
                ShareConfig.MimeType.IMAGE.get() -> {
                    imageView.visibility = View.VISIBLE
                    if (reader.isMultipleShare) {
                        intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)?.forEachIndexed { index, uri ->
                            if (index==0){
                                imageView.setImageURI(uri)
                            }
                        }
                    } else {
                        imageView.setImageURI(reader.stream)
                    }
                }

                ShareConfig.MimeType.VIDEO.get() -> {
                    videoView.visibility = View.VISIBLE
                    if (reader.isMultipleShare) {
                        intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                            ?.forEachIndexed { index, uri ->
                                if (index == 0) {
                                    videoView.setVideoURI(uri)
                                    videoView.requestFocus()
                                    videoView.start()
                                }
                            }
                    } else {
                        videoView.setVideoURI(reader.stream)
                        videoView.requestFocus()
                        videoView.start()
                    }
                }

                ShareConfig.MimeType.TEXT.get() -> {
                    tvText.visibility = View.VISIBLE
                    tvText.text = reader.text
                }

                else -> {

                }
            }
        }
    }
}