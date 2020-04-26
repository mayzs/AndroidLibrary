package com.cctbn.baselibrary.mvvm;


import com.cctbn.baselibrary.common.network.Params;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @createDate: 2020/4/23
 * @author: mayz
 * @version: 1.0
 */
public class ParamsBuilder {

    private String url;
    private Map<String, String> headMap = new TreeMap<>();
    private Map<String, String> paramsMap = new TreeMap<>();
    private Params params;
    private String fileMethodName;
    private List<File> files=new ArrayList<>();
    public static ParamsBuilder build() {
        return new ParamsBuilder();
    }

    public ParamsBuilder url(String url){
        this.url = url;
        return this;
    }

    public String getUrl(){
        return url;
    }
    public ParamsBuilder header(String key, String value) {
        this.headMap.put(key, value);
        return this;
    }

    public ParamsBuilder headers(Map<String, String> headMaps) {
        this.headMap.putAll(headMaps);
        return this;
    }

    public ParamsBuilder param(String key, String value) {
        this.paramsMap.put(key, value);
        return this;
    }

    public ParamsBuilder params(Map<String, String> params) {
        this.paramsMap.putAll(params);
        return this;
    }
    public Map<String,String> getHeadMap(){
        return headMap;
    }
    public Map<String,String> getParamsMap(){
        return paramsMap;
    }
    public ParamsBuilder method(Params params){
        this.params = params;
        return this;
    }

    public Params getMethod() {
        if (params==null){
            params= Params.GET;
        }
        return params;
    }

    public ParamsBuilder setFile(File file) {
        this.files.add(file);
        return this;
    }

    public void setFiles(List<File> files) {
        this.files.addAll(files);
    }

    public List<File> getFiles() {
        if (files == null) {
            return new ArrayList<>();
        }
        return files;
    }

    public ParamsBuilder setFileMethodName(String fileMethodName) {
        this.fileMethodName = fileMethodName;
        return this;
    }

    public String getFileMethodName() {
        return fileMethodName == null ? "" : fileMethodName;
    }


}
