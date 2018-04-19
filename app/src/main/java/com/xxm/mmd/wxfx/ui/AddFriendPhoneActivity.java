package com.xxm.mmd.wxfx.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.contast.Contast;
import com.xxm.mmd.wxfx.utils.DialogHelp;
import com.xxm.mmd.wxfx.utils.PrefUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendPhoneActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend_phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_friend_phone);
        ButterKnife.bind(this);

        setTitleName("自动加好友");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick(R.id.but_start)
    public void onViewClicked() {

        if (MyApp.getApp().getUser().getVip() > 0) {
            Intent intent = new Intent();
            intent.setAction(Contast.accessBroad);
            intent.putExtra(Contast.BroadType, Contast.addFriendPhone);
            sendBroadcast(intent);
        }else {
            int testNum = PrefUtils.getInt(this, "TestNum", 3);
            if (testNum != 0) {
                Toast.makeText(this, "您还有" + testNum + "次试用机会！！", Toast.LENGTH_SHORT).show();
                PrefUtils.putInt(this,"TestNum",testNum-1);
                Intent intent = new Intent();
                intent.setAction(Contast.accessBroad);
                intent.putExtra(Contast.BroadType, Contast.addFriendPhone);
                sendBroadcast(intent);
            }else{
                DialogHelp.getConfirmDialog(this, "您当前已经没有试用机会，开通会员才可继续使用", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VipActivity.start(AddFriendPhoneActivity.this);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        }

    }
}
