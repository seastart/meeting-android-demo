package com.freewind.seastarvideo.utils.activityManager

import android.app.Activity

/**
 * @author: wiatt
 * @date: 2024/7/28 17:05
 * @description:
 */
interface TopActivityHelp {
    /**
     * 创建顶端 activity 工具类
     */
    fun createTopActivityUtil(activity: Activity)

    /**
     * 释放顶端 activity 工具类
     */
    fun releaseTopActivityUtil()

    /**
     * 配置顶端 activity 工具类参数
     */
    fun configTopActivityUtil(stayTop: Boolean)
}