package com.freewind.seastarvideo.authorize.login

import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.base.BaseContract
import com.freewind.seastarvideo.http.bean.LoginBean

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
        fun requestLoginWithPwd(account: String, pwd: String)

        /**
         * 请求：获取短信验证码
         */
        fun requestGetSmsCode(phoneNumber: String)

        /**
         * 请求：手机号验证码登录
         */
        fun requestLoginWithCode(phoneNumber: String, code: String)
    }

    interface ILoginViewModel: BaseContract.IViewModel {

        /**
         * 返回：手机号密码登录
         */
        fun responseLoginWithPwd(uiResponse: UiResponse<LoginBean>)

        /**
         * 返回：获取短信验证码
         */
        fun responseGetSmsCode(uiResponse: UiResponse<String>)

        /**
         * 返回：手机号验证码登录
         */
        fun responseLoginWithCode(uiResponse: UiResponse<LoginBean>)
    }
}