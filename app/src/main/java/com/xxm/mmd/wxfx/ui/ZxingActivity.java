package com.xxm.mmd.wxfx.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.utils.FileUtils;
import com.xxm.mmd.wxfx.utils.ImageUtils;
import com.xxm.mmd.wxfx.utils.SysUtils;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import org.apache.http.client.utils.URIUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ZxingActivity extends BaseActivity {

    private CodeUtils.AnalyzeCallback analyzeCallback;
    private int REQUEST_IMAGE = 0x233;
    private static int CAMERA_REQUEST_CODE = 0x896;


    public static int TEAM_ = 0x4532;
    public static String TEAM_S = "teams";

    boolean ifTeam = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zxing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int intExtra = getIntent().getIntExtra(TEAM_S, 0);
        if (intExtra == TEAM_) {
            ifTeam = true;
        }

        startFragment();
        findViewById(R.id.butToPhoto)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(openAlbumIntent, REQUEST_IMAGE);
                        }else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(intent, REQUEST_IMAGE);
                        }


                    }
                });
        checkPremission();
    }

    private void startFragment() {
        CaptureFragment captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                backActivity(result,true);
            }

            @Override
            public void onAnalyzeFailed() {
                backActivity("",false);
            }
        };
        captureFragment.setAnalyzeCallback(analyzeCallback);


        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (null != data) {
                Uri imageUri = data.getData();
                Log.d("ZxingActivity", imageUri.toString());
                FileUtils.getFilePathByUri(this, data.getData());

                ImageUtils.analyzeBitmap(FileUtils.getFilePathByUri(ZxingActivity.this,imageUri))
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                backActivity(s,true);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("tt", "accept: ",throwable );
                                Toast.makeText(ZxingActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }

    private void checkPremission() {
        final String permission = Manifest.permission.CAMERA;  //相机权限
        final String permission1 = Manifest.permission.WRITE_EXTERNAL_STORAGE; //写入数据权限
        final String permission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission1) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission2) != PackageManager.PERMISSION_GRANTED) {  //先判断是否被赋予权限，没有则申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {  //给出权限申请说明
                ActivityCompat.requestPermissions(ZxingActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
            } else { //直接申请权限
                ActivityCompat.requestPermissions(ZxingActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE); //申请权限，可同时申请多个权限，并根据用户是否赋予权限进行判断
            }
        } else {  //赋予过权限，则直接调用相机拍照
//            openCamera();
//            startFragment();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {  //申请权限的返回值
                int length = grantResults.length;
                final boolean isGranted = length >= 1 && PackageManager.PERMISSION_GRANTED == grantResults[length - 1];
                if (isGranted) {  //如果用户赋予权限，则调用相机
//                    openCamera();
//                    startFragment();
                }else{ //未赋予权限，则做出对应提示
                    Log.d("ZxingActivity", "未获得权限，无法扫描");
                }

        }else
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static final String TAG = "ZxingActivity";
    private void backActivity(String s, boolean isOk) {

        if (ifTeam) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            if (isOk) {
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, s);
                resultIntent.putExtras(bundle);
                ZxingActivity.this.setResult(RESULT_OK, resultIntent);
                ZxingActivity.this.finish();
            }else {
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "图片格式错误");
                resultIntent.putExtras(bundle);
                ZxingActivity.this.setResult(RESULT_OK, resultIntent);
                ZxingActivity.this.finish();
            }
        }else {
            if (!isOk) {
                return;
            }

            Observable.just(s)
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
                            return SysUtils.getStringID(s);
                        }
                    }).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String s) {
                    WeiXinShareUtil.shareDataToWx(s,ZxingActivity.this);
                    finish();
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: ",e );
                    Toast.makeText(ZxingActivity.this, "图片格式错误", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
        }




    }
}
