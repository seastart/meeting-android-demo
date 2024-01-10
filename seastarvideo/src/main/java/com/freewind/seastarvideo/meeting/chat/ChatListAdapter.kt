/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.freewind.seastarvideo.databinding.ItemChatContentMeBinding
import com.freewind.seastarvideo.databinding.ItemChatContentOtherBinding
import com.freewind.seastarvideo.databinding.ItemChatTimeBinding
import com.freewind.seastarvideo.meeting.chat.bean.BaseChatBean
import com.freewind.seastarvideo.meeting.chat.bean.ChatContentBean
import com.freewind.seastarvideo.meeting.chat.bean.ChatTimeBean
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @date: 2024/1/9 16:08
 * @description:
 */
class ChatListAdapter(): BaseMultiItemAdapter<BaseChatBean>() {

    init {
        addItemType(ITEM_TYPE_TIME, object : BaseMultiItemAdapter.OnMultiItemAdapterListener<BaseChatBean, ChatTimeViewHolder> {
            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): ChatTimeViewHolder {
                val viewBinding = ItemChatTimeBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                val holder = ChatTimeViewHolder(viewBinding)
                return holder
            }

            override fun onBind(holder: ChatTimeViewHolder, position: Int, item: BaseChatBean?) {
                item?.let {
                    val chatTimeBean = it as ChatTimeBean
                    // todo 此处要做时间转换
                    holder.chatTimeTv.text = "2023-06-05  11:36"
                }
            }

        }).addItemType(ITEM_TYPE_CONTENT_OTHER, object : BaseMultiItemAdapter.OnMultiItemAdapterListener<BaseChatBean, ChatContentOtherViewHolder> {
            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): ChatContentOtherViewHolder {
                val viewBinding = ItemChatContentOtherBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                val holder = ChatContentOtherViewHolder(viewBinding)
                return holder
            }

            override fun onBind(holder: ChatContentOtherViewHolder, position: Int, item: BaseChatBean?) {
                item?.let {
                    val chatContentBean = it as ChatContentBean
                    // todo 添加头像
                    holder.nickNameTv.text = chatContentBean.nickName
                    holder.contentTv.text = chatContentBean.content
                }
            }

        }).addItemType(ITEM_TYPE_CONTENT_ME, object : BaseMultiItemAdapter.OnMultiItemAdapterListener<BaseChatBean, ChatContentMeViewHolder> {
            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): ChatContentMeViewHolder {
                val viewBinding = ItemChatContentMeBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                val holder = ChatContentMeViewHolder(viewBinding)
                return holder
            }

            override fun onBind(holder: ChatContentMeViewHolder, position: Int, item: BaseChatBean?) {
                item?.let {
                    val chatContentBean = it as ChatContentBean
                    // todo 添加头像
                    holder.nickNameTv.text = chatContentBean.nickName
                    holder.contentTv.text = chatContentBean.content
                }
            }

        }).onItemViewType { position, list ->
            val item = list[position]
            if (item is ChatContentBean) {
                if (item.isMe) {
                    ITEM_TYPE_CONTENT_ME
                } else {
                    ITEM_TYPE_CONTENT_OTHER
                }
            } else if (item is ChatTimeBean) {
                ITEM_TYPE_TIME
            } else {
                LogUtil.e("BaseChatBean 类型错误")
                ITEM_TYPE_TIME
            }
        }
    }

    class ChatTimeViewHolder(viewBinding: ItemChatTimeBinding):
        RecyclerView.ViewHolder(viewBinding.root) {
        val chatTimeTv = viewBinding.chatTimeTv
    }

    class ChatContentOtherViewHolder(viewBinding: ItemChatContentOtherBinding):
        RecyclerView.ViewHolder(viewBinding.root) {
        val avatarIv = viewBinding.avatarIv
        val nickNameTv = viewBinding.nickNameTv
        val contentTv = viewBinding.contentTv
    }

    class ChatContentMeViewHolder(viewBinding: ItemChatContentMeBinding):
        RecyclerView.ViewHolder(viewBinding.root) {
        val avatarIv = viewBinding.avatarIv
        val nickNameTv = viewBinding.nickNameTv
        val contentTv = viewBinding.contentTv
    }

    companion object {
        // 聊天列表中 item 类型：时间点
        val ITEM_TYPE_TIME = 0
        // 聊天列表中 item 类型：我的聊天内容
        val ITEM_TYPE_CONTENT_ME = 1
        // 聊天列表中 item 类型：别人的聊天内容
        val ITEM_TYPE_CONTENT_OTHER = 2
    }
}