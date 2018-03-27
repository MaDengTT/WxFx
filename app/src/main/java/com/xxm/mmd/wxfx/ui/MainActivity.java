package com.xxm.mmd.wxfx.ui;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.xxm.mmd.wxfx.BuildConfig;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.BannerImage;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.bean.UpdateSys;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.FileUtils;
import com.xxm.mmd.wxfx.utils.ImageUtils;
import com.xxm.mmd.wxfx.utils.SysUtils;
import com.xxm.mmd.wxfx.utils.UpdateManager;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import abc.abc.abc.nm.bn.BannerManager;
import abc.abc.abc.nm.bn.BannerViewListener;
import abc.abc.abc.nm.cm.ErrorCode;


import abc.abc.abc.nm.sp.SpotListener;
import abc.abc.abc.nm.sp.SpotManager;
import abc.abc.abc.nm.sp.SpotRequestListener;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    private int REQUEST_CODE = 0x222;

    private CardView cvUpdate,cvScan,cvSetting,cvHelp, cvAbount;

    ProgressDialog dialog;

    private ImageView ivBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpotManager.getInstance(this).requestSpot(new SpotRequestListener() {
            @Override
            public void onRequestSuccess() {
                Log.d("SplashActivity", "onRequestSuccess");
            }

            @Override
            public void onRequestFailed(int i) {
                Log.d("RequesSplashActivity", "onFailde:" + i);
            }
        });

        initView();

        update();

//        initBannerView();

        initBannerImage();

    }



    private void initBannerImage() {
        BmobUtils.getImage()
                .subscribe(new Consumer<BannerImage>() {
                    @Override
                    public void accept(BannerImage bannerImage) throws Exception {
                        if (bannerImage != null && !TextUtils.isEmpty(bannerImage.getImageUrl())) {
                            Glide.with(MainActivity.this).load(bannerImage.getImageUrl())
                                    .error(R.drawable.ic_banner).into(ivBanner);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("img", "accept: 没有图片");
                    }
                });
    }


    private void initBannerView() {
//        LinearLayout banner = findViewById(R.id.ll_banner);
        View bannerView = BannerManager.getInstance(this).getBannerView(this, new BannerViewListener() {
            @Override
            public void onRequestSuccess() {
                Log.d("MainActivity", "Success");
            }

            @Override
            public void onSwitchBanner() {
                Log.d("MainActivity", "Switch");
            }

            @Override
            public void onRequestFailed() {
                Log.d("MainActivity", "Failed");
            }
        });
//        banner.addView(bannerView);
    }


    private void initYouMi() {
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);
        SpotManager.getInstance(this).showSpot(this, new SpotListener() {
            @Override
            public void onShowSuccess() {
                Log.d("MainActivity", "showSuccess".toString());
            }

            @Override
            public void onShowFailed(int i) {
                Log.d("MainActivity", "i:" + i);
            }

            @Override
            public void onSpotClosed() {
                if (BuildConfig.DEBUG) Log.d("MainActivity", "结束");
            }

            @Override
            public void onSpotClicked(boolean b) {
                Log.d("MainActivity", "isClicked:" + b);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (SpotManager.getInstance(this).isSlideableSpotShowing()) {
//            SpotManager.getInstance(this).hideSlideableSpot();
            return;
        }else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
//        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SpotManager.getInstance(this).onDestroy();
//        SpotManager.getInstance(this).onAppExit();
//        BannerManager.getInstance(this).onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
//        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initYouMi();
    }

    private void update() {

        BmobUtils.loadSysDataToBmob()
                .subscribe(new Consumer<UpdateSys>() {
                    @Override
                    public void accept(UpdateSys updateSys) throws Exception {
                        if (updateSys != null) {
//                            Log.d(TAG, updateSys.toString());
                            update(updateSys);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(MainActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void update(UpdateSys sys) {
        UpdateManager manager = new UpdateManager(this);

        manager.setClientVersion(SysUtils.getVersionCode(getBaseContext()));
        manager.setServerVersion(Integer.valueOf(sys.getVesion()));
        manager.setForceUpdate(sys.isTrue());
        manager.setUpdateDescription(sys.getMessage());
        manager.setApkUrl(sys.getUpdateP());
        manager.setVersionNum(sys.getVesion());
        manager.showNoticeDialog(true);
    }

    private void initView() {

        ivBanner = findViewById(R.id.iv_banner);

        dialog = new ProgressDialog(this);
        dialog.setTitle("请稍后");
        dialog.setCanceledOnTouchOutside(false);

        cvUpdate = findViewById(R.id.cv_update);
        cvScan = findViewById(R.id.cv_scan);
        cvSetting = findViewById(R.id.cv_setting);
        cvHelp = findViewById(R.id.cv_help);
        cvAbount = findViewById(R.id.cv_about);

        cvAbount.setOnClickListener(this);
        cvScan.setOnClickListener(this);
        cvSetting.setOnClickListener(this);
        cvUpdate.setOnClickListener(this);
        cvHelp.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.d("MainActivity", result);
                    getDataToWx(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void startZxing() {
        Intent intent = new Intent(MainActivity.this, ZxingActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    public void getDataToWx(String id) {
        if (dialog != null) {
            dialog.show();
            Log.d("MainActivity", "dialog");
        }
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
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(dialog.isShowing())
                            dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<DataWx>() {
                    @Override
                    public void accept(DataWx wx) throws Exception {

                        WeiXinShareUtil.sharePhotosToWx(MainActivity.this,wx.getText(),wx.getImage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("tt", "accept: ",throwable );
                    }
                });
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_about:
                AbountActivity.start(this);
//                String s = "u49dfngkdj";
//                String s1 = SysUtils.getStringToBase64(s);
//                Log.d("MainActivity", s1);
//                String s2 = SysUtils.getBase64ToString(s1);
//                Log.d("MainActivity", s2);

//                String s1 = SysUtils.getStringUrl(s);
//                Log.d("MainActivity", s1);
//                String s2 = SysUtils.getStringID(s1);
//                Log.d("MainActivity", s2);

                break;
            case R.id.cv_help:
                HelpActivity.start(this);
                break;
            case R.id.cv_scan:
                startZxing();
                break;
            case R.id.cv_setting:
                SettingActivity.start(this);
                break;
            case R.id.cv_update:
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(intent);
                break;
        }
    }
}
