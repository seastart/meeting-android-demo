/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */
package com.freewind.seastarvideo.utils

import android.widget.Toast
import com.freewind.seastarvideo.EnvArgument

/**
 * @author: wiatt
 * @date: 2024/6/6 17:47
 * @description:
 */
object ToastUtil {

    /**
     * 长 toast
     */
    fun showLongToast(any: Any) {
        showToast(any, Toast.LENGTH_LONG)
    }

    /**
     * 短 toast
     */
    fun showShortToast(any: Any) {
        showToast(any, Toast.LENGTH_SHORT)
    }

    private fun showToast(any: Any, time: Int) {
        EnvArgument.instance.app?.let { app ->
            Toast.makeText(app, any.toString(), time).show()
        }
    }
}