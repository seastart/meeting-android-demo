/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.enterpriseService

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.QuickAdapterHelper
import com.freewind.seastarvideo.EnvArgument
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.activity.LoginActivity
import com.freewind.seastarvideo.activity.PreMeetingRoomActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentEnterpriseServiceBinding
import com.freewind.seastarvideo.ui.LinearDividerItemDecoration
import com.freewind.seastarvideo.utils.LogUtil
import com.freewind.seastarvideo.utils.OtherUiManager

/**
 * @author wiatt
 * @description: 企业服务页面
 */
class EnterpriseServiceFragment : BaseFragment() {

    private var _binding: FragmentEnterpriseServiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EnterpriseServiceViewModel

    private var mRoomTypesAdapter: RoomTypesAdapter? = null
    private var helper: QuickAdapterHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[EnterpriseServiceViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentEnterpriseServiceBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)
        // 目前这个数据是写死的，以后可能会成为变动的
        val roomTypes = initData()
        initRoomTypeRv(roomTypes)
        return rootView
    }

    private fun initData(): MutableList<RoomTypeBean> {
        val roomTypeBean = mutableListOf<RoomTypeBean>()
        val meetingRoomInfo = MeetingRoomInfo(
            R.mipmap.image_meeting_room,
            resources.getString(R.string.room_title_meeting),
            resources.getString(R.string.room_des_meeting)
        )
        roomTypeBean.add(
            RoomTypeBean(
                RoomTypeBean.RoomTypeEnum.ROOM_TYPE_MEETING,
                meetingRoomInfo)
        )
        return roomTypeBean
    }

    private fun initRoomTypeRv(data: MutableList<RoomTypeBean>) {
        val adapter = initRoomTypeAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.roomListRv.adapter = adapter
        binding.roomListRv.layoutManager = layoutManager
        binding.roomListRv.addItemDecoration(
            LinearDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                resources.getDimension(R.dimen.dp_16).toInt(), 0,
                Color.TRANSPARENT)
        )
    }

    private fun initRoomTypeAdapter(data: MutableList<RoomTypeBean>): ConcatAdapter {
        mRoomTypesAdapter = RoomTypesAdapter()
        mRoomTypesAdapter!!.isEmptyViewEnable = false
        mRoomTypesAdapter!!.submitList(data)
        mRoomTypesAdapter!!.setOnItemClickListener { adapterClick, view, position ->
            if (EnvArgument.instance.token.isNullOrEmpty()) {
                this@EnterpriseServiceFragment.activity?.let { activity ->
                    LoginActivity.startActivity(activity)
                }
                return@setOnItemClickListener
            }
            // 点击 item 进入对应类型的房间
            val roomtype = adapterClick.getItem(position)?.roomType
            roomtype?.let {
                when(it) {
                    RoomTypeBean.RoomTypeEnum.ROOM_TYPE_MEETING -> {
                        startActivity(Intent(requireContext(), PreMeetingRoomActivity::class.java))
                    }
                }
            }
        }

        helper = QuickAdapterHelper.Builder(mRoomTypesAdapter!!).build()
        return helper!!.adapter
    }

    /**
     * 登录状态改变
     */
    fun loginStatusChange(isLogin: Boolean) {
        LogUtil.i("企业服务页面，登录状态改变， isLogin = $isLogin, isCreateView = $isCreateView")
    }

    companion object {

        @JvmStatic
        fun newInstance() = EnterpriseServiceFragment()
    }
}