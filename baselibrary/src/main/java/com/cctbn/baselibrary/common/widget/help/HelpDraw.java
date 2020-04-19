package com.cctbn.baselibrary.common.widget.help;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;

/**
 * @createDate: 2018/11/17
 * @author: mayz
 * @version: 1.0
 */
public class HelpDraw {
    /**
     * 获取屏幕尺寸
     */
    public static Point getWinSize(Context context) {
        Point point = new Point();
        HelpUtils.loadWinSize(context, point);
        return point;
    }

    /**
     * 绘制网格
     */
    public static Picture getGrid(Context context) {
        return getGrid(getWinSize(context));
    }

    /**
     * 绘制坐标系
     */
    public static Picture getCoo(Context context, Point coo) {
        return getCoo(coo, getWinSize(context));
    }


    /**
     * 绘制网格
     *
     * @param winSize 屏幕尺寸
     */
    private static Picture getGrid(Point winSize) {

        Picture picture = new Picture();
        Canvas recording = picture.beginRecording(winSize.x, winSize.y);
        //初始化网格画笔
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        recording.drawPath(HelpPath.gridPath(50, winSize), paint);
        return picture;

    }

    /**
     * 绘制坐标系
     *
     * @param coo     坐标系原点
     * @param winSize 屏幕尺寸
     */
    private static Picture getCoo(Point coo, Point winSize) {
        Picture picture = new Picture();
        Canvas recording = picture.beginRecording(winSize.x, winSize.y);
        //初始化网格画笔
        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(null);

        //绘制直线
        recording.drawPath(HelpPath.cooPath(coo, winSize), paint);
        //左箭头
        recording.drawLine(winSize.x, coo.y, winSize.x - 40, coo.y - 20, paint);
        recording.drawLine(winSize.x, coo.y, winSize.x - 40, coo.y + 20, paint);
        //下箭头
        recording.drawLine(coo.x, winSize.y, coo.x - 20, winSize.y - 40, paint);
        recording.drawLine(coo.x, winSize.y, coo.x + 20, winSize.y - 40, paint);
        //为坐标系绘制文字
        drawText4Coo(recording, coo, winSize, paint);
        return picture;
    }

    /**
     * 为坐标系绘制文字
     *
     * @param canvas  画布
     * @param coo     坐标系原点
     * @param winSize 屏幕尺寸
     * @param paint   画笔
     */
    private static void drawText4Coo(Canvas canvas, Point coo, Point winSize, Paint paint) {
        //绘制文字
        paint.setTextSize(50);
        canvas.drawText("x", winSize.x - 60, coo.y - 40, paint);
        canvas.drawText("y", coo.x - 40, winSize.y - 60, paint);
        paint.setTextSize(25);
        //X正轴文字
        for (int i = 1; i < (winSize.x - coo.x) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x - 20 + 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x + 100 * i, coo.y, coo.x + 100 * i, coo.y - 10, paint);
        }

        //X负轴文字
        for (int i = 1; i < coo.x / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x - 20 - 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x - 100 * i, coo.y, coo.x - 100 * i, coo.y - 10, paint);
        }

        //y正轴文字
        for (int i = 1; i < (winSize.y - coo.y) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x + 20, coo.y + 10 + 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y + 100 * i, coo.x + 10, coo.y + 100 * i, paint);
        }

        //y负轴文字
        for (int i = 1; i < coo.y / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x + 20, coo.y + 10 - 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y - 100 * i, coo.x + 10, coo.y - 100 * i, paint);
        }
    }

}
