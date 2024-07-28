/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.authorize.register

import android.util.Log
import com.freewind.seastarvideo.base.BaseModel
import com.freewind.seastarvideo.base.ErrorBean
import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.enumerate.SceneType
import com.freewind.seastarvideo.http.ApiCode
import com.freewind.seastarvideo.http.Callback
import com.freewind.seastarvideo.http.bean.Data
import com.freewind.seastarvideo.http.bean.RegisterBean
import com.freewind.seastarvideo.http.bean.SelfDetailBean

/**
 * @author: wiatt
 * @date: 2023/12/21 16:39
 * @description:
 */
class RegisterModel(viewModelImpl: RegisterViewModel.RegisterViewModelImpl):
    BaseModel<RegisterContract.IRegisterViewModel, RegisterContract.IRegisterModel>(viewModelImpl)
{
    private var TAG = RegisterModel::class.java.simpleName
    override fun getContract(): RegisterContract.IRegisterModel {
        return RegisterModelImpl()
    }

    inner class RegisterModelImpl: RegisterContract.IRegisterModel {

        override fun requestGetSmsCode(phoneNumber: String) {
            apiHelper.getSmsCode(SceneType.Register, phoneNumber, object : Callback<Data<String?>>() {
                override fun onSuccess(data: Data<String?>) {
                    super.onSuccess(data)
                    Log.i(TAG, "requestGetSmsCode-onSuccess: ")
                    listener.responseGetSmsCode(UiResponse("success"))
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    Log.i(TAG, "requestGetSmsCode-onFailed: code = $code, msg = $msg")
                    listener.responseGetSmsCode(UiResponse(ErrorBean(code, msg ?: "", msg ?: "")))
                }
            })
        }

        override fun requestRegister(phoneNumber: String, code: String, pwd: String) {
            apiHelper.register(phoneNumber, code, pwd, object : Callback<Data<RegisterBean>>() {
                override fun onSuccess(data: Data<RegisterBean>) {
                    super.onSuccess(data)
                    Log.i(TAG, "requestRegister-onSuccess: data = ${data.data?.toString()}")
                    val registerBean = data.data
                    if (registerBean == null) {
                        listener.responseRegister(
                            UiResponse(
                                ErrorBean(ApiCode.ERROR_HTTP_RESULT_NULL, ApiCode.ERROR_HTTP_RESULT_NULL_STR, ApiCode.ERROR_HTTP_RESULT_NULL_STR)
                            )
                        )
                    } else {
                        listener.responseRegister(UiResponse(registerBean))
                    }
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    Log.i(TAG, "requestRegister-onFailed: code = $code, msg = $msg")
                    listener.responseRegister(
                        UiResponse(
                            ErrorBean(code, msg ?: "", msg ?: "")
                        )
                    )
                }
            })
        }

        override fun requestUpdateSelfDetail(nickName: String, avatar: String) {
            apiHelper.updateSelfDetail(nickName, avatar, object : Callback<Data<SelfDetailBean>>() {
                override fun onSuccess(data: Data<SelfDetailBean>) {
                    super.onSuccess(data)
                    Log.i(TAG, "requestUpdateSelfDetail-onSuccess: data = ${data.data?.toString()}")
                    val selfDetailBean = data.data
                    if (selfDetailBean == null) {
                        listener.responseUpdateSelfDetail(
                            UiResponse(
                                ErrorBean(ApiCode.ERROR_HTTP_RESULT_NULL, ApiCode.ERROR_HTTP_RESULT_NULL_STR, ApiCode.ERROR_HTTP_RESULT_NULL_STR)
                            )
                        )
                    } else {
                        listener.responseUpdateSelfDetail(UiResponse(selfDetailBean))
                    }
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    listener.responseUpdateSelfDetail(
                        UiResponse(ErrorBean(code, msg ?: "", msg ?:""))
                    )
                }
            })
        }
    }
}