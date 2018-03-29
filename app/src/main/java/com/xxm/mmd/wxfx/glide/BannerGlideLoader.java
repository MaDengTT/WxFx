package com.xxm.mmd.wxfx.glide;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by MaDeng on 2018/3/29.
 */
public class BannerGlideLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideLoader.loadNormal(imageView,path);
    }
}
