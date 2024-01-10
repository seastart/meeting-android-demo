/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.chat

import androidx.lifecycle.MutableLiveData
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.meeting.chat.bean.BaseChatBean
import com.freewind.seastarvideo.meeting.chat.bean.ChatContentBean
import com.freewind.seastarvideo.meeting.chat.bean.ChatTimeBean
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class ChatViewModel():
    BaseViewModel<ChatModel, ChatContract.IChatViewModel>() {

    val chatsLiveData: MutableLiveData<MutableList<BaseChatBean>> = MutableLiveData()
    val chatOneLiveData: MutableLiveData<BaseChatBean> = MutableLiveData()

    lateinit var meInfo: MemberInfo
    val chats = mutableListOf<BaseChatBean>()

    override fun getModel(): ChatModel {
        return ChatModel(ChatViewModelImpl())
    }

    fun init(meInfo: MemberInfo) {
        this.meInfo = meInfo
    }

    /**
     * 获取历史聊天记录
     * todo 此处只做示意使用，实际场景中需要结合接口操作
     */
    fun getHistoryChat() {
        chats.add(ChatContentBean("111", "钱均泽", "这是一段会议详情内容文案这是一段会议详情内容文案这是一段会议详情内容文案这是一段会议详情内容文案这是一段会议详情内容文案", false))
        chats.add(ChatTimeBean(1111111))
        chats.add(ChatContentBean("111", "何颖其", "22222", true))
        chats.add(ChatContentBean("111", "何萌", "这是一段会议详情内容文案这是一段会议详情内容文案这是一段会议详情内容文案这是一段会议详情内容文案这是一段会议详情内容文案", false))
        val tmpMembers = mutableListOf<BaseChatBean>().apply {
            this.addAll(chats)
        }
        chatsLiveData.value = tmpMembers
    }

    /**
     * 发送自己的留言
     * todo 此处只做示意使用，实际场景中需要结合接口操作
     */
    fun sendInputStr(inputStr: String) {
        LogUtil.i("sendInputStr, inputStr = $inputStr", "wiatt")
        val chatContent = ChatContentBean("111", "何颖其", inputStr, true)
        chats.add(chatContent)
        chatOneLiveData.value = chatContent
    }

    inner class ChatViewModelImpl: ChatContract.IChatViewModel {

    }


}