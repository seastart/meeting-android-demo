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
import cn.seastart.meeting.api.Callback
import cn.seastart.meeting.bean.Data
import cn.seastart.meeting.enumerate.MeetingType
import cn.seastart.meeting.enumerate.MuteState
import com.freewind.seastarvideo.MeetingEngineHelper
import com.freewind.seastarvideo.base.BaseModel
import com.freewind.seastarvideo.base.ErrorBean
import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.http.ApiCode

/**
 * @author: wiatt
 * @date: 2023/12/26 18:02
 * @description:
 */
class PreMeetingRoomModel(viewModelImpl: PreMeetingRoomViewModel.PreMeetingRoomViewModelImpl):
    BaseModel<
            PreMeetingRoomContract.IPreMeetingRoomViewModel,
            PreMeetingRoomContract.IPreMeetingRoomModel>(viewModelImpl) {
    override fun getContract(): PreMeetingRoomContract.IPreMeetingRoomModel {
        return PreMeetingRoomModelImpl()
    }

    inner class PreMeetingRoomModelImpl: PreMeetingRoomContract.IPreMeetingRoomModel {
        override fun requestCreateMeeting(title: String, content: String?, password: String?) {
            MeetingEngineHelper.instance.engine?.createMeeting(
                null, title, content, password,
                MeetingType.Initiate, 0, 0,
                MuteState.MuteState3, true, false, false, null,
                object : Callback<Data<String?>>() {
                    override fun onSuccess(data: Data<String?>) {
                        super.onSuccess(data)
                        val roomNo = data.data
                        if (roomNo == null) {
                            listener.responseCreateMeeting(
                                UiResponse(
                                    ErrorBean(ApiCode.ERROR_HTTP_RESULT_NULL, ApiCode.ERROR_HTTP_RESULT_NULL_STR, ApiCode.ERROR_HTTP_RESULT_NULL_STR)
                                )
                            )
                        } else {
                            listener.responseCreateMeeting(UiResponse(roomNo))
                        }
                    }

                    override fun onFailed(code: Int, msg: String?) {
                        super.onFailed(code, msg)
                        listener.responseCreateMeeting(
                            UiResponse(ErrorBean(code, msg ?: "", msg ?: ""))
                        )
                    }
                })
        }

        override fun requestEnterMeeting(
            activity: Activity, roomNo: String, password: String?,
            nickName: String, avatar: String
        ) {
            MeetingEngineHelper.instance.engine?.enterMeeting(
                activity, roomNo, password, nickName, avatar, null,
                object : Callback<Data<String>>() {
                    override fun onSuccess(data: Data<String>) {
                        super.onSuccess(data)
                        listener.responseEnterMeeting(UiResponse(roomNo))
                    }

                    override fun onFailed(code: Int, msg: String?) {
                        super.onFailed(code, msg)
                        listener.responseEnterMeeting(
                            UiResponse(ErrorBean(code, msg ?: "", msg ?: ""))
                        )
                    }
                })
        }
    }
}