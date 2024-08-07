/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.preMeetingRoom

import android.app.Activity
import com.freewind.seastarvideo.base.BaseContract
import com.freewind.seastarvideo.base.UiResponse

/**
 * @author: wiatt
 * @date: 2023/12/26 18:00
 * @description:
 */
class PreMeetingRoomContract {

    interface IPreMeetingRoomModel: BaseContract.IModel {
        /**
         * 请求：创建会议
         */
        fun requestCreateMeeting(title: String, content: String?, password: String?)

        /**
         * 请求：加入会议
         */
        fun requestEnterMeeting(activity: Activity, roomNo: String, password: String?, nickName: String, avatar: String)

    }

    interface IPreMeetingRoomViewModel: BaseContract.IViewModel {
        /**
         * 返回：创建会议
         */
        fun responseCreateMeeting(uiResponse: UiResponse<String>)

        /**
         * 返回：加入会议
         */
        fun responseEnterMeeting(uiResponse: UiResponse<String>)
    }
}