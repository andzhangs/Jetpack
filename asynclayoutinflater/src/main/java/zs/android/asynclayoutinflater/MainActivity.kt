package zs.android.asynclayoutinflater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.asynclayoutinflater.view.AsyncLayoutInflater

/**
 * AndroidX库中的AsyncLayoutInflater是一个用于异步加载布局的工具类。
 * 它可以在后台线程中加载布局资源，并在加载完成后将结果传递到主线程以进行显示。
 * 这对于在主线程中加载复杂布局或包含大量视图的布局时特别有用，以避免阻塞UI线程。
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val parentView=findViewById<LinearLayoutCompat>(R.id.ll_root)

        val inflater = AsyncLayoutInflater(this)
        inflater.inflate(R.layout.layout_child_view, null) { view, resid, parent ->
            Log.i("print_logs", "onCreate: ${Thread.currentThread().name}")
            parentView.addView(view)
        }
    }
}