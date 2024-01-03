/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import androidx.lifecycle.MutableLiveData
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.utils.LogUtil
/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class MeetingRoomViewModel():
    BaseViewModel<MeetingRoomModel, MeetingRoomContract.IMeetingRoomViewModel>() {

    // 房间 Id liveData
    val roomIdLiveData: MutableLiveData<String> = MutableLiveData()
    // 自己的昵称 liveData
    val myNickNameLiveData: MutableLiveData<String> = MutableLiveData()
    // 自己的扬声器状态 liveData
    val mySpeakerStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    // 自己的麦克风状态 liveData
    val myMicStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    // 自己的照相机状态 liveData
    val myCameraStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    // 自己的屏幕共享状态 liveData
    val myScreenShareLiveData: MutableLiveData<Boolean> = MutableLiveData()
    // 展示哪一页二级 fragment liveData
    val showSecFragmentLiveData: MutableLiveData<String> = MutableLiveData()


    // 房间 id
    var roomId: String? = null
    // 房间音频状态
    var roomVideoStatus: Boolean = false
    // 自己的昵称
    var myNickName: String? = null
    // 自己的麦克风状态，可能受各种因素共同作用，也可能不为 boolean 类型
    var myMicStatus: Boolean = false
    // 自己的照相机状态，可能受各种因素共同作用，也可能不为 boolean 类型
    var myCameraStatus: Boolean = false
    // 自己的屏幕共享状态，可能受各种因素共同作用，也可能不为 boolean 类型
    var myScreenShare: Boolean = false
    // 自己的扬声器状态，可能受各种因素共同作用，也可能不为 boolean 类型
    var mySpeakerStatus: Boolean = false

    // 成员列表，此处暂时用 String 替代表示，后面会换成入会成员对象类型
    var memberList: MutableList<MemberInfo> = mutableListOf()

    override fun getModel(): MeetingRoomModel {
        return MeetingRoomModel(MeetingRoomViewModelImpl())
    }

    /**
     * 更新初始参数
     */
    fun updateInitialValue(nickName: String, roomId: String) {
        myNickName = nickName
        this.roomId = roomId
        myMicStatus = false
        myCameraStatus = false
        memberList.add(MemberInfo(nickName, myMicStatus, myCameraStatus))
//        memberList.add(MemberInfo("成员一", false, false))
//        memberList.add(MemberInfo("成员二", true, false))
//        memberList.add(MemberInfo("成员三", false, true))
//        memberList.add(MemberInfo("成员四", true, true))
//        memberList.add(MemberInfo("成员五", false, false))
//        memberList.add(MemberInfo("成员六", false, false))
//        memberList.add(MemberInfo("成员七", false, true))
//        memberList.add(MemberInfo("成员八", false, false))
//        memberList.add(MemberInfo("成员九", false, true))
//        memberList.add(MemberInfo("成员十", false, false))
//        memberList.add(MemberInfo("成员十一", false, true))
//        memberList.add(MemberInfo("成员十二", false, false))
    }

    /**
     * 获取房间Id
     */
    fun getRoomId() {
        roomId?.let {
            roomIdLiveData.value = it
        }
    }

    /**
     * 获取自己的昵称
     */
    fun getMyNickName() {
        LogUtil.i("获取自己的昵称，nickName1111 = $myNickName", "wiatt")
        // 获取自己的昵称
        myNickName?.let {
            LogUtil.i("获取自己的昵称，nickName = $it", "wiatt")
            myNickNameLiveData.value = it
        }
    }

    /**
     * 获取自己的扬声器状态
     */
    fun getMySpeakerStatus() {
        mySpeakerStatusLiveData.value = mySpeakerStatus
    }

    /**
     * 切换扬声器状态
     */
    fun switchSpeakerStatus() {
        // 执行开关扬声器的操作
        mySpeakerStatus = !mySpeakerStatus
        mySpeakerStatusLiveData.value = mySpeakerStatus
    }

    /**
     * 获取自己的麦克风状态
     */
    fun getMyMicStatus() {
        myMicStatusLiveData.value = myMicStatus
    }

    /**
     * 切换麦克风状态
     */
    fun switchMyMicStatus() {
        // 执行开关麦克风的操作
        myMicStatus = !myMicStatus
        myMicStatusLiveData.value = myMicStatus
    }

    /**
     * 获取自己的照相机状态
     */
    fun getMyCameraStatus() {
        myCameraStatusLiveData.value = myCameraStatus
    }

    /**
     * 切换照相机状态
     */
    fun switchMyCameraStatus() {
        // 执行开关照相机的操作
        myCameraStatus = !myCameraStatus
        myCameraStatusLiveData.value = myCameraStatus
        checkSecFragment()
    }

    /**
     * 获取自己的屏幕共享状态
     */
    fun getMyScreenShareStatus() {
        myScreenShareLiveData.value = myScreenShare
    }

    /**
     * 选择二级 fragment
     */
    fun checkSecFragment() {
        if (memberList.size <= 1) {
            // 如果会议成员列表中只有自己
            if (myCameraStatus) {
                // 如果视频是开启的
                showSecFragmentLiveData.value = MeetingRoomFragment.FRAGMENT_SEC_SOLO_VIDEO
            } else {
                // 如果视频是关闭的
                showSecFragmentLiveData.value = MeetingRoomFragment.FRAGMENT_SEC_SOLO_AVATAR
            }
        } else {
            // 如果会议成员列表有多名成员
            showSecFragmentLiveData.value = MeetingRoomFragment.FRAGMENT_SEC_MULTI_MIX
        }
    }

    /**
     * 离开房间
     */
    fun liveRoom() {
        // 离开房间，回收资源

    }

    inner class MeetingRoomViewModelImpl: MeetingRoomContract.IMeetingRoomViewModel {

    }
}