package com.xxm.mmd.wxfx.dagger.module;

import android.content.Context;

import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.bean.UserBean;

import javax.inject.Singleton;

import cn.bmob.v3.BmobUser;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
public class AppModule {

    public MyApp app;
    public Context context;

    public AppModule(MyApp app) {
        this.app = app;
        context = app;
    }

    @Provides
    public UserBean provideUserBean() {
        return BmobUser.getCurrentUser(UserBean.class);
    }

    @Provides
    @Singleton
    public MyApp provideMyApp() {
        return app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }
}
