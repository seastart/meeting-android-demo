/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import android.app.Activity
import cn.seastart.meeting.ScreenManager
import com.freewind.seastarvideo.base.BaseContract
import com.freewind.seastarvideo.base.UiResponse

/**
 * @author: wiatt
 * @date: 2023/12/27 14:22
 * @description:
 */
class MeetingRoomContract {

    interface IMeetingRoomModel: BaseContract.IModel {

        /**
         * 请求：加入会议
         */
        fun requestEnterMeeting(activity: Activity, roomNo: String, password: String?, nickName: String, avatar: String)

        /**
         * 请求：打开摄像头
         */
        fun requestOpenCamera(activity: Activity)

        /**
         * 请求：关闭摄像头
         */
        fun requestCloseCamera()

        /**
         * 请求：打开麦克风
         */
        fun requestOpenMic()

        /**
         * 请求：关闭麦克风
         */
        fun requestCloseMic()

        /**
         * 请求：打开屏幕共享
         */
        fun requestStartScreenShare(activity: Activity, param: ScreenManager.NotificationParam?, event: ScreenManager.ScreenShareEvent)

        /**
         * 请求：关闭屏幕共享
         */
        fun requestStopScreenShare()
    }

    interface IMeetingRoomViewModel: BaseContract.IViewModel {

        /**
         * 返回：加入会议
         */
        fun responseEnterMeeting(uiResponse: UiResponse<String>)

        /**
         * 响应：打开摄像头
         */
        fun responseOpenCamera(uiResponse: UiResponse<Boolean>)

        /**
         * 响应：打开麦克风
         */
        fun responseOpenMic(uiResponse: UiResponse<Boolean>)

        /**
         * 响应：打开屏幕共享
         * 只需要回调打开失败的事件，不需要回调打开成功的，
         * 如果屏幕共享打开成功，会通过另一个回调被通知到
         */
        fun responseStartScreenShare(uiResponse: UiResponse<Boolean>)
    }
}