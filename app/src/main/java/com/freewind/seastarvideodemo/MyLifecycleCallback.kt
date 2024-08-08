package com.freewind.seastarvideodemo

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.freewind.seastarvideo.utils.activityManager.LifecycleListener

/**
 * @author: wiatt
 * @date: 2024/7/28 16:46
 * @description:
 */
class MyLifecycleCallback: Application.ActivityLifecycleCallbacks {

    private val lifecycleListener = LifecycleListener()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        lifecycleListener.onActivityCreated(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
        lifecycleListener.onActivityStarted(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        lifecycleListener.onActivityResumed(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        lifecycleListener.onActivityPaused(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        lifecycleListener.onActivityStopped(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        lifecycleListener.onActivitySaveInstanceState(activity, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        lifecycleListener.onActivityDestroyed(activity)
    }
}