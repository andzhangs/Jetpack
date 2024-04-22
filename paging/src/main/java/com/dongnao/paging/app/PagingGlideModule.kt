package com.dongnao.paging.app

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 *
 * @author zhangshuai
 * @date 2023/11/21 14:15
 * @mark 自定义类描述
 */
@GlideModule
class PagingGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}