/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.http

/**
 * @author: wiatt
 * @date: 2024/7/27 10:19
 * @description: 记录请求路径
 */
object ConstantHttp {

    // 获取文档详情
    const val HTTP_GET_DOCUMENT = "v1/document/get-detail"
    // 获取短信验证码
    const val HTTP_GET_SMS_CODE = "v1/auth/sms-code"
    // 用户注册
    const val HTTP_REGISTER = "v1/auth/register"
    // 手机验证码登录
    const val HTTP_LOGIN_BY_MOBILE = "v1/auth/login-mobile"
    // 账号密码登录
    const val HTTP_LOGIN_BY_ACCOUNT = "v1/auth/login-account"
    // 获取自身用户详情
    const val HTTP_GET_SELF_DETAIL = "v1/member/self-detail"
    // 更新自身详情
    const val HTTP_UPDATE_SELF_DETAIL = "v1/member/self-update"
    // 用户举报
    const val HTTP_REPORT_VIOLATION = "v1/member/report-violation"
    // meet 授权
    const val HTTP_MEETING_GRANT = "v1/meeting/grant"

    // 授权信息
    const val HEAD_AUTHORIZATION = "Authorization"
    const val HEAD_NEW_TOKEN = "new-token"
    // 内容格式
    const val HEAD_CONTENT_TYPE = "Content-Type"

    // 资源路径：男生头像
    const val RES_AVATAR_MAN = "http://192.168.0.172:8089/resource/avatar01.png"
    // 资源路径：女生头像
    const val RES_AVATAR_WOMAN = "http://192.168.0.172:8089/resource/avatar02.png"
}