package com.cctbn.baselibrary.common.ucrop.callback;

import android.graphics.Bitmap;

import com.cctbn.baselibrary.common.ucrop.model.ExifInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public interface BitmapLoadCallback {

    void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull String imageInputPath, @Nullable String imageOutputPath);

    void onFailure(@NonNull Exception bitmapWorkerException);

}