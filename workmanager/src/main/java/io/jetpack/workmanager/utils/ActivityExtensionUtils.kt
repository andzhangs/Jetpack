package io.jetpack.workmanager.utils

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * @author zhangshuai
 * @date 2022/4/3 星期日
 * @email zhangshuai@dushu365.com
 * @description activity扩展类
 */
inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val invoke = bindingInflater.invoke(layoutInflater)
        setContentView(invoke.root) //可选
        invoke
    }
