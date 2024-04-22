package io.dushu.databinding.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.dushu.databinding.R
import io.dushu.databinding.databinding.LayoutItemRecycleViewBinding

/**
 * author: zhangshuai 6/27/21 5:13 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class RvAdapter(var list: ArrayList<DataBean>) : RecyclerView.Adapter<RvAdapter.ItemViewHolder>() {


    class ItemViewHolder(var binding: LayoutItemRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val dataBinding = DataBindingUtil.inflate<LayoutItemRecycleViewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_item_recycle_view,
            parent,
            false
        )
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.data = list[position]
    }

    override fun getItemCount(): Int = list.size
}