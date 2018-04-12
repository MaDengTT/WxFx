package com.xxm.mmd.wxfx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.contast.Contast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_phone);
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

        Intent intent = new Intent();
        intent.setAction(Contast.accessBroad);
        intent.putExtra(Contast.BroadType, Contast.addFriendPhone);
        sendBroadcast(intent);

    }
}
