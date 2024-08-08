package com.freewind.seastarvideo.utils.activityManager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.lang.ref.SoftReference

/**
 * @author wiatt
 */
class TopActivityUtil(topActivity: Activity) {

    private val mReference: SoftReference<Activity>
    private var mTime: Long = 0
    private val mTasksManager: ActivityManager
    private var isStayTop = true

    init {
        mTasksManager = topActivity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        mReference = SoftReference(topActivity)
    }

    /**
     * 是否保持在顶端
     * @param stayTop
     */
    fun isStayTop(stayTop: Boolean) {
        isStayTop = stayTop
    }

    fun onBackground(background: Boolean) {
        val topActivity = mReference.get()
        if (topActivity != null) {
            if (background) {
                mTime = System.currentTimeMillis()
            } else {
                /*
                 * android系统限制：
                 * 如果回到桌面，5秒钟之内又立即点击APP图标唤起APP，那么APP内的task切换不会立即生效
                 * APP还是在主task内（与5秒内点击悬浮窗不会立即回到app的情况一样）
                 * 如果此时主task存在activity切换，则task切换会立即生效
                 * ps：
                 * 华为mate30pro手机在这种情况下也不会立即生效，但是在“5秒内点击悬浮窗”的情况下能立即回到app
                 */
                if (!isStayTop) {
                    return
                }
                if (System.currentTimeMillis() - mTime <= 5000) {
                    val isActivityOnTop = isActivityOnTop(topActivity, topActivity::class.java)
                    if (!isActivityOnTop) {
                        AirActivity.start(topActivity)
                    }
                } else {
                    mTasksManager.moveTaskToFront(topActivity.taskId, ActivityManager.MOVE_TASK_NO_USER_ACTION)
                }
                mTime = System.currentTimeMillis()
            }
        }
    }

    /**
     * 判断 activity 是否处于顶部
     */
    private fun isActivityOnTop(context: Context, activityClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appTasks = activityManager.appTasks
        if (appTasks.isNotEmpty()) {
            val taskInfo = appTasks[0].taskInfo
            val topActivity = taskInfo.topActivity
            return topActivity?.className == activityClass.name
        }
        return false
    }
}
