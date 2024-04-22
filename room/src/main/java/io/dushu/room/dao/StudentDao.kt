package io.dushu.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.dushu.room.entity.Student
import kotlinx.coroutines.flow.Flow

/**
 * author: zhangshuai 6/27/21 8:55 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
@Dao
interface StudentDao {

    @Insert
    fun insert(vararg student: Student)

    @Query("DELETE FROM student WHERE id=:id")
    fun deleteById(id: Int)

    @Delete
    fun delete(student: Student)

    @Query("DELETE FROM student")
    fun clearAll()

    @Update
    fun update(student: Student)

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM student ORDER BY id")
    fun getAllStudent(): MutableList<Student>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM student WHERE id =:id")
    fun getStudentById(id: Int): LiveData<List<Student>>


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM student ORDER BY id")
    fun getAllStudent2(): LiveData<List<Student>>

    @Query("SELECT * FROM student ORDER BY id")
    fun getAllStudent3(): Flow<List<Student>>

}