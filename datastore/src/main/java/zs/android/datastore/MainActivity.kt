package zs.android.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import zs.android.datastore.databinding.ActivityMainBinding

/**
 * https://developer.android.com/topic/libraries/architecture/datastore?hl=zh-cn
 *
 * DataStore 提供两种不同的实现：
 * Preferences DataStore 使用键存储和访问数据。此实现不需要预定义的架构，也不确保类型安全。
 * Proto DataStore 将数据作为自定义数据类型的实例进行存储。此实现要求您使用协议缓冲区来定义架构，但可以确保类型安全。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    private val mDataStore: DataStore<Preferences> = DataStoreApplication.getInstance().getDataStore()
    private val USER_NAME_KEY = stringPreferencesKey("user_name")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mDataBinding.lifecycleOwner = this

        mDataBinding.acBtnSave.setOnClickListener {
            save()
        }
        printData()
    }

    /**
     * 保存数据
     */
    private fun save() {
        lifecycleScope.launch {
            //写入数据
            mDataStore.edit {
                it[USER_NAME_KEY] = mDataBinding.acEtText.text.toString()
            }
        }
    }

    /**
     * /**
     * 读取数据
    */
     */
    private fun printData(){
        lifecycleScope.launch {
            mDataStore.data.map {
                it[USER_NAME_KEY]
            }.stateIn(lifecycleScope, SharingStarted.Eagerly, null)
                .collect {
                    Log.i("print_logs", "打印: $it")

                    mDataBinding.acEtText.text = null
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
//        UserBeanOuterClass.UserBean.newBuilder().build()
    }
}