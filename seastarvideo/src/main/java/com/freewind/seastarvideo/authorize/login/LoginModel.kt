package com.freewind.seastarvideo.authorize.login

import android.util.Log
import com.freewind.seastarvideo.base.BaseModel
import com.freewind.seastarvideo.base.ErrorBean
import com.freewind.seastarvideo.base.UiResponse
import com.freewind.seastarvideo.enumerate.SceneType
import com.freewind.seastarvideo.http.ApiCode
import com.freewind.seastarvideo.http.Callback
import com.freewind.seastarvideo.http.bean.Data
import com.freewind.seastarvideo.http.bean.LoginBean

/**
 * @author: wiatt
 * @date: 2023/12/20 10:22
 * @description:
 */
class LoginModel(viewModelImpl: LoginViewModel.LoginViewModelImpl):
    BaseModel<LoginContract.ILoginViewModel, LoginContract.ILoginModel>(viewModelImpl)
{
    private var TAG = LoginModel::class.java.simpleName
    override fun getContract(): LoginContract.ILoginModel {
        return LoginModelImpl()
    }

    inner class LoginModelImpl: LoginContract.ILoginModel {
        override fun requestLoginWithPwd(account: String, pwd: String) {
            apiHelper.loginByAccount(account, pwd, object : Callback<Data<LoginBean>>() {
                override fun onSuccess(data: Data<LoginBean>) {
                    super.onSuccess(data)
                    val loginBean = data.data
                    if (loginBean == null) {
                        listener.responseLoginWithPwd(
                            UiResponse(
                                ErrorBean(ApiCode.ERROR_HTTP_RESULT_NULL, ApiCode.ERROR_HTTP_RESULT_NULL_STR, ApiCode.ERROR_HTTP_RESULT_NULL_STR)
                            )
                        )
                    } else {
                        listener.responseLoginWithPwd(UiResponse(loginBean))
                    }
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    listener.responseLoginWithPwd(
                        UiResponse(ErrorBean(code, msg ?: "", msg ?: ""))
                    )
                }
            })
        }

        override fun requestGetSmsCode(phoneNumber: String) {
            apiHelper.getSmsCode(SceneType.LoginByMobile, phoneNumber, object : Callback<Data<String?>>() {
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

        override fun requestLoginWithCode(phoneNumber: String, code: String) {
            apiHelper.loginByMobile(phoneNumber, code, object : Callback<Data<LoginBean>>() {
                override fun onSuccess(data: Data<LoginBean>) {
                    super.onSuccess(data)
                    val loginBean = data.data
                    if (loginBean == null) {
                        listener.responseLoginWithCode(
                            UiResponse(
                                ErrorBean(ApiCode.ERROR_HTTP_RESULT_NULL, ApiCode.ERROR_HTTP_RESULT_NULL_STR, ApiCode.ERROR_HTTP_RESULT_NULL_STR)
                            )
                        )
                    } else {
                        listener.responseLoginWithCode(UiResponse(loginBean))
                    }
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    listener.responseLoginWithCode(
                        UiResponse(ErrorBean(code, msg ?: "", msg ?: ""))
                    )
                }
            })
        }

        override fun requestMeetingGrant() {
            apiHelper.meetingGrant(object :Callback<Data<String>>() {
                override fun onSuccess(data: Data<String>) {
                    super.onSuccess(data)
                    val token = data.data
                    if (token.isNullOrEmpty()) {
                        UiResponse<String>(
                            ErrorBean(ApiCode.ERROR_HTTP_RESULT_NULL, ApiCode.ERROR_HTTP_RESULT_NULL_STR, ApiCode.ERROR_HTTP_RESULT_NULL_STR)
                        )
                    } else {
                        listener.responseMeetingGrant(UiResponse(token))
                    }
                }

                override fun onFailed(code: Int, msg: String?) {
                    super.onFailed(code, msg)
                    listener.responseMeetingGrant(
                        UiResponse(
                            ErrorBean(code, msg ?: "", msg ?: "")
                        )
                    )
                }
            })
        }
    }
}