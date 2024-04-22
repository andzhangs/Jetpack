package com.dongnao.paging

import android.graphics.Color
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
import java.util.Random

/**
 *
 * @author zhangshuai
 * @date 2023/11/21 18:04
 * @mark 自定义类描述
 */
object TransformationUtils {

    private val transformations = arrayListOf<BitmapTransformation>().apply {
        //裁剪自定义圆
        add(CropTransformation(80, 80, CropTransformation.CropType.CENTER))
        //裁剪圆形
        add(CropCircleTransformation())
        //裁剪带边框圆形
        add(CropCircleWithBorderTransformation(1, Color.BLUE))
        //裁剪正方形
        add(CropSquareTransformation())
        //裁边角
        add(RoundedCornersTransformation(255, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT))
        //外形轮廓
        add(MaskTransformation(R.drawable.ic_launcher_foreground))
        //颜色过滤
        add(ColorFilterTransformation(Color.YELLOW))
        //添加高斯模糊
        add(BlurTransformation(5))
        //色彩置灰
        add(GrayscaleTransformation())

        /**
         * 使用GPU
         */
        //卡通过滤
        add(ToonFilterTransformation())
        //棕色过滤
        add(SepiaFilterTransformation())
        //对比度
        add(ContrastFilterTransformation())
        //反色
        add(InvertFilterTransformation())
        //像素化
        add(PixelationFilterTransformation())
        //素描
        add(SketchFilterTransformation())
        //画面旋转
        add(SwirlFilterTransformation())
        //亮度
        add(BrightnessFilterTransformation(0.1f))
        //加载
        add(KuwaharaFilterTransformation(50))
        //边框阴影
        add(VignetteFilterTransformation())
    }


    private val mRandom: Random by lazy { Random() }
    fun getRandom(): BitmapTransformation {
        val position = mRandom.nextInt(transformations.size - 1)
        return transformations[position]
    }
}