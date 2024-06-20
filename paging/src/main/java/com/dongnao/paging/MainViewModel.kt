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
 *
 */
class MainViewModel : ViewModel(), DefaultLifecycleObserver {

    private var mApiService: ApiService = PagingApplication.getInstance().getApiService()

    private val pagingSource = DataXPagingSource(mApiService)

    //1. pageSize:
    //作用: 指定每次分页加载的数据量。
    //类型: Int
    //默认值: 无，必须指定
    //说明:
    //选择合适的 pageSize 至关重要。过小的值会导致频繁的网络请求，而过大的值则会增加内存消耗。
    //最佳的 pageSize 取决于您的应用程序需求、数据大小以及网络状况。
    //2. prefetchDistance:
    //作用: 控制预取下一页数据的时机。
    //类型: Int
    //默认值: pageSize
    //说明:
    //当剩余数据量小于 prefetchDistance 时，Paging 库会自动开始加载下一页数据。
    //设置较大的 prefetchDistance 可以减少用户等待数据加载的时间，但也会增加内存消耗。
    //如果您的应用程序对加载速度要求较高，可以将 prefetchDistance 设置为 pageSize 的几倍。
    //3. initialLoadSize:
    //作用: 指定初始加载的数据量。
    //类型: Int
    //默认值: pageSize * 3
    //说明:
    //通常情况下，initialLoadSize 应该大于 pageSize，以确保首次加载足够的数据填充 UI。
    //4. enablePlaceholders:
    //作用: 控制是否启用占位符。
    //类型: Boolean
    //默认值: true
    //说明:
    //当启用占位符时，Paging 库会在数据加载完成之前使用占位符填充 UI，从而提供更流畅的用户体验。
    //如果您的数据源已经支持返回占位符数据，则可以将此参数设置为 false。
    //5. maxSize:
    //作用: 限制 Paging 库缓存的最大数据量。
    //类型: Int
    //默认值: Int.MAX_VALUE
    //说明:
    //设置 maxSize 可以限制内存消耗，但可能会导致旧数据被清除。
    //如果您的应用程序需要缓存大量数据，可以将 maxSize 设置为一个较大的值。
    //6. jumpThreshold:
    //作用: 控制当用户快速滚动到距离当前位置较远的位置时，是否跳过中间数据的加载。
    //类型: Int
    //默认值: Int.MIN_VALUE
    //说明:
    //设置较大的 jumpThreshold 可以优化快速滚动时的性能，但可能会导致某些数据无法加载。
    private val pagingConfig = PagingConfig(
        pageSize = 50,              // 每次加载 50 条数据
        prefetchDistance = 15,      // 当剩余 15 条数据时开始预取
        enablePlaceholders = false, // 启用占位符
        initialLoadSize = 150,      // 初始加载 40 条数据
        maxSize = 200,              // 最大缓存 200 条数据
        jumpThreshold = 50          // 当快速滚动超过 50 条数据时跳过中间数据的加载
    )

    private val pager = Pager(pagingConfig, pagingSourceFactory = { pagingSource })

    val pagingDataFlow = pager.flow.cachedIn(viewModelScope)

}