package com.xxm.mmd.wxfx.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.CircleAdapter;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.glide.BannerGlideLoader;
import com.xxm.mmd.wxfx.ui.BaseFrament;
import com.xxm.mmd.wxfx.ui.MainActivity;
import com.xxm.mmd.wxfx.ui.UpdateActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends BaseFrament implements View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private CardView cvUpdate, cvScan;

    ProgressDialog dialog;

    private View view;

    Banner banner;

    CircleAdapter adapter;

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
        unbinder = ButterKnife.bind(this, view);
        initView();

        initData();

        List<String> images = new ArrayList<>();
        images.add("http://bmob-cdn-17447.b0.upaiyun.com/2018/03/29/6c2150a6e1d54b80902f45cf2f8e5092.jpg");
        images.add("http://bmob-cdn-17447.b0.upaiyun.com/2018/03/29/6c2150a6e1d54b80902f45cf2f8e5092.jpg");
        images.add("http://bmob-cdn-17447.b0.upaiyun.com/2018/03/29/6c2150a6e1d54b80902f45cf2f8e5092.jpg");
        images.add("http://bmob-cdn-17447.b0.upaiyun.com/2018/03/29/6c2150a6e1d54b80902f45cf2f8e5092.jpg");
        initBanner(images);
        return view;
    }

    private void initData() {

        List<CirlceBean> data = new ArrayList<>();
        for(int i = 0;i<10;i++) {
            data.add(new CirlceBean());
        }

        setData(data);

    }
    private void setData(List<CirlceBean> data) {
        adapter.setNewData(data);
    }


    private void initBanner(List<String> images) {
//        banner.setImageLoader(new BannerGlideLoader());
//        banner.setImages(images);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerGlideLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    //    private void initBannerImage() {
//        BmobUtils.getImage()
//                .subscribe(new Consumer<BannerImage>() {
//                    @Override
//                    public void accept(BannerImage bannerImage) throws Exception {
//                        if (bannerImage != null && !TextUtils.isEmpty(bannerImage.getImageUrl())) {
//                            GlideLoader.loadNormal(ivBanner,R.drawable.ic_banner);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.d("img", "accept: 没有图片");
//                    }
//                });
//    }
    private void initView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CircleAdapter(null);
        recyclerView.setAdapter(adapter);

//        ivBanner = view.findViewById(R.id.iv_banner);

        setTitleName((TextView) view.findViewById(R.id.tv_title), "微信转发助手");

        banner = view.findViewById(R.id.iv_banner);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("请稍后");
        dialog.setCanceledOnTouchOutside(false);

        cvUpdate = view.findViewById(R.id.cv_update);
        cvScan = view.findViewById(R.id.cv_scan);

        cvScan.setOnClickListener(this);
        cvUpdate.setOnClickListener(this);
    }

    public void startZxing() {
        Intent intent = new Intent(getActivity(), ZxingActivity.class);
        startActivityForResult(intent, MainActivity.REQUEST_CODE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cv_scan:
                startZxing();
                break;

            case R.id.cv_update:
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        banner.startAutoPlay();
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
