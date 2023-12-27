/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.webPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.databinding.FragmentProtocolWebBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus

private const val ARG_PAGE_TYPE = "page_type"

class ProtocolWebFragment : Fragment() {

    private var _binding: FragmentProtocolWebBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WebViewModel

    private lateinit var pageType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageType = it.getString(ARG_PAGE_TYPE) ?: PAGE_TYPE_DISCLAIMER
        }
        viewModel = ViewModelProvider(this)[WebViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProtocolWebBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarCtb)

        initView()
        initListener()

        return rootView
    }

    private fun initView() {
        // 根据传入的协议类型，请求对应的网页链接并显示到页面上
        when(pageType) {
            PAGE_TYPE_DISCLAIMER -> {
                binding.topBarCtb.titleContent =
                    resources.getString(R.string.disclaimer)
            }
            PAGE_TYPE_PICL -> {
                binding.topBarCtb.titleContent =
                    resources.getString(R.string.personal_information_collection_list)
            }
            PAGE_TYPE_TPISL -> {
                binding.topBarCtb.titleContent =
                    resources.getString(R.string.third_party_information_sharing_list)
            }
            PAGE_TYPE_UA -> {
                binding.topBarCtb.titleContent =
                    resources.getString(R.string.user_agreement_1)
            }
            PAGE_TYPE_PP -> {
                binding.topBarCtb.titleContent =
                    resources.getString(R.string.privacy_policy_1)
            }
        }

        val setting = binding.contentWv.settings
        setting.domStorageEnabled = true
        setting.databaseEnabled = true
        setting.javaScriptEnabled = true
        binding.contentWv.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        // todo 根据页面类型获取对应的 url
        binding.contentWv.loadUrl("https://www.baidu.com/")
    }

    private fun initListener() {
        binding.topBarCtb.listener = object : CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                EventBus.getDefault().post(WebEventBean.goBackPageEvent())
            }

            override fun onClickTitle() {

            }

        }
    }

    companion object {

        // 免责声明页面
        const val PAGE_TYPE_DISCLAIMER = "type_disclaimer"
        // 个人信息收集清单页面
        const val PAGE_TYPE_PICL = "type_picl"
        // 第三方信息共享清单页面
        const val PAGE_TYPE_TPISL = "type_tpisl"
        // 用户协议页面
        const val PAGE_TYPE_UA = "type_ua"
        // 隐私协议页面
        const val PAGE_TYPE_PP = "type_pp"

        @JvmStatic
        fun newInstance(pageTypeParam: String) =
            ProtocolWebFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PAGE_TYPE, pageTypeParam)
                }
            }
    }
}