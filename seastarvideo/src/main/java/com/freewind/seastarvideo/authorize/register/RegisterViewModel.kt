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
import com.freewind.seastarvideo.base.BaseViewModel

/**
 * @author: wiatt
 * @date: 2023/12/21 16:38
 * @description:
 */
class RegisterViewModel(): BaseViewModel<RegisterModel, RegisterContract.IRegisterViewModel>() {

    override fun getModel(): RegisterModel {
        return RegisterModel(RegisterViewModelImpl())
    }

    inner class RegisterViewModelImpl: RegisterContract.IRegisterViewModel {

        override fun responseLoginWithCode(uiResponse: UiResponse<String>) {
            TODO("Not yet implemented")
        }
    }
}