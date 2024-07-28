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
import com.freewind.seastarvideo.base.BaseContract
import com.freewind.seastarvideo.http.bean.RegisterBean
import com.freewind.seastarvideo.http.bean.SelfDetailBean

/**
 * @author: wiatt
 * @date: 2023/12/21 16:34
 * @description:
 */
class RegisterContract {

    interface IRegisterModel: BaseContract.IModel {

        /**
         * 请求：获取短信验证码
         */
        fun requestGetSmsCode(phoneNumber: String)

        /**
         * 请求：注册
         */
        fun requestRegister(phoneNumber: String, code: String, pwd: String)

        /**
         * 自身更新
         */
        fun requestUpdateSelfDetail(nickName: String, avatar: String)
    }

    interface IRegisterViewModel: BaseContract.IViewModel {
        /**
         * 返回：获取短信验证码
         */
        fun responseGetSmsCode(uiResponse: UiResponse<String>)

        /**
         * 返回：注册
         */
        fun responseRegister(uiResponse: UiResponse<RegisterBean>)

        /**
         * 返回：自身更新
         */
        fun responseUpdateSelfDetail(uiResponse: UiResponse<SelfDetailBean>)
    }
}