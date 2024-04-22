package io.dushu.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.dushu.room.entity.Student
import io.dushu.room.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhangshuai
 * @date 2022/3/14 21:55
 * @description
 */
class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val mStudentRepository: StudentRepository by lazy { StudentRepository(application) }

    fun insert(student: Student) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mStudentRepository.insertStudent(student)
            }
        }
    }

    fun delete(student: Student) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mStudentRepository.deleteStudent(student)
            }
        }
    }

    fun deleteById(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mStudentRepository.deleteById(id)
            }
        }
    }

    fun update(student: Student) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mStudentRepository.updateStudent(student)
            }
        }
    }

    fun getStudentById(id: Int): LiveData<List<Student>> {
//        var data = arrayListOf<Student>()
//        viewModelScope.async {
//            withContext(Dispatchers.IO) {
//                data = mStudentRepository.selectStudentById(id) as ArrayList<Student>
//            }
//        }
//        return data
        return mStudentRepository.selectStudentById(id)
    }


    fun getAllStudent2(): LiveData<List<Student>> {
        return mStudentRepository.getAll()
    }


    fun getAllStudent3(): Flow<List<Student>> {
        return mStudentRepository.getAllFlow()
    }

}