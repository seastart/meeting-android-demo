/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.databinding.FragmentMultiMixOBinding
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.utils.DisplayUtil

/**
 * @author: wiatt
 * @description: 会议房间中，多人混合页面。简单的列表
 * 暂未使用
 */
class MultiMixOFragment : Fragment() {

    private var _binding: FragmentMultiMixOBinding? = null
    private val binding get() = _binding!!

    private var adapter: MeetingMultiMixOAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiMixOBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initView()
        initListener()

        return rootView
    }

    private fun initView() {
        adapter = initAdapter()
        val layoutManager = object : GridLayoutManager(requireContext(), 2, HORIZONTAL, false){

            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return getGridLayoutManagerLayoutParams(this.orientation)
            }

            override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams {
                return getGridLayoutManagerLayoutParams(this.orientation)
            }

            override fun generateLayoutParams(c: Context?, attrs: AttributeSet?): RecyclerView.LayoutParams {
                return getGridLayoutManagerLayoutParams(this.orientation)
            }
        }
        binding.memberMultiRv.adapter = adapter
        binding.memberMultiRv.layoutManager = layoutManager

        binding.memberMultiRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.bottom = resources.getDimension(R.dimen.dp_5).toInt()
                }
                outRect.left = resources.getDimension(R.dimen.dp_3).toInt()
                outRect.right = resources.getDimension(R.dimen.dp_3).toInt()
            }
        })

        val memberList = mutableListOf<MemberInfo>()
//        memberList.add(MemberInfo("10001", "nickName", MemberInfo.MEMBER_ROLE_NORMAL, true, true))
//        memberList.add(MemberInfo("10002", "成员一", MemberInfo.MEMBER_ROLE_COMPERE, false, false))
//        memberList.add(MemberInfo("10003", "成员二", MemberInfo.MEMBER_ROLE_NORMAL, true, false))
//        memberList.add(MemberInfo("10004", "成员三", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
//        memberList.add(MemberInfo("10005", "成员四", MemberInfo.MEMBER_ROLE_NORMAL, true, true))
//        memberList.add(MemberInfo("10006", "成员五", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        memberList.add(MemberInfo("10007", "成员六", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        memberList.add(MemberInfo("10008", "成员七", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
//        memberList.add(MemberInfo("10009", "成员八", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        memberList.add(MemberInfo("10010", "成员九", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
//        memberList.add(MemberInfo("10011", "成员十", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        memberList.add(MemberInfo("10012", "成员十一", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
//        memberList.add(MemberInfo("10013", "成员十二", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
        adapter!!.submitList(memberList)
    }

    private fun initAdapter(): MeetingMultiMixOAdapter {
        val adapter = MeetingMultiMixOAdapter()
        // 默认不使用空布局
        adapter.isEmptyViewEnable = false
        return adapter
    }

    var testNum = 0
    private fun initListener() {
        binding.btnAdd.setOnClickListener {
//            testNum++
//            adapter!!.add(MemberInfo("10001", "新增成员$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false, false))
        }
        binding.btnRemove.setOnClickListener {
            adapter!!.removeAt(9)
        }
    }

    private fun getGridLayoutManagerLayoutParams(orientation: Int): GridLayoutManager.LayoutParams{
        val mobileWidth = DisplayUtil.instance.getMobileWidth()
        return if (orientation == GridLayoutManager.HORIZONTAL) {
            GridLayoutManager.LayoutParams(
                (mobileWidth - resources.getDimension(R.dimen.dp_5).toInt() * 2) / 2,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        } else {
            GridLayoutManager.LayoutParams(
                (mobileWidth - resources.getDimension(R.dimen.dp_5).toInt() * 2) / 2,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MultiMixOFragment()
    }
}