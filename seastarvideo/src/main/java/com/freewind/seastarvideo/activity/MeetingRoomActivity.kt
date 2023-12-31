/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.ActivityMeetingRoomBinding
import com.freewind.seastarvideo.meeting.room.MeetingRoomEventBean
import com.freewind.seastarvideo.meeting.room.MeetingRoomFragment
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class MeetingRoomActivity : BaseActivity() {

    private lateinit var binding: ActivityMeetingRoomBinding

    private var currentFragment: Fragment? = null
    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    private lateinit var fragmentType: String

    private var meetingRoomFragment: MeetingRoomFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingRoomBinding.inflate(layoutInflater)
        val rootView = binding.root
        OtherUiManager.instance.adaptBottomHeight(binding.meetingRoomCl)
        setContentView(rootView)
        EventBus.getDefault().register(this)

        showMeetingRoomPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        if (fragmentStack.size <= 1) {
            super.onBackPressed()
        } else {
            fragmentStack.pop()
            val curFragment = fragmentStack.peek()
            switchFragment(curFragment)
        }
    }

    /**
     * 切换到 会议房间页面
     */
    @Synchronized
    private fun showMeetingRoomPage() {
        if (meetingRoomFragment == null) {
            // TODO 此处如果从 intent 中没有获取到数据，则应该从 MMKV中获取，依然没有则显示异常并退出
            val nickName = intent.getStringExtra(PARAM_NICKNAME) ?: resources.getString(R.string.def_nickname)
            val roomId = intent.getStringExtra(PARAM_ROOM_ID) ?: resources.getString(R.string.def_room_id)
            meetingRoomFragment = MeetingRoomFragment.newInstance(nickName, roomId)
        }

        if (switchFragment(meetingRoomFragment)) {
            fragmentType = MEETING_ROOM_DETAIL
            fragmentStack.push(meetingRoomFragment)
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
    fun onEventUpdatePage(event: MeetingRoomEventBean.goBackPageEvent) {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: MeetingRoomEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                MEETING_ROOM_DETAIL -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            MEETING_ROOM_DETAIL -> {
                showMeetingRoomPage()
            }
        }
    }

    companion object {
        const val MEETING_ROOM_KEY = "meeting_room_key"
        const val PARAM_NICKNAME = "param_nickname"
        const val PARAM_ROOM_ID = "param_room_id"

        // 会议房间详情页
        const val MEETING_ROOM_DETAIL = "meeting_room_detail"

        /**
         * 启动会议房间页面
         */
        fun startMeetingRoomPage(context: Context, nickname: String, roomId: String) {
            val intent = Intent(context, MeetingRoomActivity::class.java)
            intent.putExtra(MEETING_ROOM_KEY, MEETING_ROOM_DETAIL)
            intent.putExtra(PARAM_NICKNAME, nickname)
            intent.putExtra(PARAM_ROOM_ID, roomId)
            context.startActivity(intent)
        }
    }
}