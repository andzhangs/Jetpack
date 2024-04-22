package io.dushu.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.dushu.databinding.*
import io.dushu.databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.data= Idol("斯嘉丽.约翰逊",4)
//        dataBinding.networkImage= "sijiali2.jpeg" //"https://img-blog.csdnimg.cn/20210124002108308.png"
        dataBinding.localImage= R.drawable.sijiali
        dataBinding.subLayout.clickListener= EventHandlerListener(this)

        dataBinding.user= UserViewModel()
        dataBinding.user2= UserViewModel2()
    }
}
