package com.xxm.mmd.wxfx.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.CircleAdapter;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.glide.BannerGlideLoader;
import com.xxm.mmd.wxfx.ui.BaseFrament;
import com.xxm.mmd.wxfx.ui.MainActivity;
import com.xxm.mmd.wxfx.ui.UpdateActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


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

        BmobUtils.getDataWxs("",10,0)
                .flatMap(new Function<List<DataWx>, ObservableSource<List<CirlceBean>>>() {
                    @Override
                    public ObservableSource<List<CirlceBean>> apply(List<DataWx> dataWxes) throws Exception {
                        List<CirlceBean> cirlceBeanList = new ArrayList<>();
                        for (int i = 0; i < dataWxes.size(); i++) {
                            DataWx dataWx = dataWxes.get(i);
                            CirlceBean bean = new CirlceBean();
                            bean.setContent(dataWx.getText());
                            bean.setImageUrls(dataWx.getImage());
                            if (dataWx.getUser() != null) {
                                bean.setAvatarUrl(dataWx.getUser().getUseravatar());
                                bean.setUserName(dataWx.getUser().getUsername());
//                                Log.d(TAG, dataWx.getUser().getMobilePhoneNumber() + "");
                            }
                            cirlceBeanList.add(bean);
                        }
                        return Observable.just(cirlceBeanList);
                    }
                })
                .subscribe(new Consumer<List<CirlceBean>>() {
                    @Override
                    public void accept(List<CirlceBean> cirlceBeans) throws Exception {
                        setData(cirlceBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ",throwable );
                    }
                });


    }

    private static final String TAG = "HomeFragment";
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

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_share:
                        CirlceBean item = (CirlceBean) adapter.getItem(position);
                        WeiXinShareUtil.shareDataToWx(item,getActivity());
                        break;
                }
            }
        });
    }

    public void startZxing() {
        Intent intent = new Intent(getActivity(), ZxingActivity.class);
        getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE);
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
