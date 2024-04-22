package io.dushu.lifcycle.lifecycleservice

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.toPublisher
import io.dushu.lifcycle.R
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class LifeCycleServiceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle_service)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            200
        )


        val mLiveData = MutableLiveData<String>()
        toPublisher(this, mLiveData).subscribe(object : Subscriber<String> {
            override fun onSubscribe(s: Subscription?) {
                Log.i("print_logs", "LifeCycleServiceActivity::onSubscribe: ")
            }

            override fun onError(t: Throwable?) {
                Log.d("print_logs", "LifeCycleServiceActivity::onError: $t")
            }

            override fun onComplete() {
                Log.i("print_logs", "LifeCycleServiceActivity::onComplete: ")
            }

            override fun onNext(t: String?) {
                Log.i("print_logs", "LifeCycleServiceActivity::onNext: $t")
            }
        })

        loadLiveDataReactiveStreams()

        loadPublishProcessorWithLiveDataReactiveStreams()
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

    private var mIntent: Intent? = null

    fun startGpsLocation(view: View) {
        if (mIntent == null) {
            mIntent = Intent(this, MyLocationService::class.java)
        }
        startService(mIntent)
    }

    fun stopGpsLocation(view: View) {
        mIntent?.also {
            stopService(it)
            mIntent = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mIntent?.also {
            stopService(it)
        }
        mIntent = null
    }
}
