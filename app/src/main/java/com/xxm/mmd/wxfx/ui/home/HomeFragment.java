package com.xxm.mmd.wxfx.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.BannerImage;
import com.xxm.mmd.wxfx.ui.AbountActivity;
import com.xxm.mmd.wxfx.ui.BaseFrament;
import com.xxm.mmd.wxfx.ui.HelpActivity;
import com.xxm.mmd.wxfx.ui.MainActivity;
import com.xxm.mmd.wxfx.ui.SettingActivity;
import com.xxm.mmd.wxfx.ui.UpdateActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.xxm.mmd.wxfx.utils.BmobUtils;

import io.reactivex.functions.Consumer;


public class HomeFragment extends BaseFrament implements View.OnClickListener{

    private CardView cvUpdate,cvScan,cvSetting,cvHelp, cvAbount;

    ProgressDialog dialog;

    private ImageView ivBanner;
    private View view;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    private void initBannerImage() {
        BmobUtils.getImage()
                .subscribe(new Consumer<BannerImage>() {
                    @Override
                    public void accept(BannerImage bannerImage) throws Exception {
                        if (bannerImage != null && !TextUtils.isEmpty(bannerImage.getImageUrl())) {
                            Glide.with(getContext()).load(bannerImage.getImageUrl())
                                    .error(R.drawable.ic_banner).into(ivBanner);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("img", "accept: 没有图片");
                    }
                });
    }
    private void initView() {

        ivBanner = view.findViewById(R.id.iv_banner);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("请稍后");
        dialog.setCanceledOnTouchOutside(false);

        cvUpdate = view.findViewById(R.id.cv_update);
        cvScan = view.findViewById(R.id.cv_scan);
        cvSetting = view.findViewById(R.id.cv_setting);
        cvHelp = view.findViewById(R.id.cv_help);
        cvAbount = view.findViewById(R.id.cv_about);

        cvAbount.setOnClickListener(this);
        cvScan.setOnClickListener(this);
        cvSetting.setOnClickListener(this);
        cvUpdate.setOnClickListener(this);
        cvHelp.setOnClickListener(this);
    }

    public void startZxing() {
        Intent intent = new Intent(getActivity(), ZxingActivity.class);
        startActivityForResult(intent,MainActivity.REQUEST_CODE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_about:
                AbountActivity.start(getActivity());
//                String s = "u49dfngkdj";
//                String s1 = SysUtils.getStringToBase64(s);
//                Log.d("MainActivity", s1);
//                String s2 = SysUtils.getBase64ToString(s1);
//                Log.d("MainActivity", s2);

//                String s1 = SysUtils.getStringUrl(s);
//                Log.d("MainActivity", s1);
//                String s2 = SysUtils.getStringID(s1);
//                Log.d("MainActivity", s2);

                break;
            case R.id.cv_help:
                HelpActivity.start(getActivity());
                break;
            case R.id.cv_scan:
                startZxing();
                break;
            case R.id.cv_setting:
                SettingActivity.start(getActivity());
                break;
            case R.id.cv_update:
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                startActivity(intent);
                break;
        }
    }
}
