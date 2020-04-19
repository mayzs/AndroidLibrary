package com.cctbn.baselibrary.common.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cctbn.baselibrary.R;
import com.cctbn.baselibrary.common.network.RetrofitUtils;
import com.cctbn.baselibrary.common.network.delagate.FileCallback;
import com.cctbn.baselibrary.common.permissions.PermissionsManager;
import com.cctbn.baselibrary.common.permissions.PermissionsResultAction;
import com.cctbn.baselibrary.common.picture.PictureSelectionModel;
import com.cctbn.baselibrary.common.picture.PictureSelector;
import com.cctbn.baselibrary.common.picture.config.PictureConfig;
import com.cctbn.baselibrary.common.picture.config.PictureMimeType;
import com.cctbn.baselibrary.common.picture.entity.LocalMedia;
import com.cctbn.baselibrary.common.activity.BaseActivity;
import com.cctbn.baselibrary.common.utils.SystemUtils;
import com.cctbn.baselibrary.common.webview.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class X5WebViewActivity extends BaseActivity {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;

    private String mHomeUrl = "";
    private final String TAG = "SdkDemo";
    private final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable = 255;

    private ProgressBar mPageLoadingProgressBar = null;

    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFileAbovel;

    private URL mIntentUrl;
    private String userAgent;
    protected ImageView backIcon;
    protected ImageButton rightIcon;
    protected TextView title;
    protected RelativeLayout titleLayout;
    private String token;
    private String cameraPath;
    private H5AndroidInteration h5AndroidInteration;
    private int themeId = 0;
    private String method;
    private String titleText;
    protected Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_x5_webview);
        backIcon = findViewById(R.id.back_image);
        rightIcon = findViewById(R.id.right_image);
        title = findViewById(R.id.title);
        titleLayout = findViewById(R.id.title_layout);
        backIcon.setOnClickListener(view -> {
            finish();
        });
        titleText = getIntent().getStringExtra("title");
        title.setText(titleText);
    }

    protected void setUrl(String url) {

        Intent intent = getIntent();

        mHomeUrl = url;

        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getData().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		/*getWindow().addFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        mViewParent = (ViewGroup) findViewById(R.id.webView_layout);


        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);

    }

    public void setLoadUrl(String url) {
        if (mWebView != null) {
            mWebView.loadUrl(url);
        }
    }

    public void setUserAgentString(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCameraPath(String cameraPath) {
        this.cameraPath = cameraPath;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar);// new
        // ProgressBar(getApplicationContext(),
        // null,
        // android.R.attr.progressBarStyleHorizontal);
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));
    }


    private void init() {
        mWebView = new X5WebView(this, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mWebView.setInitialScale(25);
        initProgressBar();
        try {
            mWebView.getX5WebViewExtension().setScrollBarFadingEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                // TODO Auto-generated method stub
                return super.shouldInterceptRequest(view, request);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //moreMenuClose();
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
//				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                //changGoForwardButton(view);
                /* mWebView.showLog("test Log"); */

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            //进度条变化
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mPageLoadingProgressBar.setVisibility(GONE);
                } else {
                    if (mPageLoadingProgressBar.getVisibility() == GONE)
                        mPageLoadingProgressBar.setVisibility(VISIBLE);
                    mPageLoadingProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(webView, newProgress);
            }

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) findViewById(R.id.webView_layout);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView arg0,
                                             ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
                // TODO Auto-generated method stub
                Log.e("app", "onShowFileChooser");
                X5WebViewActivity.this.uploadFileAbovel = arg1;
                String[] acceptTypes = arg2.getAcceptTypes();
                String acceptType = "";
                for (String accept : acceptTypes) {
                    acceptType = accept;
                }
                if (!TextUtils.isEmpty(acceptType)) {
                    if ("image/*".equals(acceptType)) {
                        //takePhoto();
                        X5WebViewActivity.this.openCamera(false, 1, 1, 0);
                    }
                    /*打开摄像头录像*/
                    if ("video/*".equals(acceptType)) {
                        X5WebViewActivity.this.recordVideo(0);
                    }
                }
//                return super.onShowFileChooser(arg0, arg1, arg2);
                return true;
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
                X5WebViewActivity.this.uploadFile = uploadFile;
                if (!TextUtils.isEmpty(acceptType)) {
                    if ("image/*".equals(acceptType)) {
                        //takePhoto();
                        X5WebViewActivity.this.openCamera(false, 1, 1, 0);
                    }
                    /*打开摄像头录像*/
                    if ("video/*".equals(acceptType)) {
                        X5WebViewActivity.this.recordVideo(0);
                    }
                }

