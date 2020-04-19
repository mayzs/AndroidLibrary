package com.cctbn.baselibrary.common.network.apiservice;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface APIService {

    @GET()
    Observable<String> get(@HeaderMap Map<String, String> hearder, @Url String url, @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Observable<String> post(@HeaderMap Map<String, String> hearder, @Url String url, @FieldMap Map<String, String> map);

    @POST()
    Observable<String> postJson(@HeaderMap Map<String, String> hearder, @Url String url, @Body RequestBody requestBody);

    @Multipart
    @POST()
    Observable<String> postFile(@HeaderMap Map<String, String> hearder, @Url String url,@Part() List<MultipartBody.Part> parts);

    @DELETE()
    Observable<String> delete(@HeaderMap Map<String, String> hearder, @Url String url, @QueryMap Map<String, String> params);

    @PUT()
    Observable<String> put(@HeaderMap Map<String, String> hearder, @Url String url, @QueryMap Map<String, String> params);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
