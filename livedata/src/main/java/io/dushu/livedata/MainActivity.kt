package io.dushu.livedata

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                MyViewModel::class.java
            )

        lifecycle.addObserver(viewModel)

        val textview=findViewById<AppCompatTextView>(R.id.textview)

        viewModel.getCurrentSecond().observe(this) { t ->
            textview.text = t.toString()
        }


        NetWorkLiveData.getInstance(this@MainActivity)
            .observe(this@MainActivity){
                Log.i("print_logs", "$it")
            }

        loadListener()
        with(arrayListOf(1,2,3,4,5)){
            forEachIndexed { _, it ->
                if (BuildConfig.DEBUG) {
                    Log.i("print_logs", "1- $it")
                }
                if (it == 3) {
                    if (BuildConfig.DEBUG) {
                        Log.i("print_logs", "2- $it")
                    }
                    return@with
                }
                if (BuildConfig.DEBUG) {
                    Log.d("print_logs", "3- $it")
                }
            }
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "MainActivity::onCreate: 1")
            }
        }
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MainActivity::onCreate: 2")
        }
    }

    fun clickEvent(view: View) {
        viewModel.startTimer()
    }

    fun clickLiveDataTransformations(view: View) {
        viewModel.setUserData(User("张", "帅"))
    }

    fun clickMediatorLiveData(view: View) {
        viewModel.setMediatorLiveData("Hello world, ")
    }

    private fun loadListener() {
        // FIXME: 2021/12/6 激活MediatorLiveData 方式一
        viewModel.getMediatorLiveData().observe(this) {
            Log.i("print_logs", "MediatorLiveData激活: $it")
        }
    }

    private fun loadLiveDataReactiveStreams() {
//        LiveDataReactiveStreams.fromPublisher(object : Publisher<String> {
//            override fun subscribe(s: Subscriber<in String>?) {
//                s?.onNext("我是来自 'fromPublisher' ")
//                s?.onComplete()
//            }
//        }).observe(this, object : Observer<String> {
//            override fun onChanged(t: String?) {
//                Log.i("print_logs", "LifeCycleServiceActivity::onChanged: $t")
//            }
//        })
    }

    private fun loadPublishProcessorWithLiveDataReactiveStreams() {
//        val stringProcessor = PublishProcessor.create<String>()
//        val mLivedata = LiveDataReactiveStreams.fromPublisher(stringProcessor)
//        Observable.interval(0, 1, TimeUnit.SECONDS)
//            .map { it ->
//                Log.d("print_logs", "PublishProcessor发送值： $it")
//                stringProcessor.onNext(it.toString())
//                return@map it
//            }.subscribe()
//
//        mLivedata.observe(this) {
//            Log.i("print_logs", "LiveDataReactiveStreams接收值: $it")
//        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
        viewModel.unsetMediatorLiveData()
    }
}
