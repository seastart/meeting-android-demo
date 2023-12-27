/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.aboutUs

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class AboutUsModel(viewModelImpl: AboutUsViewModel.AboutUsViewModelImpl):
    BaseModel<AboutUsContract.IAboutUsViewModel, AboutUsContract.IAboutUsModel>
        (viewModelImpl) {

    override fun getContract(): AboutUsContract.IAboutUsModel {
        return AboutUsModelImpl()
    }

    inner class AboutUsModelImpl: AboutUsContract.IAboutUsModel {

    }
}