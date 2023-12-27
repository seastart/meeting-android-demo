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
import com.freewind.seastarvideo.databinding.ActivityPrivacyBinding
import com.freewind.seastarvideo.privacy.PrivacyEventBean
import com.freewind.seastarvideo.privacy.PrivacyListFragment
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class PrivacyActivity : BaseActivity() {

    private lateinit var binding: ActivityPrivacyBinding

    private var currentFragment: Fragment? = null
    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    private lateinit var fragmentType: String

    private val privacyListFragment: PrivacyListFragment by lazy {
        PrivacyListFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        OtherUiManager.instance.adaptBottomHeight(binding.privacyCl)
        val rootView = binding.root
        setContentView(rootView)
        EventBus.getDefault().register(this)

        showPrivacyListPage()
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
     * 切换到 隐私列表页
     */
    @Synchronized
    private fun showPrivacyListPage() {
        if (switchFragment(privacyListFragment)) {
            fragmentType = RegisterActivity.REGISTER_INFO
            fragmentStack.push(privacyListFragment)
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
    fun onEventUpdatePage(event: PrivacyEventBean.goBackPageEvent) {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: PrivacyEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                PRIVACY_LIST -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            PRIVACY_LIST -> {
                showPrivacyListPage()
            }
        }
    }

    companion object {
        const val PRIVACY_KEY = "privacy_key"
        // 隐私列表页
        const val PRIVACY_LIST = "privacy_LIST"

        /**
         * 启动隐私详情页面
         */
        fun startPrivacyListPage(context: Context) {
            val intent = Intent(context, PrivacyActivity::class.java)
            intent.putExtra(PRIVACY_KEY, PRIVACY_LIST)
            context.startActivity(intent)
        }
    }
}