package zs.android.datastore

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 *
 * @author zhangshuai
 * @date 2024/6/20 13:51
 * @description 自定义类描述
 */
class DataStoreApplication : Application() {

    companion object {
        private lateinit var mInstance: DataStoreApplication
        fun getInstance() = mInstance
    }

    private val mDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    fun getDataStore() = mDataStore
}