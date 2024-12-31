package com.google.play

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
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
import com.facebook.share.ShareApi
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.model.ShareVideo
import com.facebook.share.model.ShareVideoContent
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.play.databinding.ActivityMainBinding
import com.google.play.facebook.FaceBookActivity
import com.google.play.reviews.ReviewsActivity
import com.google.play.update.UpdateActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Arrays
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        mDataBinding.acBtnUpdate.setOnClickListener {
            startActivity(Intent(this, UpdateActivity::class.java))
        }

        mDataBinding.acBtnReviews.setOnClickListener {
            startActivity(Intent(this, ReviewsActivity::class.java))
        }

        mDataBinding.acBtnFaceBook.setOnClickListener {
            startActivity(Intent(this, FaceBookActivity::class.java))
        }

        mDataBinding.acBtnGoogleLogin.setOnClickListener {
            loginGoogle()
        }
        
        mDataBinding.acBtnGoogleLogout.setOnClickListener {
            logoutGoogle()
        }
    }

    private val mCredentialManager by lazy { CredentialManager.Companion.create((this@MainActivity)) }
    private val mCancellationSignal by lazy { CancellationSignal().apply {
        setOnCancelListener {
            if (BuildConfig.DEBUG) {
                Log.d("print_logs", "Google: setOnCancelListener")
            }
        }
    } }


    private fun loginGoogle() {
        lifecycleScope.launch {
                runCatching {
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setAutoSelectEnabled(true)
                        .setServerClientId(resources.getString(R.string.google_oauth_web))
//            .setNonce("")
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()
                    mCredentialManager.getCredential(this@MainActivity,request)
                }.onSuccess {result ->
                    val credential = result.credential
                    when (credential) {
                        is PublicKeyCredential -> { // 使用通行密钥登录
                            val responseJson = credential.authenticationResponseJson
                            if (BuildConfig.DEBUG) {
                                Log.i("print_logs", "PublicKeyCredential: $responseJson")
                            }
                        }

                        is PasswordCredential -> { // 使用密码登录
                            val userName = credential.id
                            val password = credential.password
                            if (BuildConfig.DEBUG) {
                                Log.i(
                                    "print_logs",
                                    "PasswordCredential: userName：$userName, password：$password"
                                )
                            }
                        }

                        is CustomCredential -> { // 使用 Google 账号登录
                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                Log.i(
                                    "print_logs",
                                    "CustomCredential: ${Thread.currentThread().name}\n" +
                                            "id：${googleIdTokenCredential.id}\n" +
                                            "idToken：${googleIdTokenCredential.idToken}\n" +
                                            "givenName：${googleIdTokenCredential.givenName}\n" +
                                            "familyName：${googleIdTokenCredential.familyName}\n" +
                                            "displayName：${googleIdTokenCredential.displayName}\n" +
                                            "phoneNumber：${googleIdTokenCredential.phoneNumber}\n" +
                                            "profilePictureUri：${googleIdTokenCredential.profilePictureUri}"
                                )

//                                        mDataBinding.acBtnGoogleLogin.text="谷歌 退出登录"
                            }
                        }

                        else -> {
                            Log.w("print_logs", "MainActivity::loginGoogle: else")
                        }
                    }
                }.onFailure {
                    if (BuildConfig.DEBUG) {
                        Log.e("print_logs", "MainActivity::onError: Google登录失败：$it")
                    }
                }
        }
    }

    private fun logoutGoogle() {
        lifecycleScope.launch{
            runCatching {
                val clearCredentialStateRequest = ClearCredentialStateRequest()
                mCredentialManager.clearCredentialState(clearCredentialStateRequest)
            }.onSuccess {
                if (BuildConfig.DEBUG) {
                    Log.i("print_logs", "logoutGoogle: ${Thread.currentThread().name}, Google注销登录成功！ ")
                }
            }.onFailure {
                if (BuildConfig.DEBUG) {
                    Log.e("print_logs", "logoutGoogle: ${Thread.currentThread().name}, Google注销异常：$it")
                }
            }
        }
    }
}