package io.jetpack.workmanager.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.evernote.android.job.JobManager
import io.jetpack.workmanager.demo.jobs.DemoJobCreator

/**
 * @author zhangshuai
 * @date 2022/4/2 星期六
 * @email zhangshuai@dushu365.com
 * @description
 */
open class WMApplication : Application() {

    companion object {
        private var application: WMApplication? = null
        @JvmStatic
        fun getInstance(): WMApplication = application!!
    }

    private val demoJobCreator = DemoJobCreator()

    override fun onCreate() {
        super.onCreate()
        application = this
        JobManager.create(this).addJobCreator(demoJobCreator)

//        Configuration.Builder().setMinimumLoggingLevel(Log.DEBUG).build()
    }

    override fun onTerminate() {
        super.onTerminate()
        JobManager.create(this).removeJobCreator(demoJobCreator)
    }
}