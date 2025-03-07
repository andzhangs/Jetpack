package io.dushu.room.entity.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity

/**
 *
 * @author zhangshuai
 * @date 2024/5/14 17:25
 * @description 自定义类描述
 */
@Entity
data class StudentWithCoursesEntity(
    @Embedded
    val student: StudentEntity?,

    @Relation(
        parentColumn = "name",
        entity = CourseEntity::class,
        entityColumn = "user_name",
    )
    val courses: MutableList<CourseEntity>?
)