/*
 * Copyright (c) 2015-2024 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo

import android.app.Application
import com.seastart.rtc.RTCMediaOptions
import com.shiyuan.meeting.MediaInfoManager
import com.shiyuan.meeting.MeetingEngine
import com.shiyuan.meeting.MemberInfoManager
import com.shiyuan.meeting.impl.RoomEvent
import com.shiyuan.meeting.impl.UserEvent

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

    fun init(app: Application, meetToken: String, options: RTCMediaOptions?) {
        engine = MeetingEngine(app, meetToken, options, false)
    }

    fun getInfoManager(): MemberInfoManager? {
        return engine?.memberInfoManager
    }

    fun getMediaInfoManager(): MediaInfoManager? {
        return engine?.mediaInfoManager
    }

    fun setEvent(roomEvent: RoomEvent?, userEvent: UserEvent?) {
        if (engine != null) {
            engine!!.setEventHandler(roomEvent, userEvent)
        }
    }
}