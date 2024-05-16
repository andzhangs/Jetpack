package io.dushu.room.entity.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.TypeConverters
import com.google.gson.Gson
import io.dushu.room.database.converters.DateConverter
import java.util.Date

/**
 *
 * @author zhangshuai
 * @date 2024/5/16 17:47
 * @description 自定义类描述
 */
@DatabaseView(
    viewName = "student_entity_View",
    value = "SELECT name,age,create_time,course_name,score FROM TABLE_STUDENT JOIN TABLE_COURSE ON TABLE_STUDENT.name = TABLE_COURSE.user_name"
)
data class StudentEntityView(
    //学生名
    var name: String,
    //年纪
    var age: Int,

    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "create_time")
    var createTime: Date,

    //课程名
    @ColumnInfo(name = "course_name")
    var courseName: String,

    //分数
    var score: Int
) {
    override fun toString(): String = Gson().toJson(this)
}
