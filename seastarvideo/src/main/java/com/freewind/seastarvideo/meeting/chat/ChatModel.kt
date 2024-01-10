/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.chat

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class ChatModel(viewModelImpl: ChatViewModel.ChatViewModelImpl):
    BaseModel<ChatContract.IChatViewModel, ChatContract.IChatModel>
        (viewModelImpl) {

    override fun getContract(): ChatContract.IChatModel {
        return ChatModelImpl()
    }

    inner class ChatModelImpl: ChatContract.IChatModel {

    }
}