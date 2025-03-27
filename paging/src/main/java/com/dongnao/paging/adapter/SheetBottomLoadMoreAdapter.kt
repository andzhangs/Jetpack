package com.dongnao.paging.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongnao.paging.R
import com.dongnao.paging.databinding.LayoutSheetBottomLoadMoreBinding

/**
 *
 * @author zhangshuai
 * @date 2025/3/27 12:10
 * @description 自定义类描述
 */
class SheetBottomLoadMoreAdapter : LoadStateAdapter<SheetBottomViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): SheetBottomViewHolder {
        val binding = DataBindingUtil.inflate<LayoutSheetBottomLoadMoreBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_sheet_bottom_load_more,
            parent,
            false
        )
        return SheetBottomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SheetBottomViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}