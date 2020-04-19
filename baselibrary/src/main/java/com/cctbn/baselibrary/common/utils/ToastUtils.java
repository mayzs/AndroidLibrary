package com.cctbn.baselibrary.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @version 1.0
 * @author:mayz
 * @date: 2017/6/17
 */

public class ToastUtils {
    private static volatile ToastUtils instance;
    private ToastUtils(){}
    public static ToastUtils getInstance(){
        if(instance==null){
            synchronized (ToastUtils.class){
                if (instance==null){
                    instance=new ToastUtils();
                }
            }
        }
        return instance;
    }

    private Toast toast=null;
    public void showToast(Context context, String mess){
        if(toast==null){
            toast= Toast.makeText(context,mess, Toast.LENGTH_SHORT);
        }else {
            toast.setText(mess);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
