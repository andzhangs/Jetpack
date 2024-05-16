package io.dushu.room.repository

import android.content.Context
import androidx.lifecycle.LiveData
import io.dushu.room.database.StudentDataBase
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentWithCourseEntity
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

    /**
     * 精确查找
     */
    fun selectStudentById(id: Int): LiveData<List<StudentWithCourseEntity>> =
        mStudentDao.getStudentByIdLiveData(id)

    /**
     * 查询全部
     */
    fun getAllLiveData() = mStudentDao.getAllLiveData()

    suspend fun getCountFlow() = mStudentDao.getCountFlow()

    //----------------------------------------------------------------------------------------------
    /**
     * 添加
     */
    fun insertStudent(entity: StudentWithCourseEntity) {
        mStudentDao.insert(entity.student, entity.course)
    }

    /**
     * 删除
     */
    fun deleteById(id: Int) {
        mStudentDao.deleteById(id)
    }

    /**
     * 清空
     */
    fun clearAll() {
        mStudentDao.clearAll()
//        mCourseDao.clearAll()
    }


    /**
     * 修改
     */
    fun updateStudent(id: Int) {
        mStudentDao.getById(id = id)?.also { entity ->
            getEntity(entity,
                studentBlock = {
                    it.age = Random().nextInt(100)
                }, courseBlock = {
                    it.score = Random().nextInt(100)
                    it.courseName = "化学"
                }
            )
            mStudentDao.update(entity.student, entity.course)
        }
    }

    private fun getEntity(
        entity: StudentWithCourseEntity?,
        studentBlock: (StudentEntity) -> Unit,
        courseBlock: (CourseEntity) -> Unit
    ) {
        entity?.student?.let { studentBlock(it) }
        entity?.course?.let { courseBlock(it) }
    }
}