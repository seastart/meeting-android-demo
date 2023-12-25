/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMineBinding
import com.freewind.seastarvideo.utils.OtherUiManager

/**
 * @author wiatt
 * @description: 我的页面
 */
class MineFragment : BaseFragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MineViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topbarCl)
        initData()
        initListener()
        return rootView
    }

    private fun initData() {
        // 应该在此处获取用户信息，并更新界面
    }

    private fun initListener() {
        binding.myInfoCmi.setOnClickListener {
            // 跳转到个人信息页面
        }
        binding.privacyCei.setOnClickListener {
            // 跳转到隐私页面
        }
        binding.statementCei.setOnClickListener {
            // 跳转到免责声明页面
        }
        binding.aboutMeCei.setOnClickListener {
            // 跳转到我的页面
        }
        binding.logoutCei.setOnClickListener {
            // 跳转到登录页面
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}