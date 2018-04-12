package com.xxm.mmd.wxfx.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.xxm.mmd.wxfx.R;

import java.util.List;

/**
 * Created by MaDeng on 2018/4/12.
 */
public class ServiceHelper {


    /**
     * 前往设置界面开启服务
     */
    public static void startAccessibilityService(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("开启辅助功能")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("使用此项功能需要您开启辅助功能")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐式调用系统设置界面
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        context.startActivity(intent);
                    }
                }).create().show();
    }

    public static boolean AccessibilityIsRunning(Context context) {
//        serviceIsRunning()
       return serviceIsRunning(context, ".service.MyService");
    }

    /**
     * 判断自己的应用的Service是否在运行
     *
     * @return
     */
    private static boolean serviceIsRunning(Context context,String servicePath) {
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
//            Log.d(TAG, info.service.getClassName());
            if (info.service.getClassName().equals(context.getPackageName() + servicePath)) {
                return true;
            }
        }
        return false;
    }

}
