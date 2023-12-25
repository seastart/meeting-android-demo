/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.ui

import android.app.Activity
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


/**
 * @author wiatt
 * @description:
 */
class StatusBarManager {

    companion object {
        val instance: StatusBarManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            StatusBarManager()
        }
    }

    /**
     *  显示状态栏
     *  @param isVisible 是否显示
     */
    fun setStatusBarVisible(activity: Activity, isVisible: Boolean) {
        val window = activity.window
        WindowCompat.setDecorFitsSystemWindows(window, isVisible)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            if (isVisible) {
                controller.show(WindowInsetsCompat.Type.statusBars())
            } else {
                controller.hide(WindowInsetsCompat.Type.statusBars())
            }
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


    /**
     *  设置状态栏颜色
     *  这里还是直接操作window的statusBarColor
     */
    fun setStatusBarColor(activity: Activity, @ColorInt color: Int) {
        activity.window.statusBarColor = color
    }

    /**
     *  设置状态栏字体颜色
     *  此api只能控制字体颜色为 黑/白
     *  @param color 这里的颜色是指背景颜色
     */
    fun setStatusBarTextColor(activity: Activity, @ColorInt color: Int) {
        // 计算颜色亮度

        val luminanceValue = ColorUtils.calculateLuminance(color)
        WindowInsetsControllerCompat(activity.window, activity.window.decorView).let { controller ->
            if (color == Color.TRANSPARENT) {
                // 如果是透明颜色就默认设置成黑色
                controller.isAppearanceLightStatusBars = true
            } else {
                // 通过亮度来决定字体颜色是黑还是白
                controller.isAppearanceLightStatusBars = luminanceValue >= 0.5
            }
        }
    }

    /**
     *  沉浸式状态栏
     *  @param contentColor 内容颜色:获取内容的颜色，传入系统，它自动修改字体颜色(黑/白)
     */
    fun immersiveStatusBar(activity: Activity,@ColorInt contentColor:Int) {
        val window = activity.window.apply {
            statusBarColor = Color.TRANSPARENT
        }
        // 设置状态栏字体颜色
        setStatusBarTextColor(activity, contentColor)
        // 把内容放到系统窗口里面 可以去了解一下Window和decorView的关系
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}