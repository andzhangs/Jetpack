package com.jetpack.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 *
 * @author zhangshuai
 * @date 2023/8/22 11:59
 * @mark 自定义类描述
 */
object ShareConfig {

    enum class MimeType(private val value: String) {
        TEXT("text/*"),
        IMAGE("image/*"),
        VIDEO("video/*"),
        AUDIO("audio/*");

        fun get() = value
    }

    /**
     * 指定应用要与之互动的一组其他应用
     */
    @JvmStatic
    fun setUriQueries(context: Context, uri: Uri) {
        context.resources.getStringArray(R.array.package_name_to_uri).forEach {
            context.grantUriPermission(it, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}