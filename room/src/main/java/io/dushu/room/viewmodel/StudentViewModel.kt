package io.dushu.room.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.dushu.room.BuildConfig
import io.dushu.room.entity.relation.StudentWithCourseEntity
import io.dushu.room.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author zhangshuai
 * @date 2022/3/14 21:55
 * @description
 */
@RequiresApi(Build.VERSION_CODES.N)
class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val mStudentRepository = StudentRepository(application)

    val mCountField = ObservableField("总数：0")

    init {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                mStudentRepository.getCountFlow().collectLatest {
                    mCountField.set("总数：$it")
                }
            }

            launch(Dispatchers.IO) {
                mStudentRepository.getAllViewFlow().collect {
                    it.forEachIndexed { index, item ->

//                        if (index == 0) {
//                            mStudentRepository.getStudentByDate(item.createTime)?.also {entity->
//                                if (BuildConfig.DEBUG) {
//                                    Log.d("print_logs", "$entity")
//                                }
//                            }
//                        }

//                        if (BuildConfig.DEBUG) {
//                            Log.i("print_logs", "当前数据：$item")
//                        }
                    }
                }
            }

            launch {
                mStudentRepository.getAllGroupByDayFlow().collect{
                    it.forEach {model->
                        if (BuildConfig.DEBUG) {
                            Log.i("print_logs", "日: ${model.date}, ${model.count}")
                        }
                    }
                }
            }
            launch {
                mStudentRepository.getAllGroupByMonthFlow().collect{
                    it.forEach {model->
                        if (BuildConfig.DEBUG) {
                            Log.i("print_logs", "月: ${model.date}, ${model.count}")
                        }
                    }
                }
            }
            launch {
                mStudentRepository.getAllGroupByYearFlow().collect{
                    it.forEach {model->
                        if (BuildConfig.DEBUG) {
                            Log.i("print_logs", "年: ${model.date}, ${model.count}")
                        }
                    }
                }
            }
        }
    }

    fun getStudentById(id: Int): LiveData<List<StudentWithCourseEntity>> {
        return mStudentRepository.selectStudentById(id)
    }

    fun getAllStudent(): LiveData<List<StudentWithCourseEntity>> =
        mStudentRepository.getAllLiveData()

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

    fun update(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.updateStudent(id)
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.clearAll()
        }
    }

    //----------------------------------------------------------------------------------------------

    fun getAllCourseByUser(userName: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.getAllCourseByUser(userName).forEach { (t, u) ->
                if (BuildConfig.DEBUG) {
                    Log.i("print_logs", "getAllCourseByUser：$t, $u")
                }
            }
        }
    }
}