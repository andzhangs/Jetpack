package io.dushu.databinding

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable


/**
 * author: zhangshuai 6/27/21 4:14 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class UserViewModel : BaseObservable() {

    var userBean: UserBean = UserBean("使用BaseObservable")


    @Bindable
    fun getUserName(): String? = userBean.userName

    fun setUserName(str: String?) {
        userBean.userName = str

        notifyPropertyChanged(BR.userName)
        Log.i("print_log","setUserName：$str")
    }


}