//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                startActivityForResult(Intent.createChooser(i, "test"), 0);
            }


            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                Log.i("yuanhaizhou", "setX5webview = null");
                return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
            }

            /**
             * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
             */


            @Override
            public void onReceivedTitle(WebView arg0, final String arg1) {
                super.onReceivedTitle(arg0, arg1);
                if (TextUtils.isEmpty(titleText)) {
                    title.setText(arg0.getTitle() + "");
                }
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                if (!TextUtils.isEmpty(url)) {
                    if (TextUtils.isEmpty(contentDisposition)) {
                        String fileName = url.substring(url.lastIndexOf("/") + 1);
                        int index = fileName.indexOf("?");
                        if (index > 0) {
                            fileName = fileName.substring(0, fileName.indexOf("?"));
                        }
                        contentDisposition = fileName;
                        downloadFile(url, contentDisposition);
                    }

                }

            }
        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
//        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        webSetting.setDefaultTextEncodingName("UTF-8");
        webSetting.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webSetting.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSetting.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSetting.setAllowUniversalAccessFromFileURLs(false);


        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        if (!TextUtils.isEmpty(userAgent)) {
            String userAgentString = webSetting.getUserAgentString();
            webSetting.setUserAgentString(userAgentString + "/" + userAgent + "/" + SystemUtils.getIstance().getVersionName(X5WebViewActivity.this));
        }
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            if (!TextUtils.isEmpty(mHomeUrl))
                mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
        //与H5进行交互
        mWebView.addJavascriptInterface(new H5AndroidInteration(), "android");
        if (h5AndroidInteration != null) {
            mWebView.addJavascriptInterface(h5AndroidInteration, !TextUtils.isEmpty(method) ? method : "android");
        }

        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    private enum TEST_ENUM_FONTSIZE {
        FONT_SIZE_SMALLEST, FONT_SIZE_SMALLER, FONT_SIZE_NORMAL, FONT_SIZE_LARGER, FONT_SIZE_LARGEST
    }

    ;

    private TEST_ENUM_FONTSIZE m_font_index = TEST_ENUM_FONTSIZE.FONT_SIZE_NORMAL;

    private void downloadFile(String url, String contentDisposition) {
        String path = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS;
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(X5WebViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                RetrofitUtils.getInstance().get().url(url).execute(new FileCallback(path, contentDisposition) {
                    @Override
                    public void onStart() {
                        mTestHandler.sendEmptyMessage(MSG_DOWNLOAD_START_UI);
                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onCompleted(File file) {
                        mTestHandler.sendEmptyMessage(MSG_DOWNLOAD_UI);
                    }

                    @Override
                    public void onError(String errmsg) {

                    }
                });
            }

            //
            @Override
            public void onDenied(String permission) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(Build.VERSION.SDK) >= 16)
//					changGoForwardButton(mWebView);
                    return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (data != null) {
                        List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                        for (LocalMedia media : localMedia) {
                            if (!TextUtils.isEmpty(media.getPath())) {
                                File file = new File(media.getPath());
                                Uri uri = Uri.fromFile(file);
                                if (uploadFile != null) {
                                    uploadFile.onReceiveValue(uri);
                                    uploadFile = null;
                                }
                                if (uploadFileAbovel != null) {
                                    uploadFileAbovel.onReceiveValue(new Uri[]{uri});
                                    uploadFileAbovel = null;
                                }
                            }
                        }
                    }
                    break;
                case 1:
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    break;
                case 100:
                    if (data != null) {
                        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                        try {
                            JSONObject object = new JSONObject();
                            object.put("code", "0");
                            JSONArray array = new JSONArray();
                            for (LocalMedia media : selectList) {
                                JSONObject object1 = new JSONObject();
                                object1.put("path", "file://" + media.getPath());
//                                object1.put("image", imageToBase64(media.getPath()));
                                array.put(object1);
                            }
                            object.put("filepath", array);
                            object.put("message", "返回成功!");
                            mWebView.loadUrl("javascript:bridge.setImageResult(" + object.toString() + ")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
//            if (null != uploadFile) {
//                uploadFile.onReceiveValue(null);
//                uploadFile = null;
//            }
            cancelFilePathCallback();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();

    }

    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    public static final int MSG_DOWNLOAD_UI = 2;
    public static final int MSG_DOWNLOAD_START_UI = 3;

    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    @SuppressLint("HandlerLeak")
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
                case MSG_DOWNLOAD_UI:
                    showToastMessage("下载完成");
                    break;
                case MSG_DOWNLOAD_START_UI:
                    showToastMessage("开始下载");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    protected void share(String shareUrl, String imageUrl, String title, String description) {

    }

    /**
     * 接收h5传过来的值
     *
     * @param type
     * @param result
     */
    protected void setMessage(String type, String result) {

    }

    /**
     * 子类可以继承 H5AndroidInteration 自己实现
     *
     * @param h5AndroidInteration
     */
    public void setH5Android(H5AndroidInteration h5AndroidInteration, String method) {
        this.h5AndroidInteration = h5AndroidInteration;
        this.method = method;
    }

    //与H5进行交互
    public class H5AndroidInteration {
        @JavascriptInterface
        public void getImei() {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(X5WebViewActivity.this, new String[]{
                    Manifest.permission.READ_PHONE_STATE
            }, new PermissionsResultAction() {
                @Override
                public void onGranted() {
                    String imei = SystemUtils.getIstance().getDeviceId(X5WebViewActivity.this);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        if (!imei.equals("000")) {
                            jsonObject.put("code", "0");
                            jsonObject.put("message", "获取成功!");
                        } else {
                            jsonObject.put("code", "2");
                            jsonObject.put("message", "获取失败!");
                        }
                        jsonObject.put("imei", imei);
                        mWebView.loadUrl("javascript:setImeiResult(" + jsonObject.toString() + ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onDenied(String permission) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("code", "1");
                        jsonObject.put("message", "用户拒绝!");
                        jsonObject.put("imei", "0");
                        mWebView.loadUrl("javascript:setImeiResult(" + jsonObject.toString() + ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @JavascriptInterface
        public String getVersion() {
            String version = SystemUtils.getIstance().getVersionName(X5WebViewActivity.this);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", "0");
                jsonObject.put("version", version);
                jsonObject.put("message", "获取成功!");
                mWebView.loadUrl("javascript:setVersionResult(" + jsonObject.toString() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return version;
        }

        @JavascriptInterface
        public void close() {
            finish();
        }

        @JavascriptInterface
        public void getBack() {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Build.VERSION.SDK_INT >= 16)
//					changGoForwardButton(mWebView);
                    finish();
            } else {
                finish();
            }
        }

        @JavascriptInterface
        public void share(String shareUrl, String imageUrl, String title, String description) {
            X5WebViewActivity.this.share(shareUrl, imageUrl, title, description);
        }

        @JavascriptInterface
        public void setMessage(String type, String result) {
            X5WebViewActivity.this.setMessage(type, result);
        }

        @JavascriptInterface
        public String token() {
            try {
                JSONObject jsonObject = new JSONObject();
                if (!TextUtils.isEmpty(token)) {
                    jsonObject.put("code", "0");
                    jsonObject.put("token", token);
                    jsonObject.put("message", "获取成功!");
                } else {
                    jsonObject.put("code", "1");
                    jsonObject.put("token", "");
                    jsonObject.put("message", "获取失败!");
                }
                mWebView.loadUrl("javascript:setTokenResult(" + jsonObject.toString() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return !TextUtils.isEmpty(token) ? token : "";
        }

        @JavascriptInterface
        public void openCamera() {
            X5WebViewActivity.this.openCamera(false, 1, 1, 100);
        }

        @JavascriptInterface
        public void openCamera(String isCrop, String aspect_ratio_x, String aspect_ratio_y) {
            boolean isCrop_1 = isCrop.equalsIgnoreCase("true") ? true : false;
            int aspect_ratio_x_1 = Integer.parseInt(aspect_ratio_x) > 0 ? Integer.parseInt(aspect_ratio_x) : 1;
            int aspect_ratio_y_1 = Integer.parseInt(aspect_ratio_y) > 0 ? Integer.parseInt(aspect_ratio_y) : 1;
            X5WebViewActivity.this.openCamera(isCrop_1, aspect_ratio_x_1, aspect_ratio_y_1, 100);
        }


        @JavascriptInterface
        public void openGallery() {
            X5WebViewActivity.this.openGallery(false, false, false, false, 1, 1);
        }

        @JavascriptInterface
        public void openGallery(String isMultiple, String isCrop, String isCamera, String isPreview, String aspect_ratio_x, String aspect_ratio_y) {
            boolean isMultiple_1 = isMultiple.equalsIgnoreCase("true") ? true : false;
            boolean isCrop_1 = isCrop.equalsIgnoreCase("true") ? true : false;
            boolean isCamera_1 = isCamera.equalsIgnoreCase("true") ? true : false;
            boolean isPreview_1 = isPreview.equalsIgnoreCase("true") ? true : false;
            int aspect_ratio_x_1 = Integer.parseInt(aspect_ratio_x) > 0 ? Integer.parseInt(aspect_ratio_x) : 1;
            int aspect_ratio_y_1 = Integer.parseInt(aspect_ratio_y) > 0 ? Integer.parseInt(aspect_ratio_y) : 1;
            X5WebViewActivity.this.openGallery(isMultiple_1, isCrop_1, isCamera_1, isPreview_1, aspect_ratio_x_1, aspect_ratio_y_1);
        }

        @JavascriptInterface
        public void showToast(String message){
            X5WebViewActivity.this.showToastMessage(message);
        }

        @JavascriptInterface
        public void showLoading(){
            X5WebViewActivity.this.showLoading();
        }
        @JavascriptInterface
        public void showLoading(String message){
            X5WebViewActivity.this.showLoading(message);
        }
        @JavascriptInterface
        public void dismissLoading(){
            X5WebViewActivity.this.dismissLoading();
        }
    }

    private void openGallery(boolean isMultiple, boolean isCrop, boolean isCamera, boolean isPreview, int aspect_ratio_x, int aspect_ratio_y) {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(X5WebViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                PictureSelectionModel pictureSelectionModel = PictureSelector.create(X5WebViewActivity.this)
                        .openGallery(PictureMimeType.ofImage());
                // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                if (!TextUtils.isEmpty(cameraPath)) {
                    pictureSelectionModel.setOutputCameraPath(cameraPath);// 自定义拍照保存路径
                }
                if (X5WebViewActivity.this.themeId != 0) {
                    pictureSelectionModel.theme(X5WebViewActivity.this.themeId);
                }
                pictureSelectionModel.maxSelectNum(9)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(3)// 每行显示个数
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
                        .forResult(100);//结果回调onActivityResult code
            }

            @Override
            public void onDenied(String permission) {
                cancelFilePathCallback();
            }
        });
    }

    public void showLoading(){
        loadingDialog=loadingDialog();
        loadingDialog.show();
    }
    public void showLoading(String message){
        loadingDialog=loadingDialog(message);
        loadingDialog.show();
    }
    public void dismissLoading() {
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    private void openCamera(boolean isCrop, boolean isVideo, int aspect_ratio_x, int aspect_ratio_y, int requestCode) {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(X5WebViewActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                PictureSelectionModel pictureSelectionModel = PictureSelector.create(X5WebViewActivity.this).openCamera(isVideo ? PictureMimeType.ofVideo() : PictureMimeType.ofImage())
                        .imageFormat(PictureMimeType.JPEG);// 拍照保存图片格式后缀,默认jpeg
                if (!TextUtils.isEmpty(cameraPath)) {
                    pictureSelectionModel.setOutputCameraPath(cameraPath);// 自定义拍照保存路径
                }
                if (X5WebViewActivity.this.themeId != 0) {
                    pictureSelectionModel.theme(X5WebViewActivity.this.themeId);
                }
                pictureSelectionModel.enableCrop(isCrop)// 是否裁剪
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        .showCropGrid(false)
                        .cropCompressQuality(70)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)
                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                        .forResult(requestCode);
            }

            @Override
            public void onDenied(String permission) {
                cancelFilePathCallback();
            }
        });
    }

    private void openCamera(boolean isCrop, int aspect_ratio_x, int aspect_ratio_y, int requestCode) {
        openCamera(isCrop, false, aspect_ratio_x, aspect_ratio_y, requestCode);
    }

    private void recordVideo(int requestCode) {
        openCamera(false, true, 1, 1, requestCode);
    }

    /**
     * 将图片转换成Base64编码的字符串
     */

    public static String imageToBase64(String path) {

        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            is.close();
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    /**
     * 取消mFilePathCallback回调
     */
    private void cancelFilePathCallback() {
        if (X5WebViewActivity.this.uploadFile != null) {
            X5WebViewActivity.this.uploadFile.onReceiveValue(null);
            X5WebViewActivity.this.uploadFile = null;
        } else if (X5WebViewActivity.this.uploadFileAbovel != null) {
            X5WebViewActivity.this.uploadFileAbovel.onReceiveValue(null);
            X5WebViewActivity.this.uploadFileAbovel = null;
        }
    }
}
