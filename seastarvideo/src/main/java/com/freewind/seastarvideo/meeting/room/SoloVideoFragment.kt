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
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentSoloVideoBinding

/**
 * @author: wiatt
 * @description: 会议房间中，单人头像页面
 */
class SoloVideoFragment : BaseFragment() {

    private val ARG_VIEWMODEL = "viewModel"

    private var _binding: FragmentSoloVideoBinding? = null
    private val binding get() = _binding!!

    private var viewModel: MeetingRoomViewModel? = null

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
        _binding = FragmentSoloVideoBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initLiveData()
        initView()

        return rootView
    }

    private fun initLiveData() {
        viewModel?.let { viewModel ->
            viewModel.myNickNameLiveData.observe(viewLifecycleOwner) {
                binding.nickNameTv.text = it
            }
            viewModel.myMicStatusLiveData.observe(viewLifecycleOwner) {
                binding.micSmallIv.isSelected = it
            }
        }
    }

    private fun initView() {
        viewModel?.getMyNickName()
        viewModel?.getMyMicStatus()
    }

    companion object {

        @JvmStatic
        fun newInstance(viewModelParam: MeetingRoomViewModel) =
            SoloVideoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_VIEWMODEL, viewModelParam)
                }
            }
    }
}