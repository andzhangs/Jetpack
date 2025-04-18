package io.dushu.viewmodel

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewModel: MainViewModel

    private val mMain2ViewModel: Main2ViewModel by viewModels()


    private val macBtnLandscape by lazy { findViewById<AppCompatButton>(R.id.acBtn_landscape) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onCreate: ")
        }
        mMainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[MainViewModel::class.java]
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemBar(view: View) {
        mMain2ViewModel.sendMsg("数据：${System.currentTimeMillis()}")
        WindowCompat.getInsetsController(window,window.decorView).hide(WindowInsetsCompat.Type.systemBars())

        //findViewTreeViewModelStoreOwner的使用
        macBtnLandscape.findViewTreeViewModelStoreOwner()?.let {storeOwner->
            val getMain2ViewModel=ViewModelProvider(storeOwner)[Main2ViewModel::class.java]
            getMain2ViewModel.printLog()

            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "mMain2ViewModel == getMain2ViewModel : ${mMain2ViewModel === getMain2ViewModel}")
            }
        } ?: kotlin.run {
            Log.e("print_logs", "it.findViewTreeViewModelStoreOwner() is null.")
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    fun showSystemBar(view: View) {
        WindowCompat.getInsetsController(window,window.decorView).show(WindowInsetsCompat.Type.systemBars())
    }

    /**
     * 切换竖屏
     */
    fun togglePortrait(view: View) {
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 切换横屏
     */
    fun toggleLandscape(view: View) {
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onConfigurationChanged: ${newConfig.orientation}")
        }
    }

    override fun onStart() {
        super.onStart()
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onStart: ")
        }
    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onResume: ")
        }
    }

    override fun onStop() {
        super.onStop()
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onStop: ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onDestroy: ")
        }
    }
}
