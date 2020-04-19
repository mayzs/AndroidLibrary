package com.cctbn.baselibrary.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * @createDate: 2018/6/5
 * @author: mayz
 * @version: 1.0
 */
public class ImageUtils {
    private ImageUtils() {
    }

    private static volatile ImageUtils instance = null;

    public static ImageUtils getIstance() {
        if (instance == null) {
            synchronized (ImageUtils.class) {
                if (instance == null) {
                    instance = new ImageUtils();
                }
            }
        }
        return instance;
    }

    @SuppressLint("NewApi")
    public void loadImage(Context context, String url, ImageView imageView, int id) {
        if (context == null)
            return;
        final Activity activity = (Activity) context;
        if (activity.isDestroyed() || activity.isFinishing()) {
            return ;
        }
        Glide.with(context).asBitmap().load(url).apply(options(id)).into(imageView);
    }

    @SuppressLint("NewApi")
    public void loadImage(Context context, String url, ImageView imageView, int id,RequestListener<Bitmap> listener) {
        if (context == null)
            return;
        final Activity activity = (Activity) context;
        if (activity.isDestroyed() || activity.isFinishing()) {
            return ;
        }
        Glide.with(context).asBitmap().load(url).apply(options(id)).listener(listener).into(imageView);
    }

    @SuppressLint("NewApi")
    public void loadImage(Context context, File file, ImageView imageView, int id) {
        if (context == null)
            return;
        final Activity activity = (Activity) context;
        if (activity.isDestroyed() || activity.isFinishing()) {
            return ;
        }
        Glide.with(context).asBitmap().load(file).apply(options(id)).into(imageView);
    }

    private RequestOptions options(int id) {
        return new RequestOptions().centerCrop().placeholder(id).error(id)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).dontAnimate();
    }
}
