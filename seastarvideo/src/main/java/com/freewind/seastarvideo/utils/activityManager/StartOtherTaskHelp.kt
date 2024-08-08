package com.freewind.seastarvideo.utils.activityManager

import android.content.Context

/**
 * @author: wiatt
 * @date: 2024/7/28 17:47
 * @description:
 */
interface StartOtherTaskHelp {

    /**
     * 启动指定 task 中的 activity
     */
    fun startActivityInAssignTask(context: Context)

    /**
     * 启动默认 task 中的 activity
     */
    fun startActivityInHostTask()
}