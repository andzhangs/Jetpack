package zs.jetpack.loader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.loader.app.LoaderManager
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader

/**
 * 加载配置更改后继续存在的界面数据。
 * https://developer.android.google.cn/guide/components/loaders?hl=zh-cn
 */
class MainActivity : AppCompatActivity() {

    private val LOADER_ID = 100
    private lateinit var mLoader: Loader<UserBean>

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LoaderManager.getInstance(this).restartLoader(
            LOADER_ID,
            Bundle().apply { putString("key", "I'm from onNewIntent!") },
            mLoaderCallbacks
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLoader = LoaderManager.getInstance(this).initLoader(
            LOADER_ID,
            Bundle().apply { putString("key", "I'm from MainActivity!") },
            mLoaderCallbacks
        )
//        mLoader.registerListener(LOADER_ID, mOnLoadCompleteListener)
//        mLoader.registerOnLoadCanceledListener(mOnLoadCanceledListener)

        findViewById<AppCompatButton>(R.id.acBtn_re_intent).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private val mOnLoadCompleteListener = Loader.OnLoadCompleteListener<UserBean> { loader, data ->
        Log.i("print_logs", "MainActivity::OnLoadCompleteListener: $data")
    }

    private val mOnLoadCanceledListener = Loader.OnLoadCanceledListener<UserBean> {
        Log.i("print_logs", "MainActivity::: OnLoadCanceledListener")
    }

    private val mLoaderCallbacks = object : LoaderCallbacks<UserBean> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<UserBean> {
            Log.i("print_logs", "MainActivity::onCreateLoader: ${args?.getString("key")}")
            return MyLoader(this@MainActivity)
        }

        override fun onLoaderReset(loader: Loader<UserBean>) {
            Log.i("print_logs", "MainActivity::onLoaderReset: ")
        }

        override fun onLoadFinished(loader: Loader<UserBean>, data: UserBean?) {
            //// 处理加载完成后的数据
            //// 将数据传递给 UI 线程进行显示
            Log.i("print_logs", "MainActivity::onLoadFinished: ")
        }

    }

    private class MyLoader(context: Context) : AsyncTaskLoader<UserBean>(context) {

        init {
            Log.i("print_logs", "MyLoader::: ")
        }

        override fun loadInBackground(): UserBean {
            Log.i("print_logs", "MyLoader::loadInBackground: ")
            return UserBean("你好！！！")
        }

        override fun onLoadInBackground(): UserBean? {
            Log.i("print_logs", "MyLoader::onLoadInBackground: ")
            return super.onLoadInBackground()
        }

        override fun onStartLoading() {
            super.onStartLoading()
            Log.i("print_logs", "MyLoader::onStartLoading: ")
        }

        override fun onStopLoading() {
            super.onStopLoading()
            Log.i("print_logs", "MyLoader::onStopLoading: ")
        }

        override fun onForceLoad() {
            super.onForceLoad()
            Log.i("print_logs", "MyLoader::onForceLoad: ")
        }

        override fun onAbandon() {
            super.onAbandon()
            Log.i("print_logs", "MyLoader::onAbandon: ")
        }

        override fun onReset() {
            super.onReset()
            Log.i("print_logs", "MyLoader::onReset: ")
        }

        override fun onContentChanged() {
            super.onContentChanged()
            Log.i("print_logs", "MyLoader::onContentChanged: ")
        }

        override fun onCanceled(data: UserBean?) {
            super.onCanceled(data)
            Log.i("print_logs", "MyLoader::onCanceled: ")
        }

        override fun onCancelLoad(): Boolean {
            Log.i("print_logs", "MyLoader::onCancelLoad: ")
            return super.onCancelLoad()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        mLoader.unregisterListener(mOnLoadCompleteListener)
//        mLoader.unregisterOnLoadCanceledListener(mOnLoadCanceledListener)

        LoaderManager.getInstance(this).destroyLoader(LOADER_ID)
    }
}