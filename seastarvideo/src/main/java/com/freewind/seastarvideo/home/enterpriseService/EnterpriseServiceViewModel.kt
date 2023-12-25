/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.enterpriseService

import com.freewind.seastarvideo.base.BaseViewModel

/**
 * @author: wiatt
 * @date: 2023/12/25 21:18
 * @description:
 */
class EnterpriseServiceViewModel():
    BaseViewModel<EnterpriseServiceModel, EnterpriseServiceContract.IEnterpriseServiceViewModel>() {

    override fun getModel(): EnterpriseServiceModel {
        return EnterpriseServiceModel(EnterprseServiceViewModelImpl())
    }

    inner class EnterprseServiceViewModelImpl: EnterpriseServiceContract.IEnterpriseServiceViewModel {

    }
}