/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RecentTaskInfo
import android.content.Context
import android.os.Build


/**
 * @author: wiatt
 * @date: 2024/6/7 15:14
 * @description:
 */
object ActivityStackHelper {

    /**
     * 获取当前任务栈顶的 Activity
     *
     * @param context 当前上下文
     * @return 栈顶的 Activity 的类名，如果无法获取则返回 null
     */
    fun getTopActivity(context: Context): String? {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getAppTasks()
        if (tasks != null && tasks.isNotEmpty()) {
            val taskInfo = tasks[0].taskInfo
            return taskInfo?.topActivity?.className
        }
        return null
    }

    /**
     * 获取当前任务栈包含的 activity 数量
     */
    fun getActivityCountInCurTaskStack(context: Activity): Int {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getAppTasks()
        var activityCount = 0
        // 不调用task.finish()是因为我们只是想计数
        // 调用task.finish()会结束当前Activity
        for (task in tasks) {
            try {
                val taskInfo = task.taskInfo as RecentTaskInfo
                // 无法通过 taskAffinity 获取指定的 task
                if (
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        taskInfo.taskId == context.taskId
                    } else {
                        taskInfo.id == context.taskId
                    }
                ) {
                    activityCount = taskInfo.numActivities
                    break
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return activityCount
    }

    /**
     * 获取当前应用包含的所有 activity 数量
     */
    fun getActivityCount(context: Context): Int {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getAppTasks()
        var activityCount = 0
        // 不调用task.finish()是因为我们只是想计数
        // 调用task.finish()会结束当前Activity
        for (task in tasks) {
            try {
                val taskInfo = task.taskInfo as RecentTaskInfo
                activityCount += taskInfo.numActivities
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return activityCount
    }

    /**
     * 判断当前任务栈中是否只有当前 Activity
     *
     * @param context 当前上下文
     * @return 如果任务栈中只有当前 Activity 则返回 true，否则返回 false
     */
    fun isSingleActivityInTask(context: Activity): Boolean {
        val count = getActivityCountInCurTaskStack(context)
        return count == 1
    }
}