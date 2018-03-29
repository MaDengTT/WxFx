package com.xxm.mmd.wxfx.glide;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xxm.mmd.wxfx.R;


import java.io.File;

/**
 * Created by MaDeng on 2018/1/10.
 */

public class GlideLoader {

    /**
     * 显示本地图片
     * @param imageView
     * @param path 设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
     */
    public static void loadLocalFileImage(ImageView imageView, String path) {
        GlideApp.with(imageView.getContext())
                .load(Uri.fromFile(new File(path)))
                //                    .placeholder(R.drawable.bg_shot)
//                    .error(R.drawable.bg_shot)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadAvatar(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext())
                .asBitmap()
                .placeholder(R.drawable.avatar)  //头像占位符
                .load(url)
                .centerCrop()
                .error(R.drawable.avatar) //失败占位符
                .into(imageView);
    }

    public static void loadCircle(ImageView imageView, String url) {

        RequestOptions options = new RequestOptions();
        options.centerCrop()
        //                .placeholder()  //头像占位符
        //        .error() //失败占位符
        //.fallback(R.drawable.fallback_nodata)
        .transform(new CircleTransform());

        GlideApp.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load the images of a list. Gif and png both permitted.
     * 正常图片
     *
     * @param imageView The target [ImageView].
     * @param url The url of image.
     */
    public static void loadNormal(ImageView imageView, String url) {
        if (url.endsWith(".gif")) {
            GlideApp.with(imageView.getContext())
                    .asGif()
                    .load(url)
//                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
//                    .error(R.drawable.bg_shot)
                    .into(imageView);
        } else {
            GlideApp.with(imageView.getContext())
                    .asBitmap()
                    .load(url)
//                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
//                    .error(R.drawable.bg_shot)
                    .into(imageView);
        }
    }
    public static void loadNormal(ImageView imageView, int rId) {

            GlideApp.with(imageView.getContext())
                    .asBitmap()
                    .load(rId)
//                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
//                    .error(R.drawable.bg_shot)
                    .into(imageView);
    }
    public static void loadNormal(ImageView imageView, Object object) {

            GlideApp.with(imageView.getContext())
                    .asBitmap()
                    .load(object)
//                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
//                    .error(R.drawable.bg_shot)
                    .into(imageView);
    }
    /**
     * Load the images of a list. Gif and png both permitted.
     * 正常图片 获取宽高
     *
     * @param imageView The target [ImageView].
     * @param url The url of image.
     */
    public static void loadNormalFindWH(ImageView imageView, String url, SimpleTarget<Bitmap> listener) {
        if (url.endsWith(".gif")) {
            GlideApp.with(imageView.getContext())
                    .asGif()
                    .load(url)
//                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
//                    .error(R.drawable.bg_shot)
                    .into(imageView);
        } else {
            GlideApp.with(imageView.getContext())
                    .asBitmap()
                    .load(url)
//                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
                    .into(listener);
//                    .error(R.drawable.bg_shot)
        }
    }

    /**
     * Load the image with best quality and get the [Palette] details.
     * Only the [Bitmap] has [Palette].
     * 高清质量图片 并返回位图信息
     *
     * @param imageView The target [ImageView].
     * @param url The url of image.
     * @param callback The [OnPaletteProcessingCallback] when palette finishes processing.
     */
    public static void loadHighQualityWithPalette(ImageView imageView, String url, final OnPaletteProcessingCallback callback) {
        if (url.endsWith(".gif")) {
            GlideApp.with(imageView.getContext())
                    .asGif()
                    .load(url)
//                    .placeholder(R.drawable.bg_shot)
                    .centerCrop()
//                    .error(R.drawable.bg_shot)
                    .priority(Priority.HIGH)
                    .into(imageView);
        } else {
            GlideApp.with(imageView.getContext())
                    .asBitmap()
                    .load(url)
//                    .placeholder(R.drawable.bg_shot)
                    .thumbnail(0.5f)
                    .centerCrop()
//                    .error(R.drawable.bg_shot)
                    .priority(Priority.HIGH)
                    .into(new BitmapImageViewTarget(imageView){
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            super.onResourceReady(resource, transition);
                            Palette.from(resource).maximumColorCount(4).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    callback.onPaletteGenerated(palette);
                                }
                            });
                        }
                    });

        }
    }

}
