package io.dushu.room.entity.relation

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 *
 * @author zhangshuai
 * @date 2024/5/16 20:25
 * @description 自定义类描述
 */
@Entity(tableName = "table_student_course_join", primaryKeys = ["name", "user_name"])
data class StudentCourseJoin(
    //学生名
    var name: String,

    //学生名
    @ColumnInfo(name = "user_name")
    var userName: String
)
