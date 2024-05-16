package io.dushu.room.database.converters

import androidx.room.TypeConverter
import java.util.Date

/**
 *
 * @author zhangshuai
 * @date 2024/5/16 20:45
 * @description 自定义类描述
 */
class DateConverter {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }
}