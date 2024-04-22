package io.jetpack.workmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.work.WorkManager
import com.android.jetpack.workmanager.works.TestWorker
import io.jetpack.workmanager.base.BaseActivity
import io.jetpack.workmanager.databinding.ActivityMainBinding
import io.jetpack.workmanager.demo.jobs.MainJobActivity
import io.jetpack.workmanager.demo.works.WorkActivity
import io.jetpack.workmanager.utils.viewBinding
import io.jetpack.workmanager.video.MyWorkActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private val mBinding by viewBinding(ActivityMainBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.acBtnGoJob.setOnClickListener {
            startActivity(Intent(this, MainJobActivity::class.java))
        }

        mBinding.acBtnGoWork.setOnClickListener {
            startActivity(Intent(this, WorkActivity::class.java))
        }

        mBinding.acBtnGoMyWork.setOnClickListener {
            startActivity(Intent(this, MyWorkActivity::class.java))
        }

        val aPiService = Api.APiService {
            return@APiService "回调信息：name= $it, age= 18, sex= 男"
        }

        getService(aPiService)

        mBinding.acBtnGoTest.setOnClickListener {
            MyUniqueWork.start(this)
//            test()
//            load()
        }

        WorkManager.getInstance(this).getWorkInfosByTagLiveData(MyUniqueWork::class.java.simpleName).observe(this){
            it.forEach { workInfo ->
                if (BuildConfig.DEBUG) {
                    Log.i("print_logs", "MainActivity::onCreate: ${workInfo.state}")
                }
            }
        }


        TestWorker.setWorkListener(this)
    }

    private fun test(){
        for (i in 1..3){
//            TestWorker.start(this)
            MyUniqueWork.start(this)
        }

//        lifecycleScope.launchWhenCreated {
//            delay(6000L)
//            TestWorker.stop(this@MainActivity)
//        }
    }

    private fun getService(service: Api.APiService) {
        Log.i("print_logs", "MainActivity::onCreate: ${service.getUser("你好")}")
    }

    private fun load(){
        CoroutineScope(Dispatchers.IO).launch {
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "MainActivity::load: start")
            }
            delay(2000L)
            if (BuildConfig.DEBUG) {
                Log.d("print_logs", "MainActivity::load: end")
            }

            cancel("取消了")
        }
    }
}
