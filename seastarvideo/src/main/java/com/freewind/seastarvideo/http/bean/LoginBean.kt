/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.http.bean

import com.google.gson.annotations.SerializedName

/**
 * 登录信息
 */
data class LoginBean(
    // 用户 ID
    @SerializedName("user_id")
    val userId: String,
    // 手机号码
    @SerializedName("mobile")
    val mobile: String,
    // 昵称
    @SerializedName("nickname")
    val nickName: String,
    // 头像
    @SerializedName("avatar")
    val avatar: String,
    // demo 认证令牌
    @SerializedName("jwt_token")
    val jwtToken: String,
)