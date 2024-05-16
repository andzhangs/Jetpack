package io.dushu.room.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.dushu.room.entity.relation.StudentWithCourseEntity
import io.dushu.room.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author zhangshuai
 * @date 2022/3/14 21:55
 * @description
 */
class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val mStudentRepository = StudentRepository(application)

    val mCountField=ObservableField("总数：0")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.getCountFlow().collect{
                mCountField.set("总数：$it")
            }
        }
    }

    fun getStudentById(id: Int): LiveData<List<StudentWithCourseEntity>> {
        return mStudentRepository.selectStudentById(id)
    }

    fun getAllStudent(): LiveData<List<StudentWithCourseEntity>> =  mStudentRepository.getAllLiveData()

    fun insert(student: StudentWithCourseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.insertStudent(student)
        }
    }

    fun deleteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.deleteById(id)
        }
    }

    fun update(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.updateStudent(id)
        }
    }
}