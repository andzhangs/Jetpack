package com.dongnao.hilt.intoset

import android.util.Log
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2025/5/6 20:48
 * @description 自定义类描述
 */
class PluginC @Inject constructor(): Plugin {
    override fun execute() {
        Log.e("print_logs", "PluginC::execute: ")
    }
}