package com.freewind.seastarvideo.api;

import com.freewind.seastarvideo.bean.BaseBean;
import com.freewind.seastarvideo.bean.Data;
import com.freewind.seastarvideo.bean.LoginBean;
import com.freewind.seastarvideo.bean.RegisterBean;
import com.freewind.seastarvideo.bean.SelfDetailBean;
import com.freewind.seastarvideo.enumerate.DocumentType;
import com.freewind.seastarvideo.enumerate.SceneType;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ApiHelper {
    private static volatile ApiHelper instance;

    private ApiHelper() {
        apiEngine = new ApiEngine();
    }

    public static ApiHelper getInstance() {
        if (instance == null) {
            synchronized (ApiHelper.class) {
                if (instance == null) {
                    instance = new ApiHelper();
                }
            }
        }
        return instance;
    }

    ApiEngine apiEngine;
    ApiService apiService;

    public void init(String baseUrl, String appId, String appKey) {
        if (apiEngine != null){
            apiEngine.init(baseUrl, appId, appKey);
            apiService = apiEngine.create(ApiService.class);
        }
    }

    public void release() {
        if (apiEngine != null) {
            apiEngine.release();
            apiEngine = null;
            apiService = null;
            instance = null;
        }
    }

    private <T extends BaseBean> boolean isInit(Callback<T> callback) {
        if (apiEngine == null || apiService == null) {
            if (callback != null) {
                callback.onFailed(ApiCode.ERROR, ApiCode.ERROR_STR);
            }
            return false;
        }
        return true;
    }

    private <T extends BaseBean> void paramsError(Callback<T> callback){
        if (callback != null) {
            callback.onFailed(ApiCode.ERROR_PARAM_ILLEGAL, ApiCode.ERROR_PARAM_ILLEGAL_STR);
        }
    }

    /**
     * 获取文档详情
     * @param type 文档类型
     * @param callback 回调
     * @return
     */
    public void getDocument(DocumentType type, Callback<Data<String>> callback) {
        if (isInit(callback)) {
//            String json = putParams(callback, params -> {
//                try {
//                    params.put("user_id", userId);
//                } catch (JSONException e) {
//                    return false;
//                }
//                return true;
//            });
            if (type != null){
                apiEngine.enqueue(apiService.getDocument(type.getValue()), callback);
            }
        }
    }

    /**
     * 获取短信验证码
     * @param type 场景类型
     * @param mobile 手机号码
     * @param callback 回调
     * @return
     */
    public void getSmsCode(SceneType type, String mobile, Callback<Data<String>> callback) {
        if (isInit(callback)) {
            String json = putParams(callback, params -> {
                try {
                    params.put("scene", type.getValue());
                    params.put("mobile", mobile);
                } catch (JSONException e) {
                    return false;
                }
                return true;
            });
            if (!json.isEmpty()){
                apiEngine.enqueue(apiService.getSmsCode(json), callback);
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
    public void register(String mobile, String captcha, String password, Callback<Data<RegisterBean>> callback) {
        if (isInit(callback)) {
            String json = putParams(callback, params -> {
                try {
                    params.put("mobile", mobile);
                    params.put("captcha", captcha);
                    params.put("password", password);
                } catch (JSONException e) {
                    return false;
                }
                return true;
            });
            if (!json.isEmpty()){
                apiEngine.enqueue(apiService.register(json), callback);
            }
        }
    }

    /**
     * 手机号验证码登录
     * @param mobile  手机号
     * @param captcha  短信验证码
     * @param callback  回调
     */
    public void loginByMobile(String mobile, String captcha, Callback<Data<LoginBean>> callback) {
        if (isInit(callback)) {
            String json = putParams(callback, params -> {
                try {
                    params.put("mobile", mobile);
                    params.put("captcha", captcha);
                } catch (JSONException e) {
                    return false;
                }
                return true;
            });
            if (!json.isEmpty()){
                apiEngine.enqueue(apiService.loginByMobile(json), callback);
            }
        }
    }

    /**
     * 账号密码登录
     * @param account
     * @param password
     * @param callback
     */
    public void loginByAccount(String account, String password, Callback<Data<LoginBean>> callback) {
        if (isInit(callback)) {
            String json = putParams(callback, params -> {
                try {
                    params.put("account", account);
                    params.put("password", password);
                } catch (JSONException e) {
                    return false;
                }
                return true;
            });
            if (!json.isEmpty()){
                apiEngine.enqueue(apiService.loginByAccount(json), callback);
            }
        }
    }

    /**
     * 获取自身详情
     * @param callback  回调
     */
    public void getSelfDetail(Callback<Data<SelfDetailBean>> callback) {
        if (isInit(callback)) {
            apiEngine.enqueue(apiService.getSelfDetail(), callback);
        }
    }

    /**
     * 更新自身详情
     * @param nickName  昵称
     * @param avatar  头像
     * @param callback  回调
     */
    public void updateSelfDetail(String nickName, String avatar, Callback<Data<SelfDetailBean>> callback) {
        if (isInit(callback)) {
            String json = putParams(callback, params -> {
                try {
                    params.put("nickName", nickName);
                    params.put("avatar", avatar);
                } catch (JSONException e) {
                    return false;
                }
                return true;
            });
            if (!json.isEmpty()){
                apiEngine.enqueue(apiService.updateSelfDetail(json), callback);
            }
        }
    }

    /**
     * 用户举报
     * @param categorys  举报类型
     * @param description  描述
     * @param callback  回调
     */
    public void reportViolation(ArrayList<String> categorys, String description, Callback<Data<String>> callback) {
        if (isInit(callback)) {
            String json = putParams(callback, params -> {
                try {
                    params.put("categorys", categorys);
                    params.put("description", description);
                } catch (JSONException e) {
                    return false;
                }
                return true;
            });
            if (!json.isEmpty()){
                apiEngine.enqueue(apiService.reportViolation(json), callback);
            }
        }
    }

    /**
     * meet 授权
     * @param callback
     */
    public void meetingGrant(Callback<Data<String>> callback) {
        apiEngine.enqueue(apiService.meetingGrant(), callback);
    }

    private interface ParamSetCallback{
        boolean onPutParams(JSONObject params);
    }

    private <T extends BaseBean> String putParams(Callback<T> callback, ParamSetCallback paramSetCallback){
        JSONObject params = new JSONObject();
        if (!paramSetCallback.onPutParams(params)){
            paramsError(callback);
        }
        return params.toString();
    }
}
