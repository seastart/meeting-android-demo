/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.activity.ChatActivity
import com.freewind.seastarvideo.activity.MemberActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMeetingRoomBinding
import com.freewind.seastarvideo.ui.ClickFrameLayout
import com.freewind.seastarvideo.ui.DialogManager
import com.freewind.seastarvideo.utils.OtherUiManager
import com.freewind.seastarvideo.utils.WeakHandler
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus

/**
 * @author: wiatt
 * @description: 会议房间 fragment，会议功能的主体部分
 */
// 自己的昵称、音频状态、视频状态、自己的共享屏幕状态、扬声器状态、摄像头选择状态
// 会议成员列表、成员的音频状态、视频状态
// 房间的id、音频状态、视频状态、房间持续时间
class MeetingRoomFragment : BaseFragment() {

    private val ARG_NICKNAME = "nickname"
    private val ARG_ROOM_ID = "room_id"

    private var _binding: FragmentMeetingRoomBinding? = null
    private val binding get() = _binding!!
    private var currentFragment: Fragment? = null

    private lateinit var viewModel: MeetingRoomViewModel

    private lateinit var nickName: String
    private lateinit var roomId: String

    private var soloVideoFragment: SoloVideoFragment? = null
    private var soloAvatarFragment: SoloAvatarFragment? = null
    private var multiMixFragment: MultiMixFragment? = null
//    private var multiMixFragment: MultiMixOFragment? = null

    private var handler: MeetingRoomHandler = MeetingRoomHandler(Looper.getMainLooper(), this)
    private var HANDLER_MSG_HIDE_COVER = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(requireActivity()).reset().init()
        arguments?.let {
            nickName = it.getString(ARG_NICKNAME, resources.getString(R.string.def_nickname))
            roomId = it.getString(ARG_ROOM_ID, resources.getString(R.string.def_room_id))
        }
        viewModel = ViewModelProvider(requireActivity())[MeetingRoomViewModel::class.java]
        viewModel.updateInitialValue(nickName, roomId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingRoomBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarI.root)

        initLiveData()
        initView()
        initListener()
        handler.sendHandlerMessage(HANDLER_MSG_HIDE_COVER, null, -1, -1, 5000)

