package com.dongnao.paging.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dongnao.paging.util.TransformationUtils
import com.dongnao.paging.bean.DataX
import com.dongnao.paging.databinding.LayoutItemMainBinding

/**
 *
 * @author zhangshuai
 * @date 2025/3/27 12:02
 * @description 自定义类描述
 */
class ItemViewHolder(
    private val itemBinding: LayoutItemMainBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun load(data: DataX?) {
        data?.also {
            itemBinding.data = it

            val transformData = TransformationUtils.getRandom()
            itemBinding.acTvTransformName.text = transformData.name
            Glide.with(itemBinding.acIvIcon.context)
                .load(it.envelopePic)
                .apply(RequestOptions.bitmapTransform(transformData.transform))
                .into(itemBinding.acIvIcon)
        }
    }
}