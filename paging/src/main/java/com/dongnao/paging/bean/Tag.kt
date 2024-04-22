package com.dongnao.paging.bean

import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val url: String
) {
    override fun toString(): String = Gson().toJson(this)
}