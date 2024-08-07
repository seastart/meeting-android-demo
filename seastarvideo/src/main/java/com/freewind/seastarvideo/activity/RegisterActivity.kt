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
import com.freewind.seastarvideo.authorize.AuthorizeEventBean
import com.freewind.seastarvideo.authorize.register.RegisterEventBean
import com.freewind.seastarvideo.authorize.register.RegisterInfoFragment
import com.freewind.seastarvideo.authorize.register.RegisterNicknameFragment
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.ActivityRegisterBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private var currentFragment: Fragment? = null
    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    private lateinit var fragmentType: String

    private val registerInfoFragment: RegisterInfoFragment by lazy {
        RegisterInfoFragment.newInstance()
    }
    private val registerNicknameFragment: RegisterNicknameFragment by lazy {
        RegisterNicknameFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)
        OtherUiManager.instance.adaptBottomHeight(binding.registerRl)
        setContentView(rootView)
        EventBus.getDefault().register(this)
        fragmentType = intent.getStringExtra(REGISTER_KEY) ?: REGISTER_INFO

        initListener()
        if (fragmentType == REGISTER_NICKNAME) {
            showRegisterNicknamePage()
        } else {
            showRegisterInfoPage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        if (fragmentStack.size <= 1) {
            super.onBackPressed()
        } else {
            if (fragmentType == REGISTER_NICKNAME) {
                // 在用户昵称头像页面，直接点返回，跳转到主页，并处于登录状态
                EventBus.getDefault().post(AuthorizeEventBean.LoginStatusEvent(true))
                HomeActivity.startActivity(this@RegisterActivity)
                this@RegisterActivity.finish()
            } else {
                fragmentStack.pop()
                val curFragment = fragmentStack.peek()
                if (curFragment is RegisterInfoFragment) {
                    binding.topBarCtb.titleContent = resources.getString(R.string.register)
                    binding.topBarCtb.isShowTitle = true
                    binding.topBarCtb.isShowExitArrow = true
                    fragmentType = REGISTER_INFO
                } else if (curFragment is RegisterNicknameFragment){
                    binding.topBarCtb.isShowTitle = false
                    binding.topBarCtb.isShowExitArrow = true
                    fragmentType = REGISTER_NICKNAME
                }
                switchFragment(curFragment)
            }
        }
    }

    private fun initListener(){
        binding.topBarCtb.listener = object : CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                hideSoftInput(binding.topBarCtb)
                onBackPressed()
            }

            override fun onClickTitle() {

            }
        }
    }

    /**
     * 切换到 注册信息页
     */
    @Synchronized
    private fun showRegisterInfoPage() {
        if (switchFragment(registerInfoFragment)) {
            fragmentType = REGISTER_INFO
            binding.topBarCtb.titleContent = resources.getString(R.string.register)
            binding.topBarCtb.isShowTitle = true
            binding.topBarCtb.isShowExitArrow = true
            fragmentStack.push(registerInfoFragment)
        }
    }

    /**
     * 切换到 注册昵称页
     */
    @Synchronized
    private fun showRegisterNicknamePage() {
        if (switchFragment(registerNicknameFragment)) {
            fragmentType = REGISTER_NICKNAME
            binding.topBarCtb.isShowTitle = false
            binding.topBarCtb.isShowExitArrow = true
            fragmentStack.push(registerNicknameFragment)
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
    fun onEventUpdatePage(event: RegisterEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                REGISTER_INFO, REGISTER_NICKNAME -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            REGISTER_INFO -> {
                showRegisterInfoPage()
            }
            REGISTER_NICKNAME -> {
                showRegisterNicknamePage()
            }
        }
    }

    companion object {
        const val REGISTER_KEY = "register_key"
        // 注册信息页
        const val REGISTER_INFO = "register_info"
        // 注册昵称页
        const val REGISTER_NICKNAME = "register_nickname"

        /**
         * 启动注册信息页面
         */
        fun startRegisterInfoPage(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.putExtra(REGISTER_KEY, REGISTER_INFO)
            context.startActivity(intent)
        }

        /**
         * 启动注册昵称页面
         */
        fun startRegisterNicknamePage(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.putExtra(REGISTER_KEY, REGISTER_NICKNAME)
            context.startActivity(intent)
        }
    }
}