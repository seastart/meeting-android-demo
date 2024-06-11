/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

/**
 * @author: wiatt
 * @date: 2024/6/6 17:38
 * @description:
 */
object DeviceUtil {

    @SuppressLint("HardwareIds")
    fun getAndroidID(context: Context): String {
        val id: String = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return id ?: "AndroidID"
    }
}