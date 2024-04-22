package com.apk.downloadinstall.navigation

import android.app.Application

/**
 * @author zhangshuai
 * @date 2022/3/25 星期五
 * @email zhangshuai@dushu365.com
 * @description
 */
class NavigationApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //修改/dev/bus/usb/文件夹的权限，注意添加\n
//        val commend = "chmod 777 /dev/bus/usb/ -R \n"
//        val result = RootCmd.execRootCmd(commend)
    }


}