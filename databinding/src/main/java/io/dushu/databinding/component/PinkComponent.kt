package io.dushu.databinding.component

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

/**
 *
 * @author zhangshuai
 * @date 2024/12/12 10:25
 * @description 自定义类描述
 */
class PinkComponent {
    @BindingAdapter("android:bindName")
    fun AppCompatTextView.setBindName(name:String?){

        if (!name.isNullOrEmpty() && name != text) {
            text = "数据体"
        }
    }
}