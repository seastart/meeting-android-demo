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
import com.freewind.seastarvideo.databinding.ActivityChatBinding
import com.freewind.seastarvideo.meeting.chat.ChatEventBean
import com.freewind.seastarvideo.meeting.chat.ChatFragment
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class ChatActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBinding

    private var currentFragment: Fragment? = null
    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    private lateinit var fragmentType: String

    private val chatFragment: ChatFragment by lazy {
        ChatFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        OtherUiManager.instance.adaptBottomHeight(binding.chatCl)
        val rootView = binding.root
        setContentView(rootView)
        EventBus.getDefault().register(this)

        showChatPage()
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
     * 切换到 关于我们页面
     */
    @Synchronized
    private fun showChatPage() {
        if (switchFragment(chatFragment)) {
            fragmentType = CHAT_DETAIL
            fragmentStack.push(chatFragment)
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
    fun onEventUpdatePage(event: ChatEventBean.goBackPageEvent) {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: ChatEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                CHAT_DETAIL -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            CHAT_DETAIL -> {
                showChatPage()
            }
        }
    }

    companion object {
        const val CHAT_KEY = "chat_key"
        // 群聊页面
        const val CHAT_DETAIL = "chat_detail"

        /**
         * 启动群聊页面
         */
        fun startChatPage(context: Context) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(CHAT_KEY, CHAT_DETAIL)
            context.startActivity(intent)
        }
    }
}