package io.dushu.livedata

import android.util.Log
import androidx.lifecycle.*
import java.util.*

/**
 * author: zhangshuai 6/26/21 8:37 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class MyViewModel : ViewModel(), DefaultLifecycleObserver {

    private var index = 0
    private val mTimer: Timer by lazy { Timer() }

    private val liveData: MutableLiveData<Int> = MutableLiveData<Int>()

    fun getCurrentSecond(): MutableLiveData<Int> {
        return liveData
    }

    fun add() {
        liveData.postValue(++index)
        Log.i("print_log", "接收：$index")

    }


    fun startTimer() {
        mTimer.schedule(object : TimerTask() {
            override fun run() {
                add()
            }
        }, 1000, 1000)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.i("print_log", "取消：")
        mTimer.cancel()
    }

    override fun onCleared() {
        Log.i("print_log", "MyViewModel::onCleared：")
    }

    /**
     *                                      LiveData扩展函数 之 Transformations
     * ---------------------------------------------------------------------------------------------
     *
     * 链接 https://blog.csdn.net/catzifeng/article/details/108189050
     * ---------------------------------------------------------------------------------------------
     */

    private val userLiveData: MutableLiveData<User> = MutableLiveData()

    /**
     * Transformations.map
     */
    //map() 的操作已经是在消费上层 LiveData 的值
    private val transformationsMap = userLiveData.map {  input ->

            //修改传输过程中的值
            input?.firstName = "姓->${input?.firstName}"
            input?.lastname = "名->${input?.lastname}"
            val str = "我是修改后的数据 $input"
            Log.i("print_logs", str)
            str
        }

    /**
     *  Transformations.switchMap
     *  通过其创建一个新的 LiveData，并且我们可以在其间做一些操作，
     *  无论是单纯的转变类型，或是时间上的耗时操作……
     */
    //witchMap() 同样使消费了上层 LiveData 的值，但是它又创建了新的生产者，所以其真实的消费并不是由 switchMap() 来执行的
    private val transformationsSwitchMap = userLiveData.switchMap { input -> newLiveData(input) }

    fun newLiveData(user: User?): LiveData<String> {
        return MutableLiveData("修改数据后的数据：${user.toString()}")
    }

    /**
     * Transformations.distinctUntilChanged
     *  如果源 LiveData 一直进行 setValue()/postValue() 同一个值，
     *  那么返回的 LiveData 只接收第一次返回的值，除非源 LiveData 设置新的值。
     *  当LiveData传值没有改变时，后续不会在发送。非理性数据去重
     */
    private val transformationsDistinctUntilChanged = userLiveData.distinctUntilChanged()


    fun setUserData(user: User) {
        userLiveData.value = user
        transformationsMap.observeForever {
            Log.i("print_logs", "transformationsMap监听：$it")
        }
        transformationsSwitchMap.observeForever {
            Log.i("print_logs", "transformationsSwitchMap监听：$it")
        }
        transformationsDistinctUntilChanged.observeForever {
            Log.i("print_logs", "transformationsDistinctUntilChanged监听：$it")
        }
    }


    /**
     *                                      LiveData扩展函数 之 MediatorLiveData
     * ---------------------------------------------------------------------------------------------
     * MediatorLiveData 可以接管普通的 LiveData，使得当 LiveData 有数据更新的时候，MediatorLiveData 也能够 “收到响应”。
     * 链接：https://blog.csdn.net/catzifeng/article/details/107775686?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-0.no_search_link&spm=1001.2101.3001.4242.1
     * ---------------------------------------------------------------------------------------------
     */

    private val originData1 = MutableLiveData<String>()
    private val originData2 = MutableLiveData<String>()
    private val mediatorLiveData = MediatorLiveData<String>()
    private var isAdd = false


    private fun addListener() {
        Log.i("print_logs", "添加监听 ");
        isAdd = true
        mediatorLiveData.addSource(originData1) { t ->
            val str = "$t from originData1"
            Log.i("print_logs", "数据改变originData-1: $str")
            mediatorLiveData.value = str
        }
        mediatorLiveData.addSource(originData2) { t ->
            val str = "$t from originData2"
            Log.i("print_logs", "数据改变originData-2: $str")
            mediatorLiveData.value = str
        }

        // FIXME: 2021/12/6 激活MediatorLiveData 方式二
//        mediatorLiveData.observeForever{
//            Log.i("print_logs", "激活 mediatorLiveData: $it")
//        }

    }

    fun setMediatorLiveData(user: String) {
        originData1.value = user
        originData2.value = user
        if (!isAdd) {
            addListener()
        }
    }

    fun unsetMediatorLiveData() {
        mediatorLiveData.removeSource(originData1)
        mediatorLiveData.removeSource(originData2)
        isAdd = false
        Log.i("print_logs", "销毁 ")
    }

    fun getMediatorLiveData(): MediatorLiveData<String> {
        return mediatorLiveData
    }


}