package com.freewind.seastarvideo.utils.activityManager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

@SuppressLint("NewApi")
/**
 *
 *
 * @author wiatt
 */
class LifecycleListener : ActivityLifecycleCallbacks {
    private var refCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        refCount++
        ActivityHelp.instance.onBackground(refCount <= 0)
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        refCount--
        ActivityHelp.instance.onBackground(refCount <= 0)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
