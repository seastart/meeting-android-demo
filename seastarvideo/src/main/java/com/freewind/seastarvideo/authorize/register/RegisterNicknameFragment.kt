/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.authorize.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentRegisterNicknameBinding

/**
 * @author: wiatt
 * @date: 2023/12/21 16:31
 * @description: 注册昵称页
 */
class RegisterNicknameFragment : BaseFragment() {

    private var _binding: FragmentRegisterNicknameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterNicknameBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initView()
        initListener()

        return rootView
    }

    private fun initView() {
        binding.avatarManIv.isSelected = true
        binding.avatarWomanIv.isSelected = false
    }

    private fun initListener() {
        setOnClickNoRepeat(binding.avatarManIv, binding.avatarWomanIv, binding.registerSureStv) {
            when(it) {
                binding.avatarManIv -> {
                    if (!binding.avatarManIv.isSelected) {
                        binding.avatarManIv.isSelected = true
                        binding.avatarWomanIv.isSelected = false
                    }
                }
                binding.avatarWomanIv -> {
                    if (!binding.avatarWomanIv.isSelected) {
                        binding.avatarWomanIv.isSelected = true
                        binding.avatarManIv.isSelected = false
                    }
                }
                binding.registerSureStv -> {
                    // 注册成功，跳转回主页
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = RegisterNicknameFragment()
    }
}