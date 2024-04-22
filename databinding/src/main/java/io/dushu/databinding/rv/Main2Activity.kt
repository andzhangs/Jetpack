package io.dushu.databinding.rv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dushu.databinding.*
import io.dushu.databinding.databinding.ActivityMain2Binding
import io.dushu.databinding.databinding.ActivityMainBinding

class Main2Activity : AppCompatActivity() {

    private lateinit var mDataBinding:ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding=DataBindingUtil.setContentView(this, R.layout.activity_main2)
        val list= arrayListOf<DataBean>().apply {
            for (index in 1..15){
                add(DataBean("https://img-blog.csdnimg.cn/20210124002108308.png","斯嘉丽.约翰逊","黑寡妇《复仇者联盟》"))
            }
        }
        mDataBinding.recycleView.apply {
            layoutManager=LinearLayoutManager(this@Main2Activity,RecyclerView.VERTICAL,false)
            adapter=RvAdapter(list)
        }
    }
}
