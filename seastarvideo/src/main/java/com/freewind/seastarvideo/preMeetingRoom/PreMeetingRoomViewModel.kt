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
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.base.SingleLiveEvent
import com.freewind.seastarvideo.base.UiResponse

/**
 * @author: wiatt
 * @date: 2023/12/26 18:04
 * @description:
 */
class PreMeetingRoomViewModel():
    BaseViewModel<PreMeetingRoomModel, PreMeetingRoomContract.IPreMeetingRoomViewModel>() {

    val createMeetingResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    val enterMeetingResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()

    override fun getModel(): PreMeetingRoomModel {
        return PreMeetingRoomModel(PreMeetingRoomViewModelImpl())
    }

    /**
     * 创建会议
     */
    fun createMeeting(title: String, content: String?, password: String?) {
        mModel.getContract().requestCreateMeeting(title, content, password)
    }

    /**
     * 加入会议
     */
    fun enterMeeting(activity: Activity, roomNo: String, password: String?, nickName: String, avatar: String) {
        mModel.getContract().requestEnterMeeting(activity, roomNo, password, nickName, avatar)
    }

    inner class PreMeetingRoomViewModelImpl: PreMeetingRoomContract.IPreMeetingRoomViewModel {
        override fun responseCreateMeeting(uiResponse: UiResponse<String>) {
            createMeetingResult.value = uiResponse
        }

        override fun responseEnterMeeting(uiResponse: UiResponse<String>) {
            enterMeetingResult.value = uiResponse
        }
    }
}