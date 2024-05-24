package zs.android.camerax

import android.hardware.camera2.CameraDevice

/**
 *
 * @author zhangshuai
 * @date 2024/5/20 18:27
 * @description 摄像头数据类
 */
class CameraInfoModel {
    var cameraId: String? = null
    var face: String? = null
    var cameraService: CameraDevice? = null
    var state: Int? = null

    override fun toString(): String {
        return "CameraInfoModel(cameraId=$cameraId, face=$face, cameraService=$cameraService, state=$state)"
    }
}
