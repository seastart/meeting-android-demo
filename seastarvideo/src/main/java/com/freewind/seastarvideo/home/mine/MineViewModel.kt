/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.home.mine

import com.freewind.seastarvideo.base.BaseViewModel

/**
 * @author: wiatt
 * @date: 2023/12/25 21:31
 * @description:
 */
class MineViewModel(): BaseViewModel<MineModel, MineContract.IMineViewModel>() {

    override fun getModel(): MineModel {
        return MineModel(MineViewModelImpl())
    }

    inner class MineViewModelImpl: MineContract.IMineViewModel {

    }
}