/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.freewind.seastarvideo.R
import com.freewind.seastarvideo.base.BaseActivity
import com.freewind.seastarvideo.base.BaseFragment
import com.freewind.seastarvideo.databinding.ActivityMeetingRoomBinding
import com.freewind.seastarvideo.meeting.room.MeetingRoomEventBean
import com.freewind.seastarvideo.meeting.room.MeetingRoomFragment
import com.freewind.seastarvideo.meeting.room.MeetingRoomViewModel
import com.freewind.seastarvideo.ui.DialogManager
import com.freewind.seastarvideo.utils.OtherUiManager
import com.freewind.seastarvideo.utils.ToastUtil
import com.freewind.seastarvideo.utils.activityManager.ActivityHelp
import com.permissionx.guolindev.PermissionX
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Stack

class MeetingRoomActivity : BaseActivity() {

    private lateinit var binding: ActivityMeetingRoomBinding

    private var currentFragment: Fragment? = null
    // fragment 任务栈，当前正在显示的fragment保存在顶部
    private var fragmentStack: Stack<BaseFragment> = Stack()
    private lateinit var fragmentType: String

    private var meetingRoomFragment: MeetingRoomFragment? = null

    private lateinit var viewModel: MeetingRoomViewModel
    // 标识：是否已经执行离开操作
    private var isLeave: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingRoomBinding.inflate(layoutInflater)
        val rootView = binding.root
        OtherUiManager.instance.adaptBottomHeight(binding.meetingRoomCl)
        setContentView(rootView)
        EventBus.getDefault().register(this)
        initViewModel()

        isLeave = false
        ActivityHelp.instance.createTopActivityUtil(this)
        ActivityHelp.instance.configTopActivityUtil(true)

        showMeetingRoomPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (!isLeave) {
            leave()
        }
    }

    override fun onBackPressed() {
        if (fragmentStack.size <= 1) {
            EventBus.getDefault().post(MeetingRoomEventBean.finishActivityEvent(true))
        } else {
            fragmentStack.pop()
            val curFragment = fragmentStack.peek()
            switchFragment(curFragment)
        }
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this)[MeetingRoomViewModel::class.java]
        viewModel.onEnterRoomEvent.observe(this) {
            Log.i("wiatt", "activity-initViewModel-onEnterRoomEvent: ")
            PermissionX.init(this)
                .permissions(mutableListOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
                .request { allGranted, grantedList, deniedList ->
                    grantedList.forEach {
                        if (it.equals(Manifest.permission.CAMERA)) {
                            viewModel.updateMyCameraStatus(true, this)
                        } else if (it.equals(Manifest.permission.RECORD_AUDIO)) {
                            viewModel.updateMyMicStatus(true)
                        }
                    }
                }
        }
        viewModel.cameraPermissionCheck.observe(this) {
            PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA)
                .onForwardToSettings { forwardScope, deniedList ->

                    DialogManager.instance.showRequestPermissionDialog(this,
                        resources.getString(R.string.unable_use_camera),
                        resources.getString(R.string.method_grant_camera_permissions)
                    ) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            // 创建一个指向当前应用的 URI
                            data = Uri.fromParts("package", this@MeetingRoomActivity.packageName, null)
                        }
                        // 启动设置页面
                        this@MeetingRoomActivity.startActivity(intent)
                    }
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        viewModel.updateMyCameraStatus(true, this)
                    }
                }
        }
        viewModel.micPermissionCheck.observe(this) {
            PermissionX.init(this)
                .permissions(Manifest.permission.RECORD_AUDIO)
                .onForwardToSettings { forwardScope, deniedList ->
                    DialogManager.instance.showRequestPermissionDialog(this,
                        resources.getString(R.string.unable_use_mic),
                        resources.getString(R.string.method_grant_mic_permissions)
                    ) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            // 创建一个指向当前应用的 URI
                            data = Uri.fromParts("package", this@MeetingRoomActivity.packageName, null)
                        }
                        // 启动设置页面
                        this@MeetingRoomActivity.startActivity(intent)
                    }
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        viewModel.updateMyMicStatus(true)
                    }
                }
        }
        viewModel.screenSharePermissionCheck.observe(this) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            // 此处没必要使用 startActivityForResult，因为开启悬浮窗权限的操作，没有提供返回处理结果的接口
            startActivity(intent)
        }
        viewModel.openCameraResult.observe(this) { uiResponse ->
            uiResponse?.let { response ->
                if (!response.isSuccess) {
                    ToastUtil.showShortToast("打开摄像头失败")
                }
            }
        }
        viewModel.openMicResult.observe(this) { uiResponse ->
            uiResponse?.let { response ->
                if (!response.isSuccess) {
                    ToastUtil.showShortToast("打开麦克风失败")
                }
            }
        }
        viewModel.startScreenShareResult.observe(this) { uiResponse ->
            uiResponse?.let { response ->
                if (response.isSuccess) {
                    ToastUtil.showShortToast("屏幕共享打开成功")
                } else {
                    ToastUtil.showShortToast("屏幕共享打开失败")
                }
            }
        }
    }

    /**
     * 切换到 会议房间页面
     */
    @Synchronized
    private fun showMeetingRoomPage() {
        if (meetingRoomFragment == null) {
            val nickName = intent.getStringExtra(PARAM_NICKNAME) ?: resources.getString(R.string.def_nickname)
            val roomId = intent.getStringExtra(PARAM_ROOM_ID) ?: resources.getString(R.string.def_room_id)
            val expectCameraState = intent.getBooleanExtra(PARAM_CAMERA_STATE, false)
            val expectMicState = intent.getBooleanExtra(PARAM_MIC_STATE, false)
            meetingRoomFragment = MeetingRoomFragment.newInstance(nickName, roomId, expectCameraState, expectMicState)
        }

        if (switchFragment(meetingRoomFragment)) {
            fragmentType = MEETING_ROOM_DETAIL
            fragmentStack.push(meetingRoomFragment)
        }
    }

    /**
     * 用于切换Fragment
     * 当不显示fragment时，nextFragment为空
     */
    private fun switchFragment(nextFragment: Fragment?): Boolean{
        if(nextFragment == currentFragment) {
            return false
        }

        if (currentFragment != null && currentFragment!!.isAdded) {
            hideFragment(currentFragment!!)
        }

        if (nextFragment != null) {
            if (nextFragment.isAdded) {
                showFragment(nextFragment)
            } else {
                addFragment(nextFragment, R.id.contentFl)
            }
        }
        currentFragment = nextFragment
        return true
    }

    /**
     * 离开会议
     */
    private fun leave() {
        isLeave = true
        viewModel.liveRoom()
        ActivityHelp.instance.startActivityInHostTask()
        ActivityHelp.instance.releaseTopActivityUtil()
        ActivityHelp.instance.releaseCustomBackgroundEvent()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: MeetingRoomEventBean.UpdatePageEvent) {
        if (!event.canBack) {
            when (event.pageType) {
                MEETING_ROOM_DETAIL -> {
                    fragmentStack.clear()
                }
            }
        }
        when (event.pageType) {
            MEETING_ROOM_DETAIL -> {
                showMeetingRoomPage()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventUpdatePage(event: MeetingRoomEventBean.goBackPageEvent) {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEventFinishActivity(event: MeetingRoomEventBean.finishActivityEvent) {
        if (event.isPrompt) {
            DialogManager.instance.showLeaveRoomDialog(this) {
                leave()
            }
        } else {
            leave()
        }
    }

    companion object {
        const val MEETING_ROOM_KEY = "meeting_room_key"
        const val PARAM_NICKNAME = "param_nickname"
        const val PARAM_ROOM_ID = "param_room_id"
        const val PARAM_CAMERA_STATE = "param_camera_state"
        const val PARAM_MIC_STATE = "param_mic_state"

        // 会议房间详情页
        const val MEETING_ROOM_DETAIL = "meeting_room_detail"

        /**
         * 启动会议房间页面
         */
        fun startMeetingRoomPage(context: Context, nickname: String, roomId: String, expectCameraState: Boolean, expectMicState: Boolean) {
            val intent = Intent(context, MeetingRoomActivity::class.java)
            intent.putExtra(MEETING_ROOM_KEY, MEETING_ROOM_DETAIL)
            intent.putExtra(PARAM_NICKNAME, nickname)
            intent.putExtra(PARAM_ROOM_ID, roomId)
            intent.putExtra(PARAM_CAMERA_STATE, expectCameraState)
            intent.putExtra(PARAM_MIC_STATE, expectMicState)
            context.startActivity(intent)
        }
    }
}