package io.dushu.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentWithCourseEntity
import kotlinx.coroutines.flow.Flow

/**
 * author: zhangshuai 6/27/21 8:55 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
@Dao
interface StudentDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity?, courseEntity: CourseEntity?)

    @Query("DELETE FROM table_student WHERE id=:id")
    suspend fun deleteById(id: Int)

    @Transaction
    @Update
    suspend fun update(student: StudentEntity?, courseEntity: CourseEntity?)

    //@RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM table_student WHERE id =:id")
    suspend fun getById(id: Int): StudentWithCourseEntity?

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_student WHERE id =:id")
    fun getStudentByIdLiveData(id: Int): LiveData<List<StudentWithCourseEntity>>

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_student ORDER BY id")
    fun getAllLiveData(): LiveData<List<StudentWithCourseEntity>>

    @Query("SELECT COUNT(*) FROM table_student ORDER BY id")
    fun getCountFlow(): Flow<Int>



    @Query("DELETE FROM table_student")
    suspend fun clearAll()

    @Query("DELETE FROM sqlite_sequence WHERE name='table_student'")
    suspend fun resetAutoIncrement()

    @Transaction
    suspend fun clearAndReset(){
        clearAll()
        resetAutoIncrement()
    }
}