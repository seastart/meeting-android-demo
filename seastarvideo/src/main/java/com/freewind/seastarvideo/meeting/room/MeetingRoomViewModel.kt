/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.meeting.room

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.freewind.seastarvideo.MeetingEngineHelper
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.base.SingleLiveEvent
import com.freewind.seastarvideo.base.UiResponse
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

    // 摄像头权限申请
    val cameraPermissionCheck: SingleLiveEvent<Boolean> = SingleLiveEvent()
    // 打开摄像头操作结果
    val openCameraResult: SingleLiveEvent<UiResponse<Boolean>> = SingleLiveEvent()
    // 麦克风权限申请
    val micPermissionCheck: SingleLiveEvent<Boolean> = SingleLiveEvent()
    // 打开麦克风操作结果
    val openMicResult: SingleLiveEvent<UiResponse<Boolean>> = SingleLiveEvent()

    // 从 ViewModel 回调数据到界面
    private var viewListeners: MutableList<MeetingRoomListener> = mutableListOf()

    var meetingId: String? = null
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

    fun addListener(listener: MeetingRoomListener) {
        if (!viewListeners.contains(listener)) {
            viewListeners.add(listener)
        }
    }

    fun removeListener(listener: MeetingRoomListener) {
        if (viewListeners.contains(listener)) {
            viewListeners.remove(listener)
        }
    }

    /**
     * 更新初始参数
     */
    fun updateInitialValue(nickName: String, roomId: String, isOpenCamera: Boolean, isCloseCamera: Boolean) {
        myNickName = nickName
        this.roomId = roomId
        myMicStatus = isOpenCamera
        myCameraStatus = isCloseCamera
        memberList.add(MemberInfo("0", nickName, MemberInfo.MEMBER_ROLE_NORMAL, myMicStatus, myCameraStatus))
        memberList.add(MemberInfo("1", "成员-1", MemberInfo.MEMBER_ROLE_COMPERE, false, false))
        memberList.add(MemberInfo("2", "成员-2", MemberInfo.MEMBER_ROLE_NORMAL, true, false))
        memberList.add(MemberInfo("3", "成员-3", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
        memberList.add(MemberInfo("4", "成员-4", MemberInfo.MEMBER_ROLE_NORMAL, false, true))

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
     * 请求切换麦克风状态
     */
    fun requestSwitchMicStatus() {
        if (myMicStatus) {
            // 当前状态为打开，需要关闭
            updateMyMicStatus(false)
        } else {
            // 当前状态为关闭，需要打开
            micPermissionCheck.value = true
        }
    }

    /**
     * 更新麦克风状态
     */
    fun updateMyMicStatus(isOpen: Boolean) {
        if (isOpen) {
            // 执行打开麦克风的操作
            mModel.getContract().requestOpenMic()
        } else {
            // 执行关闭麦克风状态
            mModel.getContract().requestCloseMic()
            myMicStatus = false
            myMicStatusLiveData.value = false
        }
    }

    /**
     * 获取自己的照相机状态
     */
    fun getMyCameraStatus() {
        myCameraStatusLiveData.value = myCameraStatus
    }

    /**
     * 请求切换摄像头状态
     */
    fun requestSwitchCameraStatus() {
        if (myCameraStatus) {
            // 当前状态为打开，需要关闭
            updateMyCameraStatus(false, null)
        } else {
            // 当前状态为关闭，需要打开
            cameraPermissionCheck.value = true
        }
    }

    /**
     * 切换照相机状态
     */
    fun updateMyCameraStatus(isOpen: Boolean, activity: Activity?) {
        if (isOpen) {
            // 执行打开摄像头的操作
            mModel.getContract().requestOpenCamera(activity!!)
        } else {
            // 执行关闭摄像头的操作
            mModel.getContract().requestCloseCamera()
            myCameraStatus = false
            myCameraStatusLiveData.value = false
            checkSecFragment()
        }
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
        LogUtil.i("checkSecFragment, memberList.size = ${memberList.size}", "wiatt")
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

    var testNum: Int = 0
    fun addMoreMember() {
        testNum = memberList.size
        val list = mutableListOf<MemberInfo>()
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, true, true))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))

        list.forEach { memberInfo ->
            memberList.add(memberInfo)
            viewListeners.forEach {
                it.onMemberListAddOne(memberList.size - 1)
            }
        }
    }

    fun addOneMember() {
        testNum = memberList.size
        val memberInfo = MemberInfo(testNum.toString(), "成员-$testNum" , MemberInfo.MEMBER_ROLE_NORMAL, false, false)
        memberList.add(memberInfo)
        viewListeners.forEach {
            it.onMemberListAddOne(memberList.size - 1)
        }
    }

    fun removeOneMember(position: Int) {
        memberList.removeAt(position)
        viewListeners.forEach {
            it.onMemberListRemoveOne(position)
        }
        if (memberList.size <= 1) {
            checkSecFragment()
        }
    }

    fun updateMember(position: Int) {
        val memberInfo = memberList[position]
        memberInfo.micStatus = !memberInfo.micStatus
        memberInfo.cameraStatus = !memberInfo.cameraStatus
        memberInfo.nickName = memberInfo.nickName + "改"
        memberList[position] = memberInfo
        viewListeners.forEach {
            it.onUpdateMember(position, memberInfo)
        }
    }

    /**
     * 离开房间
     */
    fun liveRoom() {
        // 离开房间，回收资源
        MeetingEngineHelper.instance.engine?.exitMeeting()
    }

    inner class MeetingRoomViewModelImpl: MeetingRoomContract.IMeetingRoomViewModel {
        override fun responseOpenCamera(uiResponse: UiResponse<Boolean>) {
            if (uiResponse.isSuccess) {
                myCameraStatus = true
                myCameraStatusLiveData.value = true
                checkSecFragment()
            }
            openCameraResult.value = uiResponse
        }

        override fun responseOpenMic(uiResponse: UiResponse<Boolean>) {
            if (uiResponse.isSuccess) {
                myMicStatus = true
                myMicStatusLiveData.value = true
            }
            openMicResult.value = uiResponse
        }

    }

    interface MeetingRoomListener {
        // 添加一个参会成员
        fun onMemberListAddOne(position: Int)
        //移除一个参会成员
        fun onMemberListRemoveOne(position: Int)
        // 更新一个参会成员
        fun onUpdateMember(position: Int, memberInfo: MemberInfo)
    }
}