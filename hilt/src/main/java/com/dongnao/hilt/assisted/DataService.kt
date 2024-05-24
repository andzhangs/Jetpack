package com.dongnao.hilt.assisted

import android.util.Log
import com.dongnao.hilt.BuildConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 *
 * @author zhangshuai
 * @date 2024/5/22 13:56
 * @description 自定义类描述
 */

data class DataService @AssistedInject constructor(@Assisted val data2: String) {

    fun load() {
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "DataService::load: data2= $data2")
        }
    }
}
