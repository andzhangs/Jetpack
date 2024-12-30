package com.google.play

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.LoginStatusCallback
import com.facebook.Profile
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.play.databinding.ActivityMainBinding
import com.google.play.reviews.ReviewsActivity
import com.google.play.update.UpdateActivity
import kotlinx.coroutines.launch
import java.util.Arrays


class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding =DataBindingUtil.setContentView(this,R.layout.activity_main)
        mDataBinding.acBtnUpdate.setOnClickListener {
            startActivity(Intent(this,UpdateActivity::class.java))
        }

        mDataBinding.acBtnReviews.setOnClickListener {
            startActivity(Intent(this,ReviewsActivity::class.java))
        }

        mDataBinding.acBtnGoogleLogin.setOnClickListener {
            loadGoogle()
        }

        FacebookSdk.setIsDebugEnabled(true)
        
        loginFaceBook()
    }

    private val callbackManager by lazy{CallbackManager.Factory.create()}

    //生成开发秘钥散列：keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
    // ES5czcP6wEUTzdj4h7jNF/vjUtM=
    //生成生产秘钥散列：keytool -exportcert -alias com.google.play -keystore /Users/zhangshuai/Project____/Android/Jetpack/google_play/play.jks | openssl sha1 -binary | openssl base6
    private fun loginFaceBook(){

        fun printLoginInfo(accessToken: AccessToken){
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "检测登录状态: token：${accessToken.token}, \n" +
                        "isExpired：${accessToken.isExpired},\n" +
                        "expires：${accessToken.expires},\n" +
                        "isInstagramToken：${accessToken.isInstagramToken},\n" +
                        "userId：${accessToken.userId},\n"+
                        "source：${accessToken.source},\n"+
                        "dataAccessExpirationTime：${accessToken.dataAccessExpirationTime},\n"+
                        "graphDomain：${accessToken.graphDomain},\n"+
                        "applicationId：${accessToken.applicationId},\n"+
                        "lastRefresh：${accessToken.lastRefresh}")
            }
            mDataBinding.acBtnFacebookLogin.text="退出 FaceBook"
        }

        //检索登录状态
        LoginManager.getInstance().retrieveLoginStatus(this@MainActivity,object :LoginStatusCallback{
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

        LoginManager.getInstance().registerCallback(callbackManager,object :FacebookCallback<LoginResult>{
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
            val accessToken=AccessToken.getCurrentAccessToken()
            val profile= Profile.getCurrentProfile()
            if (accessToken != null && accessToken.isExpired || profile != null) {
                LoginManager.getInstance().logOut()
                Toast.makeText(this, "Facebook已退出登录！", Toast.LENGTH_SHORT).show()
                mDataBinding.acBtnFacebookLogin.text = "登录 FaceBook"
                return@setOnClickListener
            }
            LoginManager.getInstance().logIn(this@MainActivity,callbackManager, listOf("email,public_profile"))
        }
    }

    private val mCredentialManager by lazy { CredentialManager.create((this@MainActivity)) }

    private fun loadGoogle(){

        lifecycleScope.launch {
//            val getPasswordOption=GetPasswordOption()
//            val getPublicKeyCredential=GetPublicKeyCredentialOption()

            try {
                val googleIdOption=GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId("137852293720-chd8fqadfk9mbjhhh2kglohqsot8c4tp.apps.googleusercontent.com")
//            .setNonce("")
                    .build()
                val request=GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result=mCredentialManager.getCredential(
                    request=request,
                    activity = this@MainActivity
                )
                val credential=result.credential
                when (credential) {
                    is PublicKeyCredential-> {
                        val responseJson=credential.authenticationResponseJson
                        if (BuildConfig.DEBUG) {
                            Log.i("print_logs", "PublicKeyCredential: $responseJson")
                        }
                    }
                    is PasswordCredential->{
                        val userName=credential.id
                        val password=credential.password
                        if (BuildConfig.DEBUG) {
                            Log.i("print_logs", "PasswordCredential: userName：$userName, password：$password")
                        }
                    }
                    is CustomCredential->{
                        if (credential.type==GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential=GoogleIdTokenCredential.createFrom(credential.data)
                            if (BuildConfig.DEBUG) {
                                Log.i("print_logs", "CustomCredential: ${googleIdTokenCredential.id}, ${googleIdTokenCredential.data}")
                            }
                        }
                    }
                    else -> {
                        Log.w("print_logs", "MainActivity::loadGoogle: else")
                    }
                }

            }catch (e:Exception){
                e.printStackTrace()
                if (BuildConfig.DEBUG) {
                    Log.e("print_logs", "MainActivity::loadGoogle: $e")
                }
            }
        }
    }
}