        return rootView
    }

    private fun initLiveData() {
        viewModel.roomIdLiveData.observe(viewLifecycleOwner) {
            binding.topBarI.roomIdTv.text = it
        }
        viewModel.mySpeakerStatusLiveData.observe(viewLifecycleOwner) {
            binding.topBarI.switchSpeakerIv.isSelected = it
        }
        viewModel.myMicStatusLiveData.observe(viewLifecycleOwner) {
            binding.bottomToolbarI.micBigIv.isSelected = it
        }
        viewModel.myCameraStatusLiveData.observe(viewLifecycleOwner) {
            binding.bottomToolbarI.cameraBigIv.isSelected = it
        }
        viewModel.myScreenShareLiveData.observe(viewLifecycleOwner) {
            binding.bottomToolbarI.screenIv.isSelected = it
        }
        viewModel.showSecFragmentLiveData.observe(viewLifecycleOwner) {
            when(it) {
                FRAGMENT_SEC_SOLO_VIDEO -> showSoloVideoPage()
                FRAGMENT_SEC_SOLO_AVATAR -> showSoloAvatarPage()
                FRAGMENT_SEC_MULTI_MIX -> showMultiMixPage()
            }
        }
    }

    private fun initView() {
        viewModel.getRoomId()
        viewModel.getMySpeakerStatus()
        viewModel.getMyMicStatus()
        viewModel.getMyCameraStatus()
        viewModel.getMyScreenShareStatus()
        viewModel.checkSecFragment()
    }

    private fun initListener() {
        setOnClickNoRepeat(
            binding.topBarI.toggleCameraIv, binding.topBarI.switchSpeakerIv,
            binding.topBarI.reportIv, binding.topBarI.leaveRoomTv,
            binding.bottomToolbarI.micBigCl, binding.bottomToolbarI.cameraBigCl,
            binding.bottomToolbarI.screenCl, binding.bottomToolbarI.memberCl,
            binding.bottomToolbarI.chatCl) { it ->
            when(it) {
                binding.topBarI.toggleCameraIv -> {
                    Toast.makeText(requireContext(), "切换摄像头方向", Toast.LENGTH_SHORT).show()
                }
                binding.topBarI.switchSpeakerIv -> {
                    viewModel.switchSpeakerStatus()
                }
                binding.topBarI.reportIv -> {
                    Toast.makeText(requireContext(), "点击投诉按钮", Toast.LENGTH_SHORT).show()
                }
                binding.topBarI.leaveRoomTv -> {
                    DialogManager.instance.showLeaveRoomDialog(requireContext()) {
                        viewModel.liveRoom()
                        EventBus.getDefault().post(MeetingRoomEventBean.goBackPageEvent())
                    }
                }
                binding.bottomToolbarI.micBigCl -> {
                    DialogManager.instance.showRequestMicPermissionDialog(requireContext()) {
                        Toast.makeText(requireContext(), "跳转到麦克风权限授予页面", Toast.LENGTH_SHORT).show()
                        viewModel.switchMyMicStatus()
                    }
                }
                binding.bottomToolbarI.cameraBigCl -> {
                    DialogManager.instance.showRequestOpenCameraDialog(requireContext(), "主持人xxx请求开启视频") {
                        Toast.makeText(requireContext(), "同意开启视频", Toast.LENGTH_SHORT).show()
                        viewModel.switchMyCameraStatus()
                    }
                }
                binding.bottomToolbarI.screenCl -> {
                    DialogManager.instance.showShareScreenDialog(requireContext()) { shareType ->
                        if (shareType == DialogManager.FLAG_SHARE_TYPE_SCREEN) {
                            Toast.makeText(requireContext(), "开始共享屏幕", Toast.LENGTH_SHORT).show()
                        } else if (shareType == DialogManager.FLAG_SHARE_TYPE_WHITE_BOARD) {
                            Toast.makeText(requireContext(), "开始共享白板", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                binding.bottomToolbarI.memberCl -> {
                    MemberActivity.startMemberListPage(requireContext())
                    Toast.makeText(requireContext(), "点击成员列表按钮", Toast.LENGTH_SHORT).show()
                }
                binding.bottomToolbarI.chatCl -> {
                    ChatActivity.startChatPage(requireContext())
                    Toast.makeText(requireContext(), "点击聊天按钮", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.contentSecFl.mListener = object : ClickFrameLayout.ActionListener {
            override fun onActionDown() {
                toggleCoverShow()
            }
        }
    }

    /**
     * 展示单人视频页
     */
    @Synchronized
    private fun showSoloVideoPage() {
        if (soloVideoFragment == null) {
            // 获取准确的用户昵称和当前麦克风是否开启
            soloVideoFragment = SoloVideoFragment.newInstance()
        }
        switchFragment(soloVideoFragment)
    }

    /**
     * 展示单人头像页
     */
    @Synchronized
    private fun showSoloAvatarPage() {
        if (soloAvatarFragment == null) {
            // 获取准确的用户昵称和当前麦克风是否开启
            soloAvatarFragment = SoloAvatarFragment.newInstance()
        }
        switchFragment(soloAvatarFragment)
    }

    /**
     * 展示多人混合页
     */
    @Synchronized
    private fun showMultiMixPage() {
        if (multiMixFragment == null) {
//            multiMixFragment = MultiMixOFragment.newInstance()
            multiMixFragment = MultiMixFragment.newInstance()
        }
        switchFragment(multiMixFragment)
    }

    fun toggleCoverShow() {
        // 顶部的显示与隐藏
        if (binding.topBarI.root.translationY == 0f) {
            // 关闭定时隐藏上下工具栏功能
            handler.removeMessages(HANDLER_MSG_HIDE_COVER)
            animCoverHide()
        } else {
            animCoverShow()
            // 开启定时隐藏上下工具栏功能
            handler.sendHandlerMessage(HANDLER_MSG_HIDE_COVER, null, -1, -1, 5000)
        }
    }

    /**
     * 用于切换Fragment
     * 当不显示fragment时，nextFragment为空
     */
    private fun switchFragment(nextFragment: Fragment?): Boolean{
        if(nextFragment == currentFragment) {
            return false
        }

        if (currentFragment != null && currentFragment!!.isAdded) {
            hideFragment(currentFragment!!)
        }

        if (nextFragment != null) {
            if (nextFragment.isAdded) {
                showFragment(nextFragment)
            } else {
                addFragment(nextFragment, R.id.contentSecFl)
            }
        }
        currentFragment = nextFragment
        return true
    }

    fun animCoverShow() {
        if (binding.topBarI.root.translationY == 0f) {
            return
        }
        val animatorSet = AnimatorSet()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                ImmersionBar.with(requireActivity()).hideBar(BarHide.FLAG_SHOW_BAR).init()
//                ImmersionBar.with(requireActivity()).reset().init()
            }
        })
        val topAnimator = ObjectAnimator.ofFloat(binding.topBarI.root, "translationY", -binding.topBarI.root.height.toFloat(), 0f)
        val bottomAnimator = ObjectAnimator.ofFloat(binding.bottomToolbarI.root, "translationY", binding.bottomToolbarI.root.height.toFloat(), 0f)
        animatorSet.play(topAnimator).with(bottomAnimator)
        animatorSet.duration = 300
        animatorSet.start()
    }

    @SuppressLint("NewApi")
    fun animCoverHide() {
        if (binding.topBarI.root.translationY < 0) {
            return
        }
        val animatorSet = AnimatorSet()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                ImmersionBar.with(requireActivity()).hideBar(BarHide.FLAG_HIDE_BAR).init()
            }
        })
        val topAnimator = ObjectAnimator.ofFloat(binding.topBarI.root, "translationY", 0f, -binding.topBarI.root.height.toFloat())
        val bottomAnimator = ObjectAnimator.ofFloat(binding.bottomToolbarI.root, "translationY", 0f, binding.bottomToolbarI.root.height.toFloat())
        animatorSet.play(topAnimator).with(bottomAnimator)
        animatorSet.duration = 300
        animatorSet.start()
    }

    companion object {
        // 标识：单人视频 fragment
        val FRAGMENT_SEC_SOLO_VIDEO = "solo_video_fragment"
        // 标识：单人头像 fragment
        val FRAGMENT_SEC_SOLO_AVATAR = "solo_avatar_fragment"
        // 标识：多人混合 fragment
        val FRAGMENT_SEC_MULTI_MIX = "multi_mix_fragment"

        @JvmStatic
        fun newInstance(nickNameParam: String, roomIdParam: String) =
            MeetingRoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NICKNAME, nickNameParam)
                    putString(ARG_ROOM_ID, roomIdParam)
                }
            }
    }

    class MeetingRoomHandler(looper: Looper, owner: MeetingRoomFragment):
        WeakHandler<MeetingRoomFragment>(looper, owner) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            getOwner()?.let { owner ->
                when(msg.what) {
                    owner.HANDLER_MSG_HIDE_COVER -> {
                        owner.animCoverHide()
                    }
                }
            }
        }
    }
}