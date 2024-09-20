package io.dushu.room

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dushu.room.databinding.ActivityMainBinding
import io.dushu.room.entity.CourseEntity
import io.dushu.room.entity.StudentEntity
import io.dushu.room.entity.relation.StudentWithCourseEntity
import io.dushu.room.viewmodel.StudentViewModel
import java.util.Calendar
import kotlin.random.Random

/**
 * @author zhangshuai
 * @date 2022/3/14 22:32
 * @description
 */
@RequiresApi(Build.VERSION_CODES.N)
class MvvmActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding
    private lateinit var mAdapter: MyAdapter

    private val mStudentViewModel: StudentViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this@MvvmActivity, R.layout.activity_main)
        mDataBinding.lifecycleOwner = this
        mDataBinding.vm = mStudentViewModel

        mAdapter = MyAdapter().apply {
            setOnItemClickListener(object : MyAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, data: StudentWithCourseEntity) {
                    Toast.makeText(this@MvvmActivity, "${data.student}", Toast.LENGTH_SHORT).show()
                    mStudentViewModel.getAllCourseByUser(data.student?.name)
                }
            })
        }
        mDataBinding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@MvvmActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@MvvmActivity, RecyclerView.VERTICAL))
            adapter = mAdapter
        }

        getAll()

        val timeStamp=System.currentTimeMillis()
        val calendar= Calendar.getInstance().apply {
            timeInMillis=timeStamp
        }

        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day= calendar.get(Calendar.DAY_OF_MONTH)

//        if (BuildConfig.DEBUG) {
//            Log.i("print_logs", "onCreate: year = $year, month = $month, day = $day")
//        }
    }

    private fun getAll() {
        mStudentViewModel.getAllStudent().observe(this) {
            mAdapter.setData(it)
            if (it.isNotEmpty()) {
                mDataBinding.recycleView.smoothScrollToPosition(it.size - 1)
            }
        }
    }

    fun InsertClick(view: View) {
        val index = System.currentTimeMillis()
        val studentName = "用户$index"
        val entity = StudentWithCourseEntity(
            student = StudentEntity(
                name = studentName,
                age = Random.nextInt(1,100), //index+TimeUnit.DAYS.toMillis(Random().nextInt(10).toLong())
                createTime = Calendar.getInstance().let {
                it.set(Random.nextInt(1970,2024),Random.nextInt(1,12), Random.nextInt(1,30))
                    it.timeInMillis
                }

//                createTime = Date()
            ),
            course = CourseEntity(
                userName = studentName,
                courseName = "数学",
                score = Random.nextInt(1,100)
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
                mAdapter.setData(listOf(it))

            }
        } else {
            getAll()
        }
    }

    fun ClearClick(view: View) {
        mStudentViewModel.clearAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }

}