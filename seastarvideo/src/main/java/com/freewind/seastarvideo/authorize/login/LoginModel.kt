package com.freewind.seastarvideo.authorize.login

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/20 10:22
 * @description:
 */
class LoginModel(viewModelImpl: LoginViewModel.LoginViewModelImpl):
    BaseModel<LoginContract.ILoginViewModel, LoginContract.ILoginModel>(viewModelImpl) {
    override fun getContract(): LoginContract.ILoginModel {
        return LoginModelImpl()
    }

    inner class LoginModelImpl: LoginContract.ILoginModel {
        override fun requestLoginWithPwd(phoneNumber: String, pwd: String) {
            TODO("Not yet implemented")
        }

        override fun requestGetCode() {
            TODO("Not yet implemented")
        }

        override fun requestLoginWithCode(phoneNumber: String, code: String) {
            TODO("Not yet implemented")
        }

    }
}