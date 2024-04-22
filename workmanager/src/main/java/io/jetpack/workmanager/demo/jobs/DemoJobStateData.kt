package io.jetpack.workmanager.demo.jobs

import java.io.Serializable
import java.util.*

/**
 * @author zhangshuai
 * @date 2022/4/2 星期六
 * @email zhangshuai@dushu365.com
 * @description
 */
class DemoJobStateData (var jobId: Int?, var timer: Timer?, var timerTask: TimerTask?) : Serializable {}