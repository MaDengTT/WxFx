package com.xxm.mmd.wxfx.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.DialogHelp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import c.b.BP;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class VipActivity extends AppCompatActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.cl_vip2)
    ConstraintLayout clVip2;
    @BindView(R.id.cl_vip1)
    ConstraintLayout clVip1;
    private ProgressDialog waitDialog;

    public static void start(Context context) {
        Intent starter = new Intent(context, VipActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvSend.setText("F码通道");
        tvSend.setVisibility(View.VISIBLE);
    }




    private static final String TAG = "VipActivity";
    @OnClick({R.id.cl_vip2, R.id.cl_vip1,R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                FCodeActivity.start(this);
                break;
            case R.id.cl_vip2:
                break;
            case R.id.cl_vip1:

                DialogHelp.getSelectDialog(this, "支付方式", new String[]{"支付宝", "微信"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int payInt = 0x11891;
                        switch (i) {
                            case 0:
                                payInt = BP.PayType_Alipay;
                                break;
                            case 1:
                                payInt = BP.PayType_Wechat;
                                break;
                        }
                        if (payInt == 0x11891) {
                            return;
                        }
                        BmobUtils.BmobPay(payInt,"普通会员","普通会员",0.1)
                                .doOnSubscribe(new Consumer<Disposable>() {
                                    @Override
                                    public void accept(Disposable disposable) throws Exception {
                                        waitDialog = DialogHelp.getWaitDialog(VipActivity.this, "支付中");
                                        waitDialog.show();
                                    }
                                })
                                .doOnTerminate(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        if (waitDialog != null && waitDialog.isShowing()) {
                                            waitDialog.dismiss();
                                        }
                                    }
                                })
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(String s) {
                                        Toast.makeText(VipActivity.this, s, Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onNext: "+s);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, "onError: ",e );
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }).show();

                break;
        }
    }
}
