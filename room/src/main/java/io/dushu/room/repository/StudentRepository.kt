package io.dushu.room.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteDatabase
import io.dushu.room.database.StudentDataBase
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentWithCourseEntity
import java.util.Date
import java.util.Random

/**
 * @author zhangshuai
 * @date 2022/3/14 21:53
 * @description
 */
class StudentRepository(private val context: Context) {

    private val mDataBase = StudentDataBase.getInstance(context)
    private val mStudentDao = mDataBase.getStudentDao()
    private val mCourseDao = mDataBase.getCourseDao()

    private val getSqliteDb: SupportSQLiteDatabase by lazy{mDataBase.openHelper.writableDatabase}

    /**
     * 使用ByContentValues新增数据
     */
    fun insertByContentValues(){
        val userName ="你好"+kotlin.random.Random.nextInt(1,100)
        val studentContentValues= contentValuesOf(
            "name" to userName,
            "age" to kotlin.random.Random.nextInt(1,100),
            "create_time" to System.currentTimeMillis()
        )
        getSqliteDb.insert("table_student",SQLiteDatabase.CONFLICT_REPLACE,studentContentValues)

        val courseContentValues= contentValuesOf(
            "user_name" to userName,
            "course_name" to "数学",
            "score" to kotlin.random.Random.nextInt(1,100)
        )
        getSqliteDb.insert("table_course",SQLiteDatabase.CONFLICT_REPLACE,courseContentValues)

    }

    private fun getEntity(
        entity: StudentWithCourseEntity?,
        studentBlock: (StudentEntity) -> Unit,
        courseBlock: (CourseEntity) -> Unit
    ) {
        entity?.student?.let { studentBlock(it) }
        entity?.course?.let { courseBlock(it) }
    }


    /**
     * 精确查找
     */
    fun selectStudentById(id: Int): LiveData<StudentWithCourseEntity> =
        mStudentDao.getStudentByIdLiveData(id)

    /**
     * 查询全部
     */
    fun getAllLiveData() = mStudentDao.getAllLiveData()

    fun getAllStudentCountFlow() = mStudentDao.getAllStudentCountFlow()

    fun getAllCourseCountFlow() = mCourseDao.getAllCourseCountFlow()

    //----------------------------------------------------------------------------------------------
    /**
     * 添加
     */
    suspend fun insertCourse(studentEntity: StudentEntity) {
        mStudentDao.insert(studentEntity)
    }

    suspend fun insertCourse(courseEntity: CourseEntity) {
        mCourseDao.insert(courseEntity)
    }
    suspend fun insertStudent(entity: StudentWithCourseEntity) {
//        insertByContentValues()

        mStudentDao.insert(entity.student, entity.course)
    }



    /**
     * 删除
     */
    suspend fun deleteById(id: Int) {
        mStudentDao.deleteById(id)
    }

    /**
     * 清空
     */
    suspend fun clearAll() {
        mStudentDao.clearAndReset()
    }

    /**
     * 修改
     */
    private val courseNameArray =
        arrayOf("语文", "英语", "化学", "物理", "生物", "体育", "政治", "地理", "历史")

    suspend fun updateStudent(id: Int) {
        mStudentDao.getById(id = id)?.also { entity ->
            getEntity(entity,
                studentBlock = {
                    it.age = Random().nextInt(100)
                }, courseBlock = {
                    it.score = Random().nextInt(100)
                    it.courseName = courseNameArray[Random().nextInt(9)]
                }
            )
            mStudentDao.update(entity.student, entity.course)
        }
    }

    //----------------------------------------------------------------------------------------------

    suspend fun getAllCourseByUser(userName: String?) = mStudentDao.getAllCourseByUser(userName)

    //----------------------------------------------------------------------------------------------

    fun getAllViewFlow() = mStudentDao.getAllViewFlow()

    //----------------------------------------------------------------------------------------------

    suspend fun getStudentByDate(date: Date) = mStudentDao.getStudentByDate(date)


    fun getAllGroupByDayFlow() = mStudentDao.getAllGroupByDayFlow()
    fun getAllGroupByMonthFlow() = mStudentDao.getAllGroupByMonthFlow()
    fun getAllGroupByYearFlow() = mStudentDao.getAllGroupByYearFlow()
}