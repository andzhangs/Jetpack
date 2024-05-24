package com.dongnao.hilt.early

import android.util.Log
import com.dongnao.hilt.BuildConfig
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2024/5/22 14:17
 * @description 自定义类描述
 */
class Foo @Inject constructor() {
    fun load(){
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "Foo::load: ")
        }
    }
}