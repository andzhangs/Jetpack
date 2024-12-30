package com.google.play.reviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.google.play.R

/**
 * 应用内评价
 * https://developer.android.com/guide/playcore/in-app-review/kotlin-java?hl=zh-cn
 */
class ReviewsActivity : AppCompatActivity() {

    private lateinit var mReviewManager: ReviewManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)
        mReviewManager = ReviewManagerFactory.create(this)
    }

    /**
     * 请求 ReviewInfo 对象
     */
    private fun requestReviewInfo() {
        mReviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {

                val info = it.result

                //启动应用内评价流程
                //使用 ReviewInfo 实例启动应用内评价流程。等到用户完成应用内评价流程后，再继续执行应用的正常用户流（例如进入下一关）
                mReviewManager.launchReviewFlow(this,info).addOnCompleteListener {

                }
            } else {
                @ReviewErrorCode val reviewErrorCode = (it.exception as ReviewException).errorCode
            }
        }
    }
}