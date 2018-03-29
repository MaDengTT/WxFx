package com.xxm.mmd.wxfx;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.xxm.mmd.wxfx.bean.UserBean;

import abc.abc.abc.AdManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;


/**
 * Created by Administrator on 2018/3/17.
 */

public class MyApp extends Application {


    public static MyApp app;
    public static String AD_APP_Id = "";
    public static String AD_POS_Id = "";

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Bmob.initialize(this, "7882a66a3adff7cbc4be16a0c9de5a9c");
//        AdManager.getInstance(this).init("0ff2ce00fbaadac6", "4b17d630489c5c7a ", true);
        AdManager.getInstance(this).init("42edfee8bb282105", "d3122d1190a0100c", true);
        ZXingLibrary.initDisplayOpinion(this);
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.setDebugMode(true);
    }

    public static MyApp getApp() {
        return app;
    }

    public UserBean getUser() {
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);
        return currentUser;
    }

}
