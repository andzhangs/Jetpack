package com.dongnao.hilt.intomap

import dagger.MapKey
import kotlin.reflect.KClass

/**
 *
 * @author zhangshuai
 * @date 2025/5/7 10:22
 * @description 自定义类描述
 */
@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ServiceLazyClassKey(val value: KClass<out Service>)
