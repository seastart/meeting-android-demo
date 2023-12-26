/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.preMeetingRoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentJoinMeetingRoomBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus

/**
 * @author wiatt
 * @description: 加入会议房间页面
 */
class JoinMeetingRoomFragment : BaseFragment() {

    private var _binding: FragmentJoinMeetingRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PreMeetingRoomViewModel

    private val ARG_NICKNAME = "nickname"
    private lateinit var nickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nickname = it.getString(ARG_NICKNAME, resources.getString(R.string.def_nickname))
        }
        viewModel = ViewModelProvider(this)[PreMeetingRoomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinMeetingRoomBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarCtb)

        initView()
        initListener()

        // Inflate the layout for this fragment
        return rootView
    }

    private fun initView() {
        binding.valueNicknameCkvt.valueContent = nickname
        // todo 获取麦克风、摄像头开关状态，并设置进控件
        binding.switchMicSb.isChecked = false
        binding.switchCameraSb.isChecked = false
    }

    private fun initListener() {
        binding.topBarCtb.listener = object: CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                hideSoftInput()
                EventBus.getDefault().post(PreMeetingRoomEventBean.goBackPageEvent())
            }

            override fun onClickTitle() {

            }
        }
        setOnClickNoRepeat(binding.joinRoomStv) {
            when(it) {
                binding.joinRoomStv -> {
                    joinRoom()
                }
            }
        }
    }

    /**
     * 加入房间
     */
    private fun joinRoom() {
        val roomId = binding.valueRoomIdCkve.editContent
        if (roomId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "房间ID为必填项", Toast.LENGTH_LONG).show()
            return
        }
        val isCheckedMic = binding.switchMicSb.isChecked
        val isCheckedCamera = binding.switchCameraSb.isChecked
        // todo 请求加入房间的接口
        Toast.makeText(requireContext(), "加入房间数据获取完整", Toast.LENGTH_SHORT).show()
    }

    companion object {

        @JvmStatic
        fun newInstance(nicknameParam: String) =
            JoinMeetingRoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NICKNAME, nicknameParam)
                }
            }
    }
}