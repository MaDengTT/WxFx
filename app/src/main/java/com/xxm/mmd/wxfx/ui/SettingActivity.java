package com.xxm.mmd.wxfx.ui;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.UpdateSys;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.FileUtils;
import com.xxm.mmd.wxfx.utils.ImageUtils;
import com.xxm.mmd.wxfx.utils.SysUtils;
import com.xxm.mmd.wxfx.utils.UpdateManager;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SettingActivity extends BaseActivity implements AccessibilityManager.AccessibilityStateChangeListener{

    private static final String TAG = "SettingActivity";
    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }

    private AccessibilityManager accessibilityManager;

    Switch aSwitch;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.ll_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUtils.loadSysDataToBmob()
                        .subscribe(new Consumer<UpdateSys>() {
                            @Override
                            public void accept(UpdateSys updateSys) throws Exception {
                                if (updateSys != null) {
                                    Log.d(TAG, updateSys.toString());
                                    if(SysUtils.getVersionCode(getBaseContext()) == Integer.valueOf(updateSys.getVesion()))
                                        Toast.makeText(SettingActivity.this, "您的应用不需要跟新", Toast.LENGTH_SHORT).show();
                                    else
                                        update(updateSys);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(SettingActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        aSwitch = findViewById(R.id.switch1);

        findViewById(R.id.ll_action).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Log.d(TAG, "is : " + isServiceEnabled());
//                if (!isServiceEnabled()) {
                if(!serviceIsRunning(".service.MyService")){
//                    aSwitch.set
                    startAccessibilityService();
                }else {
                    stopAccessibilityService();
                }
            }
        });

        findViewById(R.id.ll_clera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                io.reactivex.Observable.just(1)
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/fx";
                                    FileUtils.deleteFile(new File(filePath));
                                    Toast.makeText(SettingActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: ",throwable );
                            }
                        });

            }
        });

        findViewById(R.id.ll_share)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Disposable s = BmobUtils.loadSysDataToBmob()
                                .flatMap(new Function<UpdateSys, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(UpdateSys sys) throws Exception {
                                        return ImageUtils.createImage(sys.getUpdateP())
                                                .flatMap(new Function<Bitmap, ObservableSource<String>>() {
                                                    @Override
                                                    public ObservableSource<String> apply(Bitmap bitmap) throws Exception {
                                                        return FileUtils.saveQRCode(bitmap);
                                                    }
                                                });
                                    }
                                }).subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        WeiXinShareUtil.shareSingleImage(SettingActivity.this, s);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toast.makeText(SettingActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);

    }

    @Override
    protected void onDestroy() {
        accessibilityManager.removeAccessibilityStateChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (serviceIsRunning(".service.MyService")) {
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }
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


    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Log.d(TAG, getPackageName() + "/.service.MyService");
        for (AccessibilityServiceInfo info : accessibilityServices) {
            Log.d(TAG, info.getId());
            if (info.getId().equals(getPackageName() + "/.service.MyService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断自己的应用的Service是否在运行
     *
     * @return
     */
    private boolean serviceIsRunning(String servicePath) {
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            Log.d(TAG, info.service.getClassName());
            if (info.service.getClassName().equals(getPackageName() + servicePath)) {
                return true;
            }
        }
        return false;
    }


//    /**
//     * 判断当前服务是否正在运行
//     * */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public static boolean isRunning() {
//        if(service == null) {
//            return false;
//        }
//        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
//        AccessibilityServiceInfo info = service.getServiceInfo();
//        if(info == null) {
//            return false;
//        }
//        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
//        Iterator<AccessibilityServiceInfo> iterator = list.iterator();
//
//        boolean isConnect = false;
//        while (iterator.hasNext()) {
//            AccessibilityServiceInfo i = iterator.next();
//            if(i.getId().equals(info.getId())) {
//                isConnect = true;
//                break;
//            }
//        }
//        if(!isConnect) {
//            return false;
//        }
//        return true;
//    }

    /**
     * 前往设置界面开启服务
     */
    private void startAccessibilityService() {
        new AlertDialog.Builder(this)
                .setTitle("开启辅助功能")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("使用此项功能需要您开启辅助功能")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐式调用系统设置界面
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                }).create().show();
    }    /**
     * 前往设置界面开启服务
     */
    private void stopAccessibilityService() {
        new AlertDialog.Builder(this)
                .setTitle("关闭辅助功能")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("请手动关闭此功能")
                .setPositiveButton("立即关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐式调用系统设置界面
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                }).create().show();
    }


    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        if (serviceIsRunning(".service.MyService")) {
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }
    }
}
