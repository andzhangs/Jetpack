package com.dongnao.paging.http

import com.dongnao.paging.bean.WanAndroidBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 * @author zhangshuai
 * @date 2023/11/21 14:40
 * @mark 自定义类描述
 */
interface ApiService {

    @GET("project/list/{pager}/json")
    suspend fun getWanData(
        @Path("pager") pager: Int,
        @Query("cid") cid: Int
    ): WanAndroidBean

}