package com.freewind.seastarvideo.http

import com.freewind.seastarvideo.BuildConfig
import com.freewind.seastarvideo.EnvArgument
import com.freewind.seastarvideo.http.bean.BaseBean
import com.freewind.seastarvideo.http.interceptor.HeaderInterceptor
import com.freewind.seastarvideo.http.interceptor.ResponseInterceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * author superK
 * update_at 2024/1/29
 * description 网络请求引擎
 */
class ApiEngine {
    companion object {
        const val BaseUrl = "http://localv2.srtc.live:8089/api/"
    }
    var okHttpClient: OkHttpClient? = null
        private set
    var retrofit: Retrofit? = null
        private set
    // 连接超时时间
    private val connectTimeout = 30
    // 读取超时时间
    private val readTimeout = 30
    // 写入超时时间
    private val writeTimeout = 30
    // 是否自动重连
    private val reconnect = true

    /**
     * 初始化引擎
     * @param baseUrl 请求前缀
     */
    fun init(baseUrl: String) {
        if (baseUrl.isEmpty()) {
            return
        }
        var url = baseUrl
        if (!url.endsWith("/")) {
            url = "$url/"
        }
        release()
        if (okHttpClient == null) {
            val okhttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS) //设置连接超时
                .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS) //设置读超时
                .writeTimeout(writeTimeout.toLong(), TimeUnit.SECONDS) //设置写超时
                .retryOnConnectionFailure(reconnect) //是否自动重连
            okhttpBuilder.addInterceptor(HeaderInterceptor())
            okhttpBuilder.addInterceptor(ResponseInterceptor())
//            okhttpBuilder.addInterceptor(HttpLogCollectorInterceptor())
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                okhttpBuilder.addInterceptor(logging)
            }

            okHttpClient = okhttpBuilder.build()
        }
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(okHttpClient!!)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    /**
     * 获取常规post请求参数
     */
    @Throws(IOException::class)
    private fun getParamContent(body: RequestBody): String {
        val buffer = Buffer()
        body.writeTo(buffer)
        var json = buffer.readUtf8()
        json = json.replace("\"{", "{")
        json = json.replace("}\"", "}")
        return json.replace("\\\"", "\"")
    }

    fun <T> create(service: Class<T>): T? {
        return if (retrofit != null) {
            retrofit!!.create(service)
        } else {
            null
        }
    }

    /**
     * 异步请求队列
     * @param call  请求
     * @param apiCallback 回调
     * @param <T> 类型
    </T> */
    fun <T : BaseBean> enqueue(call: Call<T>, apiCallback: Callback<T>?) {
        apiCallback?.onStart()
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val t = response.body()
                if (t != null) {
                    if (t.code == ApiCode.SUCCESS) {
                        apiCallback?.onSuccess(t)
                    } else if (t.code == ApiCode.ERROR_AUTH_FAILED ||
                        t.code == ApiCode.ERROR_TOKEN_INVALID ||
                        t.code == ApiCode.ERROR_TOKEN_TOKEN_EXPIRED) {
                        // 如果错误是“未登录”、“token失效”、“token已过期”，则需要跳转到登录页面重新做登录操作
                        EnvArgument.instance.goToLoginPage()
                        apiCallback?.onFailed(t.code, t.msg)
                    } else {
                        apiCallback?.onFailed(t.code, t.msg)
                    }
                } else {
                    apiCallback?.onFailed(ApiCode.ERROR, "body为空")
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                if (apiCallback != null) {
                    if (call.isCanceled) {
                        apiCallback.onCancel()
                        return
                    }
                    apiCallback.onFailed(ApiCode.ERROR, throwable.message)
                }
            }
        })
    }

    /**
     * 取消队列中的请求和进行中的请求
     * @param requestKey    key
     */
    fun cancelCallWithKey(requestKey: String) {
        if (okHttpClient == null) {
            return
        }
        if (requestKey.isEmpty()) {
            return
        }
        //When you want to cancel:
        //A) go through the queued calls and cancel if the tag matches:
        for (call in okHttpClient!!.dispatcher.queuedCalls()) {
            val tag = call.request().tag() as String?
            if (tag != null && tag.startsWith(requestKey)) {
                call.cancel()
                break
            }
        }

        //B) go through the running calls and cancel if the tag matches:
        for (call in okHttpClient!!.dispatcher.runningCalls()) {
            val tag = call.request().tag() as String?
            if (tag != null && tag.startsWith(requestKey)) {
                call.cancel()
                break
            }
        }
    }

    /**
     * 取消队列中的请求和进行中的请求
     */
    fun cancelCallForAll() {
        if (okHttpClient == null) {
            return
        }
        //When you want to cancel:
        //A) go through the queued calls and cancel if the tag matches:
        for (call in okHttpClient!!.dispatcher.queuedCalls()) {
            call.cancel()
        }

        //B) go through the running calls and cancel if the tag matches:
        for (call in okHttpClient!!.dispatcher.runningCalls()) {
            call.cancel()
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        cancelCallForAll()
        if (okHttpClient != null) {
//            okHttpClient.interceptors().clear();//清除拦截器
            okHttpClient!!.dispatcher.executorService.shutdown() //清除并关闭线程池
            okHttpClient!!.connectionPool.evictAll() //清除并关闭连接池
            val cache = okHttpClient!!.cache
            try {
                cache?.close()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        retrofit = null
        okHttpClient = null
    }
}
