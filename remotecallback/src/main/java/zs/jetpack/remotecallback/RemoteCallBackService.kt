package zs.jetpack.remotecallback

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.Toast

class RemoteCallBackService : Service() {

    private val mListeners = RemoteCallbackList<IClientCallBack>()

    override fun onCreate() {
        super.onCreate()
        Log.i("print_logs", "RemoteCallBackService::onCreate: ")
    }

    override fun onBind(intent: Intent): IBinder = object : IServerCallBack.Stub() {

        override fun register(callback: IClientCallBack?) {
            mListeners.register(callback)
            Log.i(
                "print_logs",
                "RemoteCallBackService:: IServerCallBack.register: ${mListeners.beginBroadcast()}, ${mListeners.finishBroadcast()}"
            )
        }

        override fun unregister(callback: IClientCallBack?) {
            Log.i(
                "print_logs",
                "RemoteCallBackService:: IServerCallBack.unregister-before: ${mListeners.beginBroadcast()}, ${mListeners.finishBroadcast()}"
            )
            mListeners.unregister(callback)
            Log.i(
                "print_logs",
                "RemoteCallBackService:: IServerCallBack.unregister-after: ${mListeners.beginBroadcast()}, ${mListeners.finishBroadcast()}"
            )
        }

        /**
         * 接收来自客户端的数据
         * 有个Bug，服务解绑了，还能接收到信息
         */
        override fun callServer(msg: String?) {
            Log.d("print_logs", "RemoteCallBackService::callServer: $msg")

            Toast.makeText(this@RemoteCallBackService, msg, Toast.LENGTH_SHORT).show()

            sendMsg()
        }
    }

    private fun sendMsg() {
        try {
            val len = mListeners.beginBroadcast()
            if (len >= 1) {
                mListeners.getBroadcastItem(len - 1).onReceived("我是来服务的数据！")
            } else {
                Log.e("print_logs", "RemoteCallBackService::callServer: 未注册监听")
            }
            mListeners.finishBroadcast()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("print_logs", "RemoteCallBackService::callServer: $e")
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("print_logs", "RemoteCallBackService::onUnbind: ")
        mListeners.kill()
        return super.onUnbind(intent)
    }

}