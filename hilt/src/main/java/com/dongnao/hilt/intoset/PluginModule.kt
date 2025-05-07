package com.dongnao.hilt.intoset

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import javax.inject.Singleton

/**
 *
 * @author zhangshuai
 * @date 2025/5/6 20:50
 * @description 自定义类描述
 */
@Module
@InstallIn(SingletonComponent::class)
class PluginModule {

    @Provides
    @IntoSet
    @Singleton
    fun providePluginA(): Plugin {
        return PluginA()
    }

    @Provides
    @IntoSet
    @Singleton
    fun providePluginB(): Plugin {
        return PluginB()
    }
}