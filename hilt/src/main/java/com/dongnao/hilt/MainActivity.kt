package com.dongnao.hilt

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dongnao.hilt.databinding.ActivityMainBinding
import com.dongnao.hilt.analytics.AnalyticsService
import com.dongnao.hilt.intoset.PluginA
import com.dongnao.hilt.intoset.PluginB
import com.dongnao.hilt.model.ActivityBean
import com.dongnao.hilt.model.ActivityContextBean
import com.dongnao.hilt.model.ApplicationBean
import com.dongnao.hilt.model.ApplicationContextBean
import com.dongnao.hilt.reciver.MyHiltReceiver
import com.dongnao.hilt.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mApplicationContextBean: ApplicationContextBean

    @Inject
    lateinit var mApplicationBean: ApplicationBean

    @Inject
    lateinit var mTestHelper: TestHelper

    //----------------------------------------------------------------------------------------------

    @Inject
    lateinit var mActivityContextBean: ActivityContextBean

    @Inject
    lateinit var mActivityBean: ActivityBean

    //----------------------------------------------------------------------------------------------

    @Inject
    lateinit var analyticsService: AnalyticsService

    private lateinit var mDataBinding: ActivityMainBinding

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        analyticsService.analyticsMethods("I am from MainActivity.")

        mApplicationContextBean.showCode()
        mApplicationBean.showCode()

        mTestHelper.loadPrint()

        mActivityContextBean.showCode()
        mActivityBean.showCode()


        val intentFilter = IntentFilter().apply {
            addAction(MyHiltReceiver.ACTION_SEND)
        }
        registerReceiver(mReceiver, intentFilter)
    }

    fun printMsg() {
        Log.d("print_logs", "MainActivity::printMsg")
        sendBroadcast(Intent().apply {
            action = MyHiltReceiver.ACTION_SEND
        })
    }

    private val mReceiver: MyHiltReceiver by lazy { MyHiltReceiver() }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        mDataBinding.unbind()
    }
}