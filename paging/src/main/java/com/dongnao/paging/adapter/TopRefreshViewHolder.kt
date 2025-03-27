package com.dongnao.paging.adapter

import androidx.core.view.isGone
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.dongnao.paging.databinding.LayoutSheetBottomLoadMoreBinding
import com.dongnao.paging.databinding.LayoutTopRefreshBinding

/**
 *
 * @author zhangshuai
 * @date 2025/3/27 12:02
 * @description 自定义类描述
 */
class TopRefreshViewHolder(
    private val itemBinding: LayoutTopRefreshBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(loadState: LoadState) {
        itemBinding.clRoot.isGone = loadState is LoadState.Error
    }
}