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
import com.freewind.seastarvideo.activity.HomeActivity
import com.freewind.seastarvideo.authorize.AuthorizeEventBean
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentRegisterNicknameBinding
import com.freewind.seastarvideo.http.ConstantHttp
import com.freewind.seastarvideo.utils.ToastUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author: wiatt
 * @date: 2023/12/21 16:31
 * @description: 注册昵称页
 */
class RegisterNicknameFragment : BaseFragment() {

    private var _binding: FragmentRegisterNicknameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegisterViewModel
    private var avatarType: AvatarType = AvatarType.AvatarMan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
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
        avatarType = AvatarType.AvatarMan
    }

    private fun initListener() {
        setOnClickNoRepeat(binding.avatarManIv, binding.avatarWomanIv, binding.registerSureStv) {
            when(it) {
                binding.avatarManIv -> {
                    if (!binding.avatarManIv.isSelected) {
                        binding.avatarManIv.isSelected = true
                        binding.avatarWomanIv.isSelected = false
                        avatarType = AvatarType.AvatarMan
                    }
                }
                binding.avatarWomanIv -> {
                    if (!binding.avatarWomanIv.isSelected) {
                        binding.avatarWomanIv.isSelected = true
                        binding.avatarManIv.isSelected = false
                        avatarType = AvatarType.AvatarWoman
                    }
                }
                binding.registerSureStv -> {
                    // 注册成功，跳转回主页
                    val nickName = binding.nickNameCet.text.toString().trim()
                    if (nickName.isEmpty()) {
                        ToastUtil.showShortToast("昵称不可为空")
                        return@setOnClickNoRepeat
                    }
                    val avatar = if (avatarType == AvatarType.AvatarMan) {
                        ConstantHttp.RES_AVATAR_MAN
                    } else {
                        ConstantHttp.RES_AVATAR_WOMAN
                    }
                    viewModel.updateSelfDetail(nickName, avatar)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
        viewModel.updateSelfDetailResult.observe(this) { uiResponse ->
            uiResponse?.let { result ->
                if (result.isSuccess) {
                    EventBus.getDefault().post(AuthorizeEventBean.LoginStatusEvent(true))
                    HomeActivity.startActivity(requireActivity())
                    requireActivity().finish()
                } else {
                    ToastUtil.showShortToast("更新自身信息失败")
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = RegisterNicknameFragment()
    }

    enum class AvatarType {
        AvatarMan,
        AvatarWoman
    }
}