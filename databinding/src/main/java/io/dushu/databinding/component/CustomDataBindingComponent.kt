package io.dushu.databinding.component

import androidx.databinding.DataBindingComponent

/**
 *
 * @author zhangshuai
 * @date 2024/12/12 10:12
 * @description 自定义类描述
 */
class CustomDataBindingComponent: DataBindingComponent {
    override fun getPinkComponent(): PinkComponent {
        return PinkComponent()
    }
}