package io.dushu.room

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dushu.room.databinding.ActivityMainBinding
import io.dushu.room.entity.Student
import io.dushu.room.viewmodel.StudentViewModel
import kotlinx.coroutines.launch
import java.util.Random

/**
 * @author zhangshuai
 * @date 2022/3/14 22:32
 * @description
 */
class MvvmActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding
    private val list = arrayListOf<Student>()
    private lateinit var mAdapter: MyAdapter

    private val mStudentViewModel: StudentViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this@MvvmActivity, R.layout.activity_main)
        mDataBinding.lifecycleOwner = this

        mAdapter = MyAdapter(list)
        mDataBinding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@MvvmActivity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        getAll()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                mStudentViewModel.getAllStudent3().collect {
                    Log.i("print_logs", "getAllStudent3: ${it.size}")
                }
            }
        }
    }

    private fun getAll() {
        mStudentViewModel.getAllStudent2().observe(this) {
            Log.i("print_logs", "getAllStudent2: ${it.size}")
            list.clear()
            if (it.isNotEmpty()) {
                list.addAll(it)
                mDataBinding.recycleView.smoothScrollToPosition(list.size - 1)
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    fun JumpClick(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun InsertClick(view: View) {
        mStudentViewModel.insert(Student(0, "Hello", Random().nextInt(100)))
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
            mStudentViewModel.update(Student(inputText.toInt(), "Tom", Random().nextInt(100)))
        }
    }

    fun QueryClick(view: View) {
        val inputText = mDataBinding.acEtId.text.toString()
        if (inputText.isNotEmpty()) {
            mStudentViewModel.getStudentById(inputText.toInt()).observe(this) {
                list.clear()
                list.addAll(it)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}