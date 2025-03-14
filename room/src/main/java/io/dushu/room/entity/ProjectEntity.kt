package io.dushu.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson

/**
 *
 * @author zhangshuai
 * @date 2024/5/14 17:02
 * @description 学生课程信息表
 */
@Entity(
    tableName = "table_project"
    ,indices = [Index(value = ["project_name"], unique = true)]
)
data class ProjectEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    //课程名
    @ColumnInfo(name = "project_name")
    var projectName: String = ""

) {
    override fun toString(): String = Gson().toJson(this)
}
