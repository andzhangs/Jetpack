package zs.android.concurrent.futures

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.concurrent.futures.CallbackToFutureAdapter
import com.google.common.util.concurrent.ListenableFuture

/**
 * CallbackToFutureAdapter是AndroidX库中的一个类，用于将回调形式的异步操作转换为ListenableFuture，
 * 以便更方便地使用Future相关的API。它提供了一种将回调风格的代码与基于Future的代码进行集成的方式
 */
class MainActivity : AppCompatActivity() {

    private val mAsyncApi by lazy { AsyncApi() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        asyncOperation()
    }

    private fun asyncOperation(): ListenableFuture<String> {
        return CallbackToFutureAdapter.getFuture { completer ->
            mAsyncApi.load(object : AsyncApi.OnResult {
                override fun onSuccess(data: String) {
                    completer.set(data)
                }

                override fun onError(e: Throwable) {
                    completer.setException(e)
                }
            })
        }
    }
}