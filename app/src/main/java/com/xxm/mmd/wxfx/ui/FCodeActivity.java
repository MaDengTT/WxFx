package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.FCode;
import com.xxm.mmd.wxfx.utils.BmobUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class FCodeActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.but_put)
    Button butPut;

    public static void start(Context context) {
        Intent starter = new Intent(context, FCodeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcode);
        ButterKnife.bind(this);
    }

    private static final String TAG = "FCodeActivity";
    @OnClick(R.id.but_put)
    public void onViewClicked() {

        BmobUtils.getFCodeToNet(edCode.getText().toString())
                .flatMap(new Function<FCode, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final FCode fCode) throws Exception {
                        if (!fCode.getTrue()) {
                            throw new Exception("激活码已经被使用！！！");
                        }
                        return BmobUtils.activateVip(fCode.getVipnum())
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String s) throws Exception {
                                        return BmobUtils.deleFcode(fCode);
                                    }
                                });
                    }
                }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(FCodeActivity.this, s, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ",e );
                Toast.makeText(FCodeActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
