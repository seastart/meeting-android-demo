package com.freewind.seastarvideo.http

import com.freewind.seastarvideo.http.bean.BaseBean
import com.freewind.seastarvideo.http.bean.Data
import com.freewind.seastarvideo.http.bean.LoginBean
import com.freewind.seastarvideo.http.bean.RegisterBean
import com.freewind.seastarvideo.http.bean.SelfDetailBean
import com.freewind.seastarvideo.enumerate.DocumentType
import com.freewind.seastarvideo.enumerate.SceneType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

class ApiHelper private constructor() {
    var apiEngine: ApiEngine? = null
    var apiService: ApiService? = null

    companion object {
        const val TAG: String = "ApiHelper"
        internal val instance = ApiHelperHolder.holder
    }

    private object ApiHelperHolder {
        val holder = ApiHelper()
    }

    fun init(baseUrl: String) {
        if (apiEngine == null || apiService == null) {
            apiEngine = ApiEngine()
            apiEngine!!.init(baseUrl)
            apiService = apiEngine!!.create(ApiService::class.java)
        }
    }

    fun release() {
        if (apiEngine != null) {
            apiEngine!!.release()
            apiEngine = null
            apiService = null
        }
    }

    private fun <T : BaseBean> isInit(callback: Callback<T>?): Boolean {
        if (apiEngine == null || apiService == null) {
            callback?.onFailed(
                ApiCode.ERROR,
                ApiCode.ERROR_STR
            )
            return false
        }
        return true
    }

    private fun <T : BaseBean> paramsError(callback: Callback<T>?) {
        callback?.onFailed(
            ApiCode.ERROR_PARAM_ILLEGAL,
            ApiCode.ERROR_PARAM_ILLEGAL_STR
        )
    }

    /**
     * 获取文档详情
     * @param type 文档类型
     * @param callback 回调
     * @return
     */
    fun getDocument(type: DocumentType, callback: Callback<Data<String>>?) {
        if (isInit(callback)) {
            apiEngine!!.enqueue(apiService!!.getDocument(type.value), callback)
        }
    }

    /**
     * 获取短信验证码
     * @param type 场景类型
     * @param mobile 手机号码
     * @param callback 回调
     * @return
     */
    fun getSmsCode(
        type: SceneType, mobile: String,
        callback: Callback<Data<String?>>?
    ) {
        if (isInit<Data<String?>>(callback)) {
            val requestBody = putParams<Data<String?>>(callback) { params ->
                try {
                    params.put("scene", type.value)
                    params.put("mobile", mobile)
                } catch (e: JSONException) {
                    return@putParams false
                }
                true
            }
            requestBody?.let {
                apiEngine!!.enqueue(apiService!!.getSmsCode(it), callback)
            }
        }
    }

    /**
     * 用户注册
     * @param mobile  手机号
     * @param captcha  短信验证码
     * @param password  密码
     * @param callback  回调
     */
    fun register(
        mobile: String, captcha: String, password: String,
        callback: Callback<Data<RegisterBean>>?
    ) {
        if (isInit<Data<RegisterBean>>(callback)) {
            val requestBody = putParams<Data<RegisterBean>>(callback) { params: JSONObject ->
                    try {
                        params.put("mobile", mobile)
                        params.put("captcha", captcha)
                        params.put("password", password)
                    } catch (e: JSONException) {
                        return@putParams false
                    }
                    true
                }
            requestBody?.let {
                apiEngine!!.enqueue(apiService!!.register(it), callback)
            }
        }
    }

    /**
     * 手机号验证码登录
     * @param mobile  手机号
     * @param captcha  短信验证码
     * @param callback  回调
     */
    fun loginByMobile(
        mobile: String, captcha: String,
        callback: Callback<Data<LoginBean>>?
    ) {
        if (isInit<Data<LoginBean>>(callback)) {
            val requestBody = putParams<Data<LoginBean>>(callback) { params: JSONObject ->
                try {
                    params.put("mobile", mobile)
                    params.put("captcha", captcha)
                } catch (e: JSONException) {
                    return@putParams false
                }
                true
            }
            requestBody?.let {
                apiEngine!!.enqueue(apiService!!.loginByMobile(it), callback)
            }
        }
    }

    /**
     * 账号密码登录
     * @param account
     * @param password
     * @param callback
     */
    fun loginByAccount(
        account: String, password: String,
        callback: Callback<Data<LoginBean>>?
    ) {
        if (isInit<Data<LoginBean>>(callback)) {
            val requestBody = putParams<Data<LoginBean>>(callback) { params: JSONObject ->
                try {
                    params.put("account", account)
                    params.put("password", password)
                } catch (e: JSONException) {
                    return@putParams false
                }
                true
            }
            requestBody?.let {
                apiEngine!!.enqueue(
                    apiService!!.loginByAccount(it), callback
                )
            }
        }
    }

    /**
     * 获取自身详情
     * @param callback  回调
     */
    fun getSelfDetail(callback: Callback<Data<SelfDetailBean>>?) {
        if (isInit(callback)) {
            apiEngine!!.enqueue(apiService!!.getSelfDetail(), callback)
        }
    }

    /**
     * 更新自身详情
     * @param nickName  昵称
     * @param avatar  头像
     * @param callback  回调
     */
    fun updateSelfDetail(
        nickName: String, avatar: String,
        callback: Callback<Data<SelfDetailBean>>?
    ) {
        if (isInit<Data<SelfDetailBean>>(callback)) {
            val requestBody = putParams<Data<SelfDetailBean>>(callback) { params: JSONObject ->
                    try {
                        params.put("nickName", nickName)
                        params.put("avatar", avatar)
                    } catch (e: JSONException) {
                        return@putParams false
                    }
                    true
                }
            requestBody?.let {
                apiEngine!!.enqueue(apiService!!.updateSelfDetail(it), callback)
            }
        }
    }

    /**
     * 用户举报
     * @param categorys  举报类型
     * @param description  描述
     * @param callback  回调
     */
    fun reportViolation(
        categorys: ArrayList<String?>, description: String,
        callback: Callback<Data<String>>?
    ) {
        if (isInit<Data<String>>(callback)) {
            val requestBody = putParams<Data<String>>(callback) { params: JSONObject ->
                try {
                    params.put("categorys", categorys)
                    params.put("description", description)
                } catch (e: JSONException) {
                    return@putParams false
                }
                true
            }
            requestBody?.let {
                apiEngine!!.enqueue(apiService!!.reportViolation(it), callback)
            }
        }
    }

    /**
     * meet 授权
     * @param callback
     */
    fun meetingGrant(callback: Callback<Data<String>>?) {
        if (isInit(callback)) {
            apiEngine!!.enqueue(apiService!!.meetingGrant(), callback)
        }
    }

    private fun interface ParamSetCallback {
        fun onPutParams(params: JSONObject): Boolean
    }

    private fun <T : BaseBean> putParams(
        callback: Callback<T>?, paramSetCallback: ParamSetCallback
    ): RequestBody? {
        val params = JSONObject()
        if (!paramSetCallback.onPutParams(params)) {
            paramsError(callback)
        }
        val json = params.toString()
        if (json.isEmpty()) {
            return null
        } else {
            return json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        }
    }
}
