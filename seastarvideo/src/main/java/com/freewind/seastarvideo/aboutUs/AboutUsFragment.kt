/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.aboutUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentAboutUsBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus


/**
 * @author wiatt
 * @description: 关于我们页面
 */
class AboutUsFragment : BaseFragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AboutUsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AboutUsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)

        initView()
        initListener()

        return rootView
    }

    private fun initView() {
        // todo 获取 SDK 版本，获取应用版本。最好不要在这里获取，而是在启动页，或者首页
        // todo 这里只从缓存中获取
        binding.sdkVersionCkvt.valueContent = "1.0.0"
        binding.appVersionCkvt.valueContent = "1.0.0"
    }

    private fun initListener() {
        binding.topBarCtb.listener = object : CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                EventBus.getDefault().post(AboutUsEventBean.goBackPageEvent())
            }

            override fun onClickTitle() {

            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = AboutUsFragment()
    }
}