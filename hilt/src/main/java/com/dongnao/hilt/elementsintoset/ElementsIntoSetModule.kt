package com.dongnao.hilt.elementsintoset

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

/**
 *
 * @author zhangshuai
 * @date 2025/5/6 20:50
 * @description 自定义类描述
 */
@Module
@InstallIn(SingletonComponent::class)
class ElementsIntoSetModule {

    @Provides
    @ElementsIntoSet
    fun provideSetString(): Set<String> {
        return setOf("1", "2", "3", "4")
    }
}