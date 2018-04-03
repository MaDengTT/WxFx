package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.UserBean;
import com.xxm.mmd.wxfx.utils.BmobUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv_phone)
    AppCompatEditText tvPhone;
    @BindView(R.id.tv_vCode)
    AppCompatEditText tvVCode;
    @BindView(R.id.tv_but_vCode)
    TextView tvButVCode;
    @BindView(R.id.tv_but_login)
    TextView tvButLogin;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tvSend.setText("注册");
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.start(LoginActivity.this);
            }
        });
        tvSend.setVisibility(View.VISIBLE);
    }

    private static final String TAG = "LoginActivity";

    @OnClick({R.id.tv_but_vCode, R.id.tv_but_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_but_vCode:

                BmobUtils.requestSMSLOGCODE(tvPhone.getText().toString())
                        .map(new Function<Integer, String>() {
                            @Override
                            public String apply(Integer integer) throws Exception {
                                Log.d(TAG, "integer:" + integer);
                                return String.valueOf(integer);
                            }
                        }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ", throwable);
                    }
                });

                break;
            case R.id.tv_but_login:
                BmobUtils.signOrLoginByMobilePhone(tvPhone.getText().toString(), tvVCode.getText().toString())
                        .subscribe(new Consumer<UserBean>() {
                            @Override
                            public void accept(UserBean userBean) throws Exception {
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
//                                Log.d(TAG, userBean.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
//                                Log.e(TAG, "accept: ",throwable );
                                Toast.makeText(LoginActivity.this, "登陆失败请稍候重试！！！", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}
