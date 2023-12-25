/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.mine

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/25 21:29
 * @description:
 */
class MineModel(viewModelImpl: MineViewModel.MineViewModelImpl):
    BaseModel<MineContract.IMineViewModel, MineContract.IMineModel>(viewModelImpl) {

    override fun getContract(): MineContract.IMineModel {
        return MineModelImpl()
    }

    inner class MineModelImpl: MineContract.IMineModel {

    }
}