package zs.android.jetpack

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import zs.android.jetpack.base.BaseActivity
import zs.android.jetpack.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val province = arrayOf("江西", "湖南")

    private var provinceindex = 0
    private val city = arrayOf(arrayOf("南昌", "赣州"), arrayOf("长沙", "湘潭"))
    private val counstryside = arrayOf(
        arrayOf(arrayOf("青山湖区", "南昌县"), arrayOf("章贡区", "赣县")),
        arrayOf(arrayOf("长沙县", "沙县"), arrayOf("湘潭县", "象限"))
    )
    private lateinit var adapter1: ArrayAdapter<String?>
    private lateinit var adapter2: ArrayAdapter<String?>
    private lateinit var adapter3: ArrayAdapter<String?>

    override fun setLayoutResId() = R.layout.activity_main

    external fun stringFromJNI(): String

    override fun initView(savedInstanceState: Bundle?) {
        adapter1 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, province)
        mDataBinding.spn.adapter = adapter1

        adapter2 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, city[0])
        mDataBinding.city.adapter = adapter2

        adapter3 = ArrayAdapter(
            this, android.R.layout.simple_dropdown_item_1line,
            counstryside[0][0]
        )
        mDataBinding.counstryside.adapter = adapter3
        mDataBinding.spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                Toast.makeText(this@MainActivity, "点击了：${province[position]}", Toast.LENGTH_SHORT).show()

                provinceindex = position
                adapter2 = ArrayAdapter(
                    this@MainActivity, android.R.layout.simple_dropdown_item_1line,
                    city[position]
                )
                mDataBinding.city.adapter = adapter2
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                Toast.makeText(this@MainActivity, "什么也没选中", Toast.LENGTH_SHORT).show()
            }
        }

        mDataBinding.city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?,
                position: Int, id: Long
            ) {

                adapter3 = ArrayAdapter(
                    this@MainActivity, android.R.layout.simple_dropdown_item_1line,
                    counstryside[provinceindex][position]
                )
                //adapter3.notifyDataSetChanged();
                mDataBinding.counstryside.adapter = adapter3
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //当时据为空的时候触发的
            }
        }

        val acBtnViewAnimator = findViewById<AppCompatButton>(R.id.acBtn_ViewAnimator)
        acBtnViewAnimator.setOnClickListener {
//            com.github.florent37.viewanimator.ViewAnimator.animate(it).apply {
//                bounce()
//                textColor(R.color.purple_200)
//                backgroundColor(R.color.white)
//                duration(500)
//                start()
//            }
        }

        Looper.myQueue().addIdleHandler {
            //  在这里去处理你想延时加载的东西
            Toast.makeText(this, "延迟弹出了，${Thread.currentThread().name} ", Toast.LENGTH_SHORT)
                .show()
            // 最后返回false，后续不用再监听了。
            false
        }
    }
}