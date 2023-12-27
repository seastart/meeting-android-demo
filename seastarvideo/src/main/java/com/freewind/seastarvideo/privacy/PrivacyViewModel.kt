/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.privacy

import com.freewind.seastarvideo.base.BaseViewModel

/**
 * @author: wiatt
 * @date: 2023/12/26 18:04
 * @description:
 */
class PrivacyViewModel():
    BaseViewModel<PrivacyModel, PrivacyContract.IPrivacyViewModel>() {

    override fun getModel(): PrivacyModel {
        return PrivacyModel(PrivacyViewModelImpl())
    }

    inner class PrivacyViewModelImpl: PrivacyContract.IPrivacyViewModel {

    }
}