package com.dongnao.paging.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dongnao.paging.R
import com.dongnao.paging.bean.DataX

/**
 *
 * @author zhangshuai
 * @date 2025/3/27 12:01
 * @description 自定义类描述
 */
class PagingListAdapter(private val itemClickBlock: (() -> Unit)? = null) :
    PagingDataAdapter<DataX, ItemViewHolder>(object : DiffUtil.ItemCallback<DataX>() {
        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_main,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.load(getItem(position))

        holder.itemView.setOnClickListener {
            itemClickBlock?.invoke()
        }
    }
}