/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMultiMixBinding
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @description: 会议房间中，多人混合页面
 */
class MultiMixFragment : BaseFragment() {

    private lateinit var viewModel: MeetingRoomViewModel

    private var _binding: FragmentMultiMixBinding? = null
    private val binding get() = _binding!!

    private lateinit var memberMultiAdapter: MemberMultiStatePageAdapter
    private var viewListenerImpl: MeetingRoomListenerImpl = MeetingRoomListenerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MeetingRoomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiMixBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initLiveData()
        viewModel.addListener(viewListenerImpl)
        initListener()
        initPage()

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeListener(viewListenerImpl)
    }

    private fun initLiveData() {

    }

    private fun initListener() {
        binding.btnAddMuch.setOnClickListener {
            viewModel.addMoreMember()
        }
        binding.btnAdd.setOnClickListener {
            viewModel.addOneMember()
        }
        binding.btnRemove.setOnClickListener {
            val removeNum = binding.removeNumEt.text.toString().toInt()
            viewModel.removeOneMember(removeNum)
        }
        binding.btnUpdateMember.setOnClickListener {
            val updateMemberNum = binding.updateMemberEt.text.toString().toInt()
            viewModel.updateMember(updateMemberNum)
        }
    }

    private fun initPage() {
        val fragments = mutableListOf<MultiListFragment>()
        val totalPage = calculatePageNum()
        for (index in 0 until totalPage) {
            fragments.add(MultiListFragment.newInstance(index))
        }

        binding.memberMultiVp.offscreenPageLimit = 1
        binding.memberMultiVp.isSaveEnabled = false
        memberMultiAdapter = MemberMultiStatePageAdapter(childFragmentManager, lifecycle, fragments, viewModel)
        binding.memberMultiVp.adapter = memberMultiAdapter
    }

    /**
     * 计算当前总页数
     */
    private fun calculatePageNum(): Int {
        val addOne = if (viewModel.memberList.size % 4 == 0) {
            0
        } else {
            1
        }
        return viewModel.memberList.size / 4 + addOne
    }

    companion object {

        @JvmStatic
        fun newInstance() = MultiMixFragment()
    }

    inner class MeetingRoomListenerImpl: MeetingRoomViewModel.MeetingRoomListener {
        override fun onMemberListAddOne(position: Int) {
            val totalPage = calculatePageNum()
            if (totalPage > memberMultiAdapter.itemCount) {
                for (index in memberMultiAdapter.itemCount until totalPage) {
                    memberMultiAdapter.insertFragment(MultiListFragment.newInstance(index))
                }
            }
        }

        override fun onMemberListRemoveOne(position: Int) {
            if (viewModel.memberList.size == 0) {
                return
            }

            val totalPage = calculatePageNum()
            val targetPageIndex = position / 4
            val targetItemIndex = position % 4
            LogUtil.i("memberListRemoveLiveData, totalPage = $totalPage, targetPageIndex = $targetPageIndex, targetItemIndex = $targetItemIndex", "wiatt")
            memberMultiAdapter.updateMultiListPage(targetPageIndex, targetItemIndex)
            if (totalPage < memberMultiAdapter.itemCount) {
                memberMultiAdapter.removeFragment(memberMultiAdapter.itemCount - 1)
            }
        }

        override fun onUpdateMember(position: Int, memberInfo: MemberInfo) {

        }
    }
}