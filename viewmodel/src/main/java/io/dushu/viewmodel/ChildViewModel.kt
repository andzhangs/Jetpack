package io.dushu.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.dushu.viewmodel.user.UserRepository

/**
 * author: zhangshuai 6/26/21 8:02 PM
 * email: zhangshuai@dushu365.com
 * mark:  不要想ViewModel中传入Context 会造成内存泄漏
 *  若需要是Context,请使用AndroidViewModel
 *  反之使用ViewModel
 */
class ChildViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    companion object{
        const val KEY_DATA="key_data"
        const val KEY_INFO="key_info"
    }

    init {
        Log.i(
            "print_logs",
            "ChildViewModel::: ${userRepository.getUserId()}, ${savedStateHandle.get<String>(KEY_DATA)}"
        )
    }
}