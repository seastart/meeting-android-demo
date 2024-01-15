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
import com.freewind.seastarvideo.databinding.FragmentMultiListBinding
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @description: 多成员页面，四个成员的列表页
 */
class MultiListFragment : BaseFragment() {
    private val ARG_INDEX = "index"

    private lateinit var viewModel: MeetingRoomViewModel
    private var viewListenerImpl: MeetingRoomListenerImpl = MeetingRoomListenerImpl()

    private var _binding: FragmentMultiListBinding? = null
    private val binding get() = _binding!!

    private var curZeroFragment: MultiItemFragment? = null
    private var curOneFragment: MultiItemFragment? = null
    private var curTwoFragment: MultiItemFragment? = null
    private var curThreeFragment: MultiItemFragment? = null

    private var mIndex: Int = 0

    private var isCreate: Boolean = false
    var isShow: Boolean = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mIndex = it.getInt(ARG_INDEX, 0)
        }
        LogUtil.i("test，onCreate--mIndex = $mIndex", "wiatt")
        viewModel = ViewModelProvider(requireActivity())[MeetingRoomViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiListBinding.inflate(inflater, container, false)
        val rootView = binding.root
        isCreate = true

        binding.flagTv.text = "测试Flag，当前页：$mIndex"
        initLiveData()
        viewModel.addListener(viewListenerImpl)
        initView()
        LogUtil.i("test，onCreateView--mIndex = $mIndex", "wiatt")
        return rootView
    }

    override fun onResume() {
        super.onResume()
        isShow = true

        LogUtil.i("test，onResume--mIndex = $mIndex", "wiatt")
    }

    override fun onPause() {
        super.onPause()
        isShow = false
        LogUtil.i("test，onPause--mIndex = $mIndex", "wiatt")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.i("test，onDestroy--mIndex = $mIndex", "wiatt")
        isCreate = false
        viewModel.removeListener(viewListenerImpl)
    }

    private fun initLiveData() {
    }

    private fun initView() {
        LogUtil.i("fragments.size = ${childFragmentManager.fragments.size}", "wiatt")
        childFragmentManager.fragments.forEach {
            removeFragment(it)
        }
        val startMemberIndex = mIndex * totalItemCount
        LogUtil.i("MultiListFragment_initView, mIndex = $mIndex, startMemberIndex = $startMemberIndex", "wiatt")
        for (i in 0 until totalItemCount) {
            val memberIndex = startMemberIndex + i
            if (memberIndex < viewModel.memberList.size) {
                val memberInfo = viewModel.memberList[memberIndex]
                LogUtil.i("MultiListFragment_initView, itemIndex = $memberIndex, memberInfo = $memberInfo", "wiatt")
                switchFragment(memberInfo, i)
            }
        }
    }

    /**
     * 更新当前页所处的索引
     */
    fun updatePageIndex(index: Int) {
        mIndex = index
        arguments?.putInt(ARG_INDEX, index)
        if (isCreate) {
            LogUtil.i("test，updatePageIndex--mIndex = $mIndex", "wiatt")
            binding.flagTv.text = "测试Flag，当前页：$mIndex"
        }
    }

    /**
     * 更新页面项
     */
    fun updatePageItems(startItemIndex: Int) {
        if (!isCreate) {
            return
        }
        val startMemberIndex = mIndex * totalItemCount
        for (index in startItemIndex until totalItemCount) {
            val memberIndex = startMemberIndex + index
            switchFragment(null, index)
            if (memberIndex < viewModel.memberList.size) {
                val memberInfo = viewModel.memberList[memberIndex]
                LogUtil.i("MultiListFragment_updatePageItems, itemIndex = $memberIndex, memberInfo = $memberInfo", "wiatt")
                switchFragment(memberInfo, index)
            }
        }
    }

    /**
     * 新增一个成员
     */
    private fun addOneItem(position: Int) {
        if (!isCreate) {
            return
        }
        val startMemberIndex = mIndex * totalItemCount
        if (position >= startMemberIndex && position < startMemberIndex + totalItemCount) {
            if (position < viewModel.memberList.size) {
                val memberInfo = viewModel.memberList[position]
                LogUtil.i("MultiListFragment_updatePageItems, position = $position, memberInfo = $memberInfo", "wiatt")
                switchFragment(memberInfo, position % totalItemCount)
            }
        }
    }

    /**
     * 用于切换Fragment
     * 当不显示fragment时，nextFragment为空
     * 该函数必须根据 totalItemCount 的值而改变
     */
    private fun switchFragment(memberInfo: MemberInfo?, itemIndex: Int): Boolean{
        when(itemIndex) {
            0 -> {
                if (memberInfo == null) {
                    if (curZeroFragment?.isAdded == true) {
                        hideFragment(curZeroFragment!!)
                    }
                } else {
                    if (curZeroFragment == null) {
                        curZeroFragment = MultiItemFragment.newInstance(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    } else {
                        curZeroFragment!!.updateMember(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    }

                    if (curZeroFragment!!.isAdded) {
                        showFragment(curZeroFragment!!)
                    } else {
                        addFragment(curZeroFragment!!, R.id.containerZeroFl)
                    }
                }
                return true
            }
            1 -> {
                if (memberInfo == null) {
                    if (curOneFragment?.isAdded == true) {
                        hideFragment(curOneFragment!!)
                    }
                } else {
                    if (curOneFragment == null) {
                        curOneFragment = MultiItemFragment.newInstance(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    } else {
                        curOneFragment!!.updateMember(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    }

                    if (curOneFragment!!.isAdded) {
                        showFragment(curOneFragment!!)
                    } else {
                        addFragment(curOneFragment!!, R.id.containerOneFl)
                    }
                }
                return true
            }
            2 -> {
                if (memberInfo == null) {
                    if (curTwoFragment?.isAdded == true) {
                        hideFragment(curTwoFragment!!)
                    }
                } else {
                    if (curTwoFragment == null) {
                        curTwoFragment = MultiItemFragment.newInstance(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    } else {
                        curTwoFragment!!.updateMember(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    }

                    if (curTwoFragment!!.isAdded) {
                        showFragment(curTwoFragment!!)
                    } else {
                        addFragment(curTwoFragment!!, R.id.containerTwoFl)
                    }
                }
                return true
            }
            3 -> {
                if (memberInfo == null) {
                    if (curThreeFragment?.isAdded == true) {
                        hideFragment(curThreeFragment!!)
                    }
                } else {
                    if (curThreeFragment == null) {
                        curThreeFragment = MultiItemFragment.newInstance(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    } else {
                        curThreeFragment!!.updateMember(memberInfo.nickName, memberInfo.micStatus, memberInfo.cameraStatus)
                    }

                    if (curThreeFragment!!.isAdded) {
                        showFragment(curThreeFragment!!)
                    } else {
                        addFragment(curThreeFragment!!, R.id.containerThreeFl)
                        curThreeFragment!!.isAdded
                    }
                }
                return true
            }
        }
        return false
    }

    companion object {

        const val totalItemCount = 4

        @JvmStatic
        fun newInstance(indexParam: Int) =
            MultiListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INDEX, indexParam)
                }
            }
    }

    inner class MeetingRoomListenerImpl: MeetingRoomViewModel.MeetingRoomListener {
        override fun onMemberListAddOne(position: Int) {
            LogUtil.i("onMemberListAddOne, position = $position", "wiatt")
            addOneItem(position)
        }

        override fun onMemberListRemoveOne(position: Int) {

        }

    }
}