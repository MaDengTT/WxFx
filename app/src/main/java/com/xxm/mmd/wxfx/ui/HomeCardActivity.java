package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.HomePage;
import com.xxm.mmd.wxfx.contast.Contast;
import com.xxm.mmd.wxfx.utils.ServiceHelper;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeCardActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.ed_card_name)
    TextView edCardName;
    @BindView(R.id.tv_card_info)
    TextView tvCardInfo;
    @BindView(R.id.ed_card_info)
    TextView edCardInfo;
    @BindView(R.id.tv_wx_name)
    TextView tvWxName;
    @BindView(R.id.ed_wx_name)
    TextView edWxName;
    @BindView(R.id.tv_phone_name)
    TextView tvPhoneName;
    @BindView(R.id.ed_phone_name)
    TextView edPhoneName;
    @BindView(R.id.but_send)
    Button butSend;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private HomePage home;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_card;
    }



    public static void start(Context context, HomePage page) {
        Intent starter = new Intent(context, HomeCardActivity.class);
        starter.putExtra("home",page);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        home = (HomePage) getIntent().getSerializableExtra("home");

        initView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initView() {

        setTitleName(home.getUser().getName());
        setDataToImageNet(home.getImages());
        edCardName.setText(home.getCardName());
        edCardInfo.setText(home.getCardInfo());
        edWxName.setText(home.getWxInfo());
        edPhoneName.setText(home.getPhoneNum());
    }
    public void setDataToImageNet(List<String> dataToImage) {
        if (dataToImage == null) {
            return;
        }
        for(int i = 0;i<dataToImage.size();i++) {
            switch (i) {
                case 0:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv1,dataToImage.get(i));
                    break;
                case 1:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv2,dataToImage.get(i));
                    break;
                case 2:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv3,dataToImage.get(i));
                    break;
            }
        }
    }


    @OnClick(R.id.but_send)
    public void onViewClicked() {
        if (!ServiceHelper.AccessibilityIsRunning(this)) {
            ServiceHelper.startAccessibilityService(this);
        } else {
            Intent intent = new Intent();
            intent.setAction(Contast.accessBroad);
            intent.putExtra(Contast.BroadType, Contast.addFriendText);
            intent.putExtra(Contast.BroadNameID, home.getWxInfo());
            this.sendBroadcast(intent);
        }
    }
}
