package com.freewind.seastarvideo.authorize.login

import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.base.BaseContract

/**
 * @author: wiatt
 * @date: 2023/12/20 10:21
 * @description:
 */
class LoginContract {

    interface ILoginModel: BaseContract.IModel {
        /**
         * 请求：手机号密码登录
         */
        fun requestLoginWithPwd(phoneNumber: String, pwd: String)

        /**
         * 请求：获取验证码
         */
        fun requestGetCode()

        /**
         * 请求：手机号验证码登录
         */
        fun requestLoginWithCode(phoneNumber: String, code: String)
    }

    interface ILoginViewModel: BaseContract.IViewModel {

        /**
         * 返回：手机号密码登录
         */
        fun responseLoginWithPwd(uiResponse: UiResponse<String>)

        /**
         * 返回：获取验证码
         */
        fun responseGetCode(uiResponse: UiResponse<String>)

        /**
         * 返回：手机号验证码登录
         */
        fun responseLoginWithCode(uiResponse: UiResponse<String>)
    }
}