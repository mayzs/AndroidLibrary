package com.cctbn.baselibrary.common.widget.help;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @createDate: 2018/11/17
 * @author: mayz
 * @version: 1.0
 */
public class HelpUtils {
    /**
     * 获得屏幕高度
     *
     * @param ctx 上下文
     * @param winSize 屏幕尺寸
     */
    public static void loadWinSize(Context ctx, Point winSize) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        winSize.x = outMetrics.widthPixels;
        winSize.y = outMetrics.heightPixels;
    }

}
