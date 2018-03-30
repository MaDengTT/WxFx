package com.xxm.mmd.wxfx.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.utils.BmobUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

public class CreateTeamActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ed_team_name)
    EditText edTeamName;
    @BindView(R.id.but_create)
    Button butCreate;
    private Unbinder bind;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_team;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CreateTeamActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        bind.unbind();
        super.onDestroy();

    }

    private static final String TAG = "CreateTeamActivity";
    @SuppressLint("CheckResult")
    private void createTeam(String name) {
        BmobUtils.createTeamData(name)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("CreateTeamActivity", s);
                        Toast.makeText(CreateTeamActivity.this, "成功创建团队", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ", throwable);
                        Toast.makeText(CreateTeamActivity.this, "错误!! 请稍候重试!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.but_create)
    public void onViewClicked() {
        if (TextUtils.isEmpty(edTeamName.getText().toString())) {
            return;
        }
        createTeam(edTeamName.getText().toString());
    }
}
