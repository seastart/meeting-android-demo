package com.freewind.seastarvideo.utils.activityManager

import android.app.Activity
import android.content.Context

/**
 * @author: wiatt
 * @date: 2024/7/28 15:36
 * @description:
 */
class ActivityHelp: TopActivityHelp, StartOtherTaskHelp {

    companion object {
        const val TAG: String = "RTCHelper"
        val instance = ActivityHelpHolder.holder
    }

    private object ActivityHelpHolder {
        val holder = ActivityHelp()
    }

    private var topActivityUtil: TopActivityUtil? = null
    private var customBackgroundEvent: CustomBackgroundEvent? = null
    fun onBackground(background: Boolean) {
        topActivityUtil?.onBackground(background)
        customBackgroundEvent?.onBackground(background)
    }

    fun setCustomBackgroundEvent(event: CustomBackgroundEvent) {
        customBackgroundEvent = event
    }

    fun releaseCustomBackgroundEvent() {
        customBackgroundEvent = null
    }

    override fun createTopActivityUtil(activity: Activity) {
        topActivityUtil = TopActivityUtil(activity)
    }

    override fun releaseTopActivityUtil() {
        topActivityUtil = null
    }

    override fun configTopActivityUtil(stayTop: Boolean) {
        topActivityUtil?.isStayTop(stayTop)
    }

    override fun startActivityInAssignTask(context: Context) {
        AirActivity.start(context)
    }

    override fun startActivityInHostTask() {
        AirActivity.startFromHost()
    }

    interface CustomBackgroundEvent {
        fun onBackground(background: Boolean)
    }
}