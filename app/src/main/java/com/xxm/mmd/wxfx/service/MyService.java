package com.xxm.mmd.wxfx.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by MaDeng on 2017/12/11.
 */

public class MyService extends AccessibilityService {

    private static final String TAG = "MyService";
    final String pknWX = "com.tencent.mm";


    @Override
    public void onCreate() {
        super.onCreate();
////        key = MyApplication.getInstanct().getKEY();
//        msgReceiver = new MsgReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.example.mm");
//        registerReceiver(msgReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onServiceConnected() {

        Log.d(TAG, "onServiceConnected: sever");
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        setServiceInfo(info);
        info.packageNames = new String[]{"com.tencent.mm"};
        setServiceInfo(info);

        super.onServiceConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        
        String pkgName = accessibilityEvent.getPackageName().toString();
        int eventType = accessibilityEvent.getEventType();

        Log.d(TAG, pkgName);

        KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        Log.d(TAG, "flag:" + flag);

        // true 锁 flash 打开
        Log.d(TAG, "onAccessibilityEvent TYPE: " + eventType);
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:

                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                Log.d(TAG, "onAccessibilityEvent: " + "窗口改变");

                String className = accessibilityEvent.getClassName().toString();
                Log.d(TAG, "onAccessibilityEvent: "+className);

                if (className.equals("com.tencent.mm.plugin.sns.ui.SnsUploadUI")) {
                    AccessibilityNodeInfo info = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> buttons = null;
//                    List<AccessibilityNodeInfo> editTexts = null;


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        buttons = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hd");
//                        editTexts = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dez");
                    }else {
                        buttons = info.findAccessibilityNodeInfosByText("发送");
//                        editTexts = info.findAccessibilityNodeInfosByText("这一刻的想法...");
                    }

                    if (!buttons.isEmpty()) {
                        AccessibilityNodeInfo button = buttons.get(0);
                        Log.d(TAG, "button.getText():" + button.getText());
                        button.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }


                } else /*if (className.equals())*/ {//com.tencent.mm.plugin.sns.ui.SnsTimeLineUI
                    AccessibilityNodeInfo info = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> buttons = new ArrayList<>();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if(info != null)
                            buttons = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hx");
//                        editTexts = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dez");
                    }else {
//                        buttons = info.findAccessibilityNodeInfosByText("发送");
//                        editTexts = info.findAccessibilityNodeInfosByText("这一刻的想法...");
                    }
                    if (!buttons.isEmpty()) {
                        final AccessibilityNodeInfo button = buttons.get(0);
                        Log.d(TAG, "button.getText():" + button.getText());
                        io.reactivex.Observable.timer(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                button.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        });

                    }
                }

                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:

                break;
        }
    }

    private void showToast(final String message) {
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onInterrupt() {

    }



}
