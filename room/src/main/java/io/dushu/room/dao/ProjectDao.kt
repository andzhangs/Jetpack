package io.dushu.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

/**
 *
 * @author zhangshuai
 * @date 2024/5/14 17:20
 * @description 课程
 */
@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg projectEntity: ProjectEntity)

    @Delete
    fun delete(vararg projectEntity: ProjectEntity)

    @Update
    fun update(vararg projectEntity: ProjectEntity)

    @Query("DELETE FROM TABLE_PROJECT")
    fun clearAll()
}