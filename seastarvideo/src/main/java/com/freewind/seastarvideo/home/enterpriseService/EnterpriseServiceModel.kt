/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.enterpriseService

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/25 21:17
 * @description:
 */
class EnterpriseServiceModel(viewModelImpl: EnterpriseServiceViewModel.EnterprseServiceViewModelImpl):
    BaseModel<
            EnterpriseServiceContract.IEnterpriseServiceViewModel,
            EnterpriseServiceContract.IEnterpriseServiceModel>(viewModelImpl)  {

    override fun getContract(): EnterpriseServiceContract.IEnterpriseServiceModel {
        return EnterpriseServiceModelImpl()
    }

    inner class EnterpriseServiceModelImpl: EnterpriseServiceContract.IEnterpriseServiceModel {

    }
}