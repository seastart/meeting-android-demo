/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.activity.WebActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentPrivacyListBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.OtherUiManager
import org.greenrobot.eventbus.EventBus

/**
 * @author wiatt
 * @description: 隐私列表页面
 */
class PrivacyListFragment : BaseFragment() {

    private var _binding: FragmentPrivacyListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PrivacyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PrivacyViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyListBinding.inflate(inflater, container, false)
        val rootView = binding.root
        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)

        initListener()

        return rootView
    }

    private fun initListener() {
        binding.topBarCtb.listener = object : CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                hideSoftInput()
                EventBus.getDefault().post(PrivacyEventBean.goBackPageEvent())
            }

            override fun onClickTitle() {

            }
        }
        setOnClickNoRepeat(binding.piclCei, binding.tpislCei, binding.uaCei, binding.ppCei) {
            when(it) {
                binding.piclCei -> {
                    WebActivity.startPersonalInfoCollectionListWebPage(requireContext())
                }
                binding.tpislCei -> {
                    WebActivity.startThirdPartyInfoSharingListWebPage(requireContext())
                }
                binding.uaCei -> {
                    WebActivity.startUserAgreementWebPage(requireContext())
                }
                binding.ppCei -> {
                    WebActivity.startPrivacyPolicyWebPage(requireContext())
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = PrivacyListFragment()
    }
}