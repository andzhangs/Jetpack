package com.dongnao.hilt.analytics

/**
 *
 * @author zhangshuai
 * @date 2023/12/23 16:38
 * @description 自定义类描述
 */
interface AnalyticsService {
    fun analyticsMethods(info: String)
    fun load()
}