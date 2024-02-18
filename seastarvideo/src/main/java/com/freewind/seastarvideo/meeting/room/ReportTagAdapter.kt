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
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.freewind.seastarvideo.databinding.ItemReportTagBinding

/**
 * @author: wiatt
 * @date: 2024/2/18 11:52
 * @description:
 */
class ReportTagAdapter(reportTags: ArrayList<ReportTagBean>):
    BaseQuickAdapter<ReportTagBean, ReportTagAdapter.ReportTagViewHolder>() {

    init {
        submitList(reportTags)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ReportTagViewHolder {
        val viewBinding = ItemReportTagBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        val holder = ReportTagViewHolder(viewBinding)

        return holder
    }

    override fun onBindViewHolder(
        holder: ReportTagViewHolder,
        position: Int,
        item: ReportTagBean?
    ) {
        item?.let { reportTag ->
            holder.contentTv.text = reportTag.desc
            holder.contentTv.isSelected = reportTag.isSelected
        }
    }

    inner class ReportTagViewHolder(viewBinding: ItemReportTagBinding): RecyclerView.ViewHolder(viewBinding.root) {
        val contentTv = viewBinding.contentTv
    }
}