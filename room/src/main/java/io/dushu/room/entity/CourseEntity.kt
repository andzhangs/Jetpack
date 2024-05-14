package io.dushu.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.Gson

/**
 *
 * @author zhangshuai
 * @date 2024/5/14 17:02
 * @description 学生课程信息表
 */
@Entity(
    tableName = "table_course",
    foreignKeys =
    [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["name"],
            childColumns = ["user_name"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class CourseEntity(
    //学生名
    @PrimaryKey
    @ColumnInfo(name = "user_name")
    var userName: String,

    //课程名
    @ColumnInfo(name = "course_name")
    var courseName: String,

    //分数
    @ColumnInfo(name = "score")
    var score: Int
) {
    override fun toString(): String = Gson().toJson(this)
}
