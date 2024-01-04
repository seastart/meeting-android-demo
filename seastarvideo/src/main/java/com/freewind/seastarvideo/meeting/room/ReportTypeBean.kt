/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

/**
 * @author: wiatt
 * @date: 2024/1/4 9:38
 * @description:
 */

// 政治敏感
const val FLAG_political: Int = 1
// 低俗色情
const val FLAG_eroticism: Int = 2
// 攻击辱骂
const val FLAG_ATTACK: Int = 3
// 血腥暴力
const val FLAG_VIOLENCE: Int = 4
// 不良广告
const val FLAG_ADVERTISEMENT: Int = 5
// 涉嫌诈骗
const val FLAG_SWINDLE: Int = 6
// 违法信息
const val FLAG_BREAK_THE_LAW: Int = 7
// 其他违规
const val FLAG_OTHER: Int = 8

data class ReportTypeBean(var desc: String, var flag: Int)
