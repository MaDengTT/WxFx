package com.xxm.mmd.wxfx.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MaDeng on 2017/4/6.
 * 使用方法
 *  mUpdateManager = new UpdateManager(this);
 *  mUpdateManager.showNoticeDialog();
 */
public class UpdateManager {
    private Context mContext; //上下文

    private String apkUrl =
            "a"; //apk下载地址
    private static final String savePath = "/sdcard/updateAPK/"; //apk保存到SD卡的路径
    private static final String saveFileName = savePath + "apkName.apk"; //完整路径名

    private ProgressBar mProgress; //下载进度条控件
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private int progress; //下载进度
    private boolean cancelFlag = false; //取消下载标志位

    private double serverVersion = 2.0; //从服务器获取的版本号
    private double clientVersion = 1.0; //客户端当前的版本号
    private String updateDescription = "更新描述"; //更新内容描述信息
    private boolean forceUpdate = true; //是否强制更新

    private AlertDialog alertDialog1, alertDialog2; //表示提示对话框、进度条对话框
    private TextView tvProgress;
    private TextView tvProgressMax;
    private ProgressDialog downDialog;

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    private String versionNum;

    /**
     * 更新地址
     * @param apkUrl
     */
    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    /**
     * 服务器版本号
     * @param serverVersion
     */
    public void setServerVersion(double serverVersion) {
        this.serverVersion = serverVersion;
    }

    /**
     * 当前版本号
     * @param clientVersion
     */
    public void setClientVersion(double clientVersion) {
        this.clientVersion = clientVersion;
    }

    /**
     * 提示信息
     * @param updateDescription
     */
    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }

    /**
     * 是否强制
     * @param forceUpdate
     */
    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    /** 构造函数 */
    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /** 显示更新对话框 */
    public void showNoticeDialog(boolean isShowLog) {
        //如果版本最新，则不需要更新
        if (serverVersion <= clientVersion){
            if (isShowLog) {
//                ToastUitl.showShort("您的版本不需要更新！！！");
            }
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("发现新版本 ：" + versionNum);
        dialog.setMessage(updateDescription);
        dialog.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
                showDownloadDialog();
            }
        });
        //是否强制更新
        if (forceUpdate == false) {
            dialog.setNegativeButton("待会更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                }
            });
        }
        alertDialog1  = dialog.create();
        alertDialog1.setCancelable(false);
        alertDialog1.show();
    }

    /** 显示进度条对话框 */
    public void showDownloadDialog() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        downDialog = getProgressDialog(mContext, ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setTitle("正在更新");
//        final LayoutInflater inflater = LayoutInflater.from(mContext);
//        View v = inflater.inflate(R.layout_works_to_home_header.softupdate_progress, null);
//        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//        tvProgress = (TextView) v.findViewById(R.id.tv_progress);
//        tvProgressMax = (TextView) v.findViewById(R.id.tv_progress_max);
//
//        dialog.setView(v);
        //如果是强制更新，则不显示取消按钮
//        if (forceUpdate == false) {
//            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                    // TODO Auto-generated method stub
//                    arg0.dismiss();
//                    cancelFlag = false;
//                }
//            });
//        }
//        alertDialog2  = downDialog.create();
        downDialog.setCancelable(false);
        downDialog.show();

        //下载apk
        downloadAPK();
    }

    /** 下载apk的线程 */
    public void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if(!file.exists()){
                        file.mkdir();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do{
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int)(((float)count / length) * 100);
                        //更新进度
                        Message message = Message.obtain();
                        message.arg1 = progress;
                        message.arg2 = length;
                        message.what = DOWNLOADING;
                        mHandler.sendMessage(message);
                        if(numread <= 0){
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWNLOADED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    }while(!cancelFlag); //点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch(Exception e) {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                    Log.e("UpdateManager", e.toString());

                }
            }
        }).start();
    }

    /** 更新UI的handler */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case DOWNLOADING:
                    downDialog.setProgress(progress);
//                    mProgress.setProgress(progress);
//                    tvProgress.setText(msg.arg1+"");
//                    tvProgressMax.setText(100+"");
                    break;
                case DOWNLOADED:
                    if (downDialog != null)
                        downDialog.dismiss();
                    installAPK();
                    break;
                case DOWNLOAD_FAILED:
                    Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
                    if (downDialog != null)
                        downDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private static ProgressDialog getProgressDialog(Context context, int style) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(style);
        dialog.setTitle("下载中...");
        return dialog;
    }
    String authorities = "com.yst.shw.fileprovider";
    /** 下载完成后自动安装apk */
    public void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT < 24) {
            intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        } else {
            Uri apkUri = FileProvider.getUriForFile(mContext, authorities, apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}