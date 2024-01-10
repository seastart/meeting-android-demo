/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.chat.bean

/**
 * @author: wiatt
 * @date: 2024/1/9 16:18
 * @description:
 */
class ChatContentBean(var avatar: String, var nickName: String, var content: String, var isMe: Boolean): BaseChatBean() {
    val type: String = CHAT_CONTENT
}