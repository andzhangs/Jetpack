package com.dongnao.hilt.reciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dongnao.hilt.di.AnalyticsService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class MyHiltReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SEND = "com.dongnao.hilt.reciver.ACTION_SEND"
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AnalyticsServiceEntryPoint {
        fun analyticsService(): AnalyticsService
    }

    init {
        Log.w("print_logs", "MyHiltReceiver::: init ${hashCode()}")
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_SEND) {
            Log.w("print_logs", "MyHiltReceiver::onReceive: 调用成功！")
            val hiltEntryPoint =
                EntryPointAccessors.fromApplication(context, AnalyticsServiceEntryPoint::class.java)
            val service=hiltEntryPoint.analyticsService()
            service.analyticsMethods("I am from MyHiltReceiver.")
        }
    }
}