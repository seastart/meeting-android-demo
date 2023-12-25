package com.freewind.seastarvideo.authorize.login

import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.base.BaseViewModel

/**
 * @author: wiatt
 * @date: 2023/12/20 10:24
 * @description:
 */
class LoginViewModel(): BaseViewModel<LoginModel, LoginContract.ILoginViewModel>() {

    override fun getModel(): LoginModel {
        return LoginModel(LoginViewModelImpl())
    }

    fun loginWithPwd(phoneNumber: String, pwd: String) {

    }

    fun getCode() {

    }

    fun loginWithCode(phoneNumber: String, code: String) {

    }

    inner class LoginViewModelImpl: LoginContract.ILoginViewModel {
        override fun responseLoginWithPwd(uiResponse: UiResponse<String>) {

        }

        override fun responseGetCode(uiResponse: UiResponse<String>) {

        }

        override fun responseLoginWithCode(uiResponse: UiResponse<String>) {

        }

    }
}