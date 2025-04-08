package io.dushu.lifcycle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.DefaultTaskExecutor
import androidx.databinding.DataBindingUtil
import io.dushu.lifcycle.databinding.ActivityMainBinding
import io.dushu.lifcycle.lifecycle.LifecycleActivity
import io.dushu.lifcycle.lifecycleservice.LifeCycleServiceActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainBinding =DataBindingUtil.setContentView(this,R.layout.activity_main)


        mMainBinding.acBtnLifecycle.setOnClickListener {
            startActivity(Intent(this,LifecycleActivity::class.java))
        }

        mMainBinding.acBtnLifecycleService.setOnClickListener {
            startActivity(Intent(this,LifeCycleServiceActivity::class.java))
        }
    }
}