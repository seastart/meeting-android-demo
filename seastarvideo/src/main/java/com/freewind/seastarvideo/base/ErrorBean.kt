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
data class ErrorBean(var mErrorType: Int, var mErrorCode: Int,
                     var mErrorMessage: String, var mShowMessage: String) {

    companion object {
        // 错误类型
        const val ERROR_TYPE_DATA = 1 // 数据错误
        const val ERROR_TYPE_NET = 2    // 网络错误
        const val ERROR_TYPE_DATABASE = 3   // 数据库错误
        const val ERROR_TYPE_OTHER = 4  // 其他错误

        // 数据错误
        const val ERROR_TOKEN_FAILED = 10041 // 未登录
        const val ERROR_TOKEN_INVALID = 10042 // token 无效
        const val ERROR_TOKEN_EXPIRED = 10043 // token 过期
        const val ERROR_TOKEN_BANNED = 10044 // token 封禁

        // 网络错误
        const val ERROR_CODE_NET_OTHER = 2001     // Http异常
        const val ERROR_MSG_NET_OTHER = "unknown network error"

        // 数据库错误
        const val ERROR_CODE_DB_EMPTY = 3001 //数据库中没有数据
        const val ERROR_MSG_DB_EMPTT = "no data"

        // 其他错误
        const val ERROR_CODE_OTHER = 5001     // 其他异常
        const val ERROR_MSG_OTHER = "unknown error"
    }
}