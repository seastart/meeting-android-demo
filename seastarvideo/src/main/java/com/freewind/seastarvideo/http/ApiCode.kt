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
     * 数据库错误、异常
     */
    const val ERROR_SQL = 100301

    /**
     * 数据记录未找到
     */
    const val ERROR_DATA_NOT_FOUND = 100302

    /**
     * 数据记录已存在
     */
    const val ERROR_DATA_EXIST = 100303

    /**
     * 无权限
     */
    const val ERROR_NO_PERMISSION = 100304

    /**
     * 未登录
     */
    const val ERROR_NOT_LOGIN = 100305

    /**
     * token已过期
     */
    const val ERROR_TOKEN_EXPIRE = 100306

    /**
     * token无效
     */
    const val ERROR_TOKEN_INVALID = 100307
    const val ERROR_TOKEN_INVALID_STR = "token无效"

    /**
     * 网络错误、异常
     */
    const val ERROR_NET = 100308

    /**
     * 请求超时
     */
    const val ERROR_REQUEST_TIMEOUT = 100309

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
