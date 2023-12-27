/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.webPage

import com.freewind.seastarvideo.base.BaseModel

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class WebModel(viewModelImpl: WebViewModel.WebViewModelImpl):
    BaseModel<WebContract.IWebViewModel, WebContract.IWebModel>
        (viewModelImpl) {

    override fun getContract(): WebContract.IWebModel {
        return WebModelImpl()
    }

    inner class WebModelImpl: WebContract.IWebModel {

    }
}