package com.xxm.mmd.wxfx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.contast.Contast;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendActivity extends BaseActivity {

    @BindView(R.id.ed_name)
    EditText edName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        String s = edName.getText().toString();
        if (TextUtils.isEmpty(s)) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Contast.accessBroad);
        intent.putExtra(Contast.BroadType, Contast.addFriendText);
        intent.putExtra(Contast.BroadNameID, s);
        sendBroadcast(intent);


    }
}
