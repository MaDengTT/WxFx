package com.xxm.mmd.wxfx.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.UserCardAdapter;
import com.xxm.mmd.wxfx.bean.HomePage;
import com.xxm.mmd.wxfx.contast.Contast;
import com.xxm.mmd.wxfx.glide.BannerGlideLoader;
import com.xxm.mmd.wxfx.ui.BaseFrament;
import com.xxm.mmd.wxfx.ui.EditHomeActivity;
import com.xxm.mmd.wxfx.ui.HomeCardActivity;
import com.xxm.mmd.wxfx.ui.Login2Activity;
import com.xxm.mmd.wxfx.ui.MainActivity;
import com.xxm.mmd.wxfx.ui.UpdateActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.DialogHelp;
import com.xxm.mmd.wxfx.utils.ServiceHelper;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class HomeFragment extends BaseFrament implements View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private CardView cvUpdate, cvScan;

    ProgressDialog dialog;

    private View view;

    Banner banner;


    UserCardAdapter adapter;
    private View viewTop;
    private int pageSize = 10;
    private int pageNo = 0;
    private ProgressDialog waitDialog;

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
        view = inflater.inflate(R.layout.content_home, container, false);
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

        BmobUtils.findHomeBean(pageSize, pageNo)
                .subscribe(new Observer<List<HomePage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        waitDialog.show();
                    }

                    @Override
                    public void onNext(List<HomePage> homePages) {
                        setData(homePages);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        waitDialog.dismiss();
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        waitDialog.dismiss();
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                    }
                });


    }

    private static final String TAG = "HomeFragment";

    private void setData(List<HomePage> data) {
        if (pageNo == 0) {
            adapter.setNewData(data);
        }else {
            adapter.addData(data);
        }

        if (data.size() == pageSize) {
            adapter.loadMoreComplete();
            pageNo++;
        }else {
            adapter.loadMoreEnd();
        }
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

        waitDialog = DialogHelp.getWaitDialog(getActivity(), "请稍后");

        viewTop = getLayoutInflater().inflate(R.layout.home_top, null, false);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                initData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new UserCardAdapter(null);
        adapter.addHeaderView(viewTop);
        adapter.setLoadMoreView(new SimpleLoadMoreView());
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initData();
            }
        },recyclerView);
        recyclerView.setAdapter(adapter);


//        ivBanner = view.findViewById(R.id.iv_banner);

        setTitleName((TextView) this.view.findViewById(R.id.tv_title), "微信转发助手");

        banner = viewTop.findViewById(R.id.iv_banner);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("请稍后");
        dialog.setCanceledOnTouchOutside(false);

        cvUpdate = viewTop.findViewById(R.id.cv_update);
        cvScan = viewTop.findViewById(R.id.cv_scan);

        cvScan.setOnClickListener(this);
        cvUpdate.setOnClickListener(this);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePage page = ((HomePage) adapter.getItem(position));
                HomeCardActivity.start(getActivity(), page);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.but_add:
                        if (!ServiceHelper.AccessibilityIsRunning(getActivity())) {
                            ServiceHelper.startAccessibilityService(getActivity());
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Contast.accessBroad);
                            intent.putExtra(Contast.BroadType, Contast.addFriendText);
                            intent.putExtra(Contast.BroadNameID, ((HomePage) adapter.getItem(position)).getWxInfo());
                            getActivity().sendBroadcast(intent);
                        }
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

    @OnClick(R.id.fab)
    public void onViewClicked() {
        if (MyApp.getApp().getUser() == null) {
            Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
            Login2Activity.start(getActivity());
        } else {
            EditHomeActivity.start(getActivity());
        }
    }
}
