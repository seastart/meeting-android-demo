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
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentSoloAvatarBinding
import com.freewind.seastarvideo.http.ConstantHttp

/**
 * @author: wiatt
 * @description: 会议房间中，单人视频页面
 */
class SoloAvatarFragment : BaseFragment() {

    private lateinit var viewModel: MeetingRoomViewModel

    private var _binding: FragmentSoloAvatarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MeetingRoomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoloAvatarBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initLiveData()
        initView()

        return rootView
    }

    private fun initLiveData() {
        viewModel.myNickNameLiveData.observe(viewLifecycleOwner) {
            binding.nickNameTv.text = it
        }
        viewModel.myAvatarLiveData.observe(viewLifecycleOwner) {
            if (it == ConstantHttp.RES_AVATAR_MAN) {
                binding.avatarIv.setImageResource(R.mipmap.avatar_man)
            } else {
                binding.avatarIv.setImageResource(R.mipmap.avatar_woman)
            }
        }
        viewModel.myMicStatusLiveData.observe(viewLifecycleOwner) {
            binding.micSmallIv.isSelected = it
        }
    }

    private fun initView() {
        viewModel.getMyNickName()
        viewModel.getMyAvatar()
        viewModel.getMyMicStatus()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SoloAvatarFragment()
    }
}