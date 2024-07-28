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
 * @date: 2024/7/27 10:18
 * @description: 请求头拦截器，可以在这里添加请求头
 */
class HeaderInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val builder = request.newBuilder()

        if (url.contains(ConstantHttp.HTTP_GET_DOCUMENT)
            || url.contains(ConstantHttp.HTTP_GET_SMS_CODE)
            || url.contains(ConstantHttp.HTTP_REGISTER)
            || url.contains(ConstantHttp.HTTP_LOGIN_BY_MOBILE)
            || url.contains(ConstantHttp.HTTP_LOGIN_BY_ACCOUNT)) {
            // 获取文档信息、获取验证码、登录、注册等请求不需要添加 Authorization 请求头
        } else {
            val jwtToken = KvUtil.decodeString(KvUtil.JWT_TOKEN)
            builder.addHeader(ConstantHttp.HEAD_AUTHORIZATION, "Bearer $jwtToken")
        }
        builder.addHeader(ConstantHttp.HEAD_CONTENT_TYPE, "application/json")

        return chain.proceed(builder.build())
    }
}