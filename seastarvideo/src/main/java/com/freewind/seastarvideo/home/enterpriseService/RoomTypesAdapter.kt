/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.enterpriseService

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.freewind.seastarvideo.databinding.ItemRoomTypeBinding

/**
 * @author: wiatt
 * @date: 2023/12/25 15:13
 * @description:
 */
class RoomTypesAdapter: BaseMultiItemAdapter<RoomTypeBean>() {

    init {
        addItemType(RoomTypeBean.RoomTypeEnum.ROOM_TYPE_MEETING.ordinal,
            object : OnMultiItemAdapterListener<RoomTypeBean, MeetingRoomTypeViewHolder> {
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): MeetingRoomTypeViewHolder {
                    val viewBinding = ItemRoomTypeBinding.inflate(
                        LayoutInflater.from(context), parent, false
                    )
                    return MeetingRoomTypeViewHolder(viewBinding)
                }

                override fun onBind(
                    holder: MeetingRoomTypeViewHolder,
                    position: Int,
                    item: RoomTypeBean?
                ) {
                    item?.let {
                        val roomTypeInfo = it.roomTypeInfo as MeetingRoomInfo
                        holder.roomImageIv.setImageResource(roomTypeInfo.imageRes)
                        holder.roomTitleTv.text = roomTypeInfo.title
                        holder.roomDesTv.text = roomTypeInfo.des
                    }
                }
            })
            .onItemViewType { position, list ->
                val item = list[position]
                item.roomType.ordinal
            }
    }

    class MeetingRoomTypeViewHolder(viewBinding: ItemRoomTypeBinding):
        RecyclerView.ViewHolder(viewBinding.root) {
        val roomImageIv = viewBinding.roomImageIv
        val roomTitleTv = viewBinding.roomTitleTv
        val roomDesTv = viewBinding.roomDesTv
    }
}