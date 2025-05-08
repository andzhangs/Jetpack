package com.dongnao.hilt.intomap

import dagger.Binds
import dagger.Module
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
abstract class ServiceModule {

    @Binds
    @IntoMap
    @ServiceClassKey(ServiceA::class)
    abstract fun bindServiceA(serviceA: ServiceA): Service

    @Binds
    @IntoMap
    @ServiceClassKey(ServiceB::class)
    abstract fun bindServiceB(serviceB: ServiceB): Service

    @Binds
    @IntoMap
    @ServiceIntKey(1)
    abstract fun bindIntServiceA(serviceA: ServiceA): Service

    @Binds
    @IntoMap
    @ServiceIntKey(2)
    abstract fun bindIntServiceB(serviceB: ServiceB): Service


    @Multibinds //生成的实例对应@IntoMap，即返回Map类型
    abstract fun bindClassMultiServiceMap(): Map<Class<Service>, @JvmSuppressWildcards Service>

    @Multibinds //生成的实例对应@IntoMap，即返回Map类型
    abstract fun bindIntMultiServiceMap(): Map<Int, @JvmSuppressWildcards Service>
}