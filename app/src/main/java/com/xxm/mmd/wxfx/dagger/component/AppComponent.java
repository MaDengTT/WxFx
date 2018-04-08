package com.xxm.mmd.wxfx.dagger.component;

import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MaDeng on 2018/4/2.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MyApp myApp);
}
