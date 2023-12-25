/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.enterpriseService

/**
 * @author: wiatt
 * @date: 2023/12/25 15:18
 * @description: 房间类型数据类
 */
data class RoomTypeBean(var roomType: RoomTypeEnum, var roomTypeInfo: BaseRoomTypeInfo) {

    enum class RoomTypeEnum {
        ROOM_TYPE_MEETING
    }

    // 基类，其子类用于记录某一类型具体的房间类型信息
    abstract class BaseRoomTypeInfo()
}

data class MeetingRoomInfo(var imageRes: Int, var title: String, var des: String):
    RoomTypeBean.BaseRoomTypeInfo()
