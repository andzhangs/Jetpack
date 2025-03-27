package zs.android.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 *
 * @author zhangshuai
 * @date 2025/3/20 17:35
 * @description 自定义类描述
 */
object DataStorePreferencesHelper {


    @JvmStatic
    fun init(){

    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @JvmStatic
    suspend fun put(context: Context, key: String, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    suspend fun put(context: Context, key: String, value: Int) {
        context.dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    suspend fun put(context: Context, key: String, value: Long) {
        context.dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    suspend fun put(context: Context, key: String, value: Float) {
        context.dataStore.edit {
            it[floatPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    suspend fun put(context: Context, key: String, value: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    suspend fun put(context: Context, key: String, value: Set<String>) {
        context.dataStore.edit {
            it[stringSetPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    suspend fun getString(context: Context, key: String, defaultValue: String): String {
        return context.dataStore.data.map { it[stringPreferencesKey(key)] }.first() ?: defaultValue
    }

    @JvmStatic
    suspend fun getInt(context: Context, key: String, defaultValue: Int): Int {
        return context.dataStore.data.map { it[intPreferencesKey(key)] }.first() ?: defaultValue
    }

    @JvmStatic
    suspend fun getLong(context: Context, key: String, defaultValue: Long): Long {
        return context.dataStore.data.map { it[longPreferencesKey(key)] }.first() ?: defaultValue
    }

    @JvmStatic
    suspend fun getFloat(context: Context, key: String, defaultValue: Float): Float {
        return context.dataStore.data.map { it[floatPreferencesKey(key)] }.first() ?: defaultValue
    }

    @JvmStatic
    suspend fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return context.dataStore.data.map { it[booleanPreferencesKey(key)] }.first() ?: defaultValue
    }

    @JvmStatic
    suspend fun getStringSet(
        context: Context,
        spName: String,
        key: String,
        defaultValue: Set<String>
    ): Set<String> {
        return context.dataStore.data.map { it[stringSetPreferencesKey(key)] }.first() ?: defaultValue
    }

    @JvmStatic
    suspend fun <T> contains(context: Context, key: Preferences.Key<T>): Boolean {
        return context.dataStore.data.map { it.contains(key) }.first()
    }

    @JvmStatic
    suspend fun <T> remove(context: Context, key: Preferences.Key<T>) {
        context.dataStore.edit {
            it.remove(key)
        }
    }

    @JvmStatic
    suspend fun clear(context: Context, spName: String) {
        context.dataStore.edit {
            it.clear()
        }
    }
}
