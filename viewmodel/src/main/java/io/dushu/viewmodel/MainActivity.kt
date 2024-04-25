package io.dushu.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewModel: MainViewModel

    private val mMain2ViewModel: Main2ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[MainViewModel::class.java]
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemBar(view: View) {
        mMain2ViewModel.sendMsg("数据：${System.currentTimeMillis()}")
        val windowInsetsController=WindowCompat.getInsetsController(window,window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    fun showSystemBar(view: View) {
        val windowInsetsController=WindowCompat.getInsetsController(window,window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }

}
