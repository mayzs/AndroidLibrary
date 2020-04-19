package com.cctbn.baselibrary.common.network.builder;

import android.text.TextUtils;
import android.util.Log;

import com.cctbn.baselibrary.common.network.Params;
import com.cctbn.baselibrary.common.network.apiservice.APIService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @createDate: 2019/1/8
 * @author: mayz
 * @version: 1.0
 */
public class PostBuilder extends BaseBuilder {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Params params;
    private JSONObject jsonObject;
    private Map<String, String> jsonParams;
    private String fileMethodName;
    private File file;
    private List<File> files;

    public PostBuilder(Params params) {
        super(params);
        this.params = params;
    }

    public PostBuilder(Params params, JSONObject jsonObject) {
        super(params);
        this.params = params;
        this.jsonObject = jsonObject;
    }

    public PostBuilder(Params params, Map<String, String> jsonParams) {
        super(params);
        this.params = params;
        this.jsonParams = jsonParams;
    }

    public PostBuilder(Params params, String fileMethodName, File file) {
        super(params);
        this.params = params;
        this.fileMethodName = fileMethodName;
        this.file = file;
    }

    public PostBuilder(Params params, String fileMethodName, List<File> files) {
        super(params);
        this.params = params;
        this.fileMethodName = fileMethodName;
        this.files = files;
    }

    @Override
    protected Observable<String> getObservable(APIService apiService, Map<String, String> headMap, String url, Map<String, String> paramsMap) {
        Observable<String> observable = null;
        if (this.params == Params.POST_JSON) {
            String json = "";
            if (jsonParams != null && jsonParams.size() > 0) {
                this.jsonObject = new JSONObject();
                for (String key : jsonParams.keySet()) {
                    try {
                        jsonObject.put(key, jsonParams.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.jsonObject != null) {
                json = this.jsonObject.toString();
                Log.i("tag","===123json=="+json);
            }
            RequestBody requestBody = RequestBody.create(JSON, json);
            observable = apiService.postJson(headMap, url, requestBody);
            return observable;
        } else if (this.params == Params.POST_FILE) {
            List<MultipartBody.Part> parts = new ArrayList<>();
            if (!TextUtils.isEmpty(fileMethodName)) {
                if (files != null && files.size() > 0) {
                    for (File listFile : files) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), listFile);
                        MultipartBody.Part part = MultipartBody.Part.createFormData(fileMethodName, listFile.getName(), requestBody);
                        parts.add(part);
                    }
                } else if (file != null) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData(fileMethodName, file.getName(), requestBody);
                    parts.add(part);
                }
            }
            observable = apiService.postFile(headMap, url, parts);
            return observable;
        }
        return super.getObservable(apiService, headMap, url, paramsMap);
    }
}
