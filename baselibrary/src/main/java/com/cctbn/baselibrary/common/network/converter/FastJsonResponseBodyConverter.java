package com.cctbn.baselibrary.common.network.converter;


import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type type;

    FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }


    //zhi
    @Override
    public T convert(ResponseBody value) throws IOException {

        Class clazz = (Class) type;
        String canonicalName = clazz.getCanonicalName();

//        String string = value.string();
        if (canonicalName.equals(JSONObject.class.getName())) {

            try {
                JSONObject jsonObj;
                jsonObj = new JSONObject(value.string());
                return (T) jsonObj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (canonicalName.equals(JSONArray.class.getName())) {

            try {
                JSONArray jsonArray;
                jsonArray = new JSONArray(value.string());
                return (T) jsonArray;
            } catch (Exception e) {
//                LogUtil.e("apiservice 写的JSONArray不对");
                e.printStackTrace();
            }
        } else {
            T data = JSON.parseObject(value.string(), this.type);
            return data;
        }
        return null;
    }
}
