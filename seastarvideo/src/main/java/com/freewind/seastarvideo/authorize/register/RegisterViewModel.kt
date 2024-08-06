/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.authorize.register

import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.base.SingleLiveEvent
import com.freewind.seastarvideo.http.bean.RegisterBean
import com.freewind.seastarvideo.http.bean.SelfDetailBean
import com.freewind.seastarvideo.utils.KvUtil

/**
 * @author: wiatt
 * @date: 2023/12/21 16:38
 * @description:
 */
class RegisterViewModel(): BaseViewModel<RegisterModel, RegisterContract.IRegisterViewModel>() {

    val getSmsCodeResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    val registerResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    val updateSelfDetailResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    val meetingGrantResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()

    override fun getModel(): RegisterModel {
        return RegisterModel(RegisterViewModelImpl())
    }

    /**
     * 获取短信验证码
     */
    fun getSmsCode(phoneNumber: String) {
        mModel.getContract().requestGetSmsCode(phoneNumber)
    }

    /**
     * 注册账号
     */
    fun register(phoneNumber: String, code: String, pwd: String) {
        mModel.getContract().requestRegister(phoneNumber, code, pwd)
    }

    /**
     * meeting 授权
     */
    fun meetingGrant() {
        mModel.getContract().requestMeetingGrant()
    }

    /**
     * 更新自身信息
     */
    fun updateSelfDetail(nickName: String, avatar: String) {
        mModel.getContract().requestUpdateSelfDetail(nickName, avatar)
    }

    inner class RegisterViewModelImpl: RegisterContract.IRegisterViewModel {

        override fun responseGetSmsCode(uiResponse: UiResponse<String>) {
            getSmsCodeResult.value = uiResponse
        }

        override fun responseRegister(uiResponse: UiResponse<RegisterBean>) {
            if (uiResponse.isSuccess) {
                val uid = uiResponse.mResult!!.userId
                val mobile = uiResponse.mResult!!.mobile
                val nickName = uiResponse.mResult!!.nickName
                val avatar = uiResponse.mResult!!.avatar
                val jwtToken = uiResponse.mResult!!.jwtToken
                KvUtil.encode(KvUtil.USER_INFO_UID, uid)
                KvUtil.encode(KvUtil.USER_INFO_MOBILE, mobile)
                KvUtil.encode(KvUtil.USER_INFO_NICK_NAME, nickName)
                KvUtil.encode(KvUtil.USER_INFO_AVATAR, avatar)
                KvUtil.encode(KvUtil.JWT_TOKEN, jwtToken)

                registerResult.value = UiResponse("success")
            } else {
                registerResult.value = UiResponse(uiResponse.mError!!)
            }
        }

        override fun responseMeetingGrant(uiResponse: UiResponse<String>) {
            meetingGrantResult.value = uiResponse
        }

        override fun responseUpdateSelfDetail(uiResponse: UiResponse<SelfDetailBean>) {
            if (uiResponse.isSuccess) {
                val uid = uiResponse.mResult!!.userId
                val mobile = uiResponse.mResult!!.mobile
                val nickName = uiResponse.mResult!!.nickName
                val avatar = uiResponse.mResult!!.avatar
                KvUtil.encode(KvUtil.USER_INFO_UID, uid)
                KvUtil.encode(KvUtil.USER_INFO_MOBILE, mobile)
                KvUtil.encode(KvUtil.USER_INFO_NICK_NAME, nickName)
                KvUtil.encode(KvUtil.USER_INFO_AVATAR, avatar)

                updateSelfDetailResult.value = UiResponse("success")
            } else {
                updateSelfDetailResult.value = UiResponse(uiResponse.mError!!)
            }
        }
    }
}