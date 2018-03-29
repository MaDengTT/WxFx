package com.xxm.mmd.wxfx.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.utils.FolderManager;


import java.io.InputStream;

/**
 * Created by MaDeng on 2018/1/10.
 */
@GlideModule
public class XxmAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        int imageDiskCacheSize = 1024 * 1024 * 512;
        int bitmapLruCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        builder.setBitmapPool(new LruBitmapPool(bitmapLruCacheSize));
        builder.setDiskCache(new DiskLruCacheFactory(FolderManager.getDiskCacheDir(MyApp.app), "imageCache", imageDiskCacheSize));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
