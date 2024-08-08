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
    }

    interface IMeetingRoomViewModel: BaseContract.IViewModel {

        /**
         * 响应：打开摄像头
         */
        fun responseOpenCamera(uiResponse: UiResponse<Boolean>)

        /**
         * 响应：打开麦克风
         */
        fun responseOpenMic(uiResponse: UiResponse<Boolean>)
    }
}