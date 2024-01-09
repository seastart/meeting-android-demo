/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting

/**
 * @author: wiatt
 * @date: 2024/1/2 15:13
 * @description:
 */

data class MemberInfo(
    // 用户id
    var id: String,
    // 昵称
    var nickName: String,
    // 角色
    var role: Int,
    // 麦克风状态
    var micStatus: Boolean,
    // 摄像头状态
    var cameraStatus: Boolean,
    // 该成员是否是自己
    var isMe: Boolean = false
    ) {

    companion object {
        // 成员角色：创建者
        const val MEMBER_ROLE_CREATOR = 1
        // 成员角色：主持人
        const val MEMBER_ROLE_COMPERE = 2
        // 成员角色：普通成员
        const val MEMBER_ROLE_NORMAL = 3
    }
}
