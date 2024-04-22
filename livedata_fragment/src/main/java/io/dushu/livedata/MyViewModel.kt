package io.dushu.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * author: zhangshuai 6/26/21 10:22 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class MyViewModel : ViewModel() {

    private val livdata: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply {
            value = 0
        }
    }

    fun getProgress(): LiveData<Int> = livdata

    fun setProgress(i: Int) {
        livdata.value = i
    }

    override fun onCleared() {
        Log.i("print_logs", "MyViewModel::onCleared:()")
    }
}