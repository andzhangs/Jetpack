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
import io.dushu.room.entity.relation.StudentWithCoursesEntity
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

    private val courseNameArray =arrayOf("语文", "英语", "化学", "物理", "生物", "体育", "政治", "地理", "历史")

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

    private val mStudentEntity by lazy { StudentEntity() }
    private val mCourseEntity by lazy { CourseEntity() }

    fun InsertClick(view: View) {
//        extracted()
//        insertException()
        inserts()
    }

    private fun extracted() {
        val index = "${System.currentTimeMillis()}"

        mStudentEntity.apply {
            name = "用户2024"
            age = Random.nextInt(1, 100)
            createTime = Calendar.getInstance().let {
                it.set(Random.nextInt(1970, 2024), Random.nextInt(1, 12), Random.nextInt(1, 30))
                it.timeInMillis
            }
        }

        mCourseEntity.apply {
            userName = "用户2024"
            courseName = courseNameArray.random()
            score = Random.nextInt(1, 100)
        }

        val entity = StudentWithCourseEntity(
            student = mStudentEntity,
            course = mCourseEntity
        )
        mStudentViewModel.insert(entity)
    }

    private fun insertException(){
        val studentName = "用户2024"

        mStudentEntity.apply {
            name = studentName
            age = Random.nextInt(1,100)
            createTime = Calendar.getInstance().let {
                it.set(Random.nextInt(1970,2024),Random.nextInt(1,12), Random.nextInt(1,30))
                it.timeInMillis
            }
        }

        mStudentViewModel.insert(mStudentEntity)

        mCourseEntity.apply {
            userName =  studentName
            courseName = courseNameArray.random()
            score = Random.nextInt(1,100)
        }

        //若关联的主表没有先插入数据，则会报错：
        // android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: table_course.user_name (code 2067 SQLITE_CONSTRAINT_UNIQUE[2067])
        //  at android.database.sqlite.SQLiteConnection.nativeExecuteForLastInsertedRowId(Native Method)
        mStudentViewModel.insert(mCourseEntity)
    }


    private fun inserts() {

        mStudentEntity.apply {
            name = "用户2024"
            age = Random.nextInt(1, 100)
            createTime = Calendar.getInstance().let {
                it.set(Random.nextInt(1970, 2024), Random.nextInt(1, 12), Random.nextInt(1, 30))
                it.timeInMillis
            }
        }

        val mList= mutableListOf<CourseEntity>(
            CourseEntity( userName = "用户2024", courseName = courseNameArray[1], score = 9),
            CourseEntity( userName = "用户2024", courseName = courseNameArray[2], score = 19),
            CourseEntity( userName = "用户2024", courseName = courseNameArray[3], score = 29),
            CourseEntity( userName = "用户2024", courseName = courseNameArray[4], score = 39),
            CourseEntity( userName = "用户2024", courseName = courseNameArray[5], score = 49),
        )

        val entity = StudentWithCoursesEntity(
            student = mStudentEntity,
            courses = mList
        )
        mStudentViewModel.inserts(entity)
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