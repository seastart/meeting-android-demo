/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.authorize

/**
 * @author: wiatt
 * @date: 2024/6/7 10:24
 * @description:
 */
class AuthorizeEventBean {
    /**
     * 登录状态改变事件
     * @param isLogin true：登录； false：登出
     */
    class LoginStatusEvent(var isLogin: Boolean)
}