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
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMultiMixBinding
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @description: 会议房间中，多人混合页面
 * todo 功能遇阻，暂不使用
 */
class MultiMixFragment : BaseFragment() {

    private val ARG_VIEWMODEL = "viewModel"

    private var viewModel: MeetingRoomViewModel? = null

    private var _binding: FragmentMultiMixBinding? = null
    private val binding get() = _binding!!

    private lateinit var memberMultiAdapter: MemberMultiStatePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel = it.getSerializable(ARG_VIEWMODEL) as MeetingRoomViewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiMixBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initListener()
        initPage()

        return rootView
    }

    var testNum = 0
    private fun initListener() {
        binding.btnAdd.setOnClickListener {
//            testNum++
//            memberMultiAdapter.insertFragment(
//                MultiListFragment.newInstance(viewModel, testNum)
//            )
        }
        binding.btnRemove.setOnClickListener {
            viewModel?.memberList?.let {
                it.removeAt(2)
                memberMultiAdapter.updateItemCount((it.size / 4) + 1)
                memberMultiAdapter.notifyItemChanged(0)
            }
        }
    }

    private fun initPage() {
//        LogUtil.i("MultiMixFragment_initPage", "wiatt")
//        val fragmentList = mutableListOf<MultiListFragment>()
//        viewModel?.memberList?.let {
//            val totalPage = (it.size / 4) + 1
//            for (index in 0 until totalPage) {
//                fragmentList.add(MultiListFragment.newInstance(viewModel, index))
//            }
//        }

        binding.memberMultiVp.offscreenPageLimit = 1
        memberMultiAdapter = MemberMultiStatePageAdapter(childFragmentManager, lifecycle, viewModel!!)
        viewModel?.memberList?.let {
            memberMultiAdapter.updateItemCount((it.size / 4) + 1)
        }
        binding.memberMultiVp.adapter = memberMultiAdapter
    }

    companion object {

        @JvmStatic
        fun newInstance(viewModelParam: MeetingRoomViewModel) =
            MultiMixFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_VIEWMODEL, viewModelParam)
                }
            }
    }
}