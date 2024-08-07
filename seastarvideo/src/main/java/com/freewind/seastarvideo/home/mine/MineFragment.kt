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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.activity.AboutUsActivity
import com.freewind.seastarvideo.activity.LoginActivity
import com.freewind.seastarvideo.activity.PrivacyActivity
import com.freewind.seastarvideo.activity.RegisterActivity
import com.freewind.seastarvideo.activity.WebActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMineBinding
import com.freewind.seastarvideo.http.ConstantHttp
import com.freewind.seastarvideo.utils.KvUtil
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
        viewModel = ViewModelProvider(requireActivity())[MineViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)
        initData()
        initListener()
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun initData() {
        if (KvUtil.decodeString(KvUtil.JWT_TOKEN).isEmpty()) {
            // 未登录状态
            binding.myInfoCmi.avatarIcon = R.mipmap.avatar_woman
            binding.myInfoCmi.nickNameContent = "未登录"
            binding.myInfoCmi.idContent = ""
        } else {
            // 登录
            val avatar = KvUtil.decodeString(KvUtil.USER_INFO_AVATAR)
            if (avatar == ConstantHttp.RES_AVATAR_MAN) {
                binding.myInfoCmi.avatarIcon = R.mipmap.avatar_man
            } else {
                binding.myInfoCmi.avatarIcon = R.mipmap.avatar_woman
            }
            binding.myInfoCmi.nickNameContent = KvUtil.decodeString(KvUtil.USER_INFO_NICK_NAME)
            binding.myInfoCmi.idContent = KvUtil.decodeString(KvUtil.USER_INFO_UID)
        }
    }

    private fun initListener() {
        binding.myInfoCmi.setOnClickListener {
            if (KvUtil.decodeString(KvUtil.JWT_TOKEN).isEmpty()) {
                // 未登录状态
                LoginActivity.startActivity(requireActivity())
            } else {
                // 登录
                RegisterActivity.startRegisterNicknamePage(requireContext())
            }
        }
        binding.privacyCei.setOnClickListener {
            PrivacyActivity.startPrivacyListPage(requireContext())
        }
        binding.statementCei.setOnClickListener {
            WebActivity.startDisclaimerWebPage(requireContext())
        }
        binding.aboutUsCei.setOnClickListener {
            AboutUsActivity.startAboutUsPage(requireContext())
        }
        binding.logoutCei.setOnClickListener {
            KvUtil.encode(KvUtil.USER_INFO_UID, "")
            KvUtil.encode(KvUtil.USER_INFO_MOBILE, "")
            KvUtil.encode(KvUtil.USER_INFO_PWD, "")
            KvUtil.encode(KvUtil.USER_INFO_NICK_NAME, "")
            KvUtil.encode(KvUtil.USER_INFO_AVATAR, "")
            KvUtil.encode(KvUtil.JWT_TOKEN, "")
            LoginActivity.startActivity(this@MineFragment.requireActivity())
        }
    }

    /**
     * 登录状态改变
     */
    fun loginStatusChange(isLogin: Boolean) {
        Log.i("wiatt", "我的也页面，登录状态改变， isLogin = $isLogin, isCreateView = $isCreateView")
        if (!isCreateView) {
            // 页面未创建，下面这些控件可能都没有被初始化
            return
        }
        if (isLogin && KvUtil.decodeString(KvUtil.JWT_TOKEN).isNotEmpty()) {
            // 登录
            val avatar = KvUtil.decodeString(KvUtil.USER_INFO_AVATAR)
            if (avatar == ConstantHttp.RES_AVATAR_MAN) {
                binding.myInfoCmi.avatarIcon = R.mipmap.avatar_man
            } else {
                binding.myInfoCmi.avatarIcon = R.mipmap.avatar_woman
            }
            binding.myInfoCmi.nickNameContent = KvUtil.decodeString(KvUtil.USER_INFO_NICK_NAME)
            binding.myInfoCmi.idContent = KvUtil.decodeString(KvUtil.USER_INFO_UID)
        } else {
            // 未登录状态
            binding.myInfoCmi.avatarIcon = R.mipmap.avatar_woman
            binding.myInfoCmi.nickNameContent = "未登录"
            binding.myInfoCmi.idContent = ""
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}