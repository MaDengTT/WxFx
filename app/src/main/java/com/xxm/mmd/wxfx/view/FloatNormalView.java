package com.xxm.mmd.wxfx.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by MaDeng on 2018/3/26.
 */

public class FloatNormalView extends LinearLayout {

    /**
     * 记录悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 用于更新悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    /**
     * 点击监听器
     */
    private OnClickListener mClickLinstener;

    /**
     * 设置点击响应灵敏度
     */
    private int threshold = 30;

    public FloatNormalView(Context context) {
        super(context,null);
    }

    public FloatNormalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public FloatNormalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
//        windowManager =
    }
}
