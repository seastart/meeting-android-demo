package com.freewind.seastarvideo

import android.app.Application
import android.content.Context
import android.content.Intent
import cn.seastart.meeting.bean.LoginBean
import cn.seastart.meeting.impl.MeetingResultListener
import cn.seastart.rtc.enumerate.DeviceType
import com.freewind.seastarvideo.activity.HomeActivity
import com.freewind.seastarvideo.http.ApiEngine
import com.freewind.seastarvideo.http.ApiHelper
import com.freewind.seastarvideo.http.Callback
import com.freewind.seastarvideo.http.bean.Data
import com.freewind.seastarvideo.utils.DeviceUtil
import com.freewind.seastarvideo.utils.KvUtil
import com.freewind.seastarvideo.utils.ToastUtil
import com.tencent.mmkv.MMKV

/**
 * @author: wiatt
 * @date: 2023/12/20 9:59
 * @description:
 */
class SeaStarClientImpl: SeaStarClient {

    var app: Application? = null

    override fun SSC_Init(application: Application) {
        app = application
        EnvArgument.instance.app = application
        MMKV.initialize(application)
        ApiHelper.instance.init(ApiEngine.BaseUrl)

        val jwtToken = KvUtil.decodeString(KvUtil.JWT_TOKEN)
        if (jwtToken.isNotEmpty()) {
            ApiHelper.instance.meetingGrant(object : Callback<Data<String>>() {
                override fun onSuccess(data: Data<String>) {
                    super.onSuccess(data)
                    val uid = KvUtil.decodeString(KvUtil.USER_INFO_UID)
                    val token = data.data
                    if (uid.isNotEmpty() && !token.isNullOrEmpty()) {
                        meetingSdkLogin(uid, token)
                    }
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                }
            })
        }
    }

    override fun SSC_StartHomeActivity(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }

    /**
     * 登录 sdk
     */
    private fun meetingSdkLogin(userId: String, token: String) {
        val context = EnvArgument.instance.app!!
        MeetingEngineHelper.instance.init(context, token, null,
            object : MeetingResultListener {
                override fun onFail(code: Int, message: String) {
                    ToastUtil.showShortToast("sdk 初始化失败")
                }

                override fun onSuccess() {
                    MeetingEngineHelper.instance.engine?.login(
                        userId, 1, DeviceType.Android.value, DeviceUtil.getAndroidID(context),
                        object : cn.seastart.meeting.api.Callback<cn.seastart.meeting.bean.Data<LoginBean>?>() {
                            override fun onSuccess(data: cn.seastart.meeting.bean.Data<LoginBean>?) {
                                super.onSuccess(data)
                                // 此处登录成功，应该什么都不用做
                            }

                            override fun onFailed(code: Int, msg: String?) {
                                super.onFailed(code, msg)
                                // TODO 此处等捋顺了 SDK 的创建和初始化后再来解决
                            }
                        })
                }
            }
        )
    }
}