package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.UserBean;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class Login2Activity extends BaseActivity {

    @BindView(R.id.ed_login_name)
    AppCompatEditText edLoginName;
    @BindView(R.id.ed_login_password)
    AppCompatEditText edLoginPassword;
    @BindView(R.id.tv_login)
    Button tvLogin;
    @BindView(R.id.tv_send)
    TextView tvSend;

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

        initView();
    }

    private void initView() {
        setTitleName("登录");
    }

    @OnClick({R.id.ed_login_password, R.id.tv_login,R.id.tv_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ed_login_password:
                break;
            case R.id.tv_login:
                login(edLoginName.getText().toString(), edLoginPassword.getText().toString());
                break;
            case R.id.tv_regist:
                RegisterActivity.start(Login2Activity.this);
                break;
        }
    }

    private void login(String userName, String password) {
        BmobUser.loginByAccount(userName, password, new LogInListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    Toast.makeText(Login2Activity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new UserBean());
                    finish();
                } else {
                    Log.d("Login2Activity", "登陆失败");
                }
            }
        });
    }

}
