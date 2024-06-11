package com.freewind.seastarvideo.authorize.login

import com.freewind.seastarvideo.base.BaseModel
import com.freewind.seastarvideo.base.UiResponse

/**
 * @author: wiatt
 * @date: 2023/12/20 10:22
 * @description:
 */
class LoginModel(viewModelImpl: LoginViewModel.LoginViewModelImpl):
    BaseModel<LoginContract.ILoginViewModel, LoginContract.ILoginModel>(viewModelImpl) {
    val tempToken = "B3EZ1dNi69415alCoA434q2P9O2QuMoYsSIOzFnsmMIamv/jDW12bUk/BUW/EwE59jxwAqVsphonFFG8dWz2jRQ+eKui3AWno5dT8tY5isfHeq4JjMR/LoZJ55Tpzz5GlE0PVp4OLgr7WAa9zzEFIAqPv9PzkA7mscbAuK9TlTUj2NEkblvEcDVOsZ4tkhMdqgpwOHR23qZ/PUNbpbkr1f0Gzx5sFPCWu0Mhkp+ZPuMg0zhQW7gDKNuYN+KyN7HdvMTOWIe6zWZhTIverT/yWQ=="
    override fun getContract(): LoginContract.ILoginModel {
        return LoginModelImpl()
    }

    inner class LoginModelImpl: LoginContract.ILoginModel {
        override fun requestLoginWithPwd(phoneNumber: String, pwd: String) {
            // todo 此处要做网络请求
            listener.responseLoginWithPwd(UiResponse(tempToken))
        }

        override fun requestGetCode() {
        }

        override fun requestLoginWithCode(phoneNumber: String, code: String) {
            // todo 此处要做网络请求
            listener.responseLoginWithCode(UiResponse(tempToken))
        }

    }
}