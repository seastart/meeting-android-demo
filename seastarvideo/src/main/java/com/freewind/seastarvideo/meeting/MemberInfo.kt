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
    // 昵称
    var nickName: String,
    // 麦克风状态
    var micStatus: Boolean,
    // 摄像头状态
    var cameraStatus: Boolean
    )
