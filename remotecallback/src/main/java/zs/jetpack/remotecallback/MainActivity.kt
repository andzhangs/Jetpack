package zs.jetpack.remotecallback

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import zs.jetpack.remotecallback.databinding.ActivityMainBinding

/**
 * RemoteCallback 是一个用于在 Android 应用程序中处理远程回调的类。
 * 它提供了一种机制，使您可以在应用程序的不同组件之间进行跨进程通信，并处理远程方法调用的结果。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    private val serverIntent: Intent by lazy { Intent(this, RemoteCallBackService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        clickListeners()
    }

    private fun clickListeners() {
        //方式一
        mDataBinding.acBtnClick.setOnClickListener {
            sendMsg()
        }

        //绑定服务
        mDataBinding.acBtnBind.setOnClickListener {
            bindService(serverIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        //解绑服务
        mDataBinding.acBtnUnbind.setOnClickListener {
            mIServerCallBack?.unregister(mClientCallBack)
            unbindService(serviceConnection)
        }

        mDataBinding.acBtnCallServer.setOnClickListener {
            mIServerCallBack?.callServer("我是来自客户端的，绑定成功了！")
        }
    }

    private var mIServerCallBack: IServerCallBack? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("print_logs", "MainActivity::onServiceConnected: ")
            mIServerCallBack = IServerCallBack.Stub.asInterface(service)
            try {
                mIServerCallBack?.register(mClientCallBack)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("print_logs", "MainActivity::onServiceDisconnected: ")
        }
    }

    private val mClientCallBack = object : IClientCallBack.Stub() {

        override fun onReceived(msg: String?) {
            Log.i("print_logs", "MainActivity::onReceived: $msg")
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------
     *
     * ---------------------------------------------------------------------------------------------
     * description：方式一
     */

    private val mListeners: RemoteCallbackList<IDataInfoCallBack> by lazy { RemoteCallbackList<IDataInfoCallBack>() }

    private fun sendMsg() {
        val len = mListeners.beginBroadcast()
        for (i in 0 until len) {
            val callBack = mListeners.getBroadcastItem(i)
            callBack.onCall(100)
        }
        mListeners.finishBroadcast()
    }

    override fun onStart() {
        super.onStart()
        mListeners.register(mDataInfoCallBack)
    }

    private val mDataInfoCallBack = object : IDataInfoCallBack.Stub() {

        override fun onCall(value: Int) {
            Log.i("print_logs", "MainActivity::onCall: $value")
        }
    }

    override fun onPause() {
        super.onPause()
        mListeners.kill()
        mListeners.unregister(mDataInfoCallBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}