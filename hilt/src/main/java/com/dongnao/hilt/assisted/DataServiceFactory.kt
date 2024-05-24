package com.dongnao.hilt.assisted

import dagger.assisted.AssistedFactory

/**
 *
 * @author zhangshuai
 * @date 2024/5/22 13:58
 * @description 自定义类描述
 */
@AssistedFactory
interface DataServiceFactory {
    fun create(data2: String): DataService
}