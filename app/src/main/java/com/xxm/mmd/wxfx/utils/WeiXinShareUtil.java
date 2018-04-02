package com.xxm.mmd.wxfx.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.ui.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2018/3/17.
 */

public class WeiXinShareUtil {

    private static final String TAG = "WeiXinShareUtil";

    public static void sharePhotosToWx(Context context, String text, List<String> imageUrlArray) {
        if (!uninstallSoftware(context, "com.tencent.mm")) {
            Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ArrayList<Uri> imageUris = new ArrayList<>();
        for(int i = 0; i<imageUrlArray.size(); i++) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    Uri documentUri = MediaStore.getDocumentUri(context, Uri.parse(imageUrlArray.get(i)));
//                    Log.d(TAG, documentUri.toString());
//                }
                imageUris.add(Uri.parse(imageUrlArray.get(i)));
            }
            else {

                File file = new File(imageUrlArray.get(i));
                if (!file.exists()) {
                    String tip = "文件不存在";
                    Toast.makeText(context, tip + " path = " + file.getName(), Toast.LENGTH_LONG).show();
                    return;
                }
                Uri uriForFile = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uriForFile = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
                    Log.d(TAG, uriForFile.toString());
                }else {
                    uriForFile = Uri.fromFile(file);
                }
                imageUris.add(uriForFile);
            }

        }
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.putExtra("Kdescription", text);

        shareIntent.setType("image/*");
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        shareIntent.setComponent(componentName);

        try {
//            Intent intent = Intent.createChooser(shareIntent, "分享图片");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(shareIntent);
        } catch (Exception e) {
            Log.e(TAG, "shareImages: ",e );
            Toast.makeText(context, "分享失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sharePhotoToWX(Context context, String text, String photoPath) {
        if (!uninstallSoftware(context, "com.tencent.mm")) {
            Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(photoPath);
        if (!file.exists()) {
            String tip = "文件不存在";
            Toast.makeText(context, tip + " path = " + photoPath, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("Kdescription", text);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);
    }

    private static boolean uninstallSoftware(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void shareSingleImage(Context context,String path) {
        Uri imageUri = Uri.fromFile(new File(path));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    @SuppressLint("CheckResult")
    public static void shareDataToWx(String id, final Activity activity) {
        String ids = SysUtils.getStringID(id);
        BmobUtils.loadDataToBmob(ids)
                .flatMap(new Function<DataWx, ObservableSource<DataWx>>() {
                    @Override
                    public ObservableSource<DataWx> apply(final DataWx wx) throws Exception {
                        return io.reactivex.Observable.create(new ObservableOnSubscribe<DataWx>() {
                            @Override
                            public void subscribe(final ObservableEmitter<DataWx> emitter) throws Exception {
                                ImageUtils.loadImageForGlide(wx.getImage())
                                        .subscribe(new Consumer<List<String>>() {
                                            @Override
                                            public void accept(List<String> strings) throws Exception {
                                                wx.setImage(strings);
                                                emitter.onNext(wx);
                                            }
                                        });
                            }
                        });
                    }
                })
                .subscribe(new Consumer<DataWx>() {
                    @Override
                    public void accept(DataWx wx) throws Exception {
                        sharePhotosToWx(activity,wx.getText(),wx.getImage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("tt", "accept: ",throwable );
                    }
                });
    }
    public static void shareDataToWx(CirlceBean bean, final Activity activity) {
        io.reactivex.Observable.just(bean)
                .flatMap(new Function<CirlceBean, ObservableSource<DataWx>>() {
                    @Override
                    public ObservableSource<DataWx> apply(final CirlceBean bean) throws Exception {

                        return io.reactivex.Observable.create(new ObservableOnSubscribe<DataWx>() {
                            @Override
                            public void subscribe(final ObservableEmitter<DataWx> emitter) throws Exception {
                                final DataWx wx = new DataWx();
                                wx.setText(bean.getContent());
                                ImageUtils.loadImageForGlide(bean.getImageUrls())
                                        .subscribe(new Consumer<List<String>>() {
                                            @Override
                                            public void accept(List<String> strings) throws Exception {
                                                wx.setImage(strings);
                                                emitter.onNext(wx);
                                            }
                                        });
                            }
                        });
                    }
                })
                .subscribe(new Consumer<DataWx>() {
                    @Override
                    public void accept(DataWx wx) throws Exception {
                        sharePhotosToWx(activity,wx.getText(),wx.getImage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("tt", "accept: ",throwable );
                    }
                });
    }
}