package com.apk.downloadinstall.navigation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import java.io.DataOutputStream

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<AppCompatButton>(R.id.acBtnHome)?.setOnClickListener {

            //传值方式一
//            val toBundle1 = HomeFragmentArgs("你好", 11).toBundle()
//            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_detailFragment,toBundle1)

            //传值方式二
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("你好")
            it.findNavController().navigate(action)
        }


        //deeplink
        view.findViewById<AppCompatButton>(R.id.acBtnDeeplink_PendingIntent)?.setOnClickListener {
            sendNotification(it)
        }

        //Url
        view.findViewById<AppCompatButton>(R.id.acBtnSend_Url).setOnClickListener {
            Toast.makeText(activity, "请执行 adb 命令", Toast.LENGTH_SHORT).show()
            //执行命令
            // adb shell am start -a android.intent.action.VIEW -d "http://www.dongnaoedu.com/fromWeb"
        }
    }

    var ids: Int = 0

    private fun sendNotification(view:View) {
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    it.packageName,
                    "下载文件通知",  //通知类别，在手机设置的应用程序中对应的APP的"通知"中可见
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "My Notification Channel"
                    enableLights(true)
                    enableVibration(true)
                    vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                    lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    setShowBadge(true) //设置是否显示角标
                }
                val notificationManager = it.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(notificationChannel)
            }

            val notification = NotificationCompat.Builder(it, it.packageName)
                .setSmallIcon(R.drawable.ic_launcher_foreground) //通知栏的左上角小图标
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)) //通知栏右边内容图标
                .setContentTitle("下载完成")
                .setContentText("点击安装")
                .setPriority(NotificationCompat.PRIORITY_MAX) //当发出此类型的通知时，通知会以悬挂的方法显示在屏幕上
                .setAutoCancel(true) //点击通知后清除
                .setOngoing(true) // 除非app死掉或者在代码中取消，否则都不会消失。
                .setDefaults(Notification.DEFAULT_ALL)// DEFAULT_VIBRATE  添加默认震动提醒  需要VIBRATE permission; DEFAULT_SOUND  添加默认声音提醒; DEFAULT_LIGHTS  添加默认三色灯提醒;  DEFAULT_ALL  添加默认以上3种全部提醒
                .setVibrate(longArrayOf(100, 200, 300, 400, 500)) //设置震动，需要配置权限(android.permission.VIBRATE)
                .setLights(Color.RED, 300, 0) //设置呼吸灯
                .setTicker("通知到达的时候会在状态栏上方直接显示通知")
                .setAllowSystemGeneratedContextualActions(true)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
//                .setStyle(NotificationCompat.BigTextStyle().bigText("在APP中的开发中会用到提醒，通知类似的功能，之前一篇文章我已经介绍到了[**《Android使用NotificationListenerService监听手机收到的通知》**](https://zhuanlan.zhihu.com/p/62380569)，监听了通知，没有通知的实现"))
                .setStyle(NotificationCompat.InboxStyle()
                    .addLine("1、在APP中的开发中会用到提醒，通知类似的功能")
                    .addLine("2、使用NotificationListenerService监听")
                    .addLine("3、监听了通知，没有通知的实现"))

                //方式一
//                .addAction(R.drawable.ic_launcher_foreground,"点击安装",createPendingIntent()) //设置意图
                //方式二
//                .setFullScreenIntent(createPendingIntent(),false)  //不建议使用这个方法，华为小米适配有问题
                //方式三
                .setContentIntent(getPendingIntent(view))
                .build()
            NotificationManagerCompat.from(it).apply {
                notify(ids++, notification)
            }
        }
    }

    /**
     * deeplink之PendingIntent方式
     */
    private fun getPendingIntent(view:View): PendingIntent? {
        return activity?.let {
            Navigation.findNavController(view)
                .createDeepLink()
                .setGraph(R.navigation.nav_gragh)
                .setDestination(R.id.detailFragment)
                .setArguments(HomeFragmentArgs(deeplinkParams = "我是来自通知栏的DeepLink数据").toBundle())
                .createPendingIntent()
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val clickIntent = Intent(activity, OnNotificationCompatClickReceiver::class.java).apply {
            putExtra("param3", "From：安装成功！")
        }
        return PendingIntent.getBroadcast(activity?.applicationContext, ids, clickIntent, 0)
    }

    internal class OnNotificationCompatClickReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, intent?.getStringExtra("param3"), Toast.LENGTH_SHORT).show()
            NotificationManagerCompat.from(context!!).cancelAll()
        }
    }
}