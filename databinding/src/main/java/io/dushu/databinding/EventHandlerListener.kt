package io.dushu.databinding

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import io.dushu.databinding.demo.Main3Activity
import io.dushu.databinding.rv.Main2Activity

/**
 * author: zhangshuai 6/26/21 11:56 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class EventHandlerListener constructor(private val context: Context) {

    fun btnOnClick(view: View) {
        Toast.makeText(context, "喜欢", Toast.LENGTH_SHORT).show()
        view.context.startActivity(Intent(view.context, Main2Activity::class.java))
    }

    fun btnOnClickDemo(view: View){
        view.context.startActivity(Intent(view.context, Main3Activity::class.java))
    }

}