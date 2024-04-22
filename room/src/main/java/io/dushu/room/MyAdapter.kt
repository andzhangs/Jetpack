package io.dushu.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.dushu.room.databinding.LayoutItemBinding
import io.dushu.room.entity.Student

/**
 * author: zhangshuai 6/27/21 10:28 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class MyAdapter(var list: ArrayList<Student>) : RecyclerView.Adapter<MyAdapter.ItemViewHolder>() {

    class ItemViewHolder(var dataBinding: LayoutItemBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val dataBinding = DataBindingUtil.inflate<LayoutItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_item,
            parent,
            false
        )
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (list.size>0){
            holder.dataBinding.data = list[position]
        }
    }

    override fun getItemCount(): Int = if (list.size > 0) list.size else 0
}