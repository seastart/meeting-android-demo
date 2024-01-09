/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMemberListBinding
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.ui.DialogManager
import com.freewind.seastarvideo.utils.KvUtil
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus

/**
 * @author: wiatt
 * @description: 参会成员 fragment
 */
class MemberListFragment : BaseFragment() {

    private var _binding: FragmentMemberListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MemberListViewModel

    private var adapter: MemberListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MemberListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberListBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)

        initView()
        initLiveData()
        initListener()
        initData()

        return rootView
    }

    private fun initData() {
        viewModel.getMembers()
    }

    private fun initLiveData() {
        viewModel.membersLiveData.observe(viewLifecycleOwner) { members ->
            adapter?.submitList(members)
        }
    }

    fun initView() {
        val meInfo = viewModel.meInfo
        adapter = initAdapter(meInfo)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.membersRv.adapter = adapter
        binding.membersRv.layoutManager = layoutManager

        if (meInfo.role == MemberInfo.MEMBER_ROLE_COMPERE) {
            binding.allOptCl.visibility = View.VISIBLE
        } else {
            binding.allOptCl.visibility = View.GONE
        }
    }

    // 此处有几个问题需要确认。
    // 1. 是否只有主持人可以操作禁音、禁画、踢人的操作，普通成员能否执行禁音、禁画操作？
    // 2. 主持人禁音、禁画操作是禁止对应成员发音频流、视频流，还是只是自己不接收
    // 3. 普通成员如果可以执行禁音、禁画操作，是否只是自己不接收音频流、视频流
    // 4. 普通成员是否可以执行全体禁音、全体禁画的操作
    fun initAdapter(meInfo: MemberInfo): MemberListAdapter{
        val adapter = MemberListAdapter(meInfo, viewModel)
        // 默认不使用空布局
        adapter.isEmptyViewEnable = false
        adapter.addOnItemChildClickListener(R.id.audioStatusIv) {
                adapterClick, view, position ->
            val expectStatus = !view.isSelected

            adapterClick.getItem(position)?.let {
                val isSuccess = viewModel.setMemberMicStatus(expectStatus, it.id)
                if (isSuccess) {
                    it.micStatus = expectStatus
                    view.isSelected = expectStatus
                }
            }
        }
        adapter.addOnItemChildClickListener(R.id.videoStatusIv) {
                adapterClick, view, position ->
            val expectStatus = !view.isSelected
            adapterClick.getItem(position)?.let {
                val isSuccess = viewModel.setMemberCameraStatus(expectStatus, it.id)
                if (isSuccess) {
                    it.cameraStatus = expectStatus
                    view.isSelected = expectStatus
                }
            }
        }
        adapter.addOnItemChildClickListener(R.id.removeMemberIv) {
                adapterClick, view, position ->
            val dialogContent = String.format(resources.getString(R.string.remove_someone_member), adapterClick.getItem(position)?.nickName ?: " ")
            DialogManager.instance.showRemoveMemberDialog(requireContext(), dialogContent) {
                // todo 此处应该要做移除成员的网络请求，然后再执行移除操作
                adapterClick.removeAt(position)
                Toast.makeText(requireContext(), "移除该成员", Toast.LENGTH_SHORT).show()
            }
        }
        return adapter
    }

    fun initListener() {
        binding.topBarCtb.listener = object : CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                EventBus.getDefault().post(MemberEventBean.goBackPageEvent())
            }

            override fun onClickTitle() {
                viewModel.updateMembers()
            }

        }
        setOnClickNoRepeat(binding.optAllMicStv) {
            // 因为在现实的时候已经判断过是否是主持人，所以此处不必再做判断
            if (it.isSelected) {
                DialogManager.instance.showAllMemberOpenMicDialog(requireContext()) {
                    it.isSelected = false
                    KvUtil.encode(KvUtil.MEETING_ROOM_AUDIO_STATUS, true)
                    adapter?.notifyDataSetChanged()
                }
            } else {
                DialogManager.instance.showAllMemberCloseMicDialog(requireContext(), false) { enableSelfOpen ->
                    it.isSelected = true
                    KvUtil.encode(KvUtil.MEETING_ROOM_AUDIO_STATUS, false)
                    KvUtil.encode(KvUtil.MEETING_ROOM_ENABLE_SELF_OPEN_AUDIO, enableSelfOpen)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
        setOnClickNoRepeat(binding.optAllCameraStv) {
            // 因为在现实的时候已经判断过是否是主持人，所以此处不必再做判断
            if (it.isSelected) {
                DialogManager.instance.showAllMemberOpenCameraDialog(requireContext()) {
                    it.isSelected = false
                    KvUtil.encode(KvUtil.MEETING_ROOM_VIDEO_STATUS, true)
                    adapter?.notifyDataSetChanged()
                }
            } else {
                DialogManager.instance.showAllMemberCloseCameraDialog(requireContext(), false) { enableSelfOpen ->
                    it.isSelected = true
                    KvUtil.encode(KvUtil.MEETING_ROOM_VIDEO_STATUS, false)
                    KvUtil.encode(KvUtil.MEETING_ROOM_ENABLE_SELF_OPEN_VIDEO, enableSelfOpen)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MemberListFragment()
    }
}