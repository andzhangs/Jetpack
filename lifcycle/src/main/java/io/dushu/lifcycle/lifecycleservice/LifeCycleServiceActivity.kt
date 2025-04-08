package io.dushu.lifcycle.lifecycleservice

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toPublisher
import io.dushu.lifcycle.R
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

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
    }


    private val mIntent: Intent by lazy { Intent(this, MyLocationService::class.java) }

    fun startGpsLocation(view: View) {
        startService(mIntent)
    }

    fun stopGpsLocation(view: View) {
        stopService(mIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mIntent?.also {
            stopService(it)
        }
    }
}
