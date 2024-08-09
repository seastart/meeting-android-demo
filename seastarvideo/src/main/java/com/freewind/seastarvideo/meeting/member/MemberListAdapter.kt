/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.member

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.seastart.meeting.enumerate.DeviceState
import cn.seastart.meeting.enumerate.RoleType
import com.chad.library.adapter.base.BaseQuickAdapter
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.databinding.ItemMemberListBinding
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @date: 2024/1/4 16:28
 * @description:
 */
class MemberListAdapter(var meInfo: MemberInfo, var viewModel: MemberListViewModel): BaseQuickAdapter<MemberInfo, MemberListAdapter.MemberListViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): MemberListViewHolder {
        val viewBinding = ItemMemberListBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        val holder = MemberListViewHolder(viewBinding)

        return holder
    }

    override fun onBindViewHolder(holder: MemberListViewHolder, position: Int, item: MemberInfo?) {
        item?.let { info ->
            holder.nickNameTv.text = info.nickName
            if (info.isMe) {
                holder.roleTv.visibility = View.VISIBLE
                holder.roleTv.text = context.resources.getString(R.string.me_parenthesis)
            } else {
                when(info.role) {
                    RoleType.Host -> {
                        holder.roleTv.visibility = View.VISIBLE
                        holder.roleTv.text = context.resources.getString(R.string.compere_parenthesis)
                    }
                    RoleType.Normal -> {
                        holder.roleTv.visibility = View.GONE
                    }
                }
            }
            if (info.isMe || meInfo.role == RoleType.Normal) {
                holder.removeMemberIv.visibility = View.GONE
            } else {
                holder.removeMemberIv.visibility = View.VISIBLE
            }
            LogUtil.i("onBindViewHolder, micStatus = ${viewModel.getMicStatus(info)}")
            holder.audioStatusIv.isSelected = viewModel.getMicStatus(info) == DeviceState.Open
            LogUtil.i("onBindViewHolder, cameraStatus = ${viewModel.getCameraStatus(info)}")
            holder.videoStatusIv.isSelected = viewModel.getCameraStatus(info) == DeviceState.Open
        }
    }



    inner class MemberListViewHolder(viewBinding: ItemMemberListBinding): RecyclerView.ViewHolder(viewBinding.root) {
        val avatarIv = viewBinding.avatarIv
        val nickNameTv = viewBinding.nickNameTv
        val roleTv = viewBinding.roleTv
        val audioStatusIv = viewBinding.audioStatusIv
        val videoStatusIv = viewBinding.videoStatusIv
        val removeMemberIv = viewBinding.removeMemberIv
    }
}