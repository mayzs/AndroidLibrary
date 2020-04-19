package com.cctbn.baselibrary.common.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.cctbn.baselibrary.common.picture.PictureSelectionModel;
import com.cctbn.baselibrary.common.picture.PictureSelector;
import com.cctbn.baselibrary.common.picture.config.PictureConfig;
import com.cctbn.baselibrary.common.picture.config.PictureMimeType;

/**
 * @createDate: 2019/6/13
 * @author: mayz
 * @version: 1.0
 */
public class SelectImageUtils {
    private SelectImageUtils() {
    }

    private static volatile SelectImageUtils instance = null;

    public static SelectImageUtils getInstance() {
        if (instance == null) {
            synchronized (SelectImageUtils.class) {
                if (instance == null) {
                    instance = new SelectImageUtils();
                }
            }
        }
        return instance;
    }

    /**
     *
     * @param context
     * @param cameraPath 自定义拍照路径
     * @param themeId   样式
     * @param imageSpanCount  每行有多少列
     * @param isMultiple  是否多选
     * @param isCrop  是否裁剪
     * @param isCamera 是否带拍照按钮
     * @param isPreview 是否预览
     * @param aspect_ratio_x 裁剪比例
     * @param aspect_ratio_y 裁剪比例
     * @param requestCode 返回码
     */
    public void selectImage(Activity context, String cameraPath, int themeId, int imageSpanCount, boolean isMultiple, boolean isCrop, boolean isCamera, boolean isPreview, int aspect_ratio_x, int aspect_ratio_y, int requestCode) {
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(context)
                .openGallery(PictureMimeType.ofImage());
        // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
        //.theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
        if (!TextUtils.isEmpty(cameraPath)) {
            pictureSelectionModel.setOutputCameraPath(cameraPath);// 自定义拍照保存路径
        }
        if (themeId != 0) {
            pictureSelectionModel.theme(themeId);
        }
        pictureSelectionModel.maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(imageSpanCount < 0 ? 1 : imageSpanCount)// 每行显示个数
                .selectionMode(isMultiple ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(isPreview)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(isCamera)// 是否显示拍照按钮
                .isZoomAnim(false)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(isCrop)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
//                        .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
//                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
//                        .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                .forResult(requestCode);//结果回调onActivityResult code
    }

    public void selectImageDefault(Activity context, int imageSpanCount, boolean isMultiple, boolean isCrop, boolean isCamera, boolean isPreview, int aspect_ratio_x, int aspect_ratio_y, int requestCode) {
        selectImage(context, null, 0, imageSpanCount, isMultiple, isCrop, isCamera, isPreview, aspect_ratio_x, aspect_ratio_y, requestCode);
    }
}
