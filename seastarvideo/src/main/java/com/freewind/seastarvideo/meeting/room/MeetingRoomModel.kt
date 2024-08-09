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
import cn.seastart.meeting.api.Callback
import cn.seastart.meeting.bean.Data
import cn.seastart.meeting.impl.MeetingResultListener
import com.freewind.seastarvideo.MeetingEngineHelper
import com.freewind.seastarvideo.base.BaseModel
import com.freewind.seastarvideo.base.ErrorBean
import com.freewind.seastarvideo.base.UiResponse

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class MeetingRoomModel(viewModelImpl: MeetingRoomViewModel.MeetingRoomViewModelImpl):
    BaseModel<MeetingRoomContract.IMeetingRoomViewModel, MeetingRoomContract.IMeetingRoomModel>
        (viewModelImpl) {

    override fun getContract(): MeetingRoomContract.IMeetingRoomModel {
        return MeetingRoomModelImpl()
    }

    inner class MeetingRoomModelImpl: MeetingRoomContract.IMeetingRoomModel {
        override fun requestOpenCamera(activity: Activity) {
            MeetingEngineHelper.instance.engine?.requestOpenCamera(activity, object : Callback<Data<String?>>() {
                override fun onSuccess(data: Data<String?>?) {
                    super.onSuccess(data)
                    listener.responseOpenCamera(UiResponse(true))
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    if (code == 1) {
                        listener.responseOpenCamera(UiResponse(
                            ErrorBean(code, "不允许打开摄像头", "不允许打开摄像头")
                        ))
                    } else {
                        listener.responseOpenCamera(UiResponse(
                            ErrorBean(code, msg ?: "请求出错", msg ?: "请求出错")
                        ))
                    }
                }
            })
        }

        override fun requestCloseCamera() {
            // 关闭摄像头不需要等待响应，应该直接执行关闭操作
            MeetingEngineHelper.instance.engine?.closeCamera(null)
        }

        override fun requestOpenMic() {
            MeetingEngineHelper.instance.engine?.requestOpenMic(object : Callback<Data<String?>>() {
                override fun onSuccess(data: Data<String?>?) {
                    super.onSuccess(data)
                    listener.responseOpenMic(UiResponse(true))
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    if (code == 1) {
                        listener.responseOpenMic(UiResponse(
                            ErrorBean(code, "不允许打开麦克风", "不允许打开麦克风")
                        ))
                    } else {
                        listener.responseOpenMic(UiResponse(
                            ErrorBean(code, msg ?: "请求出错", msg ?: "请求出错")
                        ))
                    }
                }
            })
        }

        override fun requestCloseMic() {
            // 关闭麦克风不需要等待响应，应该直接执行关闭操作
            MeetingEngineHelper.instance.engine?.closeMic(null)
        }

        override fun requestStartScreenShare(
            activity: Activity,
            param: ScreenManager.NotificationParam?,
            event: ScreenManager.ScreenShareEvent
        ) {
            MeetingEngineHelper.instance.engine?.initScreenShare(activity, param, event)
            MeetingEngineHelper.instance.engine?.startScreenShare(true, object : MeetingResultListener {
                override fun onSuccess() {
                    // 如果成功了，什么也不做，会通过另一个回调通知
                }
                override fun onFail(code: Int, message: String) {
                    listener.responseStartScreenShare(UiResponse(ErrorBean(code, message, message)))
                }
            })
        }

        override fun requestStopScreenShare() {
            MeetingEngineHelper.instance.engine?.stopScreenShare(null)
        }

    }
}