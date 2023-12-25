/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.base

/**
 * @author wiatt
 * @description:
 */
class UiResponse<T> {
    var mError: ErrorBean? = null
    var mResult: T? = null
    var isSuccess: Boolean = false
        private set

    constructor(error: ErrorBean) {
        mError = error
        isSuccess = false
    }

    constructor(result: T) {
        mResult = result
        isSuccess = true
    }
}