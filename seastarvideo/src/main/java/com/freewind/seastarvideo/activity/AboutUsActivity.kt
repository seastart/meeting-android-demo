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
import com.freewind.seastarvideo.aboutUs.AboutUsEventBean
import com.freewind.seastarvideo.aboutUs.AboutUsFragment
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.ActivityAboutUsBinding
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class AboutUsActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    private var currentFragment: Fragment? = null
    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    private lateinit var fragmentType: String

    private val aboutUsFragment: AboutUsFragment by lazy {
        AboutUsFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        OtherUiManager.instance.adaptBottomHeight(binding.aboutUsCl)
        val rootView = binding.root
        setContentView(rootView)
        EventBus.getDefault().register(this)

        showAboutUsPage()
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
    private fun showAboutUsPage() {
        if (switchFragment(aboutUsFragment)) {
            fragmentType = ABOUT_US_DETAIL
            fragmentStack.push(aboutUsFragment)
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
    fun onEventUpdatePage(event: AboutUsEventBean.goBackPageEvent) {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: AboutUsEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                ABOUT_US_DETAIL -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            ABOUT_US_DETAIL -> {
                showAboutUsPage()
            }
        }
    }

    companion object {
        const val ABOUT_US_KEY = "about_us_key"
        // 关于我们页面
        const val ABOUT_US_DETAIL = "about_us_detail"

        /**
         * 启动隐私详情页面
         */
        fun startAboutUsPage(context: Context) {
            val intent = Intent(context, AboutUsActivity::class.java)
            intent.putExtra(ABOUT_US_KEY, ABOUT_US_DETAIL)
            context.startActivity(intent)
        }
    }
}