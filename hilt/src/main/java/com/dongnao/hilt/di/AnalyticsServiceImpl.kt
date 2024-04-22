package com.dongnao.hilt.di

import android.util.Log
import com.dongnao.hilt.model.ApplicationContextBean
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2023/12/23 16:38
 * @description 自定义类描述
 */
class AnalyticsServiceImpl @Inject constructor(private val appBean: ApplicationContextBean) : AnalyticsService {

    override fun analyticsMethods(info: String) {
        Log.e("print_logs", "analyticsMethods: $info ${appBean.printLog()}")

    }
}