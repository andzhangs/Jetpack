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
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentWithCourseEntity
import io.dushu.room.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
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
//            launch(Dispatchers.IO) {
//                mStudentRepository.getAllViewFlow().collect {
//                    it.forEachIndexed { index, item ->
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
//                    }
//                }
//            }
//
//            launch {
//                mStudentRepository.getAllGroupByDayFlow().collect{
//                    it.forEach {model->
//                        if (BuildConfig.DEBUG) {
//                            Log.d("print_logs", "日-${model.count}条: ${model.date}, ${model.dataList}")
//                        }
//                    }
//                }
//            }
//            launch {
//                mStudentRepository.getAllGroupByMonthFlow().collect{
//                    it.forEach {model->
//                        if (BuildConfig.DEBUG) {
//                            Log.w("print_logs", "月-${model.count}条: ${model.date}, ${model.dataList}")
//                        }
//                    }
//                }
//            }
//            launch {
//                mStudentRepository.getAllGroupByYearFlow().collect{
//                    it.forEach {model->
//                        if (BuildConfig.DEBUG) {
//                            Log.i("print_logs", "年-${model.count}条: ${model.date}, ${model.dataList}")
//                        }
//                    }
//                }
//            }

            launch {
                //combine 将多个流的最新值结合起来，每当任何流发射新值时都会触发。
                val studentFlow=mStudentRepository.getAllStudentCountFlow()
                val courseFlow=mStudentRepository.getAllCourseCountFlow()
                studentFlow.combine(courseFlow){ studentCount, courseCount ->
                    if (BuildConfig.DEBUG) {
                        Log.i("print_logs", "当前线程：${Thread.currentThread().name}")
                    }
                    mCountField.set("总数：$studentCount")
                    "学生：$studentCount， 课程：$courseCount"
                }.collect{
                    if (BuildConfig.DEBUG) {
                        Log.i("print_logs", "总数量: $it")
                    }
                }
            }
        }
    }

    fun getStudentById(id: Int): LiveData<StudentWithCourseEntity> {
        return mStudentRepository.selectStudentById(id)
    }

    fun getAllStudent(): LiveData<List<StudentWithCourseEntity>> =
        mStudentRepository.getAllLiveData()

    fun insert(studentEntity: StudentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.insertCourse(studentEntity)
        }
    }

    fun insert(course: CourseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mStudentRepository.insertCourse(course)
        }
    }

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