package com.dongnao.hilt.app

import android.app.Application
import android.util.Log
import com.dongnao.hilt.BuildConfig
import com.dongnao.hilt.assisted.DataServiceFactory
import com.dongnao.hilt.component.AppModel
import com.dongnao.hilt.component.DaggerMyComponent
import com.dongnao.hilt.early.Foo
import com.dongnao.hilt.early.FooEarlyPoint
import dagger.hilt.android.EarlyEntryPoints
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.migration.CustomInject
import dagger.hilt.android.migration.CustomInjection
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2023/12/23 15:29
 * @description 自定义类描述
 */
@CustomInject
@HiltAndroidApp
class ExampleApplication : Application() {

    @Inject
    lateinit var dataServiceFactory: DataServiceFactory


    @Inject
    lateinit var appModel: AppModel

    //    @Inject
    lateinit var foo: Foo

    companion object {
        private lateinit var mInstance: ExampleApplication
        fun getInstance(): ExampleApplication {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        //解决某些组件需要在 Application 初始化之前就被依赖注入的问题
        foo = EarlyEntryPoints.get(this, FooEarlyPoint::class.java).provideFoo()
        foo.load()

        //@CustomInject  使用这个此注解可延迟在super.onCreate()后注入
        doSomethingBeforeInjection()
        CustomInjection.inject(this)  //必须加上这句


        val dataService = dataServiceFactory.create("I'm from ExampleApplication.")
        dataService.load()

        DaggerMyComponent.builder().provideContext(this).build().inject(this)

        Log.i("print_logs", "MyComponent: ${appModel.packet}")
    }

    private fun doSomethingBeforeInjection() {
        if (BuildConfig.DEBUG) {
            Log.w("print_logs", "ExampleApplication::doSomethingBeforeInjection")
        }
    }
}