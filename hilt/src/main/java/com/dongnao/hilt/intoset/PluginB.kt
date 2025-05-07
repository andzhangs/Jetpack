package com.dongnao.hilt.intoset

import android.util.Log

/**
 *
 * @author zhangshuai
 * @date 2025/5/6 20:48
 * @description 自定义类描述
 */
class PluginB : Plugin {
    override fun execute() {
        Log.i("print_logs", "PluginB::execute: ")
    }
}