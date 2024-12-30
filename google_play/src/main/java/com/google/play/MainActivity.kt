package com.google.play

import android.content.Intent
import android.net.Uri
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
import com.facebook.share.ShareApi
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.model.ShareVideo
import com.facebook.share.model.ShareVideoContent
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.play.databinding.ActivityMainBinding
import com.google.play.facebook.FaceBookActivity
import com.google.play.reviews.ReviewsActivity
import com.google.play.update.UpdateActivity
import kotlinx.coroutines.launch
import java.util.Arrays


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
            loadGoogle()
        }
    }

    private val mCredentialManager by lazy { CredentialManager.create((this@MainActivity)) }

    private fun loadGoogle() {

        lifecycleScope.launch {
//            val getPasswordOption=GetPasswordOption()
//            val getPublicKeyCredential=GetPublicKeyCredentialOption()

            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId("137852293720-chd8fqadfk9mbjhhh2kglohqsot8c4tp.apps.googleusercontent.com")
//            .setNonce("")
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = mCredentialManager.getCredential(
                    request = request,
                    activity = this@MainActivity
                )
                val credential = result.credential
                when (credential) {
                    is PublicKeyCredential -> {
                        val responseJson = credential.authenticationResponseJson
                        if (BuildConfig.DEBUG) {
                            Log.i("print_logs", "PublicKeyCredential: $responseJson")
                        }
                    }

                    is PasswordCredential -> {
                        val userName = credential.id
                        val password = credential.password
                        if (BuildConfig.DEBUG) {
                            Log.i(
                                "print_logs",
                                "PasswordCredential: userName：$userName, password：$password"
                            )
                        }
                    }

                    is CustomCredential -> {
                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)
                            if (BuildConfig.DEBUG) {
                                Log.i(
                                    "print_logs",
                                    "CustomCredential: ${googleIdTokenCredential.id}, ${googleIdTokenCredential.data}"
                                )
                            }
                        }
                    }

                    else -> {
                        Log.w("print_logs", "MainActivity::loadGoogle: else")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (BuildConfig.DEBUG) {
                    Log.e("print_logs", "MainActivity::loadGoogle: $e")
                }
            }
        }
    }
}