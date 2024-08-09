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
import cn.seastart.meeting.MeetingEngine
import cn.seastart.meeting.MeetingInfoManager
import cn.seastart.meeting.impl.MediaEvent
import cn.seastart.meeting.impl.MeetingResultListener
import cn.seastart.meeting.impl.RoomEvent
import cn.seastart.meeting.impl.RoomMsgEvent
import cn.seastart.meeting.impl.UserEvent
import cn.seastart.rtc.RTCMediaOptions

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

    fun init(app: Application, meetToken: String, options: RTCMediaOptions?, listener: MeetingResultListener) {
        engine = MeetingEngine(app)
        engine?.initSdk(meetToken, options, listener)
    }

    fun getInfoManager(): MeetingInfoManager? {
        return engine?.meetingInfoManager
    }

    fun setRoomEvent(roomEvent: RoomEvent) {
        engine?.roomEvent = roomEvent
    }

    fun setUserEvent(userEvent: UserEvent) {
        engine?.userEvent = userEvent
    }

    fun setRoomMsgEvent(roomMsgEvent: RoomMsgEvent) {
        engine?.roomMsgEvent = roomMsgEvent
    }

    fun setMediaEvent(mediaEvent: MediaEvent) {
        engine?.mediaEvent = mediaEvent
    }
}