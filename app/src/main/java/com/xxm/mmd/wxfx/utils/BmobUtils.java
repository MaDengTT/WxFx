package com.xxm.mmd.wxfx.utils;

import android.util.Log;

import com.xxm.mmd.wxfx.bean.BannerImage;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.bean.UpdateSys;


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

/**
 * Created by MaDeng on 2018/3/19.
 */

public class BmobUtils {

    public static Observable<String> PostData(final String text, List<String> strings) {
        String[] ss = new String[strings.size()];
        strings.toArray(ss);
        return uploadFiles(ss)
                .flatMap(new Function<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(List<String> strings) throws Exception {
                        DataWx wx = new DataWx();
                        wx.setText(text);
                        wx.setImage(strings);
                        return UpdateData(wx);
                    }
                });
    }

    public static Observable<BannerImage> getImage() {
        return Observable.create(new ObservableOnSubscribe<BannerImage>() {
            @Override
            public void subscribe(final ObservableEmitter<BannerImage> emitter) throws Exception {
                BmobQuery<BannerImage> query = new BmobQuery<>();
                query.findObjects(new FindListener<BannerImage>() {
                    @Override
                    public void done(List<BannerImage> list, BmobException e) {
//                        Log.d("BmobUtils", list.toString());
                        if (e == null&&!list.isEmpty()) {
                            emitter.onNext(list.get(0));
                        }else {
                            emitter.onError(e==null?new Throwable(""):e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<String> UpdateData(final DataWx wx) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                wx.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.d("BmobUtils", "成功上传 id = "+s);
                            emitter.onNext(s);
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });

    }

    public static Observable<UpdateSys> loadSysDataToBmob() {
        return Observable.create(new ObservableOnSubscribe<UpdateSys>() {
            @Override
            public void subscribe(final ObservableEmitter<UpdateSys> emitter) throws Exception {
                BmobQuery<UpdateSys> query = new BmobQuery<>();
                query.findObjects(new FindListener<UpdateSys>() {
                    @Override
                    public void done(List<UpdateSys> list, BmobException e) {
//                        Log.d("BmobUtils", list.toString());
                        if (e == null&&!list.isEmpty()) {
                            emitter.onNext(list.get(0));
                        }else {
                            emitter.onError(e==null?new Throwable("不需要更新"):e);
                        }
                    }
                });
            }
        });
    }


    public static Observable<List<String>> uploadFiles(final String path[]) {

        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<String>> emitter) throws Exception {
                BmobFile.uploadBatch(path, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> urls) {
                        Log.d("FileUtils", urls.toString());
                        if (urls.size() == path.length) {
                            Log.d("FileUtils", "全部上传成功");
                            emitter.onNext(urls);
                        }
                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.d("FileUtils", s);
                    }
                });
            }
        });
    }

    public static Observable<DataWx> loadDataToBmob(final String id) {
        return Observable.create(new ObservableOnSubscribe<DataWx>() {
            @Override
            public void subscribe(final ObservableEmitter<DataWx> emitter) throws Exception {
                BmobQuery<DataWx> query = new BmobQuery<>();
                query.getObject(id, new QueryListener<DataWx>() {
                    @Override
                    public void done(DataWx wx, BmobException e) {
                        if (e == null) {
                            emitter.onNext(wx);
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });

    }

}
