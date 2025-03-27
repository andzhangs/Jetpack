package com.dongnao.paging.util

import android.graphics.Color
import com.dongnao.paging.R
import jp.wasabeef.glide.transformations.BitmapTransformation
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import jp.wasabeef.glide.transformations.MaskTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation
import kotlin.random.Random

/**
 *
 * @author zhangshuai
 * @date 2023/11/21 18:04
 * @mark 自定义类描述
 */
object TransformationUtils {

    private val transformations = arrayListOf<TransformData>().apply {
        //裁剪自定义圆
        add(TransformData(name = "裁剪自定义圆", transform = CropTransformation(80, 80, CropTransformation.CropType.CENTER)))

        //裁剪圆形
        add(TransformData(name = "裁剪圆形", transform =CropCircleTransformation()))
        //裁剪带边框圆形
        add(TransformData(name = "裁剪带边框圆形", transform =CropCircleWithBorderTransformation(1, Color.BLUE)))
        //裁剪正方形
        add(TransformData(name = "裁剪正方形", transform =CropSquareTransformation()))
        //裁边角
        add(TransformData(name = "裁边角", transform =RoundedCornersTransformation(255, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT)))
        //外形轮廓
        add(TransformData(name = "外形轮廓", transform =MaskTransformation(R.drawable.ic_launcher_foreground)))
        //颜色过滤
        add(TransformData(name = "颜色过滤", transform =ColorFilterTransformation(Color.YELLOW)))
        //添加高斯模糊
        add(TransformData(name = "添加高斯模糊", transform =BlurTransformation(5)))
        //色彩置灰
        add(TransformData(name = "色彩置灰", transform =GrayscaleTransformation()))

        /**
         * 使用GPU
         */
        //卡通过滤
        add(TransformData(name = "卡通过滤", transform =ToonFilterTransformation()))
        //棕色过滤
        add(TransformData(name = "棕色过滤", transform =SepiaFilterTransformation()))
        //对比度
        add(TransformData(name = "对比度", transform =ContrastFilterTransformation()))
        //反色
        add(TransformData(name = "反色", transform =InvertFilterTransformation()))
        //像素化
        add(TransformData(name = "像素化", transform =PixelationFilterTransformation()))
        //素描
        add(TransformData(name = "素描", transform =SketchFilterTransformation()))
        //画面旋转
        add(TransformData(name = "画面旋转", transform =SwirlFilterTransformation()))
        //亮度
        add(TransformData(name = "亮度", transform =BrightnessFilterTransformation(0.1f)))
        //加载
        add(TransformData(name = "加载", transform =KuwaharaFilterTransformation(50)))
        //边框阴影
        add(TransformData(name = "边框阴影", transform =VignetteFilterTransformation()))
    }


    private val mRandom: Random by lazy { Random(transformations.size) }
    fun getRandom(): TransformData {

        val position = mRandom.nextInt(0, transformations.size - 1)
        return transformations[position]
    }

    data class TransformData(val name: String, val transform: BitmapTransformation)
}