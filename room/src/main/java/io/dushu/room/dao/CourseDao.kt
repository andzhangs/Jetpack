package io.dushu.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.dushu.room.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

/**
 *
 * @author zhangshuai
 * @date 2024/5/14 17:20
 * @description 课程
 */
@Dao
interface CourseDao {

    @Insert
    fun insert(vararg courseEntity: CourseEntity)

    @Delete
    fun delete(vararg courseEntity: CourseEntity)

    @Update
    fun update(vararg courseEntity: CourseEntity)

    @Query("DELETE FROM TABLE_COURSE")
    fun clearAll()

    @Query("SELECT COUNT(*) FROM TABLE_COURSE")
    fun getAllCourseCountFlow(): Flow<Int>
}