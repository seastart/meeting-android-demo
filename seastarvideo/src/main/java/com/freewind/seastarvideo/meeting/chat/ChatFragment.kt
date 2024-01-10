/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentChatBinding
import com.freewind.seastarvideo.ui.CustomTopBar
import com.freewind.seastarvideo.utils.LogUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author: wiatt
 * @description: 群聊界面
 */
class ChatFragment : BaseFragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatViewModel

    private var adapter: ChatListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ChatViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val rootView = binding.root
//        OtherUiManager.instance.adaptTopHeight(binding.topBarFl)

        initView()
        initLiveData()
        initData()
        initListener()

        return rootView
    }

    private fun initLiveData() {
        viewModel.chatsLiveData.observe(requireActivity()) { chats ->
            LogUtil.i("chats.size = ${chats.size}", "wiatt")
            adapter?.submitList(chats)
            adapter?.let {
                binding.chatInfoRv.smoothScrollToPosition(it.itemCount - 1)
            }
        }
        viewModel.chatOneLiveData.observe(requireActivity()) { chat ->
            adapter?.add(chat)
            adapter?.let {
                binding.chatInfoRv.smoothScrollToPosition(it.itemCount - 1)
            }
        }
    }

    fun initView() {
        adapter = initAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.chatInfoRv.adapter = adapter
        binding.chatInfoRv.layoutManager = layoutManager
    }

    fun initAdapter(): ChatListAdapter {
        val adapter = ChatListAdapter()
        // 默认不使用空布局
        adapter.isEmptyViewEnable = false
        return adapter
    }

    fun initData() {
        viewModel.getHistoryChat()
    }

    fun initListener(){
        binding.topBarCtb.listener = object : CustomTopBar.OnTopBarListener {
            override fun onClickExitArrow() {
                EventBus.getDefault().post(ChatEventBean.goBackPageEvent())
            }

            override fun onClickTitle() {
                viewModel.sendInputStr("这是一条测试消息")
            }

        }
        binding.inputSendI.inputSet.setOnClickListener { view ->
            view.requestFocus()
            showSoftInput()
            adapter?.let {
                binding.inputSendI.inputSet.postDelayed({
                    binding.chatInfoRv.smoothScrollToPosition(it.itemCount - 1)
                }, 100)
            }
        }
        binding.chatInfoRv.setOnTouchListener { v, event ->
            hideSoftInput()
            false
        }
        setOnClickNoRepeat(binding.inputSendI.sendStv) { view ->
            when(view) {
                binding.inputSendI.sendStv -> {
                    val inputStr = binding.inputSendI.inputSet.text.toString()
                    if (inputStr.isEmpty()) {
                        Toast.makeText(context, "输入内容不可为空", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.inputSendI.inputSet.editableText.clear()
                        viewModel.sendInputStr(inputStr)
                    }
                }
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}