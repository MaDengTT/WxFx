package com.xxm.mmd.wxfx.utils;

import android.text.TextUtils;
import android.util.Log;

import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.bean.BannerImage;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.bean.FCode;
import com.xxm.mmd.wxfx.bean.HomePage;
import com.xxm.mmd.wxfx.bean.OrderBean;
import com.xxm.mmd.wxfx.bean.Team;
import com.xxm.mmd.wxfx.bean.UpdateSys;
import com.xxm.mmd.wxfx.bean.UserBean;
import com.xxm.mmd.wxfx.ui.UpdateActivity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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
                    public Observable<String> apply(final List<String> strings) throws Exception {


                        return findCurrentUserTeam(false).flatMap(new Function<Team, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(Team team) throws Exception {
                                DataWx wx = new DataWx();
                                wx.setText(text);
                                wx.setImage(strings);
                                wx.setIfShowSquare(isShowSq);
                                wx.setIfShowTeam(isShowTeam);

                                if (isShowTeam) {
                                    wx.setTeam(MyApp.getApp().getTeam());
                                    Log.d("BmobUtils", MyApp.getApp().getTeam().getName());
                                }
                                UserBean user = MyApp.getApp().getUser();
                                if (user != null) {
                                    wx.setUser(user);
                                }
                                return  UpdateData(wx);
                            }
                        });

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
                            emitter.onComplete();
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
                            emitter.onComplete();
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
                            emitter.onComplete();
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
                            emitter.onComplete();
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
                            emitter.onComplete();
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
                            emitter.onComplete();
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
                            emitter.onComplete();
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
                            emitter.onComplete();
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
        Log.d("BmobUtils", "pageNo:" + pageNo);
        if (TextUtils.isEmpty(teamID)) {
            return Observable.create(new ObservableOnSubscribe<List<DataWx>>() {
                @Override
                public void subscribe(final ObservableEmitter<List<DataWx>> emitter) throws Exception {
                    BmobQuery<DataWx> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("ifShowSquare", true);
                    bmobQuery.include("user");
                    bmobQuery.order("-createdAt");
                    bmobQuery.setLimit(pageSize).setSkip(pageNo*pageSize)
                            .findObjects(new FindListener<DataWx>() {
                                @Override
                                public void done(List<DataWx> list, BmobException e) {
                                    if (e == null) {
                                        emitter.onNext(list);
                                        emitter.onComplete();
                                        Log.d("BmobUtils", "list.size():" + list.size());
                                    }else {
                                        emitter.onError(e);
                                    }
                                }
                            });
                }
            });

        }else {
            Log.d("BmobUtils", teamID);
            return  Observable.create(new ObservableOnSubscribe<List<DataWx>>() {
                @Override
                public void subscribe(final ObservableEmitter<List<DataWx>> emitter) throws Exception {
                    BmobQuery<DataWx> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("ifShowTeam", true);
                    bmobQuery.include("user");
                    Team team = new Team();
                    team.setObjectId(teamID);
                    bmobQuery.addWhereEqualTo("team",new BmobPointer(team));
                    bmobQuery.order("-createdAt");
                    bmobQuery.setLimit(pageSize).setSkip(pageNo*pageSize)
                            .findObjects(new FindListener<DataWx>() {
                                @Override
                                public void done(List<DataWx> list, BmobException e) {
                                    if (e == null) {
                                        emitter.onNext(list);
                                        emitter.onComplete();
                                        Log.d("BmobUtils", "list.size():" + list.size());
                                    }else {
                                        emitter.onError(e);
                                    }
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
                team.setAdminId(user.getObjectId());
                team.setName(teamName);
                team.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            emitter.onNext(s);
                            emitter.onComplete();
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
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取当前用户团队
     * @return
     */
    public static Observable<Team> findCurrentUserTeam(final boolean ifNetWorlk) {
        return Observable.create(new ObservableOnSubscribe<Team>() {
            @Override
            public void subscribe(final ObservableEmitter<Team> emitter) throws Exception {

                if (!ifNetWorlk&&MyApp.getApp().getTeam() != null) {
                    emitter.onNext(MyApp.getApp().getTeam());
                    emitter.onComplete();
                }else {
                    UserBean userBean = MyApp.getApp().getUser();
                    BmobQuery<UserBean> teamQ = new BmobQuery<>();
//                teamQ.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    teamQ.include("team");
                    teamQ.getObject(userBean.getObjectId(), new QueryListener<UserBean>() {
                        @Override
                        public void done(UserBean userBean, BmobException e) {
                            if (e == null) {
                                MyApp.getApp().setTeam(userBean.getTeam());
                                if (userBean.getTeam() == null) {
                                    emitter.onNext(new Team());
                                }else {
                                    emitter.onNext(userBean.getTeam());
                                }
                                emitter.onComplete();
                            }else {
                                emitter.onError(e);
                            }
                        }
                    });
                }

            }
        });
    }

    public static Observable<HomePage> getMyHomePage() {
        return Observable.create(new ObservableOnSubscribe<HomePage>() {
            @Override
            public void subscribe(final ObservableEmitter<HomePage> emitter) throws Exception {
                BmobQuery<HomePage> query = new BmobQuery<>();
                query.addWhereEqualTo("user", new BmobPointer(MyApp.getApp().getUser()));
                query.findObjects(new FindListener<HomePage>() {
                    @Override
                    public void done(List<HomePage> list, BmobException e) {
                        if (e == null) {
                            if (list.size() != 0) {
                                emitter.onNext(list.get(0));
                                emitter.onComplete();
                            }else {
                                emitter.onNext(new HomePage());
                                emitter.onComplete();
                            }
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<List<HomePage>> findHomeBean(final int pageSize, final int pageNo) {
        return Observable.create(new ObservableOnSubscribe<List<HomePage>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<HomePage>> emitter) throws Exception {
                BmobQuery<HomePage> query = new BmobQuery<>();
                query.include("user");
                query.order("-createdAt");
                query.setLimit(pageSize).setSkip(pageNo*pageSize)
                        .findObjects(new FindListener<HomePage>() {
                            @Override
                            public void done(List<HomePage> list, BmobException e) {
                                if (e == null) {
                                    emitter.onNext(list);
                                    emitter.onComplete();
                                }else {
                                    emitter.onError(e);
                                }
                            }
                        });
            }
        });
    }

    public static Observable<String> putHomePage(final String objectId, final List<String> images, final String cardName, final String cardInfo, final String cardWx, final String cardphone) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                if (images.size() != 0) {
                    String[] s = new String[images.size()];
                    images.toArray(s);
                    uploadFiles(s)
                            .subscribe(new Observer<List<String>>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(List<String> strings) {
                                    HomePage page = new HomePage();
                                    page.setImages(strings);
                                    page.setCardName(cardName);
                                    page.setCardInfo(cardInfo);
                                    page.setWxInfo(cardWx);
                                    page.setPhoneNum(cardphone);
                                    page.setUser(MyApp.getApp().getUser());

                                    if (TextUtils.isEmpty(objectId)) {

                                        page.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    emitter.onNext(s);
                                                    emitter.onComplete();
                                                }else {
                                                    emitter.onError(e);
                                                }
                                            }
                                        });
                                    }else {
                                        page.update(objectId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    emitter.onNext("成功");
                                                    emitter.onComplete();
                                                }else {
                                                    emitter.onError(e);
                                                }
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    emitter.onError(e);
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }else {
                    HomePage page = new HomePage();
//                    page.setImages(strings);
                    page.setCardName(cardName);
                    page.setCardInfo(cardInfo);
                    page.setWxInfo(cardWx);
                    page.setPhoneNum(cardphone);
                    page.setUser(MyApp.getApp().getUser());
                    if (TextUtils.isEmpty(objectId)) {

                        page.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    emitter.onNext(s);
                                    emitter.onComplete();
                                }else {
                                    emitter.onError(e);
                                }
                            }
                        });
                    }else {
                        page.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    emitter.onNext("成功");
                                    emitter.onComplete();
                                }else {
                                    emitter.onError(e);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static Observable<List<UserBean>> findTeamUsers(final Team team, final int pageSize, final int pageNo) {
        return Observable.create(new ObservableOnSubscribe<List<UserBean>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<UserBean>> emitter) throws Exception {
                BmobQuery<UserBean> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("team", new BmobPointer(team));
                bmobQuery.addQueryKeys("objectId,username,useravatar,postPerToTeam");
                bmobQuery.setLimit(pageSize).setSkip(pageNo*pageSize)
                        .findObjects(new FindListener<UserBean>() {
                            @Override
                            public void done(List<UserBean> list, BmobException e) {
                                if (e == null) {
                                    emitter.onNext(list);
                                    emitter.onComplete();
//                                    Log.d("BmobUtils", "list.size():" + list.size());
//                                    Log.d("BmobUtils", list.get(0).getUsername());
                                }else {
                                    emitter.onError(e);
                                }
                            }
                        });
            }
        });
    }

    public static Observable<UserBean> registUser(final String userName, final String password) {
        return Observable.create(new ObservableOnSubscribe<UserBean>() {
            @Override
            public void subscribe(final ObservableEmitter<UserBean> emitter) throws Exception {
                UserBean userBean = new UserBean();
                userBean.setUsername(userName);
                userBean.setPassword(password);
                userBean.setName(userName);
                userBean.setVip(0);
                userBean.signUp(new SaveListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            emitter.onNext(userBean);
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<List<DataWx>> findDataWxToUser(final String userid, final int pagesize, final int pageno) {
        return Observable.create(new ObservableOnSubscribe<List<DataWx>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<DataWx>> emitter) throws Exception {
                UserBean userBean = new UserBean();
                userBean.setObjectId(userid);
                BmobQuery<DataWx> dataWxBmobQuery = new BmobQuery<>();
                dataWxBmobQuery.addWhereEqualTo("user", new BmobPointer(userBean));
                dataWxBmobQuery.include("user");
                dataWxBmobQuery.setLimit(pagesize).setSkip(pageno * pagesize).findObjects(new FindListener<DataWx>() {
                    @Override
                    public void done(List<DataWx> list, BmobException e) {
                        if (e == null) {
                            emitter.onNext(list);
                            emitter.onComplete();
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static List<CirlceBean> dataWxToCirlceBean(List<DataWx> dataWxList) {
        List<CirlceBean> cirlceBeanList = new ArrayList<>();
        for (int i = 0; i < dataWxList.size(); i++) {
            DataWx dataWx = dataWxList.get(i);
            CirlceBean bean = new CirlceBean();
            bean.setObjeId(dataWx.getObjectId());
            bean.setContent(dataWx.getText());
            bean.setImageUrls(dataWx.getImage());
            bean.setTime(dataWx.getCreatedAt());
            if (dataWx.getUser() != null) {
                bean.setAvatarUrl(dataWx.getUser().getUseravatar());
                bean.setUserName(dataWx.getUser().getName());
//                                Log.d(TAG, dataWx.getUser().getMobilePhoneNumber() + "");
            }
            cirlceBeanList.add(bean);
        }
        return cirlceBeanList;
    }

    /**
     * 删除条目
     * @param objID
     * @return
     */
    public static Observable<String> removeDataWx(final String objID) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                DataWx dataWx = new DataWx();
                dataWx.setObjectId(objID);
                dataWx.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            emitter.onNext("成功删除");
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<String> removeUserToTeam(final String userid) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                UserBean userBean = new UserBean();
//                userBean.setTeam(new Team());
                userBean.remove("team");
                userBean.update(userid, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            emitter.onNext("成功");
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<String> activateVip(final int vipnum) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                UserBean userBean = new UserBean();
                userBean.setVip(vipnum);
                BmobDate date = new BmobDate(getDateStr(31));
                userBean.setVipdate(date);
                userBean.update(MyApp.getApp().getUser().getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            emitter.onNext("成功");
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Date getDateStr( long dayAddNum) {
        Date nowDate = new Date(System.currentTimeMillis());
        Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
        return newDate2;
    }

    public static Observable<String> deleFcode(final FCode fCode) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                fCode.setTrue(false);
                fCode.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            emitter.onNext("成功");
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<FCode> getFCodeToNet(final String fcode) {
        return Observable.create(new ObservableOnSubscribe<FCode>() {
            @Override
            public void subscribe(final ObservableEmitter<FCode> emitter) throws Exception {
                BmobQuery<FCode> query = new BmobQuery<>();
                query.addWhereEqualTo("FCode", fcode);
                query.findObjects(new FindListener<FCode>() {
                    @Override
                    public void done(List<FCode> list, BmobException e) {
                        if (e == null&&list.size()!=0) {
                            emitter.onNext(list.get(0));
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<String> BmobPay(final int payType, final String name, final String body, final double price) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                BP.pay(name, body, price, payType, new PListener() {
                    @Override
                    public void orderId(String s) {
//                        emitter.onNext("订单号:"+s);
                        putOrder(s)
                        .subscribe();
                    }

                    @Override
                    public void succeed() {
                        emitter.onNext("支付成功");
                        emitter.onComplete();
                    }

                    @Override
                    public void fail(int i, String s) {
                        emitter.onError(new Throwable("支付失败：code "+ i +" msg "+s));
                    }
                });
            }
        });
    }

    public static Observable<String> putOrder(final String orderid) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                OrderBean orderBean = new OrderBean();
                orderBean.setUser(MyApp.getApp().getUser());
                orderBean.setOrderid(orderid);
                orderBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            emitter.onNext(s);
                            emitter.onComplete();
                        }else {
                            emitter.onError(e);
                        }
                    }
                });
            }
        });
    }

    public static Observable<String> BmobPayQuery(final String orderID) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                BP.query(orderID, new QListener() {
                    @Override
                    public void succeed(String s) {
                        Log.d("BmobUtils", s);
                        emitter.onNext(s);
                        emitter.onComplete();
                    }

                    @Override
                    public void fail(int i, String s) {
                        emitter.onError(new Exception("i:"+i+" msg:"+s));
                    }
                });
            }
        });
    }

}
