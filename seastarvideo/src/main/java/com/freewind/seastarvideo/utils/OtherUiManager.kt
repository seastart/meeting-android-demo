/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


/**
 * @author wiatt
 * @description:
 */
class OtherUiManager {

    companion object {
        val TAG = OtherUiManager::class.java.simpleName
        val instance: OtherUiManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OtherUiManager()
        }
    }

    /**
     * 设置控件padding高度为状态栏高度
     */
    fun setStatusBar(context: Context, view: View) {
        val statusBarHeight = getStatusBarHeight(context)
        LogUtil.i(TAG, "statusBarHeight = $statusBarHeight")
        val param = view.layoutParams
        val height = param.height
        param.height = height + getStatusBarHeight(context)
        view.layoutParams = param
        view.setPadding(0, statusBarHeight, 0, 0)
    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    @SuppressLint("DiscouragedApi")
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 适配顶部状态栏高度
     */
    fun adaptTopHeight(moveView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(moveView) { view, insets: WindowInsetsCompat ->

            val systemWindow=insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout())

            //为需要偏移的view设置padding
            val params: ViewGroup.MarginLayoutParams = moveView.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = systemWindow.top
            moveView.layoutParams = params

//            moveView.setPadding(0,systemWindow.top,0, 0)

            insets
        }
    }

    /**
     * 适配底部导航栏高度
     */
    fun adaptBottomHeight(moveView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(moveView) { view, insets: WindowInsetsCompat ->

            val systemWindow=insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout())

            //为需要偏移的view设置padding
            val params: ViewGroup.MarginLayoutParams = moveView.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = systemWindow.bottom
            moveView.layoutParams = params

//            moveView.setPadding(0,0,0,systemWindow.bottom)

            insets
        }
    }

    /**
     * 同时适配顶部状态栏和底部导航栏高度
     */
    fun adaptHeight(moveView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(moveView) { view, insets: WindowInsetsCompat ->

            val systemWindow=insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout())

            //为需要偏移的view设置padding
            val params: ViewGroup.MarginLayoutParams = moveView.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = systemWindow.top
            params.bottomMargin = systemWindow.bottom
            moveView.layoutParams = params

//            moveView.setPadding(0,systemWindow.top,0, systemWindow.bottom)

            insets
        }
    }
}