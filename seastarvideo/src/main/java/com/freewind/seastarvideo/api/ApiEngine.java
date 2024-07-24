package com.freewind.seastarvideo.api;

import androidx.annotation.NonNull;
import com.freewind.seastarvideo.BuildConfig;
import com.freewind.seastarvideo.bean.BaseBean;
import com.google.gson.Gson;
import com.shiyuan.meeting.api.HttpLogCollectorInterceptor;
import com.shiyuan.meeting.util.EncryptUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author superK
 * update_at 2024/1/29
 * description 网络请求引擎
 */
public class ApiEngine {

    public static final String BaseUrl = "http://localv2.srtc.live:8089/api/";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private int connectTimeout = 30,readTimeout = 30,writeTimeout = 30;
    private boolean reconnect = true;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
    Gson gson = new Gson();



    /**
     * 初始化引擎
     * @param baseUrl 请求前缀
     * @param appId 应用 id
     * @param appKey 应用 key
     */
    public void init(String baseUrl, String appId, String appKey){
        if (baseUrl == null){
            return;
        }
        if (!baseUrl.endsWith("/")){
            baseUrl = baseUrl + "/";
        }
        release();
        if (okHttpClient == null){
            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)//设置连接超时
                    .readTimeout(readTimeout, TimeUnit.SECONDS)//设置读超时
                    .writeTimeout(writeTimeout,TimeUnit.SECONDS)//设置写超时
                    .retryOnConnectionFailure(reconnect);//是否自动重连
            okhttpBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                StringBuilder stringBuilder = new StringBuilder();

                String nonce = UUID.randomUUID().toString().replace("-", "").substring(0,16);
                long timestamp = System.currentTimeMillis()/1000;
                String signature = EncryptUtil.hmacSHA256Encrypt(stringBuilder.toString(), appKey);
                List<String> names = new ArrayList<>();
                names.add("app_id=" + appId);
                names.add("nonce=" + nonce);
                names.add("timestamp=" + timestamp);
                names.add("signature=" + signature);
                if (METHOD_GET.equals(original.method())) {
                    Set<String> paramKeys = original.url().queryParameterNames();
                    for (String key : paramKeys) {
                        names.add(key + "=" + original.url().queryParameter(key));
                    }
                }
                Collections.sort(names, String::compareTo);
                for (String name : names) {
                    stringBuilder.append(name).append("&");
                }

                String json = "";
                if (METHOD_GET.equals(original.method())) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }else if (METHOD_POST.equals(original.method())){
                    RequestBody body = original.body();
                    if (body != null){
                        json = getParamContent(body);
                        stringBuilder.append(json);
                    }
                }

                Request request = original.newBuilder()
                        .header("app_id", appId)
                        .header("nonce", nonce)
                        .header("timestamp", timestamp+"")
                        .header("signature", signature)
//                        .method(original.method(), original.body())
                        .post(RequestBody.create(json, MediaType.get("application/json")))
                        .build();

                return chain.proceed(request);
            });
            okhttpBuilder.addInterceptor(new HttpLogCollectorInterceptor());
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                okhttpBuilder.addInterceptor(logging);
            }
            okHttpClient = okhttpBuilder.build();
        }
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    /**
     * 获取常规post请求参数
     */
    private String getParamContent(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        String json = buffer.readUtf8();
        json = json.replace("\"{", "{");
        json = json.replace("}\"", "}");
        return json.replace("\\\"", "\"");
    }

    public <T> T create(final Class<T> service){
        if (retrofit != null){
            return retrofit.create(service);
        }
        return null;
    }

    /**
     * 异步请求队列
     * @param call  请求
     * @param apiCallback 回调
     * @param <T> 类型
     */
    public <T extends BaseBean> void enqueue(Call<T> call, Callback<T> apiCallback){
        if (apiCallback != null){
            apiCallback.onStart();
        }
        call.enqueue(new retrofit2.Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                T t = response.body();
                if (t != null){
                    if (t.getCode() == ApiCode.SUCCESS){
                        if (apiCallback != null){
                            apiCallback.onSuccess(t);
                        }
                    }else {
                        if (apiCallback != null){
                            apiCallback.onFailed(t.getCode(), t.getMsg());
                        }
                    }
                }else {
                    if (apiCallback != null){
                        apiCallback.onFailed(ApiCode.ERROR, "body为空");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
                if (apiCallback != null){
                    if (call.isCanceled()){
                        apiCallback.onCancel();
                        return;
                    }
                    apiCallback.onFailed(ApiCode.ERROR, throwable.getMessage());
                }
            }
        });
    }

    /**
     * 取消队列中的请求和进行中的请求
     * @param requestKey    key
     */
    public void cancelCallWithKey(String requestKey){
        if (okHttpClient == null){
            return;
        }
        if (requestKey == null || requestKey.isEmpty()){
            return;
        }
        //When you want to cancel:
        //A) go through the queued calls and cancel if the tag matches:
        for (okhttp3.Call call : okHttpClient.dispatcher().queuedCalls()) {
            String tag = (String) call.request().tag();
            if (tag != null && tag.startsWith(requestKey)){
                call.cancel();
                break;
            }
        }

        //B) go through the running calls and cancel if the tag matches:
        for (okhttp3.Call call : okHttpClient.dispatcher().runningCalls()) {
            String tag = (String) call.request().tag();
            if (tag != null && tag.startsWith(requestKey)){
                call.cancel();
                break;
            }
        }
    }

    /**
     * 取消队列中的请求和进行中的请求
     */
    public void cancelCallForAll(){
        if (okHttpClient == null){
            return;
        }
        //When you want to cancel:
        //A) go through the queued calls and cancel if the tag matches:
        for (okhttp3.Call call : okHttpClient.dispatcher().queuedCalls()) {
            call.cancel();
        }

        //B) go through the running calls and cancel if the tag matches:
        for (okhttp3.Call call : okHttpClient.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        cancelCallForAll();
        if (okHttpClient != null){
//            okHttpClient.interceptors().clear();//清除拦截器
            okHttpClient.dispatcher().executorService().shutdown();   //清除并关闭线程池
            okHttpClient.connectionPool().evictAll();                 //清除并关闭连接池
            Cache cache = okHttpClient.cache();
            try {
                if (cache != null){
                    cache.close();                             //清除cache
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        retrofit = null;
        okHttpClient = null;
    }

}
