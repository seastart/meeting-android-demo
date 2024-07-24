/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo

import android.app.Activity
import com.seastart.rtc.RTCMediaOptions
import com.shiyuan.meeting.MeetingEngine
import com.shiyuan.meeting.impl.MeetingResultListener

/**
 * @author: wiatt
 * @date: 2024/6/6 16:02
 * @description:
 */
class MeetingEngineHelper private constructor(){

    companion object {
        val instance = MeetingEngineHelperHolder.holder
    }

    private object MeetingEngineHelperHolder {
        val holder = MeetingEngineHelper()
    }

    var engine: MeetingEngine? = null
        private set

    fun init(activity: Activity, meetToken: String, options: RTCMediaOptions?) {
        engine = MeetingEngine(activity)
        engine?.initSdk(meetToken, options, object : MeetingResultListener {
            override fun onFail(code: Int, message: String) {
                TODO("Not yet implemented")
            }

            override fun onSuccess() {
                TODO("Not yet implemented")
            }

        })
    }
}