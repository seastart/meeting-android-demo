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
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.activity.PreMeetingRoomActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentPreMeetingRoomBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.KvUtil
import com.freewind.seastarvideo.utils.OtherUiManager
import com.freewind.seastarvideo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author wiatt
 * @description: 进入会议房间的准备页面
 */
class PreMeetingRoomFragment : BaseFragment() {

    private var _binding: FragmentPreMeetingRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PreMeetingRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreMeetingRoomBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)

        initListener()

        // Inflate the layout for this fragment
        return rootView
    }

    private fun initListener() {
        binding.topBarCtb.listener = object : CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                hideSoftInput()
                EventBus.getDefault().post(PreMeetingRoomEventBean.goBackPageEvent())
            }

            override fun onClickTitle() {

            }
        }

        setOnClickNoRepeat(binding.createMeetingRoomStv, binding.joinMeetingRoomStv) {
            when(it) {
                binding.createMeetingRoomStv -> {
                    val nickName = KvUtil.decodeString(KvUtil.USER_INFO_NICK_NAME)
                    viewModel.createMeeting("$nickName 的会议", "普通会议", null)
                }
                binding.joinMeetingRoomStv -> {
                    EventBus.getDefault().post(
                        PreMeetingRoomEventBean.UpdatePageEvent(
                            PreMeetingRoomActivity.PRE_MEETING_ROOM_JOIN, true, null
                        )
                    )
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[PreMeetingRoomViewModel::class.java]

        viewModel.createMeetingResult.observe(this) { uiResponse ->
            uiResponse?.let { uiResponse ->
                if (uiResponse.isSuccess) {
                    val roomNo = uiResponse.mResult!!
                    EventBus.getDefault().post(
                        PreMeetingRoomEventBean.UpdatePageEvent(
                            PreMeetingRoomActivity.PRE_MEETING_ROOM_CREATE, true, roomNo
                        )
                    )
                } else {
                    ToastUtil.showShortToast("创建房间失败")
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = PreMeetingRoomFragment()
    }
}