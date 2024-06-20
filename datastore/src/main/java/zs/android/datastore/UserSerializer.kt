package zs.android.datastore

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

/**
 *
 * @author zhangshuai
 * @date 2024/6/20 17:25
 * @description 自定义类描述
 */
object UserSerializer : Serializer<String> {
    override val defaultValue: String
        get() = "hhah"

    override suspend fun readFrom(input: InputStream): String {
        return "hah"
    }

    override suspend fun writeTo(t: String, output: OutputStream) {

    }

}