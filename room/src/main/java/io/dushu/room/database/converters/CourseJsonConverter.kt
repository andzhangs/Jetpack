package io.dushu.room.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.dushu.room.entity.CourseEntity

/**
 *
 * @author zhangshuai
 * @date 2024/5/16 20:45
 * @description 自定义类描述
 */
class CourseJsonConverter {

    private val mGson = Gson()

    @TypeConverter
    fun entityToString(courseEntity: CourseEntity): String {
        return mGson.toJson(courseEntity)
    }

    @TypeConverter
    fun stringToEntity(jsonString: String): CourseEntity {
        return mGson.fromJson(jsonString, CourseEntity::class.java)
    }
}