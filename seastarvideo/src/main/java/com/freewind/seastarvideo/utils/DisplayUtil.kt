/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */
package com.freewind.seastarvideo.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

class DisplayUtil private constructor() {

    companion object {
        @JvmStatic
        val instance: DisplayUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DisplayUtil()
        }
    }

    /**
     * 得到屏幕的高度
     * @return int
     */
    fun getMobileHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    /**
     * 得到屏幕的宽度
     * @return int
     */
    fun getMobileWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * @param pxValue   px
     * @return int
     */
    fun px2dip(pxValue: Float, context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * @param dipValue   dip
     * @return int
     */
    fun dip2px(dipValue: Int): Int {
        val pxValue =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dipValue.toFloat(), Resources.getSystem().displayMetrics)
        return pxValue.toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue   px
     * @return int
     */
    fun px2sp(pxValue: Float, context: Context): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue   sp
     * @return int
     */
    fun sp2px(spValue: Float): Int {
        val pxValue =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            spValue, Resources.getSystem().displayMetrics)
        return pxValue.toInt()
    }
}