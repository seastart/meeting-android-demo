/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideodemo

import android.app.Application
import com.freewind.seastarvideo.SeaStarClient

/**
 * @author: wiatt
 * @date: 2023/12/20 11:50
 * @description:
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SeaStarClient.instance.SSC_Init(this)
    }
}