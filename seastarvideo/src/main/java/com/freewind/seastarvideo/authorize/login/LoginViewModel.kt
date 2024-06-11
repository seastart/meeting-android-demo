package com.freewind.seastarvideo.authorize.login

import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.base.BaseViewModel
import com.freewind.seastarvideo.base.SingleLiveEvent

/**
 * @author: wiatt
 * @date: 2023/12/20 10:24
 * @description:
 */
class LoginViewModel: BaseViewModel<LoginModel, LoginContract.ILoginViewModel>() {

    val loginWithPwdResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()
    val loginWithCodeResult: SingleLiveEvent<UiResponse<String>> = SingleLiveEvent()

    override fun getModel(): LoginModel {
        return LoginModel(LoginViewModelImpl())
    }

    fun loginWithPwd(phoneNumber: String, pwd: String) {
        mModel.getContract().requestLoginWithPwd(phoneNumber, pwd)
    }

    fun getCode() {

    }

    fun loginWithCode(phoneNumber: String, code: String) {
        mModel.getContract().requestLoginWithCode(phoneNumber, code)
    }

    inner class LoginViewModelImpl: LoginContract.ILoginViewModel {
        override fun responseLoginWithPwd(uiResponse: UiResponse<String>) {
            loginWithPwdResult.value = uiResponse
        }

        override fun responseGetCode(uiResponse: UiResponse<String>) {

        }

        override fun responseLoginWithCode(uiResponse: UiResponse<String>) {
            loginWithCodeResult.value = uiResponse
        }

    }
}