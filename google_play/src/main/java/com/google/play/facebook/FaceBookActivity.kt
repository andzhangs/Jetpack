package com.google.play.facebook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.LoginStatusCallback
import com.facebook.Profile
import com.facebook.bolts.AppLinks
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.share.Share
import com.facebook.share.ShareApi
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.ShareMediaContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.model.ShareVideo
import com.facebook.share.model.ShareVideoContent
import com.facebook.share.widget.MessageDialog
import com.facebook.share.widget.ShareDialog
import com.google.play.BuildConfig
import com.google.play.R
import com.google.play.databinding.ActivityFaceBookBinding

class FaceBookActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityFaceBookBinding

    private val mContentResolver by lazy { this.applicationContext.contentResolver }
    
    private lateinit var mLauncherVideo:ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var mLauncherMedia:ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_face_book)
        FacebookSdk.setIsDebugEnabled(true)
        val targetUri= AppLinks.getAppLinkData(intent)?.also {bundle->

        }


        LoginManager.getInstance().retrieveLoginStatus(this@FaceBookActivity,object :
            LoginStatusCallback {
            override fun onCompleted(accessToken: AccessToken) {
                if (BuildConfig.DEBUG) {
                    Log.i("print_logs", "MainActivity::onCompleted: retrieveLoginStatus")
                }
                printLoginInfo(accessToken)
            }

            override fun onError(exception: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e("print_logs", "MainActivity::onError: $exception")
                }
            }

            override fun onFailure() {
                Log.e("print_logs", "MainActivity::onFailure: ")
            }
        })

        LoginManager.getInstance().registerCallback(callbackManager,object :
            FacebookCallback<LoginResult> {
            override fun onCancel() {
                if (BuildConfig.DEBUG) {
                    Log.e("print_logs", "MainActivity::onCancel: ")
                }
                mDataBinding.acBtnFacebookLogin.text = "登录 FaceBook"
            }

            override fun onError(error: FacebookException) {
                if (BuildConfig.DEBUG) {
                    Log.e("print_logs", "MainActivity::onError: $error")
                }
            }

            override fun onSuccess(result: LoginResult) {
                if (BuildConfig.DEBUG) {
                    Log.i("print_logs", "MainActivity::onCompleted: registerCallback")
                }
                printLoginInfo(result.accessToken)
            }
        })


        mDataBinding.acBtnFacebookLogin.setOnClickListener {
            loginFaceBook()
        }

        faceBookShareLink()

        faceBookShareImage()

        mLauncherVideo=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            it?.let {
                //分享视频
                val prePhoto= SharePhoto.Builder()
                    .setBitmap(BitmapFactory.decodeResource(resources,R.drawable.img_tiger))
                    .build()
                val video= ShareVideo.Builder()
                    .setLocalUrl(it)
                    .build()
                val videoContent= ShareVideoContent.Builder()
                    .setVideo(video)
                    .setContentTitle("标题")
                    .setContentDescription("描述")
                    .setPreviewPhoto(prePhoto)
                    .setShareHashtag(
                        ShareHashtag.Builder()
                            .setHashtag("")
                            .build())
                    .build()

                //不需要回调
//                ShareDialog.show(this,videoContent)

                //需要分享回调
                val shareDialog=ShareDialog(this).apply {
                    registerCallback(callbackManager,shareCallback)
                }
                shareDialog.show(videoContent)
            }
        }


        //视频分享
        mDataBinding.showVideoButton=ShareDialog.canShow(ShareVideoContent::class.java)
        mDataBinding.acBtnShareVideo.setOnClickListener {
            mLauncherVideo.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
        }


        mLauncherMedia=registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()){

            if (it.isEmpty() || it.size > 6) {
                Toast.makeText(this, "未选中文件或最大分享6个文件！", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            
            val mediaContentBuilder=ShareMediaContent.Builder()
            it.forEach{uri->
                mContentResolver.getType(uri)?.also { type->
                    if (type.startsWith("image")) {
                        val photo= SharePhoto.Builder()
                            .setImageUrl(uri)
                            .build()
                        mediaContentBuilder.addMedium(photo)
                    }else if(type.startsWith("video")){
                        val video= ShareVideo.Builder()
                            .setLocalUrl(uri)
                            .build()
                        mediaContentBuilder.addMedium(video)
                    }
                }
            }

            val shareDialog=ShareDialog(this).apply {
                registerCallback(callbackManager,shareCallback)
            }
            shareDialog.show( mediaContentBuilder.build())
        }

        //多媒体分享
        mDataBinding.showMediaButton=ShareDialog.canShow(ShareMediaContent::class.java)
        mDataBinding.acBtnShareMedia.setOnClickListener {
            mLauncherMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }

    private val callbackManager by lazy{ CallbackManager.Factory.create()}

    fun printLoginInfo(accessToken: AccessToken){
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "检测登录状态: \n" +
                    "token：${accessToken.token}\n" +
                    "isExpired：${accessToken.isExpired}\n" +
                    "expires：${accessToken.expires}\n" +
                    "isInstagramToken：${accessToken.isInstagramToken}\n" +
                    "userId：${accessToken.userId}\n"+
                    "source：${accessToken.source}\n"+
                    "dataAccessExpirationTime：${accessToken.dataAccessExpirationTime}\n"+
                    "graphDomain：${accessToken.graphDomain}\n"+
                    "applicationId：${accessToken.applicationId}\n"+
                    "lastRefresh：${accessToken.lastRefresh}")
        }
        mDataBinding.acBtnFacebookLogin.text="退出 FaceBook"
    }


    //生成开发秘钥散列：keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
    // ES5czcP6wEUTzdj4h7jNF/vjUtM=
    //生成生产秘钥散列：keytool -exportcert -alias com.google.play -keystore /Users/zhangshuai/Project____/Android/Jetpack/google_play/play.jks | openssl sha1 -binary | openssl base6
    private fun loginFaceBook(){
        //检索登录状态
        val accessToken= AccessToken.getCurrentAccessToken()
        val profile= Profile.getCurrentProfile()
        if (accessToken != null && accessToken.isExpired || profile != null) {
            LoginManager.getInstance().logOut()
            Toast.makeText(this, "Facebook已退出登录！", Toast.LENGTH_SHORT).show()
            mDataBinding.acBtnFacebookLogin.text = "登录 FaceBook"
            return
        }
        LoginManager.getInstance().logIn(this@FaceBookActivity,callbackManager, listOf("email,public_profile"))
    }

    private val shareCallback=object :FacebookCallback<Sharer.Result>{
        override fun onCancel() {
            Log.w("print_logs", "MainActivity::onCancel: ")
        }

        override fun onError(error: FacebookException) {
            if (BuildConfig.DEBUG) {
                Log.e("print_logs", "MainActivity::onError: $error")
            }
            Toast.makeText(this@FaceBookActivity, "分享失败!", Toast.LENGTH_SHORT).show()
        }

        override fun onSuccess(result: Sharer.Result) {
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "MainActivity::onSuccess: ")
            }
            Toast.makeText(this@FaceBookActivity, "分享成功!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun faceBookShareLink(){
        mDataBinding.showLinkButton=ShareDialog.canShow(ShareLinkContent::class.java)
        mDataBinding.acBtnShareLink.setOnClickListener {
            //分享链接
            val linkContent= ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                .setShareHashtag(ShareHashtag.Builder()
                    .setHashtag("#Facebook")
                    .build())
                .build()
            val shareDialog=ShareDialog(this).apply {
                registerCallback(callbackManager,shareCallback)
            }
            shareDialog.show(linkContent)
        }
    }


    private fun faceBookShareImage() {
        mDataBinding.showPhotoButton=ShareDialog.canShow(SharePhotoContent::class.java)
        mDataBinding.acBtnSharePhoto.setOnClickListener {
            //分享照片
            val photo = SharePhoto.Builder()
                .setBitmap(BitmapFactory.decodeResource(resources,R.drawable.img_640))
                .build()
            val imageContent = SharePhotoContent.Builder()
                .addPhoto(photo)
                .build()
            val shareDialog=ShareDialog(this).apply {
                registerCallback(callbackManager,shareCallback)
            }
            shareDialog.show(imageContent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}