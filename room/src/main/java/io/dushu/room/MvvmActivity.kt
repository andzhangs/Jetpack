package io.dushu.room

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dushu.room.databinding.ActivityMainBinding
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentWithCourseEntity
import io.dushu.room.viewmodel.StudentViewModel
import java.util.Random

/**
 * @author zhangshuai
 * @date 2022/3/14 22:32
 * @description
 */
class MvvmActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding
    private lateinit var mAdapter: MyAdapter

    private val mStudentViewModel: StudentViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this@MvvmActivity, R.layout.activity_main)
        mDataBinding.lifecycleOwner = this

        mAdapter = MyAdapter()
        mDataBinding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@MvvmActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        getAll()
    }

    private fun getAll() {
        mStudentViewModel.getAllStudent2().observe(this) {
            if (it.isNotEmpty()) {
                mAdapter.setData(it)
                mDataBinding.recycleView.smoothScrollToPosition(it.size - 1)
            }
        }
    }

    fun InsertClick(view: View) {
        val index = System.currentTimeMillis().toInt()
        val entity = StudentWithCourseEntity(
            student = StudentEntity(name = "用户-$index", age = Random().nextInt(100)),
            course = CourseEntity(
                userName = "用户-$index",
                courseName = "数学",
                score = Random().nextInt(100)
            )
        )
        mStudentViewModel.insert(entity)
    }


    fun DeleteClick(view: View) {
        val inputText = mDataBinding.acEtId.text.toString()
        if (inputText.isNotEmpty()) {
            mStudentViewModel.deleteById(inputText.toInt())
        }
    }

    fun UpdateClick(view: View) {
        val inputText = mDataBinding.acEtId.text.toString()
        if (inputText.isNotEmpty()) {
            mStudentViewModel.update(inputText.toInt())
        }
    }

    fun QueryClick(view: View) {
        val inputText = mDataBinding.acEtId.text.toString()
        if (inputText.isNotEmpty()) {
            mStudentViewModel.getStudentById(inputText.toInt()).observe(this) {
                mAdapter.setData(it)
            }
        }else{
            getAll()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}