package com.dongnao.paging

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dongnao.paging.app.PagingApplication
import com.dongnao.paging.http.ApiService
import com.dongnao.paging.paging.DataXPagingSource

/**
 *
 * @author zhangshuai
 * @date 2023/11/21 14:50
 * @mark 自定义类描述
 */
class MainViewModel : ViewModel(), DefaultLifecycleObserver {

    private var mApiService: ApiService = PagingApplication.getInstance().getApiService()

    private val pagingSource = DataXPagingSource(mApiService)

    private val pagingConfig = PagingConfig(pageSize = 5, enablePlaceholders = false)

    private val pager = Pager(pagingConfig, pagingSourceFactory = { pagingSource })

    val pagingDataFlow = pager.flow.cachedIn(viewModelScope)

}