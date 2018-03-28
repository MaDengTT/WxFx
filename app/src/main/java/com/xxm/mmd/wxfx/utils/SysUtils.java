package com.xxm.mmd.wxfx.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by MaDeng on 2018/3/20.
 */

public class SysUtils {

    static String keys = "ff06d6135e6b22dcaefd63aa497a964d";   //密钥
    static String url = "http://www.baidu.com";
    static String temp = "imgeUrl=";
    public static int getVersionCode(Context context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return packInfo != null ? packInfo.versionCode : 1;
    }

    public static String getBase64ToString(String s) {
        return CryptAES.AES_Decrypt(keys, s);
    }

    public static String getStringToBase64(String s) {
        return CryptAES.AES_Encrypt(keys, s);
    }


    public static String getStringUrl(String id) {
        return url +"?"+ temp + getStringToBase64(id);
    }

    public static String getStringID(String url) {
        String[] split = url.split(temp);
//        Log.d("SysUtils", "split:" + split);
//        Log.d("SysUtils splite 1", split[0]);
//        Log.d("SysUtils splite 2", split[1]);

        return getBase64ToString(split[1]);
    }
    /**
     * convert px to its equivalent dp
     *
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * convert dp to its equivalent px
     *
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * convert px to its equivalent sp
     *
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
