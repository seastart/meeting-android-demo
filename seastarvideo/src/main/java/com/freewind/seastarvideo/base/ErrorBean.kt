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
data class ErrorBean(
    var mErrorCode: Int,
    var mErrorMessage: String,
    var mShowMessage: String)