package io.dushu.room.repository

import android.content.Context
import androidx.lifecycle.LiveData
import io.dushu.room.dao.StudentDao
import io.dushu.room.database.StudentDataBase
import io.dushu.room.entity.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author zhangshuai
 * @date 2022/3/14 21:53
 * @description
 */
class StudentRepository(private val context: Context) {

    private val mStudentDao: StudentDao by lazy {
        StudentDataBase.getInstance(context).getStudentDao()
    }

    /**
     * 添加
     */
    fun insertStudent(student: Student) {
        mStudentDao.insert(student)
    }

    /**
     * 删除
     */
    fun deleteById(id: Int) {
        mStudentDao.deleteById(id)
    }

    /**
     * 删除
     */
    fun deleteStudent(student: Student?) {
        student?.apply {
            mStudentDao.delete(this)
        }
    }

    /**
     * 清空
     */
    fun clearAll() {
        mStudentDao.clearAll()
    }


    /**
     * 修改
     */
    fun updateStudent(student: Student?) {
        student?.apply {
            mStudentDao.update(student)
        }
    }


    /**
     * 精确查找
     */
    fun selectStudentById(id: Int): LiveData<List<Student>> = mStudentDao.getStudentById(id)

    /**
     * 查询全部
     */
    fun getAll(): LiveData<List<Student>> = mStudentDao.getAllStudent2()

    fun getAllFlow(): Flow<List<Student>> = mStudentDao.getAllStudent3().flowOn(Dispatchers.IO)

}