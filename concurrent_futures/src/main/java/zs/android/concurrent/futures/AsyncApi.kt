package zs.android.concurrent.futures

/**
 *
 * @author zhangshuai
 * @date 2024/4/25 11:22
 * @description 自定义类描述
 */
class AsyncApi {

    interface OnResult {
        fun onSuccess(data: String)
        fun onError(e: Throwable)
    }

    fun load(result: OnResult){}
}