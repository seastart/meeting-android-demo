/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @date: 2023/12/29 17:38
 * @description: 多成员页面适配器。
 * todo 功能遇阻，暂不使用
 */
class MemberMultiStatePageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle, var viewModel: MeetingRoomViewModel):
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var itemCount: Int = 2

    private val mFragmentHashCodes: MutableList<Long> = mutableListOf()

//    init {
//        fragments.forEach {
//            mFragmentHashCodes.add(it.hashCode().toLong())
//        }
//    }

    override fun getItemCount(): Int {
        LogUtil.i("MemberMultiStatePageAdapter_getItemCount, size = $itemCount", "wiatt")
//        return fragments.size
        return itemCount
    }

    override fun createFragment(position: Int): Fragment {
        LogUtil.i("MemberMultiStatePageAdapter_createFragment, position = $position", "wiatt")
//        return fragments[position]
        return MultiListFragment.newInstance(viewModel, position)
    }

//    // 按照此方法重写 getItemId、containsItem 函数，数据源中fragment的hashcode 一对一 增删时注意保持一致
//    // 可以解决删除 fragment 时，删除内容不一致的问题，以及其附带的其他崩溃问题
//    override fun getItemId(position: Int): Long {
//        return fragments[position].hashCode().toLong()
////        return super.getItemId(position)
//    }
//
//    override fun containsItem(itemId: Long): Boolean {
//        return mFragmentHashCodes.contains(itemId);
//    }

    fun updateItemCount(count: Int) {
        itemCount = count
    }



//    fun insertFragment(fragmentItem: MultiListFragment) {
//        LogUtil.i("MemberMultiStatePageAdapter_insertFragment", "wiatt")
//        fragments.add(fragmentItem)
//        mFragmentHashCodes.add(fragmentItem.hashCode().toLong())
//        notifyItemInserted(fragments.size - 1)
//    }
//
//    fun removeFragment(position: Int) {
//        LogUtil.i("MemberMultiStatePageAdapter_removeFragment, position = $position", "wiatt")
//        fragments.removeAt(position)
//        mFragmentHashCodes.removeAt(position)
//        notifyItemRemoved(position)
//    }
}