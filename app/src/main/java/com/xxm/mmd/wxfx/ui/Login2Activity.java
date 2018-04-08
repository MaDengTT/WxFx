package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;

import com.xxm.mmd.wxfx.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login2Activity extends BaseActivity {

    @BindView(R.id.ed_login_name)
    AppCompatEditText edLoginName;
    @BindView(R.id.ed_login_password)
    AppCompatEditText edLoginPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    public static void start(Context context) {
        Intent starter = new Intent(context, Login2Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ed_login_password, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ed_login_password:
                break;
            case R.id.tv_login:
                break;
        }
    }
}
