/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.seastart.meeting.enumerate.DeviceState
import com.chad.library.adapter.base.BaseQuickAdapter
import com.freewind.seastarvideo.databinding.ItemMultiMixOBinding
import com.freewind.seastarvideo.meeting.MemberInfo

/**
 * @author: wiatt
 * @date: 2024/1/10 16:16
 * @description:
 */
class MeetingMultiMixOAdapter(): BaseQuickAdapter<MemberInfo, MeetingMultiMixOAdapter.MemberMultiMixOViewHolder>()  {

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): MemberMultiMixOViewHolder {
        val viewBinding = ItemMultiMixOBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = MemberMultiMixOViewHolder(viewBinding)


        return holder
    }

    override fun onBindViewHolder(
        holder: MemberMultiMixOViewHolder, position: Int, item: MemberInfo?
    ) {
        item?.let { info ->
            if (info.cameraStatus == DeviceState.Open) {
                holder.placeholderIv.visibility = View.VISIBLE
                holder.avatarIv.visibility = View.GONE
            } else {
                holder.placeholderIv.visibility = View.GONE
                holder.avatarIv.visibility = View.VISIBLE
            }
            holder.videoSmallIv.isSelected = info.cameraStatus == DeviceState.Open
            holder.micSmallIv.isSelected = info.micStatus == DeviceState.Open
            holder.nickNameTv.text = info.nickName
        }
    }

    inner class MemberMultiMixOViewHolder(viewBinding: ItemMultiMixOBinding): RecyclerView.ViewHolder(viewBinding.root) {
        val itemCl = viewBinding.itemCl
        val placeholderIv = viewBinding.placeholderIv
        val avatarIv = viewBinding.avatarIv
        val videoSmallIv = viewBinding.videoSmallIv
        val micSmallIv = viewBinding.micSmallIv
        val nickNameTv = viewBinding.nickNameTv
    }
}