package io.dushu.databinding

import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load

/**
 * author: zhangshuai 6/27/21 12:59 AM
 * email: zhangshuai@dushu365.com
 * mark:
 */

/**
 * 加载网络图片
 */
@BindingAdapter("httpUrl")
fun setNetworkImage(img: AppCompatImageView, url: String) {
    Log.i("print_log", "setImage：${img.id}, $url")
//    val dispose = img.load(url)
    //手动销毁
//    dispose.dispose()
}

/**
 * 加载本地图片
 */
@BindingAdapter("assetUrl")
fun setAssetImage(img: AppCompatImageView, fileName: String) {
    Log.i("print_log", "setLocalImage：${img.id}, $fileName")
    try {
        val inputStream=img.context.assets.open(fileName)
        val bitmap=BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        img.load(bitmap)
    }catch (e: Exception){
        e.printStackTrace()
        Log.e("print_log", "异常输出：${e.message}")
    }
}

/**
 * 加载Drawable图片
 */
@BindingAdapter("intResImg")
fun setLocalResImage(img: AppCompatImageView, @DrawableRes resId: Int) {
    Log.i("print_log", "setLocalResImage：${img.id}, $resId")
    img.load(resId)
}

