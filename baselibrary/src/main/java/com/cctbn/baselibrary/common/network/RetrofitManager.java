/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.cctbn.baselibrary.common.network;

import android.content.Context;
import android.util.SparseArray;

import com.cctbn.baselibrary.BaseApplication;
import com.cctbn.baselibrary.common.network.apiservice.APIService;
import com.cctbn.baselibrary.common.network.converter.FastJsonConverterFactory;
import com.cctbn.baselibrary.common.network.https.HttpsUtils;
import com.cctbn.baselibrary.common.utils.NetWorkUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * @author 咖枯
 * @version 1.0 2016/5/26
 */
public class RetrofitManager {
    //    public static String BASEURL = "https://carapi.anmirror.cn";
    public String BASEURL = "";
    private Retrofit mRetrofit;

    private APIService mAPIService;

    public APIService getAPIService() {
        return mRetrofit.create(APIService.class);
    }

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    private static volatile OkHttpClient sOkHttpClient;

    private static SparseArray<RetrofitManager> sRetrofitManager = new SparseArray<>();

    private RetrofitManager() {

    }


    public static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
//                Cache cache = new Cache(new File(BaseApplication.getContext().getCacheDir(), "HttpCache"),
//                        1024 * 1024 * 100);
                if (sOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                            .cache(cache)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS);
                    //.addInterceptor(mRewriteCacheControlInterceptor);
                    //.addNetworkInterceptor(mRewriteCacheControlInterceptor)
                    //.addInterceptor(mLoggingInterceptor);
                    HttpsUtils.SSLParams sslParams = RetrofitUtils.getInstance().getSslParams();
                    if (sslParams != null) {
                        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
                    }
                    List<Interceptor> interceptorsList = RetrofitUtils.getInstance().getInterceptorsList();
                    if (interceptorsList != null && interceptorsList.size() > 0) {
                        for (Interceptor interceptor : interceptorsList) {
                            builder.addInterceptor(interceptor);
                        }
                    }
                    Interceptor networkInterceptor = RetrofitUtils.getInstance().getNetworkInterceptor();
                    if (networkInterceptor != null) {
                        builder.addNetworkInterceptor(networkInterceptor);
                    }
                    sOkHttpClient = builder.build();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略以及头部细腻
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if (!NetWorkUtil.isNetWorkConnectted(BaseApplication.getContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetWorkConnectted(BaseApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private static final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
//            LogUtil.i(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
//            LogUtil.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    };


    public static RetrofitManager getInstance() {
        RetrofitManager retrofitManager = sRetrofitManager.get(1);
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
            sRetrofitManager.put(1, retrofitManager);
            return retrofitManager;
        }
        return retrofitManager;
    }

    public RetrofitManager setUrl(String url) {
        this.BASEURL = url;
        mRetrofit = new Retrofit.Builder().baseUrl(BASEURL)
                .client(getOkHttpClient())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return this;
    }

    /**
     * 根据网络状况获取缓存的策略
     */
    public String getCacheControl(Context context) {
        return NetWorkUtil.isNetWorkConnectted(context) ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }

}
