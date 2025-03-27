package zs.android.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.nio.charset.StandardCharsets


/**
 *
 * @author zhangshuai
 * @date 2025/3/20 18:08
 * @description 自定义类描述
 */
object UserDataStore {


    @JvmStatic
    fun init(context: Context): DataStore<User> {
        return DataStoreFactory.create(UserSerializer(), produceFile = {
            File(context.applicationContext.getExternalFilesDir("datastore"), "user.pb")
        })
    }

    class UserSerializer : Serializer<User> {

        override val defaultValue: User
            get() = User()

        override suspend fun readFrom(input: InputStream): User {
//            val stringBuilder = StringBuilder()
//            BufferedReader(InputStreamReader(input)).use { reader ->
//                var line: String?
//                while ((reader.readLine().also { line = it }) != null) {
//                    stringBuilder.append(line)
//                }
//            }
//            return Json.decodeFromString<User>(stringBuilder.toString())
            val stringBuilder = StringBuilder()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while ((withContext(Dispatchers.IO) {
                    input.read(buffer)
                }.also { bytesRead = it }) != -1) {
                stringBuilder.append(String(buffer, 0, bytesRead, StandardCharsets.UTF_8))
            }
            val parts = stringBuilder.toString().split(",")
            if (parts.size == 2) {
                val name = parts[0]
                val age = parts[1].toInt()
                return User(name, age)
            }
            return User()
        }

        override suspend fun writeTo(t: User, output: OutputStream) {
            val data = "${t.name},${t.age}"
            withContext(Dispatchers.IO) {
                output.write(data.toByteArray(StandardCharsets.UTF_8))
            }

//            withContext(Dispatchers.IO) {
//                val data=Json.encodeToString(t)
//                output.write(data.toByteArray(StandardCharsets.UTF_8))
//            }
        }
    }

}