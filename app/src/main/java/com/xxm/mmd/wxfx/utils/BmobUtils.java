package com.xxm.mmd.wxfx.utils;

import android.text.TextUtils;
import android.util.Log;

import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.bean.BannerImage;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.bean.Team;
import com.xxm.mmd.wxfx.bean.UpdateSys;
import com.xxm.mmd.wxfx.bean.UserBean;


import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by MaDeng on 2018/3/19.
 */

public class BmobUtils {

    /**
     * 上传数据
     * @param text
     * @param strings
     * @param isShowSq
     * @param isShowTeam
     * @return
     */
    public static Observable<String> PostData(final String text, List<String> strings, final boolean isShowSq, final boolean isShowTeam) {
        String[] ss = new String[strings.size()];
        strings.toArray(ss);
        return uploadFiles(ss)
                .flatMap(new Function<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(List<String> strings) throws Exception {
                        DataWx wx = new DataWx();
                        wx.setText(text);
                        wx.setImage(strings);
                        wx.setIfShowSquare(isShowSq);
                        wx.setIfShowTeam(isShowTeam);
                        UserBean user = MyApp.getApp().getUser();
                        if (user != null) {
                            wx.setUser(user);
                        }
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

    public static Observable<Integer> requestSMSLOGCODE(final String phoneNum) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> emitter) throws Exception {
                BmobSMS.requestSMSCode(phoneNum, "login", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            emitter.onNext(integer);
                        }else{
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<UserBean> signOrLoginByMobilePhone(final String phoneNum, final String vCode) {
        return Observable.create(new ObservableOnSubscribe<UserBean>() {
            @Override
            public void subscribe(final ObservableEmitter<UserBean> emitter) throws Exception {
                BmobUser.signOrLoginByMobilePhone(phoneNum, vCode, new LogInListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (userBean == null) {
                            emitter.onNext(userBean);
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<Team> getTeam(final String teamID) {
        return Observable.create(new ObservableOnSubscribe<Team>() {
            @Override
            public void subscribe(final ObservableEmitter<Team> emitter) throws Exception {
                BmobQuery<Team> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject(teamID, new QueryListener<Team>() {
                    @Override
                    public void done(Team team, BmobException e) {
                        if (e == null) {
                            emitter.onNext(team);
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取朋友圈信息
     * @param teamID
     * @param pageSize
     * @param pageNo
     * @return
     */
    public static Observable<List<DataWx>> getDataWxs(final String teamID, final int pageSize, final int pageNo) {

        if (TextUtils.isEmpty(teamID)) {
            return Observable.create(new ObservableOnSubscribe<List<DataWx>>() {
                @Override
                public void subscribe(final ObservableEmitter<List<DataWx>> emitter) throws Exception {
                    BmobQuery<DataWx> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("ifShowSquare", true);
                    bmobQuery.include("user");
                    bmobQuery.setLimit(pageSize).setSkip(pageNo)
                            .findObjects(new FindListener<DataWx>() {
                                @Override
                                public void done(List<DataWx> list, BmobException e) {
                                    if (e == null) {
                                        emitter.onNext(list);
                                        Log.d("BmobUtils", "list.size():" + list.size());
                                    }else {
                                        emitter.onError(e);
                                    }
                                }
                            });
                }
            });

        }else {
           return getTeam(teamID)
                    .flatMap(new Function<Team, ObservableSource<List<DataWx>>>() {
                        @Override
                        public ObservableSource<List<DataWx>> apply(final Team team) throws Exception {
                            return  Observable.create(new ObservableOnSubscribe<List<DataWx>>() {
                                @Override
                                public void subscribe(final ObservableEmitter<List<DataWx>> emitter) throws Exception {
                                    BmobQuery<DataWx> bmobQuery = new BmobQuery<>();
                                    bmobQuery.addWhereEqualTo("ifShowSquare", true);
                                    bmobQuery.addWhereEqualTo("team",team);
                                    bmobQuery.setLimit(pageSize).setSkip(pageNo).findObjects(new FindListener<DataWx>() {
                                        @Override
                                        public void done(List<DataWx> list, BmobException e) {
                                            if (e == null) {
                                                emitter.onNext(list);
                                            }else {
                                                emitter.onError(e);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
    }

    /**
     * 创建团队
     * @param teamName
     * @return
     */
    public static Observable<String> createTeamData(final String teamName) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                UserBean user = MyApp.getApp().getUser();
                if (user == null) {
                    emitter.onError(new Throwable("User IS NULL"));
                }
                Team team = new Team();
                team.setAdminUser(user);
                team.setName(teamName);
                team.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            emitter.onNext(s);
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });

    }

    public static Observable<String> addTotoTeam(final String teamId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                UserBean user = MyApp.getApp().getUser();
                Team team = new Team();
                team.setObjectId(teamId);
                user.setTeam(team);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            emitter.onNext("成功");
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

}
