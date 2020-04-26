package com.cctbn.baselibrary.mvvm;

/**
 * @createDate: 2020/4/23
 * @author: mayz
 * @version: 1.0
 */
public class Resource<T> {
    //状态  这里有多个状态 0表示加载中；1表示成功；2表示联网失败
    public static final int LOADING = 0;
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    public static final int PROGRESS = 3;//注意只有下载文件和上传图片时才会有
    public int state;

    public String errorMsg;
    public T data;

    //这里和文件和进度有关了
    public int precent;//文件下载百分比

    public Resource(int state, T data, String errorMsg) {
        this.state = state;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public Resource(int state) {
        this.state = state;
    }

    public Resource(int state, int precent) {
        this.state = state;
        this.precent = precent;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(SUCCESS, data, null);
    }


    public static <T> Resource<T> failure(String msg) {
        return new Resource<>(FAIL, null, msg);
    }
    public static <T> Resource<T> progress(int precent) {
        return new Resource<T>(PROGRESS, precent);
    }

    public void handler(OnHandleCallback<T> callback) {
        switch (state) {
            case SUCCESS:
                callback.onSuccess(data);
                break;
            case FAIL:
                callback.onFailure(errorMsg);
                break;
            case PROGRESS:
                callback.onProgress(precent);
                break;
        }
    }

    public interface OnHandleCallback<T> {
        void onSuccess(T data);

        void onFailure(String msg);

        void onProgress(int precent);
    }
}
