/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.authorize.register

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/21 16:39
 * @description:
 */
class RegisterModel(viewModelImpl: RegisterViewModel.RegisterViewModelImpl):
    BaseModel<RegisterContract.IRegisterViewModel, RegisterContract.IRegisterModel>(viewModelImpl) {

    override fun getContract(): RegisterContract.IRegisterModel {
        return RegisterModelImpl()
    }

    inner class RegisterModelImpl: RegisterContract.IRegisterModel {

        override fun requestRegister(phoneNumber: String, code: String, pwd: String) {
            TODO("Not yet implemented")
        }
    }
}