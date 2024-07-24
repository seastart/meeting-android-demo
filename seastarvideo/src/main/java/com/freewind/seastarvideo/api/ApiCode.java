package com.freewind.seastarvideo.api;

/**
 * author superK
 * update_at 2024/2/1
 * description 错误码
 */
public interface ApiCode {
      /**
       * 成功
       */
      int SUCCESS = 0;
      /**
       * 未指定通用错误码
       */
      int ERROR = 100300;
      String ERROR_STR = "未指定通用错误码";
      /**
       * 数据库错误、异常
       */
      int ERROR_SQL = 100301;
      /**
       * 数据记录未找到
       */
      int ERROR_DATA_NOT_FOUND = 100302;
      /**
       * 数据记录已存在
       */
      int ERROR_DATA_EXIST = 100303;
      /**
       * 无权限
       */
      int ERROR_NO_PERMISSION = 100304;
      /**
       * 未登录
       */
      int ERROR_NOT_LOGIN = 100305;
      /**
       * token已过期
       */
      int ERROR_TOKEN_EXPIRE = 100306;
      /**
       * token无效
       */
      int ERROR_TOKEN_INVALID = 100307;
      String ERROR_TOKEN_INVALID_STR = "token无效";
      /**
       * 网络错误、异常
       */
      int ERROR_NET = 100308;
      /**
       * 请求超时
       */
      int ERROR_REQUEST_TIMEOUT = 100309;
      /**
       * 请求参数不合法
       */
      int ERROR_PARAM_ILLEGAL = 100310;
      String ERROR_PARAM_ILLEGAL_STR = "请求参数不合法";
}
