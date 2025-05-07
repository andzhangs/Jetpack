package com.dongnao.hilt.component

import android.app.Application
import com.dongnao.hilt.app.ExampleApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 *
 * @author zhangshuai
 * @date 2025/5/7 11:43
 * @description 自定义类描述
 */
@Singleton
@Component(modules = [MyComponentModule::class])
interface MyComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance  //它允许你在组件构建时传入一个实例，从而将这个实例绑定到依赖图中
        fun provideContext(context: Application): Builder
        fun build(): MyComponent
    }

    fun inject(application: ExampleApplication)
}