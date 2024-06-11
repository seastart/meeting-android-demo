/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo

import android.app.Application

/**
 * @author: wiatt
 * @date: 2023/12/20 11:40
 * @description:
 */
class EnvArgument {

    // 上下文环境
    var app: Application? = null
    // 暂时先在内存中存储，后续需要存入 mmvk 中，并且需要考虑过期时间和meeting层登录情况
    var token: String? = ""

    companion object {
        val instance: EnvArgument by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            EnvArgument()
        }
    }
}