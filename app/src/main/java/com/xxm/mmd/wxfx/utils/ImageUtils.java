package com.xxm.mmd.wxfx.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.glide.GlideApp;
import com.xxm.mmd.wxfx.glide.GlideLoader;
import com.xxm.mmd.wxfx.glide.GlideRequest;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by MaDeng on 2018/3/18.
 */

public class ImageUtils {

    public static Observable<List<String>> loadImageForGlide(final List<String> urls) {

        Log.d(TAG, "loadImageForGlide: "+urls.toString());
       return Observable.fromIterable(urls)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                GlideLoadImageForFile(MyApp.app.getApplicationContext(), s, e);
                            }
                        });
                    }
                }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                Log.d(TAG, "apply: " + s);
                return s;
            }
        }).buffer(urls.size());


    }

    private static final String TAG = "ImageUtils";

    private static void GlideLoadImageForFile(final Context context, String url, final ObservableEmitter<String> emitter) {

        GlideApp.with(context).download(url).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String s = FileUtils.saveImageToGallery(context,resource);
                    emitter.onNext(s);
                }else {
                    emitter.onNext(resource.getPath());
                }
            }
        });
//        GlideApp.with(context).asFile().load(url).into(new SimpleTarget<File>() {
//            @Override
//            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
//
//            }
//        });

//        Glide.with(context).load(url).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
//            @Override
//            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
//                try {
//                    //这里就不要用openFileOutput了,那个是往手机内存中写数据的
//                    String filePath = getFilePath();
//                    FileOutputStream output = new FileOutputStream(filePath);
//                    output.write(resource);
//                    //将bytes写入到输出流中
//                    output.close();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        String s = FileUtils.saveImageToGallery(context,new File(filePath));
//                        emitter.onNext(s);
//                    }else {
//                        emitter.onNext(filePath);
//                    }
//                } catch (Throwable throwable) {
//                    Log.e(TAG, "onResourceReady: ", throwable);
//                }
//            }
//        });
    }

    private static String getFilePath() throws IOException {
        String fileName = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/fx";
            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            fileName = filePath + "/" + System.currentTimeMillis() + ".jpg";
        }
        return fileName;
    }

    /**
     * 解析本地二维码
     * @param path
     * @return
     */
    public static Observable<String> analyzeBitmap(final String path) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        emitter.onNext(result);
                        emitter.onComplete();
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        emitter.onError(new Throwable("解析失败"));
                    }
                });
            }
        });
    }

    /**
     * 生成二维码
     * @param s
     * @return
     */
    public static Observable<Bitmap> createImage(final String s) {
        return Observable.just(s)
                .flatMap(new Function<String, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(String s) throws Exception {
                        return Observable.just(CodeUtils.createImage(s, 400, 400, null));
                    }
                });
    }

    public static void getImagePathToSys(Context context, String imageName) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

        if (mCursor != null) {
            mCursor.moveToNext();
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            for(int i = 0;i<mCursor.getColumnCount();i++) {
                Log.d(TAG, mCursor.getString(mCursor.getColumnIndex(mCursor.getColumnName(i))));
            }
            Log.d(TAG, path);
        }
    }
}
