/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 * All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.webPage

import com.freewind.seastarvideo.base.BaseViewModel

/**
 * @author: wiatt
 * @date: 2023/12/27 14:23
 * @description:
 */
class WebViewModel():
    BaseViewModel<WebModel, WebContract.IWebViewModel>() {

    override fun getModel(): WebModel {
        return WebModel(WebViewModelImpl())
    }

    inner class WebViewModelImpl: WebContract.IWebViewModel {

    }
}