package com.dongnao.paging.app

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.dongnao.paging.http.ApiService
import com.dongnao.paging.http.HttpConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 *
 * @author zhangshuai
 * @date 2023/11/21 14:14
 * @mark 自定义类描述
 */
class PagingApplication : Application() {

    companion object {
        private lateinit var mInstance: PagingApplication
        fun getInstance(): PagingApplication = mInstance
    }

    private lateinit var mRetrofit: Retrofit

    fun getApiService() = mRetrofit.create(ApiService::class.java)

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        initLogger()
        initRetrofit()
    }

    private fun initLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true) // (Optional) Whether to show thread info or not. Default true
//            .methodCount(2) // (Optional) How many method line to show. Default 2
//            .methodOffset(5) // (Optional) Hides internal method calls up to offset. Default 5
//            .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("print_logs") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }

    private fun initRetrofit() {
        val interceptorLogger = HttpLoggingInterceptor(httpLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .sslSocketFactory(createSSLSocketFactory()!!, trustManager)
            .hostnameVerifier(hostnameVerifier)
            .addInterceptor(interceptorLogger)
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(HttpConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }

    private val httpLogger = object : HttpLoggingInterceptor.Logger {

        private val gson: Gson by lazy { GsonBuilder().setPrettyPrinting().create() }
        private val jsonParser: JsonParser by lazy { JsonParser() }
        private val mMessage = java.lang.StringBuilder()

        override fun log(message: String) {
            try {
                // 请求或者响应开始
                var localMsg: String? = message
                if (localMsg!!.startsWith("--> POST") || localMsg.startsWith("--> GET") || localMsg.startsWith(
                        "--> PUT"
                    ) || localMsg.startsWith("--> DELETE")
                ) {
                    mMessage.setLength(0)
                }

                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if ((localMsg.startsWith("{") && localMsg.startsWith(
                        "}",
                        localMsg.length - 2
                    )) || (localMsg.startsWith("[") && localMsg.startsWith(
                        "]",
                        localMsg.length - 2
                    ))
                ) {
                    localMsg = gson.toJson(jsonParser.parse(localMsg).asJsonObject)
                }
                mMessage.append("$localMsg\n")
                // 响应结束，打印整条日志
                if (localMsg!!.startsWith("<-- END HTTP")) {
//                    Logger.i(mMessage.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf(trustManager), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
            Log.e("print_logs", "HttpModule::createSSLSocketFactory: $e")
        }
        return ssfFactory
    }

    @SuppressLint("CustomX509TrustManager")
    private val trustManager: X509TrustManager = object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)
    }

    private val hostnameVerifier = HostnameVerifier { _, _ -> true }
}