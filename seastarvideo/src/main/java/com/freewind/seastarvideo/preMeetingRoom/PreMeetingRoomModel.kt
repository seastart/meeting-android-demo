/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.preMeetingRoom

import com.freewind.seastarvideo.base.BaseModel

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

    }
}