/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.http.interceptor

import com.freewind.seastarvideo.http.ConstantHttp
import com.freewind.seastarvideo.utils.KvUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: wiatt
 * @date: 2024/7/27 11:17
 * @description: 响应拦截器，可以在这里获取响应中的数据信息
 */
class ResponseInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        // 获取响应头数据
        val headers = response.headers
        val newToken = headers[ConstantHttp.HEAD_NEW_TOKEN]
        if (newToken != null) {
            KvUtil.encode(KvUtil.JWT_TOKEN, newToken)
        }
        return response
    }
}