package com.freewind.seastarvideo.api;

import com.freewind.seastarvideo.bean.Data;
import com.freewind.seastarvideo.bean.LoginBean;
import com.freewind.seastarvideo.bean.RegisterBean;
import com.freewind.seastarvideo.bean.SelfDetailBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * author superK
 * update_at 2024/2/1
 * description api接口
 */
public interface ApiService {

    /**
     * 获取文档详情
     * @param type
     * @return
     */
    @GET("v1/document/get-detail?key=user-agreement")
    Call<Data<String>> getDocument(@Query("key") String type);

    /**
     * 获取短信验证码
     * @param json
     * @return
     */
    @POST("v1/auth/sms-code")
    Call<Data<String>> getSmsCode(@Body String json);

    /**
     * 用户注册
     * @param json
     * @return
     */
    @POST("v1/auth/register")
    Call<Data<RegisterBean>> register(@Body String json);

    /**
     * 手机验证码登录
     * @param json
     * @return
     */
    @POST("v1/auth/login-mobile")
    Call<Data<LoginBean>> loginByMobile(@Body String json);

    /**
     * 账号密码登录
     * @param json
     * @return
     */
    @POST("v1/auth/login-account")
    Call<Data<LoginBean>> loginByAccount(@Body String json);

    /**
     * 获取自身用户详情
     * @return
     */
    @GET("v1/member/self-detail")
    Call<Data<SelfDetailBean>> getSelfDetail();

    /**
     * 更新自身详情
     * @return
     */
    @POST("v1/member/self-update")
    Call<Data<SelfDetailBean>> updateSelfDetail(@Body String json);

    /**
     * 用户举报
     * @param json
     * @return
     */
    @POST("v1/member/report-violation")
    Call<Data<String>> reportViolation(@Body String json);

    /**
     * meet 授权
     * @return
     */
    @GET("v1/meeting/grant")
    Call<Data<String>> meetingGrant();
}
