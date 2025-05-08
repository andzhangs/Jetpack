package com.dongnao.hilt.intoset

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
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
abstract class PluginBindModule {

    @IntoMap
    @Binds
    @ClassKey(PluginC::class)
    abstract fun bindPluginC(pluginC: PluginC): Plugin

    @IntoMap
    @Binds
    @ClassKey(PluginD::class)
    abstract fun bindPluginD(pluginD: PluginD): Plugin
}