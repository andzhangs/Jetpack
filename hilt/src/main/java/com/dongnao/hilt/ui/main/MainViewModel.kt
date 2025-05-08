package com.dongnao.hilt.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dongnao.hilt.analytics.AnalyticsService
import com.dongnao.hilt.intomap.Service
import com.dongnao.hilt.intomap.ServiceA
import com.dongnao.hilt.intoset.Plugin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Provider


/**
 * @JvmSuppressWildcards 看到具体的 Set<Plugin> 而不是 Set<? extends Plugin>
 * @JvmWildcard Java 中显式地看到 Set<? extends Plugin>
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val analyticsService: AnalyticsService,
    private val mPluginSet: Set<@JvmSuppressWildcards Plugin>,
    private val setString: Set<String>,
    private val mProvideServices: Map<Class<out Service>, @JvmSuppressWildcards Provider<Service>>,
    private val mClassServices: Map<Class<out Service>, @JvmSuppressWildcards Service>,
    private val mIntServices: Map<Int, @JvmSuppressWildcards Service>,
    private val bindPlugins: Map<Class<*>, @JvmSuppressWildcards Provider<Plugin>>,
    private val bindClassMultiServices: Map<Class<out Service>, @JvmSuppressWildcards Service>,
    private val bindIntMultiServices: Map<Class<out Service>, @JvmSuppressWildcards Service>

) : ViewModel() {

    init {
        analyticsService.analyticsMethods("I am from MainViewModel.init.")
        loadIntoSet()
        loadElementIntoSet()
        loadIntoMap()
    }

    fun clickInfo() {
        analyticsService.analyticsMethods("I am from MainViewModel.clickInfo")
    }

    val mShowMsg: LiveData<String> = liveData {
        while (true) {
            emit("当前时间：${System.currentTimeMillis()}")
            delay(1000)
        }
    }.switchMap {
        liveData {
            emit("展示：$it")
        }
    }

    fun loadIntoSet() {
        mPluginSet.forEach { plugin ->
            plugin.execute()
        }
    }

    fun loadElementIntoSet() {
        Log.i("print_logs", "loadElementIntoSet: $setString")
    }

    fun loadIntoMap() {
        mProvideServices.forEach { (t, u) ->
            Log.i("print_logs", "mProvideServices: ${t.simpleName}, ${t.hashCode()}， ${u.get().perform()}")
        }
        mClassServices.forEach { (t, u) ->
            Log.w("print_logs", "mClassServices: ${t.simpleName},${t.hashCode()}， ${u.perform()}")
        }

        Log.e("print_logs", "具体类ServiceA: ${mClassServices.get(ServiceA::class.java)?.perform()}")

        mIntServices.forEach { (t, u) ->
            Log.d("print_logs", "IntKey: $t, ${u.perform()}")
        }

        bindPlugins.forEach {( t, u) ->
           u.get().execute()
        }

        bindClassMultiServices.forEach { t, u ->
            Log.w("print_logs", "bindClassMultiServices: ${t.simpleName},${t.hashCode()}， ${u.perform()}")
        }

        bindIntMultiServices.forEach { t, u ->
            Log.d("print_logs", "bindIntMultiServices: ${t.simpleName},${t.hashCode()}， ${u.perform()}")
        }
    }
}