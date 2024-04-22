package io.dushu.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel

/**
 * author: zhangshuai 6/26/21 8:02 PM
 * email: zhangshuai@dushu365.com
 * mark:  不要想ViewModel中传入Context 会造成内存泄漏
 *  若需要是Context,请使用AndroidViewModel
 *  反之使用ViewModel
 */
class MyViewModel(application: Application) : AndroidViewModel(application) {

    var number: Int = 0

    fun showToast(string: String) {
        Toast.makeText(getApplication(), string, Toast.LENGTH_SHORT).show()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("print_logs", "MyViewModel::onCleared: ")
    }
}