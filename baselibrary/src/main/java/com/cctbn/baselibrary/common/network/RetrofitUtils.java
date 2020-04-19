package com.cctbn.baselibrary.common.network;

import com.cctbn.baselibrary.common.network.builder.DeleteBuilder;
import com.cctbn.baselibrary.common.network.builder.GetBuilder;
import com.cctbn.baselibrary.common.network.builder.PostBuilder;
import com.cctbn.baselibrary.common.network.builder.PutBuilder;
import com.cctbn.baselibrary.common.network.https.HttpsUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;

/**
 * @createDate: 2019/1/4
 * @author: mayz
 * @version: 1.0
 */
public class RetrofitUtils {

    private HttpsUtils.SSLParams sslParams;
    private List<Interceptor> interceptorsList=new ArrayList<>();
    private Interceptor networkInterceptor;
    private RetrofitUtils() {
    }

    private static volatile RetrofitUtils instance = null;

    public static RetrofitUtils getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }


    public GetBuilder get() {
        return new GetBuilder(Params.GET);
    }

    public PostBuilder post() {
        return new PostBuilder(Params.POST);
    }

    /**
     * 上传多个文件
     *
     * @param fileMethodName
     * @param files
     * @return
     */
    public PostBuilder post(String fileMethodName, List<File> files) {

        return new PostBuilder(Params.POST_FILE, fileMethodName, files);
    }

    /**
     * 上传单个文件
     *
     * @param fileMethodName
     * @param file
     * @return
     */
    public PostBuilder post(String fileMethodName, File file) {

        return new PostBuilder(Params.POST_FILE, fileMethodName, file);
    }

    /**
     * 传json
     *
     * @param jsonObject
     * @return
     */
    public PostBuilder post(JSONObject jsonObject) {
        return new PostBuilder(Params.POST_JSON, jsonObject);
    }

    /**
     * 传json
     *
     * @param jsonParams
     * @return
     */
    public PostBuilder post(Map<String, String> jsonParams) {
        return new PostBuilder(Params.POST_JSON, jsonParams);
    }

    public DeleteBuilder delete() {
        return new DeleteBuilder(Params.DELETE);
    }

    public PutBuilder put() {
        return new PutBuilder(Params.PUT);
    }

    public RetrofitUtils setSslParams(HttpsUtils.SSLParams sslParams) {
        this.sslParams = sslParams;
        return this;
    }

    public RetrofitUtils addInterceptor(Interceptor interceptor){
        if (interceptor!=null){
            interceptorsList.add(interceptor);
        }
        return this;
    }

    public RetrofitUtils addNetworkInterceptor(Interceptor interceptor){
        this.networkInterceptor = interceptor;
        return this;
    }

    public HttpsUtils.SSLParams getSslParams() {
        if (sslParams != null) {
            return sslParams;
        } else {
            return HttpsUtils.getSslSocketFactory();
        }
    }

    public List<Interceptor> getInterceptorsList(){
        return interceptorsList;
    }
    public Interceptor getNetworkInterceptor(){
        return networkInterceptor!=null?networkInterceptor:null;
    }
}
