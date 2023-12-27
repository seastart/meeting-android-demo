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
import com.freewind.seastarvideo.databinding.ActivityWebBinding
import com.freewind.seastarvideo.utils.OtherUiManager
import com.freewind.seastarvideo.webPage.ProtocolWebFragment
import com.freewind.seastarvideo.webPage.WebEventBean
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WebActivity : BaseActivity() {

    private lateinit var binding: ActivityWebBinding

    private var currentFragment: Fragment? = null
    private lateinit var fragmentType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        OtherUiManager.instance.adaptBottomHeight(binding.webCl)
        val rootView = binding.root
        setContentView(rootView)
        EventBus.getDefault().register(this)

        fragmentType = intent.getStringExtra(WEB_KEY) ?: WEB_PROTOCOL
        if (fragmentType == WEB_PROTOCOL) {
            val protocolType = intent.getStringExtra(PROTOCOL_TYPE_KEY)
                ?: ProtocolWebFragment.PAGE_TYPE_DISCLAIMER
            showProtocolWebPage(protocolType)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * 切换到 协议网页页面
     */
    @Synchronized
    private fun showProtocolWebPage(type: String) {
        val protocolWebFragment = ProtocolWebFragment.newInstance(type)
        if (switchFragment(protocolWebFragment)) {
            fragmentType = AboutUsActivity.ABOUT_US_DETAIL
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
    fun onEventUpdatePage(event: WebEventBean.goBackPageEvent) {
        onBackPressed()
    }

    companion object {
        const val WEB_KEY = "web_key"
        const val PROTOCOL_TYPE_KEY = "protocol_type_key"
        // 协议网页
        const val WEB_PROTOCOL = "web_protocol"

        /**
         * 启动"免责声明"页面
         */
        fun startDisclaimerWebPage(context: Context) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WEB_KEY, WEB_PROTOCOL)
            intent.putExtra(PROTOCOL_TYPE_KEY, ProtocolWebFragment.PAGE_TYPE_DISCLAIMER)
            context.startActivity(intent)
        }

        /**
         * 启动“个人信息收集清单”页面
         */
        fun startPersonalInfoCollectionListWebPage(context: Context) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WEB_KEY, WEB_PROTOCOL)
            intent.putExtra(PROTOCOL_TYPE_KEY, ProtocolWebFragment.PAGE_TYPE_PICL)
            context.startActivity(intent)
        }

        /**
         * 启动“第三方信息共享清单”页面
         */
        fun startThirdPartyInfoSharingListWebPage(context: Context) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WEB_KEY, WEB_PROTOCOL)
            intent.putExtra(PROTOCOL_TYPE_KEY, ProtocolWebFragment.PAGE_TYPE_TPISL)
            context.startActivity(intent)
        }

        /**
         * 启动“用户协议”页面
         */
        fun startUserAgreementWebPage(context: Context) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WEB_KEY, WEB_PROTOCOL)
            intent.putExtra(PROTOCOL_TYPE_KEY, ProtocolWebFragment.PAGE_TYPE_UA)
            context.startActivity(intent)
        }

        /**
         * 启动“隐私协议”页面
         */
        fun startPrivacyPolicyWebPage(context: Context) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WEB_KEY, WEB_PROTOCOL)
            intent.putExtra(PROTOCOL_TYPE_KEY, ProtocolWebFragment.PAGE_TYPE_PP)
            context.startActivity(intent)
        }
    }
}