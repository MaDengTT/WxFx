package com.xxm.mmd.wxfx.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.utils.PermissionHelper;
import com.xxm.mmd.wxfx.utils.PrefUtils;

import abc.abc.abc.AdManager;
import abc.abc.abc.nm.cm.ErrorCode;
import abc.abc.abc.nm.sp.SplashViewSettings;
import abc.abc.abc.nm.sp.SpotListener;
import abc.abc.abc.nm.sp.SpotManager;
import abc.abc.abc.nm.sp.SpotRequestListener;

public class SplashActivity extends BaseActivity implements SplashADListener{

    private PermissionHelper permissionHelper;

    private SplashAD splashAD;
    private static final String SKIP_TEXT = "点击跳过 %d";
    private ViewGroup viewById;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView();
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
//        getActionBar().hide();

        viewById = findViewById(R.id.splash);
        permissionHelper = new PermissionHelper(this);

        permissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
//                runApp();
//                runAd();
            }
        });

        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            runApp();
//            runAd();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (permissionHelper.isAllRequestedPermissionGranted()) {
                runApp();
//                runAd();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                permissionHelper.applyPermissions();
            }
        }


        if (PrefUtils.getBoolean(this, "isOneOpen", true)) {
            PrefUtils.putBoolean(this,"isOneOpen",true);
            PrefUtils.putInt(this,"TestNum",3);
        }

//        MainActivity.start(this);
//        finish();

    }

    private void runAd() {
        if (!TextUtils.isEmpty(MyApp.AD_APP_Id)) {
            fetchSplashAD(this,viewById,null,MyApp.AD_APP_Id,MyApp.AD_POS_Id,this,0);
        }
    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity        展示广告的activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或下面的注意事项。
     * @param appId           应用ID
     * @param posId           广告位ID
     * @param adListener      广告状态监听器
     * @param fetchDelay      拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        splashAD = new SplashAD(activity, adContainer,/* skipContainer,*/ appId, posId, adListener, fetchDelay);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void runApp() {

        SpotManager.getInstance(this).requestSpot(new SpotRequestListener() {
            @Override
            public void onRequestSuccess() {
                Log.d("SplashActivity", "onRequestSuccess");
            }

            @Override
            public void onRequestFailed(int i) {
                logError("请求插播广告失败，errorCode: %s", i);
                switch (i) {
                    case ErrorCode.NON_NETWORK:
                        logError("网络异常");
                        break;
                    case ErrorCode.NON_AD:
                        logError("暂无视频广告");
                        break;
                    default:
                        logError("请稍后再试");
                        break;
                }
            }
        });

        setupSplashAd();
    }

    private void setupSplashAd() {

        SplashViewSettings splashViewSettings = new SplashViewSettings();
        splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);
        splashViewSettings.setTargetClass(MainActivity.class);
        splashViewSettings.setSplashViewContainer(viewById);

        SpotManager.getInstance(this).showSplash(this,
                splashViewSettings, new SpotListener() {
                    @Override
                    public void onShowSuccess() {
                        logInfo("开屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        logError("开屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                logError("网络异常");
                                break;
                            case ErrorCode.NON_AD:
                                logError("暂无开屏广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                logError("开屏资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                logError("开屏展示间隔限制");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                logError("开屏控件处在不可见状态");
                                break;
                            default:
                                logError("errorCode: %d", errorCode);
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        logDebug("开屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        logDebug("开屏被点击");
                        logInfo("是否是网页广告？%s", isWebPage ? "是" : "不是");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(this).onDestroy();
    }

    @Override
    public void onADDismissed() {
        Log.d("SplashActivity", "结束");
    }

    @Override
    public void onNoAD(AdError adError) {
        Log.d("SplashActivity", "失败");
    }

    @Override
    public void onADPresent() {
        Log.d("SplashActivity", "成功");
    }

    @Override
    public void onADClicked() {
        Log.d("SplashActivity", "点击");
    }

    @Override
    public void onADTick(long l) {
        Log.d("SplashActivity", "剩余毫秒数");
//        String.format(SKIP_TEXT, Math.round(l / 1000f));
    }

    //防止用户返回键退出APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
