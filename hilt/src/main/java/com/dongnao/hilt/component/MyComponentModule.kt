package com.dongnao.hilt.component

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.Multibinds
import javax.inject.Singleton

/**
 *
 * @author zhangshuai
 * @date 2025/5/7 10:27
 * @description 自定义类描述
 */
@Module
@InstallIn(SingletonComponent::class)
class MyComponentModule {

    @Provides
    @Singleton
    fun bindServiceA(application: Application): AppModel {
        return AppModel("I'm ${application.packageName}")
    }
}