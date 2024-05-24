package zs.android.camerax

import androidx.camera.video.Quality

/**
 *
 * @author zhangshuai
 * @date 2024/5/21 17:53
 * @description 自定义类描述
 */
data class VideoModel (

    //选中当前分辨率
    var selected: Boolean = false,

    var qualityLevel: String = "",

    var quality: Quality,
)