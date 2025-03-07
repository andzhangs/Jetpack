package io.dushu.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson

/**
 * author: zhangshuai 6/27/21 7:14 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
@Entity(
    tableName = "table_student",
    indices = [Index(value = ["name"], unique = true)] //设置了unique = true，表示"name"列的值必须是唯一的。
)
data class StudentEntity constructor(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id: Int = 0,

    //学生名
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String = "",

    //年纪
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    var age: Int = 0,

    @ColumnInfo(name = "create_time")
    var createTime: Long = System.currentTimeMillis()

    //    @TypeConverters(DateConverter::class)
//    @ColumnInfo(name = "create_date")
//    var createDate: Date = Date()

    //用户会员图标
//    @ColumnInfo(name = "level_url")
//    var levelUrl: String = ""
) {
//    @Ignore
//    constructor() : this(0)

    override fun toString(): String = Gson().toJson(this)
}