/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.ActivityPreMeetingRoomBinding
import com.freewind.seastarvideo.preMeetingRoom.CreateMeetingRoomFragment
import com.freewind.seastarvideo.preMeetingRoom.JoinMeetingRoomFragment
import com.freewind.seastarvideo.preMeetingRoom.PreMeetingRoomEventBean
import com.freewind.seastarvideo.preMeetingRoom.PreMeetingRoomFragment
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class PreMeetingRoomActivity : BaseActivity() {

    private lateinit var binding: ActivityPreMeetingRoomBinding

    private var currentFragment: Fragment? = null

    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    // 标识：当前正在展示哪个 fragment
    private lateinit var fragmentType: String

    private val pmrFragment: PreMeetingRoomFragment by lazy {
        PreMeetingRoomFragment.newInstance()
    }
    private var jmrFragment: JoinMeetingRoomFragment? = null
    private var cmrFragment: CreateMeetingRoomFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreMeetingRoomBinding.inflate(layoutInflater)
        OtherUiManager.instance.adaptBottomHeight(binding.preMeetingRoomCl)
        val rootView = binding.root
        setContentView(rootView)
        EventBus.getDefault().register(this)
        showPreMeetingRoomFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        if (fragmentStack.size <= 1) {
            super.onBackPressed()
        } else {
            // 如果 fragment 任务栈中不止一个 fragment 实例，则显示上一个
            fragmentStack.pop()
            val curFragment = fragmentStack.peek()
            switchFragment(curFragment)
        }
    }

    /**
     * 切换到 会议房间的准备页面
     */
    @Synchronized
    private fun showPreMeetingRoomFragment() {
        if (switchFragment(pmrFragment)) {
            fragmentType = PRE_MEETING_ROOM_PRE
            fragmentStack.push(pmrFragment)
        }
    }

    /**
     * 切换到 会议房间的加入页面
     */
    @Synchronized
    private fun showJoinMeetingRoomFragment() {
        if (jmrFragment == null) {
            // TODO 此处应该从 MMKV 中获取到用户的昵称
            jmrFragment = JoinMeetingRoomFragment.newInstance("橘子果酱")
        }
        if (switchFragment(jmrFragment)) {
            fragmentType = PRE_MEETING_ROOM_JOIN
            fragmentStack.push(jmrFragment)
        }
    }

    /**
     * 切换到 会议房间的创建页面
     */
    @Synchronized
    private fun showCreateMeetingRoomFragment() {
        if (cmrFragment == null) {
            // TODO 此处应该从 MMKV 中获取到用户的昵称
            cmrFragment = CreateMeetingRoomFragment.newInstance("橘子果酱")
        }
        if (switchFragment(cmrFragment)) {
            fragmentType = PRE_MEETING_ROOM_CREATE
            fragmentStack.push(cmrFragment)
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
                addFragment(nextFragment, R.id.contentFl)
            }
        }
        currentFragment = nextFragment
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: PreMeetingRoomEventBean.goBackPageEvent) {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: PreMeetingRoomEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                PRE_MEETING_ROOM_PRE, PRE_MEETING_ROOM_JOIN, PRE_MEETING_ROOM_CREATE -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            PRE_MEETING_ROOM_PRE -> {
                showPreMeetingRoomFragment()
            }
            PRE_MEETING_ROOM_JOIN -> {
                showJoinMeetingRoomFragment()
            }
            PRE_MEETING_ROOM_CREATE -> {
                showCreateMeetingRoomFragment()
            }
        }
    }

    companion object {
        // 会议房间的准备页面
        const val PRE_MEETING_ROOM_PRE = "pre_meeting_room_pre"
        // 会议房间的加入页面
        const val PRE_MEETING_ROOM_JOIN = "pre_meeting_room_join"
        // 会议房间的创建页面
        const val PRE_MEETING_ROOM_CREATE = "pre_meeting_room_create"
    }
}