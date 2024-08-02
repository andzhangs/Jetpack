package com.dongnao.hilt.early

import dagger.hilt.InstallIn
import dagger.hilt.android.EarlyEntryPoint
import dagger.hilt.components.SingletonComponent

/**
 *
 * @author zhangshuai
 * @date 2024/5/22 14:17
 * @description  解决某些组件需要在 Application 初始化之前就被依赖注入的问题
 */
@EarlyEntryPoint
@InstallIn(SingletonComponent::class)
interface FooEarlyPoint {
    fun provideFoo(): Foo
}