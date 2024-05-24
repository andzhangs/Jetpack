package com.dongnao.hilt.early

import dagger.hilt.InstallIn
import dagger.hilt.android.EarlyEntryPoint
import dagger.hilt.components.SingletonComponent

/**
 *
 * @author zhangshuai
 * @date 2024/5/22 14:17
 * @description 自定义类描述
 */
@EarlyEntryPoint
@InstallIn(SingletonComponent::class)
interface FooEarlyPoint {
    fun provideFoo(): Foo
}