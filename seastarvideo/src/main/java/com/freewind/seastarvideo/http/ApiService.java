package com.freewind.seastarvideo.http;

import com.freewind.seastarvideo.http.bean.Data;
import com.freewind.seastarvideo.http.bean.LoginBean;
import com.freewind.seastarvideo.http.bean.RegisterBean;
import com.freewind.seastarvideo.http.bean.SelfDetailBean;

import okhttp3.RequestBody;
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
    @GET(ConstantHttp.HTTP_GET_DOCUMENT)
    Call<Data<String>> getDocument(@Query("key") String type);

    /**
     * 获取短信验证码
     * @param requestBody
     * @return
     */
    @POST(ConstantHttp.HTTP_GET_SMS_CODE)
    Call<Data<String>> getSmsCode(@Body RequestBody requestBody);

    /**
     * 用户注册
     * @param requestBody
     * @return
     */
    @POST(ConstantHttp.HTTP_REGISTER)
    Call<Data<RegisterBean>> register(@Body RequestBody requestBody);

    /**
     * 手机验证码登录
     * @param requestBody
     * @return
     */
    @POST(ConstantHttp.HTTP_LOGIN_BY_MOBILE)
    Call<Data<LoginBean>> loginByMobile(@Body RequestBody requestBody);

    /**
     * 账号密码登录
     * @param requestBody
     * @return
     */
    @POST(ConstantHttp.HTTP_LOGIN_BY_ACCOUNT)
    Call<Data<LoginBean>> loginByAccount(@Body RequestBody requestBody);

    /**
     * 获取自身用户详情
     * @return
     */
    @GET(ConstantHttp.HTTP_GET_SELF_DETAIL)
    Call<Data<SelfDetailBean>> getSelfDetail();

    /**
     * 更新自身详情
     * @return
     */
    @POST(ConstantHttp.HTTP_UPDATE_SELF_DETAIL)
    Call<Data<SelfDetailBean>> updateSelfDetail(@Body RequestBody requestBody);

    /**
     * 用户举报
     * @param requestBody
     * @return
     */
    @POST(ConstantHttp.HTTP_REPORT_VIOLATION)
    Call<Data<String>> reportViolation(@Body RequestBody requestBody);

    /**
     * meet 授权
     * @return
     */
    @GET(ConstantHttp.HTTP_MEETING_GRANT)
    Call<Data<String>> meetingGrant();
}
