package com.xxm.mmd.wxfx.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by MaDeng on 2018/4/13.
 */
public abstract class WaitObserver<T> implements Observer<T> {

    String message = "请稍后";
    private final ProgressDialog waitDialog;


    public WaitObserver(Context context,String message) {
        if (!TextUtils.isEmpty(message)) {
            this.message = message;
        }
        waitDialog = DialogHelp.getWaitDialog(context, this.message);
    }

    @Override
    public void onSubscribe(Disposable d) {
        waitDialog.show();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("aa", "onError: ",e );
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }

    @Override
    public void onComplete() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }
}
