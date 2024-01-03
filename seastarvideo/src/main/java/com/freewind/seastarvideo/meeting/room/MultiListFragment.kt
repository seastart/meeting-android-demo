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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMultiListBinding
import com.freewind.seastarvideo.utils.LogUtil

/**
 * @author: wiatt
 * @description: 多成员页面，四个成员的列表页
 * todo 功能遇阻，暂不使用
 */
class MultiListFragment : BaseFragment() {

    private val ARG_VIEWMODEL = "viewModel"
    private val ARG_INDEX = "index"

    private var viewModel: MeetingRoomViewModel? = null
    private var mIndex: Int = 0

    private var _binding: FragmentMultiListBinding? = null
    private val binding get() = _binding!!

    private var curZeroFragment: Fragment? = null
    private var curOneFragment: Fragment? = null
    private var curTwoFragment: Fragment? = null
    private var curThreeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel = it.getSerializable(ARG_VIEWMODEL) as MeetingRoomViewModel
            mIndex = it.getInt(ARG_INDEX, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiListBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initView()

        return rootView
    }

    private fun initView() {
        viewModel?.let {
            val startItemIndex = mIndex * 4
            LogUtil.i("MultiListFragment_initView, mIndex = $mIndex, ", "wiatt")
            LogUtil.i("MultiListFragment_initView, startItemIndex = $startItemIndex, ", "wiatt")
            LogUtil.i("MultiListFragment_initView, it.memberList.size = ${it.memberList.size}", "wiatt")
            for (i in 0 until 4) {
                val itemIndex = startItemIndex + i
                if (itemIndex < it.memberList.size) {
                    val memberInfo = it.memberList[itemIndex]
                    LogUtil.i("MultiListFragment_initView, memberInfo = $memberInfo", "wiatt")
                    switchFragment(
                        MultiItemFragment.newInstance(
                            memberInfo.nickName,
                            memberInfo.micStatus,
                            memberInfo.cameraStatus),
                        itemIndex % 4
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.i("MultiListFragment_onDestroy", "wiatt")
    }

    /**
     * 用于切换Fragment
     * 当不显示fragment时，nextFragment为空
     */
    private fun switchFragment(nextFragment: Fragment?, itemIndex: Int): Boolean{
        when(itemIndex) {
            0 -> {
                if(nextFragment == curZeroFragment) {
                    return false
                }

                if (curZeroFragment != null && curZeroFragment!!.isAdded) {
                    removeFragment(curZeroFragment!!)
                }

                if (nextFragment != null) {
                    if (nextFragment.isAdded) {
                        showFragment(nextFragment)
                    } else {
                        addFragment(nextFragment, R.id.containerZeroFl)
                    }
                }
                curZeroFragment = nextFragment
                return true
            }
            1 -> {
                if(nextFragment == curOneFragment) {
                    return false
                }

                if (curOneFragment != null && curOneFragment!!.isAdded) {
                    removeFragment(curZeroFragment!!)
                }

                if (nextFragment != null) {
                    if (nextFragment.isAdded) {
                        showFragment(nextFragment)
                    } else {
                        addFragment(nextFragment, R.id.containerOneFl)
                    }
                }
                curOneFragment = nextFragment
                return true
            }
            2 -> {
                if(nextFragment == curTwoFragment) {
                    return false
                }

                if (curTwoFragment != null && curTwoFragment!!.isAdded) {
                    removeFragment(curZeroFragment!!)
                }

                if (nextFragment != null) {
                    if (nextFragment.isAdded) {
                        showFragment(nextFragment)
                    } else {
                        addFragment(nextFragment, R.id.containerTwoFl)
                    }
                }
                curTwoFragment = nextFragment
                return true
            }
            3 -> {
                if(nextFragment == curThreeFragment) {
                    return false
                }

                if (curThreeFragment != null && curThreeFragment!!.isAdded) {
                    removeFragment(curZeroFragment!!)
                }

                if (nextFragment != null) {
                    if (nextFragment.isAdded) {
                        showFragment(nextFragment)
                    } else {
                        addFragment(nextFragment, R.id.containerThreeFl)
                    }
                }
                curThreeFragment = nextFragment
                return true
            }
        }
        return false
    }

    companion object {

        @JvmStatic
        fun newInstance(viewModelParam: MeetingRoomViewModel?, indexParam: Int) =
            MultiListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_VIEWMODEL, viewModelParam)
                    putInt(ARG_INDEX, indexParam)
                }
            }
    }
}