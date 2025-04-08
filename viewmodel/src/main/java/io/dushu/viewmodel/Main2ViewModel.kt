package io.dushu.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * author: zhangshuai 6/26/21 8:02 PM
 * email: zhangshuai@dushu365.com
 * mark:  不要想ViewModel中传入Context 会造成内存泄漏
 *  若需要是Context,请使用AndroidViewModel
 *  反之使用ViewModel
 */
class Main2ViewModel : ViewModel() {

    init {
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "Main2ViewModel::: ${hashCode()}")
        }
    }

    private val mLivedata = MutableLiveData<String>()
    val getLivedata: LiveData<String> = mLivedata

    fun sendMsg(info: String) {
        mLivedata.value = info
    }

    fun printLog(){
        Log.d("print_logs", "Main2ViewModel::printLog: ")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("print_logs", "Main2ViewModel::onCleared: ")
    }
}