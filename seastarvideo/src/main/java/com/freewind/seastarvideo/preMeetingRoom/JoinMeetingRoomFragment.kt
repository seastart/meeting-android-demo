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
import com.freewind.seastarvideo.activity.MeetingRoomActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentJoinMeetingRoomBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.KvUtil
import com.freewind.seastarvideo.utils.OtherUiManager
import com.freewind.seastarvideo.utils.ToastUtil
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
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinMeetingRoomBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)

        initView()
        initListener()

        // Inflate the layout for this fragment
        return rootView
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[PreMeetingRoomViewModel::class.java]
//        viewModel.enterMeetingResult.observe(this) { uiResponse ->
//            uiResponse?.let { response ->
//                if (response.isSuccess) {
//                    val roomNo = binding.valueRoomIdCkve.editContent!!
//                    val isOpenMic = binding.switchMicSb.isChecked
//                    val isOpenCamera = binding.switchCameraSb.isChecked
//                    MeetingRoomActivity.startMeetingRoomPage(requireContext(), nickname, roomNo, isOpenCamera, isOpenMic)
//                } else {
//                    ToastUtil.showShortToast("加入房间失败")
//                }
//            }
//        }
    }

    private fun initView() {
        binding.valueNicknameCkvt.valueContent = nickname
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
        val roomNo = binding.valueRoomIdCkve.editContent
        if (roomNo.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "房间ID为必填项", Toast.LENGTH_LONG).show()
            return
        }

        val isOpenMic = binding.switchMicSb.isChecked
        val isOpenCamera = binding.switchCameraSb.isChecked
        MeetingRoomActivity.startMeetingRoomPage(requireContext(), nickname, roomNo, isOpenCamera, isOpenMic)

//        val avatar = KvUtil.decodeString(KvUtil.USER_INFO_AVATAR)
//        viewModel.enterMeeting(this.requireActivity(), roomNo, null, nickname, avatar)
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