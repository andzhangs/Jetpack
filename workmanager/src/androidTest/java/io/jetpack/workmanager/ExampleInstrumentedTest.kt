package io.jetpack.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.TestWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import io.reactivex.Single
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.`is`

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var mContext: Context
    private lateinit var mExecutor: Executor

    private class SleepWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters) {

        companion object {
            const val SLEEP_DURATION = "SLEEP_DURATION"
        }

        override fun doWork(): Result {
            // Sleep on a background thread.
            val sleepDuration = inputData.getLong(SLEEP_DURATION, 1000)
            Thread.sleep(sleepDuration)
            return Result.success(Data.Builder().putString("key1", "I am from SleepWorker").build())
        }
    }

    private class SleepWorker2(context: Context, workerParameters: WorkerParameters) :
        CoroutineWorker(context, workerParameters) {

        override suspend fun doWork(): Result {
            delay(1000)
            return Result.success()
        }
    }

    private class RxSleepWorker(context: Context, workerParameters: WorkerParameters) :
        RxWorker(context, workerParameters) {
        override fun createWork(): Single<Result> =
            Single.just(
                Result.success(
                    Data.Builder().putString("key", "I am from RxSleepWorker").build()
                )
            ).delay(1000, TimeUnit.MILLISECONDS)
    }

    @Before
    fun setUp() {
        mContext = ApplicationProvider.getApplicationContext()
        mExecutor = Executors.newSingleThreadExecutor()
    }


    @Before
    fun setUp2() {
//        val context = InstrumentationRegistry.getTargetContext()
//        val config = Configuration.Builder()
//            .setMinimumLoggingLevel(Log.DEBUG)
//            .setExecutor(SynchronousExecutor())
//            .build()
//        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("io.jetpack.workmanager", appContext.packageName)
    }

    @Test
    fun testSleepWorker() {
        val worker = TestWorkerBuilder<SleepWorker>(
            mContext,
            mExecutor,
            workDataOf(SleepWorker.SLEEP_DURATION to 1000L)
        ).build()
        val result = worker.doWork()
        assertThat(result, `is`(Result.success(Data.Builder().build())))
    }


    @Test
    fun testSleepWorker2() {
        val coroutineWorker = TestListenableWorkerBuilder<SleepWorker>(mContext).build()

        runBlocking {
            val coroutineResult = coroutineWorker.doWork()
            assertThat(coroutineResult, `is`(Result.success(Data.Builder().build())))
        }
    }

    @SuppressLint("CheckResult")
    @Test
    fun testRxSleepWorker() {
        val worker = TestListenableWorkerBuilder<RxSleepWorker>(mContext).build()
        worker.createWork().subscribe { result ->
            assertThat(result, `is`(Result.success(Data.Builder().build())))
        }
    }

}