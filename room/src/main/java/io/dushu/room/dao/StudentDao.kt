package io.dushu.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentWithCourseEntity
import io.dushu.room.entity.view.StudentEntityView
import kotlinx.coroutines.flow.Flow
import java.util.Date

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

    @Transaction
    @Update
    suspend fun update(student: StudentEntity?, courseEntity: CourseEntity?)

    @Transaction
    @Upsert
    suspend fun insertOrUpdate(student: StudentEntity?, courseEntity: CourseEntity?)

    @Query("DELETE FROM TABLE_STUDENT WHERE id=:id")
    suspend fun deleteById(id: Int)

    //@RewriteQueriesToDropUnusedColumns 和 @Transaction 二选一
    @Transaction
    @Query("SELECT * FROM TABLE_STUDENT WHERE id =:id")
    suspend fun getById(id: Int): StudentWithCourseEntity?

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM TABLE_STUDENT WHERE id =:id")
    fun getStudentByIdLiveData(id: Int): LiveData<List<StudentWithCourseEntity>>

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM table_student ORDER BY id")
    fun getAllLiveData(): LiveData<List<StudentWithCourseEntity>>

    @Query("SELECT COUNT(*) FROM TABLE_STUDENT ORDER BY id")
    fun getCountFlow(): Flow<Int>

    //----------------------------------------------------------------------------------------------

    @Query("DELETE FROM TABLE_STUDENT")
    suspend fun clearAll()

    @Query("DELETE FROM sqlite_sequence WHERE name='table_student'")
    suspend fun resetAutoIncrement()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name='table_student'")
    suspend fun setIndexZero()


    @Transaction
    suspend fun clearAndReset() {
        clearAll()
        resetAutoIncrement()
        setIndexZero()
    }

    //----------------------------------------------------------------------------------------------

    @MapInfo(
        keyTable = "table_student",
        keyColumn = "name",
        valueTable = "table_course",
        valueColumn = "course_name"
    )
    @Query("SELECT * FROM TABLE_STUDENT JOIN TABLE_COURSE ON TABLE_STUDENT.name = TABLE_COURSE.user_name WHERE name =:userName")
    suspend fun getAllCourseByUser(userName: String?): Map<String, String>

    //----------------------------------------------------------------------------------------------

    @Query("SELECT * FROM STUDENT_ENTITY_VIEW")
    fun getAllViewFlow(): Flow<MutableList<StudentEntityView>>

    //----------------------------------------------------------------------------------------------

    @Query("SELECT * FROM TABLE_STUDENT WHERE create_time = :date")
    suspend fun getStudentByDate(date: Date): StudentEntity?


}