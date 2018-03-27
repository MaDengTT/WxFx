package com.xxm.mmd.wxfx.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qq.e.ads.nativ.MediaView;

/**
 * Created by MaDeng on 2018/3/26.
 */

public class AlertWindowManager {

    private static AlertWindowManager instance;

    private Context mContext = null;
    private WindowManager mWindowManager = null;
    private View mView;

    private boolean isShown = false;

    private AlertWindowManager() {
    }

    public static AlertWindowManager getInstance() {
        if (instance == null) {
            instance = new AlertWindowManager();
        }
        return instance;
    }

    private void showWindow() {
        if (mContext == null) {
            return;
        }
        if (isShown) {
            return;
        }
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG; //TODO 推荐方法
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        params.gravity = Gravity.TOP;

        mWindowManager.addView(mView,params);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setmView(View mView) {
        this.mView = mView;
    }

    public void hideWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }

    private WindowManager.LayoutParams initLayoutParams() {
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.x = screenWidth - mView.getLayoutParams().width * 2;
        layoutParams.y = 0;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;
        return layoutParams;
    }

    public void show() {
        if (!isShown) {
            isShown = true;
            mWindowManager.addView(mView, initLayoutParams());
        }
    }
    public void dismiss() {
        if (isShown) {
            isShown = false;
            mWindowManager.removeView(mView);
        }
    }

}
