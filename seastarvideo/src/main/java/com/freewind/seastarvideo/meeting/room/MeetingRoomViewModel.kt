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
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import cn.seastart.meeting.ScreenManager
import cn.seastart.meeting.enumerate.DeviceState
import cn.seastart.meeting.enumerate.HandupType
import cn.seastart.meeting.enumerate.LeaveReason
import cn.seastart.meeting.enumerate.RoleType
import cn.seastart.meeting.enumerate.ShareType
import cn.seastart.meeting.impl.MediaEvent
import cn.seastart.meeting.impl.RoomEvent
import cn.seastart.meeting.impl.UserEvent
import cn.seastart.rtc.info.RTCMediaStatus
import cn.seastart.rtc.info.SpeakerInfo
import com.freewind.seastarvideo.MeetingEngineHelper
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.base.SingleLiveEvent
import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.meeting.MemberInfo
import com.freewind.seastarvideo.utils.LogUtil
import com.ook.android.VCS_EVENT_TYPE
import java.lang.ref.WeakReference

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
    // 自己的头像 liveData
    val myAvatarLiveData: MutableLiveData<String> = MutableLiveData()
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

    // 加入会议
    val enterMeetingResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    // 摄像头权限申请
    val cameraPermissionCheck: SingleLiveEvent<Boolean> = SingleLiveEvent()
    // 打开摄像头操作结果
    val openCameraResult: SingleLiveEvent<UiResponse<Boolean>> = SingleLiveEvent()
    // 麦克风权限申请
    val micPermissionCheck: SingleLiveEvent<Boolean> = SingleLiveEvent()
    // 打开麦克风操作结果
    val openMicResult: SingleLiveEvent<UiResponse<Boolean>> = SingleLiveEvent()
    // 悬浮窗权限申请
    val screenSharePermissionCheck: SingleLiveEvent<Boolean> = SingleLiveEvent()
    // 开始屏幕共享操作结果
    val startScreenShareResult: SingleLiveEvent<UiResponse<Boolean>> = SingleLiveEvent()
    // 开始白板共享操作结果
    val startWhiteBoardShareResult: SingleLiveEvent<UiResponse<Boolean>> = SingleLiveEvent()

    // 接口数据，自己进入房间
    val onEnterRoomEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    // 接口数据，自己离开房间
    val onExitRoomEvent: SingleLiveEvent<LeaveReason> = SingleLiveEvent()

    // 从 ViewModel 回调数据到界面
    private var viewListeners: MutableList<MeetingRoomListener> = mutableListOf()

    var meetingId: String? = null
    // 房间 id
    var roomNo: String? = null
    // 房间音频状态
    var roomVideoStatus: Boolean = false
    // 自己的 uid
    var myUid: String? = null
    // 自己的昵称
    var myNickName: String? = null
    // 自己的头像
    var myAvatar: String? = null
    // 自己的麦克风状态，可能受各种因素共同作用，也可能不为 boolean 类型
    var myMicStatus: Boolean = false
    // 自己的照相机状态，可能受各种因素共同作用，也可能不为 boolean 类型
    var myCameraStatus: Boolean = false
    // 自己的屏幕共享状态，可能受各种因素共同作用，也可能不为 boolean 类型
    var myScreenShareStatus: Boolean = false
    // 自己的白板共享状态
    var myWhiteBoardShareStatus: Boolean = false
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
    fun updateInitialValue(roomNo: String, nickName: String, avatar: String) {
        this.roomNo = roomNo
        myNickName = nickName
        myAvatar = avatar
    }

    /**
     * 获取房间Id
     */
    fun getRoomId() {
        roomNo?.let {
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
     * 获取自己的头像
     */
    fun getMyAvatar() {
        myAvatar?.let {
            myAvatarLiveData.value = it
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
     * 进入会议
     */
    fun enterMeeting(activity: Activity) {
        MeetingEngineHelper.instance.setRoomEvent(RoomEventImpl(this))
        MeetingEngineHelper.instance.setUserEvent(UserEventImpl(this))
        MeetingEngineHelper.instance.setMediaEvent(MediaEventImpl(this))

        mModel.getContract().requestEnterMeeting(activity, roomNo ?: "", null, myNickName ?: "", myAvatar ?: "")
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
     * 打开摄像头，需要传 activity
     * 关闭摄像头，不需要传 activity
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
        myScreenShareLiveData.value = myScreenShareStatus
    }

    /**
     * 请求打开屏幕共享
     */
    fun requestStartScreenShare(activity: Activity) {
        if (Settings.canDrawOverlays(activity)) {
            updateMyScreenShareStatus(true, activity)
        } else {
            screenSharePermissionCheck.value = true
        }
    }

    /**
     * 更新自己的屏幕共享状态
     * 开始共享，需要传 activity
     * 停止共享，不需要传 activity
     */
    fun updateMyScreenShareStatus(isStart: Boolean, activity: Activity?) {
        if (isStart) {
            mModel.getContract().requestStartScreenShare(activity!!, null, ScreenShareEvent(this))
        } else {
            mModel.getContract().requestStopScreenShare()
        }
    }

    /**
     * 更新自己的白板共享状态
     */
    fun updateMyWhiteBoardShareStatus(isStart: Boolean) {

    }

    /**
     * 添加预览控件
     */
    fun addPreview(view: View) {
        MeetingEngineHelper.instance.engine?.addPreview(view)
    }

    /**
     * 移除渲染控件
     * @param view 如果 view 不为空，则移除指定控件；如果 view 为空，则移除所有控件
     */
    fun removePreview(view: View?) {
        MeetingEngineHelper.instance.engine?.removePreview(view)
    }

    /**
     * 替换预览渲染控件
     * @param mViews
     */
    fun replacePreView(mViews: MutableList<View>) {
        MeetingEngineHelper.instance.engine?.replacePreView(mViews)
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
//        testNum = memberList.size
//        val list = mutableListOf<MemberInfo>()
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, true, true))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, true))
//        list.add(MemberInfo((++testNum).toString(), "成员-$testNum", MemberInfo.MEMBER_ROLE_NORMAL, false, false))
//
//        list.forEach { memberInfo ->
//            memberList.add(memberInfo)
//            viewListeners.forEach {
//                it.onMemberListAddOne(memberList.size - 1)
//            }
//        }
    }

    fun addOneMember() {
//        testNum = memberList.size
//        val memberInfo = MemberInfo(testNum.toString(), "成员-$testNum" , MemberInfo.MEMBER_ROLE_NORMAL, false, false)
//        memberList.add(memberInfo)
//        viewListeners.forEach {
//            it.onMemberListAddOne(memberList.size - 1)
//        }
    }

    fun removeOneMember(position: Int) {
//        memberList.removeAt(position)
//        viewListeners.forEach {
//            it.onMemberListRemoveOne(position)
//        }
//        if (memberList.size <= 1) {
//            checkSecFragment()
//        }
    }

    fun updateMember(position: Int) {
//        val memberInfo = memberList[position]
//        memberInfo.micStatus = !memberInfo.micStatus
//        memberInfo.cameraStatus = !memberInfo.cameraStatus
//        memberInfo.nickName = memberInfo.nickName + "改"
//        memberList[position] = memberInfo
//        viewListeners.forEach {
//            it.onUpdateMember(position, memberInfo)
//        }
    }

    /**
     * 离开房间
     */
    fun liveRoom() {
        // 离开房间，回收资源
        MeetingEngineHelper.instance.engine?.exitMeeting()
    }

    inner class MeetingRoomViewModelImpl: MeetingRoomContract.IMeetingRoomViewModel {
        override fun responseEnterMeeting(uiResponse: UiResponse<String>) {
            enterMeetingResult.value = uiResponse
        }

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

        override fun responseStartScreenShare(uiResponse: UiResponse<Boolean>) {
            if (!uiResponse.isSuccess) {
                myScreenShareStatus = false
                myScreenShareLiveData.value = false
                startScreenShareResult.value = uiResponse
            }
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

    /**
     * 屏幕共享事件
     */
    class ScreenShareEvent(owner: MeetingRoomViewModel): ScreenManager.ScreenShareEvent {

        private var mOwner: WeakReference<MeetingRoomViewModel> = WeakReference(owner)
        override fun onScreenStateChanged(eventId: Int, args: String?) {
            Log.i("wiatt", "onScreenStateChanged: eventId = $eventId, args = $args")
            mOwner.get()?.let { viewModel ->
                when (eventId) {
                    VCS_EVENT_TYPE.ScreenRecordError -> {

                    }
                    VCS_EVENT_TYPE.ScreenRecordStart -> {
                        viewModel.myScreenShareStatus = true
                        viewModel.myScreenShareLiveData.value = true
                    }
                    VCS_EVENT_TYPE.ScreenRecordStop -> {
                        viewModel.myScreenShareStatus = false
                        viewModel.myScreenShareLiveData.value = false
                    }
                }
            }
        }
    }

    class RoomEventImpl(owner: MeetingRoomViewModel): RoomEvent {

        private var mOwner: WeakReference<MeetingRoomViewModel> = WeakReference(owner)

        override fun onRoomCameraStateChanged(uid: String?, selfUnMuteCameraDisabled: Boolean, cameraDisabled: Boolean) {
            
        }

        override fun onRoomMicStateChanged(uid: String?, selfUnMuteMicDisabled: Boolean, micDisabled: Boolean) {
            
        }

        override fun onRoomChatDisabledChanged(uid: String?, enable: Boolean) {
            
        }

        override fun onRoomScreenshotDisabledChanged(uid: String?, screenshot: Boolean) {
            
        }

        override fun onRoomWatermarkDisabledChanged(uid: String?, enable: Boolean) {
            
        }

        override fun onRoomLockedChanged(uid: String?, locked: Boolean) {
            
        }

        override fun onRoomHostMove(sourceUid: String?, targetUid: String?) {
            
        }

        override fun onRoomShareStart(shareUid: String, shareType: ShareType) {
            
        }

        override fun onRoomShareStop(shareUid: String, shareType: ShareType) {
            
        }

        override fun onAdminRoomShareStop(shareUid: String, shareType: ShareType) {
            
        }

        override fun onRoomHandUpChanged(uid: String?, enable: Boolean?, handupType: HandupType?) {
            
        }

        override fun onUserConfirmOpenCamera(approve: Boolean) {
            
        }

        override fun onUserConfirmOpenMic(approve: Boolean) {
            
        }

        override fun onRoomMCUModeChanged() {
            
        }

        override fun onError(errorCode: Int, errMsg: String?) {
            
        }

        override fun onReconnecting() {
            
        }

        override fun onReconnected() {
            
        }
    }

    class UserEventImpl(owner: MeetingRoomViewModel): UserEvent {

        private var mOwner: WeakReference<MeetingRoomViewModel> = WeakReference(owner)

        override fun onEnterRoom(meetingId: String, uid: String?) {
            Log.i("wiatt", "onEnterRoom: ")
            mOwner.get()?.let {
                it.meetingId = meetingId
                it.myUid = uid ?: ""
                it.onEnterRoomEvent.value = true
                it.memberList.clear()
                val meInfo = MeetingEngineHelper.instance.getInfoManager()?.getMeInfo()
                meInfo?.let { info ->
                    val member = MemberInfo(
                        info.uid ?: "", info.name ?: "", info.props?.avatar ?: "",
                        info.props?.role ?: RoleType.Normal,
                        info.props?.micState ?: DeviceState.Closed,
                        info.props?.cameraState ?: DeviceState.Closed,
                        info.props?.shareState ?: ShareType.Normal,
                        info.props?.chatDisabled ?: false, true)
                    it.memberList.add(0, member)
                    it.viewListeners.forEach { listener ->
                        listener.onMemberListAddOne(it.memberList.size - 1)
                    }
                }
            }
        }

        override fun onExitRoom(reason: LeaveReason) {
            Log.i("wiatt", "onExitRoom: ")
            mOwner.get()?.let { viewModel ->
                viewModel.onExitRoomEvent.value = reason
            }
        }

        override fun onUserEnter(uid: String?) {
            Log.i("wiatt", "onUserEnter: uid = $uid")
            mOwner.get()?.let { viewModel ->
                val memberInfo = MeetingEngineHelper.instance.getInfoManager()?.getMemberByUid(uid ?: "")
                memberInfo?.let { info ->
                    val member = MemberInfo(
                        info.uid ?: "", info.name ?: "", info.props?.avatar ?: "",
                        info.props?.role ?: RoleType.Normal,
                        info.props?.micState ?: DeviceState.Closed,
                        info.props?.cameraState ?: DeviceState.Closed,
                        info.props?.shareState ?: ShareType.Normal,
                        info.props?.chatDisabled ?: false, false)
                    viewModel.memberList.add(member)
                    viewModel.checkSecFragment()
                    viewModel.viewListeners.forEach { listener ->
                        listener.onMemberListAddOne(viewModel.memberList.size - 1)
                    }
                }
            }
        }

        override fun onUserExit(uid: String?) {
            Log.i("wiatt", "onUserExit: uid = $uid")
            mOwner.get()?.let { viewModel ->
                val index = viewModel.memberList.indexOfFirst { memberInfo ->
                    memberInfo.uid == uid
                }
                if (index >= 0 && index < viewModel.memberList.size) {
                    viewModel.memberList.removeAt(index)
                    viewModel.checkSecFragment()
                    viewModel.viewListeners.forEach { listener ->
                        listener.onMemberListRemoveOne(index)
                    }
                }
            }
        }

        override fun onUserNameChanged(targetUid: String?, nick: String) {
            
        }

        override fun onUserRoleChanged(targetUid: String?, roleType: RoleType) {
            
        }

        override fun onUserCameraStateChanged(targetUid: String?, cameraState: DeviceState) {
            
        }

        override fun onUserMicStateChanged(targetUid: String?, micState: DeviceState) {
            
        }

        override fun onUserChatDisabledChange(targetUid: String?, enable: Boolean) {
            
        }

        override fun onHandupConfirm(uid: String?, approve: Boolean, handupType: HandupType) {
            
        }

        override fun onRequestUserCameraChange(targetUid: String?, isOpen: Boolean) {
            
        }

        override fun onRequestUserMicChange(targetUid: String?, isOpen: Boolean) {
            
        }
    }

    class MediaEventImpl(owner: MeetingRoomViewModel): MediaEvent {

        private var mOwner: WeakReference<MeetingRoomViewModel> = WeakReference(owner)

        override fun onDownBitrateAdaptiveLevel(uid: String, level: RTCMediaStatus.DownLevel) {
            
        }

        override fun onDownLossLevel(level: RTCMediaStatus.DownLossLevel) {
            
        }

        override fun onDownLossRateAverage(average: Float) {
            
        }

        override fun onDownMediaStatus(downInfos: MutableList<RTCMediaStatus.DownInfo>) {
            
        }

        override fun onMediaConnected() {
            
        }

        override fun onMembersAudioStatus(speakers: MutableList<SpeakerInfo>) {
            
        }

        override fun onNetTestResult(result: String) {
            
        }

        override fun onSpeakAllow(allow: Boolean) {
            
        }

        override fun onUploadBitrateAdaptiveLevel(level: RTCMediaStatus.UploadLevel) {
            
        }

        override fun onUploadMediaStatus(uploadInfo: RTCMediaStatus.UploadInfo) {
            
        }

        override fun onVideoFrame(
            y: ByteArray?, u: ByteArray?, v: ByteArray?,
            width: Int, height: Int, format: Int,
            uid: String, trackId: Int, angle: Int
        ) {
            
        }
    }
}