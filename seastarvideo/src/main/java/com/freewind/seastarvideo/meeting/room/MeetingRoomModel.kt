/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import com.freewind.seastarvideo.base.BaseModel

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

    }
}