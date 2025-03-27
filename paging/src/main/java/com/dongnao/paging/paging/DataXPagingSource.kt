package com.dongnao.paging.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dongnao.paging.app.PagingApplication
import com.dongnao.paging.bean.DataX
import com.dongnao.paging.http.ApiService
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.math.max

/**
 *
 * @author zhangshuai
 * @date 2023/11/21 14:51
 * @mark 定义数据源
 */
class DataXPagingSource(private val mCurrentPage: MutableSharedFlow<Triple<Int?,Int,Int?>>) : PagingSource<Int, DataX>() {

    private val STARTING_KEY = 1

    //刷新的起始页码位置
    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        Log.i("print_logs", "getRefreshKey: anchorPosition= $anchorPosition")
//        return state.closestItemToPosition(anchorPosition)?.id ?: return null
//        val refreshKey = state.anchorPosition?.let {
//            val anchorPage = state.closestPageToPosition(it)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//        if (BuildConfig.DEBUG) {
//            Log.i("print_logs", "上一页: $refreshKey")
//        }
            Log.i("print_logs", "getRefreshKey: 刷新列表.")
        return STARTING_KEY
    }

//    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    /**
     * 以异步方式提取更多数据，用于在用户滚动过程中显示
     * @param params 对象保存有与加载操作相关的信息，包括以下信息:
     *   A、要加载的页面的键 - 如果这是第一次调用 load()，LoadParams.key 将为 null。
     *      在这种情况下，必须定义初始页面键。对于我们的项目，我们将报道 ID 用作键。
     *      此外，我们还要在初始页面键的 ArticlePagingSource 文件顶部添加一个为 0 的 STARTING_KEY 常量。
     *   B、加载大小 - 请求加载内容的数量。
     */
    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, DataX> {
        return try {
            val currentPage = params.key ?: STARTING_KEY

            val response = PagingApplication.getInstance().getApiService().getWanData(currentPage, 1)

//            Log.i("print_logs", "response: $response")
//            Logger.json(response.toString())

            val items = response?.data?.datas

            val prevKey = if (currentPage > 1) currentPage - 1 else null
            val nextKey = if ( response?.data?.over == false) currentPage + 1 else null

//            Log.d("print_logs", "prevKey: $prevKey, currentKey：$currentPage, nextKey：$nextKey")

            mCurrentPage.emit(Triple(prevKey,currentPage,nextKey))

            LoadResult.Page(items as List<DataX>, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
//            Log.e("print_logs", "load: $e")
            LoadResult.Error(e)
        }
    }

    override val keyReuseSupported: Boolean = true

    override val jumpingSupported: Boolean = true

}