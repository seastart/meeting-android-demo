/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.member

import androidx.lifecycle.MutableLiveData
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.utils.KvUtil

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class MemberListViewModel():
    BaseViewModel<MemberListModel, MemberListContract.IMemberListViewModel>() {

    // 房间 Id liveData
    val membersLiveData: MutableLiveData<MutableList<MemberInfo>> = MutableLiveData()

    lateinit var meInfo: MemberInfo
    lateinit var members: MutableList<MemberInfo>

    override fun getModel(): MemberListModel {
        return MemberListModel(MemberListViewModelImpl())
    }

    fun init(meInfo: MemberInfo) {
        this.meInfo = meInfo
        members = mutableListOf(meInfo)
    }

    fun getMembers() {
        members.add(MemberInfo("10002", "蜡笔小生", MemberInfo.MEMBER_ROLE_NORMAL, true, false, false))
        val tmpMembers = mutableListOf<MemberInfo>().apply {
            this.addAll(members)
        }
        membersLiveData.value = tmpMembers
    }

    fun updateMembers() {
        members.add(MemberInfo("10003", "宵夜", MemberInfo.MEMBER_ROLE_NORMAL, false, true, false))
        members.add(MemberInfo("10004", "啊哈哈", MemberInfo.MEMBER_ROLE_NORMAL, false, false, false))
        val tmpMembers = mutableListOf<MemberInfo>().apply {
            this.addAll(members)
        }
        membersLiveData.value = tmpMembers
    }

    /**
     * 设置某个成员的麦克风状态
     * todo 此处只做示意使用，实际场景中需要结合接口操作
     */
    fun setMemberMicStatus(micStatus: Boolean, memberId: String): Boolean {
        val roomAudioStatus = KvUtil.decodeBooleanTure(KvUtil.MEETING_ROOM_AUDIO_STATUS, true)
        if (roomAudioStatus || meInfo.role == MemberInfo.MEMBER_ROLE_COMPERE) {
            for (member: MemberInfo in members) {
                if (member.id == memberId) {
                    member.micStatus = micStatus
                    return true
                }
            }
            return false
        } else {
            return false
        }
    }

    /**
     * 设置某个成员的摄像头状态
     * todo 此处只做示意使用，实际场景中需要结合接口操作
     */
    fun setMemberCameraStatus(cameraStatus: Boolean, memberId: String): Boolean {
        val roomVideoStatus = KvUtil.decodeBooleanTure(KvUtil.MEETING_ROOM_VIDEO_STATUS, true)
        if (roomVideoStatus || meInfo.role == MemberInfo.MEMBER_ROLE_COMPERE) {
            for (member: MemberInfo in members) {
                if (member.id == memberId) {
                    member.cameraStatus = cameraStatus
                    return true
                }
            }
            return false
        } else {
            return false
        }
    }

    /**
     * 获取综合计算过的麦克风表现状态
     * todo 此处只做示意使用，实际场景中需要结合接口操作
     */
    fun getMicStatus(memberInfo: MemberInfo): Boolean {
        val roomAudioStatus = KvUtil.decodeBooleanTure(KvUtil.MEETING_ROOM_AUDIO_STATUS, true)
        return if (roomAudioStatus) {
            memberInfo.micStatus
        } else {
            false
        }
    }

    /**
     * 获取综合计算过的摄像头表现状态
     * todo 此处只做示意使用，实际场景中需要结合接口操作
     */
    fun getCameraStatus(memberInfo: MemberInfo): Boolean {
        val roomVideoStatus = KvUtil.decodeBooleanTure(KvUtil.MEETING_ROOM_VIDEO_STATUS, true)
        return if (roomVideoStatus) {
            memberInfo.cameraStatus
        } else {
            false
        }
    }

    inner class MemberListViewModelImpl: MemberListContract.IMemberListViewModel {

    }
}