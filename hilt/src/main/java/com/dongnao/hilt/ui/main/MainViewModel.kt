package com.dongnao.hilt.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dongnao.hilt.di.AnalyticsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val analyticsService: AnalyticsService) :
    ViewModel() {

    init {
        analyticsService.analyticsMethods("I am from MainViewModel.init.")
    }

    fun clickInfo() {
        analyticsService.analyticsMethods("I am from MainViewModel.clickInfo")
    }

    val mShowMsg: LiveData<String> = liveData {
        Log.i("print_logs", "MainViewModel::: mShowMsg")
        while (true) {
            emit("当前时间：${System.currentTimeMillis()}")
            delay(1000)
        }
    }.switchMap {
        liveData {
            emit("展示：$it")
        }
    }
}