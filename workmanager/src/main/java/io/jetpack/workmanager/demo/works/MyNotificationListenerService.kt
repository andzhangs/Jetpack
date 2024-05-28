package io.jetpack.workmanager.demo.works

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.content.Context
import android.os.UserHandle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import io.jetpack.workmanager.BuildConfig

class MyNotificationListenerService : NotificationListenerService() {

    /**
     * 检测权限
     * if (BuildConfig.DEBUG) {
     *      val state=NotificationManagerCompat.getEnabledListenerPackages(mContext.applicationContext).contains(mContext.applicationContext.packageName)
     *      Log.i("print_logs", "检测通知: $state")
     * }
     */

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onNotificationPosted: 1")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onNotificationRemoved: 1")
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationPosted(sbn, rankingMap)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onNotificationPosted: 2")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationRemoved(sbn, rankingMap)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onNotificationRemoved: 2")
        }
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification?,
        rankingMap: RankingMap?,
        reason: Int
    ) {
        super.onNotificationRemoved(sbn, rankingMap, reason)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onNotificationRemoved: 3")
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::attachBaseContext: ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onDestroy: ")
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onListenerConnected: ")
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onListenerDisconnected: ")
        }
    }

    override fun onNotificationRankingUpdate(rankingMap: RankingMap?) {
        super.onNotificationRankingUpdate(rankingMap)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onNotificationRankingUpdate: ")
        }
    }

    override fun onListenerHintsChanged(hints: Int) {
        super.onListenerHintsChanged(hints)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onListenerHintsChanged: ")
        }
    }

    override fun onSilentStatusBarIconsVisibilityChanged(hideSilentStatusIcons: Boolean) {
        super.onSilentStatusBarIconsVisibilityChanged(hideSilentStatusIcons)
        if (BuildConfig.DEBUG) {
            Log.i(
                "print_logs",
                "MyNotificationListenerService::onSilentStatusBarIconsVisibilityChanged: "
            )
        }
    }

    override fun onNotificationChannelModified(
        pkg: String?,
        user: UserHandle?,
        channel: NotificationChannel?,
        modificationType: Int
    ) {
        super.onNotificationChannelModified(pkg, user, channel, modificationType)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onNotificationChannelModified: ")
        }
    }

    override fun onNotificationChannelGroupModified(
        pkg: String?,
        user: UserHandle?,
        group: NotificationChannelGroup?,
        modificationType: Int
    ) {
        super.onNotificationChannelGroupModified(pkg, user, group, modificationType)
        if (BuildConfig.DEBUG) {
            Log.i(
                "print_logs",
                "MyNotificationListenerService::onNotificationChannelGroupModified: "
            )
        }
    }

    override fun onInterruptionFilterChanged(interruptionFilter: Int) {
        super.onInterruptionFilterChanged(interruptionFilter)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyNotificationListenerService::onInterruptionFilterChanged: ")
        }
    }
}