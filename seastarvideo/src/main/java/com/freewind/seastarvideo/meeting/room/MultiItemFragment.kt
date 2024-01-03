/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.FragmentMultiItemBinding


/**
 * @author: wiatt
 * @description: 多成员页面，单个成员的fragment
 * todo 功能遇阻，暂不使用
 */
class MultiItemFragment : BaseFragment() {

    private var ARG_NICKNAME = "nickName"
    private var ARG_AUDIO_STATUS = "audioStatus"
    private var ARG_VIDEO_STATUS = "videoStatus"

    private var _binding: FragmentMultiItemBinding? = null
    private val binding get() = _binding!!

    // 昵称
    private var mNickName: String? = null
    // 音频状态，可能受各种因素共同作用，也可能不为 boolean 类型
    private var mAudioStatus: Boolean = false
    // 视频状态，可能受各种因素共同作用，也可能不为 boolean 类型
    private var mVideoStatus: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mNickName = it.getString(ARG_NICKNAME, "")
            mAudioStatus = it.getBoolean(ARG_AUDIO_STATUS, false)
            mVideoStatus = it.getBoolean(ARG_VIDEO_STATUS, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiItemBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initView()

        return rootView
    }

    private fun initView() {
        if (mVideoStatus) {
            binding.placeholderIv.visibility = View.VISIBLE
            binding.avatarIv.visibility = View.GONE
        } else {
            binding.placeholderIv.visibility = View.GONE
            binding.avatarIv.visibility = View.VISIBLE
        }
        binding.videoSmallIv.isSelected = mVideoStatus
        binding.micSmallIv.isSelected = mAudioStatus
        binding.nickNameTv.text = mNickName
    }

    private fun updateNickName(nickname: String) {
        mNickName = nickname
        arguments?.putString(ARG_NICKNAME, mNickName)
        binding.nickNameTv.text = mNickName
    }

    private fun updateAudioStatus(audioStatus: Boolean) {
        mAudioStatus = audioStatus
        arguments?.putBoolean(ARG_AUDIO_STATUS, audioStatus)
        binding.micSmallIv.isSelected = mAudioStatus
    }

    private fun updateVideoStatus(videoStatus: Boolean) {
        mVideoStatus = videoStatus
        arguments?.putBoolean(ARG_VIDEO_STATUS, videoStatus)
        if (mVideoStatus) {
            binding.placeholderIv.visibility = View.VISIBLE
            binding.avatarIv.visibility = View.GONE
        } else {
            binding.placeholderIv.visibility = View.GONE
            binding.avatarIv.visibility = View.VISIBLE
        }
        binding.videoSmallIv.isSelected = mVideoStatus
    }

    companion object {

        @JvmStatic
        fun newInstance(
            nickNameParam: String, videoStatusParam: Boolean, cameraStatusParam: Boolean
        ) =
            MultiItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NICKNAME, nickNameParam)
                    putBoolean(ARG_AUDIO_STATUS, videoStatusParam)
                    putBoolean(ARG_VIDEO_STATUS, cameraStatusParam)
                }
            }
    }
}