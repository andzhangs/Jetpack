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
import com.dongnao.paging.databinding.LayoutTopRefreshBinding

/**
 *
 * @author zhangshuai
 * @date 2025/3/27 12:10
 * @description 自定义类描述
 */
class TopRefreshAdapter : LoadStateAdapter<TopRefreshViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): TopRefreshViewHolder {
        val binding = DataBindingUtil.inflate<LayoutTopRefreshBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_top_refresh,
            parent,
            false
        )
        return TopRefreshViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopRefreshViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}