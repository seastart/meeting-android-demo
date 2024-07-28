package com.freewind.seastarvideo.authorize.login

import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.base.SingleLiveEvent
import com.freewind.seastarvideo.http.bean.LoginBean
import com.freewind.seastarvideo.utils.KvUtil

/**
 * @author: wiatt
 * @date: 2023/12/20 10:24
 * @description:
 */
class LoginViewModel: BaseViewModel<LoginModel, LoginContract.ILoginViewModel>() {

    val loginWithPwdResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    val loginWithCodeResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    val getSmsCodeResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()

    override fun getModel(): LoginModel {
        return LoginModel(LoginViewModelImpl())
    }

    fun loginWithPwd(account: String, pwd: String) {
        mModel.getContract().requestLoginWithPwd(account, pwd)
    }

    /**
     * 获取短信验证码
     */
    fun getSmsCode(phoneNumber: String) {
        mModel.getContract().requestGetSmsCode(phoneNumber)
    }

    fun loginWithCode(phoneNumber: String, code: String) {
        mModel.getContract().requestLoginWithCode(phoneNumber, code)
    }

    inner class LoginViewModelImpl: LoginContract.ILoginViewModel {
        override fun responseLoginWithPwd(uiResponse: UiResponse<LoginBean>) {
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

                loginWithPwdResult.value = UiResponse("success")
            } else {
                loginWithPwdResult.value = UiResponse(uiResponse.mError!!)
            }
        }

        override fun responseGetSmsCode(uiResponse: UiResponse<String>) {
            getSmsCodeResult.value = uiResponse
        }

        override fun responseLoginWithCode(uiResponse: UiResponse<LoginBean>) {
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

                loginWithCodeResult.value = UiResponse("success")
            } else {
                loginWithCodeResult.value = UiResponse(uiResponse.mError!!)
            }
        }

    }
}