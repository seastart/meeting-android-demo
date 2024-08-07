package com.freewind.seastarvideo.http

/**
 * author superK
 * update_at 2024/2/1
 * description 错误码
 */
object ApiCode {

    /**
     * 成功
     */
    const val SUCCESS = 0

    /**
     * 未指定通用错误码
     */
    const val ERROR = 100300
    const val ERROR_STR = "未指定通用错误码"

    /**
     * 未登录
     */
    const val ERROR_AUTH_FAILED = 10041
    const val ERROR_AUTH_FAILED_STR = "未登录"

    /**
     * token 失效
     */
    const val ERROR_TOKEN_INVALID = 10042
    const val ERROR_TOKEN_INVALID_STR = "token 失效"

    /**
     * token 已过期
     */
    const val ERROR_TOKEN_TOKEN_EXPIRED = 10043
    const val ERROR_TOKEN_TOKEN_EXPIRED_STR = "token"

    /**
     * 请求参数不合法
     */
    const val ERROR_PARAM_ILLEGAL = 100310
    const val ERROR_PARAM_ILLEGAL_STR = "请求参数不合法"

    /**
     * http返回结果为空
     */
    const val ERROR_HTTP_RESULT_NULL = 100310
    const val ERROR_HTTP_RESULT_NULL_STR = "http返回数据为空"
}
