package zs.android.camerax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * https://developer.android.google.cn/jetpack/androidx/releases/camera?hl=zh-cn#1.4.0-alpha01
 * IdleHandler的适用场景：
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

}