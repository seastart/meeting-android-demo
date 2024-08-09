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
import androidx.lifecycle.ViewModelProvider
import cn.seastart.meeting.enumerate.DeviceState
import cn.seastart.meeting.enumerate.RoleType
import cn.seastart.meeting.enumerate.ShareType
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.ActivityMemberBinding
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.meeting.member.MemberEventBean
import com.freewind.seastarvideo.meeting.member.MemberListFragment
import com.freewind.seastarvideo.meeting.member.MemberListViewModel
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class MemberActivity : BaseActivity() {

    private lateinit var binding: ActivityMemberBinding

    private lateinit var viewModel: MemberListViewModel

    private var currentFragment: Fragment? = null
    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    private lateinit var fragmentType: String

    private val memberListFragment: MemberListFragment by lazy {
        MemberListFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberBinding.inflate(layoutInflater)
        val rootView = binding.root
        OtherUiManager.instance.adaptBottomHeight(binding.memberCl)
        setContentView(rootView)
        EventBus.getDefault().register(this)

        viewModel = ViewModelProvider(this)[MemberListViewModel::class.java]
        // todo 在这里对本人的信息进行赋值
        viewModel.init(MemberInfo(
            "10001", "抹茶玛奇朵", "", RoleType.Host,
            DeviceState.Closed, DeviceState.Closed, ShareType.Normal,
            false, true))

        showMemberListPage()
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
     * 切换到 成员列表页
     */
    @Synchronized
    private fun showMemberListPage() {
        if (switchFragment(memberListFragment)) {
            fragmentType = MEMBER_LIST
            fragmentStack.push(memberListFragment)
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
    fun onEventUpdatePage(event: MemberEventBean.goBackPageEvent) {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: MemberEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                MEMBER_LIST -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            MEMBER_LIST -> {
                showMemberListPage()
            }
        }
    }

    companion object {
        const val MEMBER_KEY = "member_key"
        // 成员列表页
        const val MEMBER_LIST = "member_list"

        /**
         * 启动成员列表页
         */
        fun startMemberListPage(context: Context) {
            val intent = Intent(context, MemberActivity::class.java)
            intent.putExtra(MEMBER_KEY, MEMBER_LIST)
            context.startActivity(intent)
        }
    }
}