package com.xxm.mmd.wxfx.ui.my;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.MenuAdapter;
import com.xxm.mmd.wxfx.bean.MenuBean;
import com.xxm.mmd.wxfx.bean.Team;
import com.xxm.mmd.wxfx.bean.UserBean;
import com.xxm.mmd.wxfx.glide.GlideLoader;
import com.xxm.mmd.wxfx.ui.AbountActivity;
import com.xxm.mmd.wxfx.ui.EditUserInfoActivity;
import com.xxm.mmd.wxfx.ui.HelpActivity;
import com.xxm.mmd.wxfx.ui.Login2Activity;
import com.xxm.mmd.wxfx.ui.LoginActivity;
import com.xxm.mmd.wxfx.ui.MyCircleActivity;
import com.xxm.mmd.wxfx.ui.SettingActivity;
import com.xxm.mmd.wxfx.ui.TeamActivity;
import com.xxm.mmd.wxfx.ui.VipActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.view.RCRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {


    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.rc_avatar)
    RCRelativeLayout rcAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_info)
    TextView tvUserInfo;
    @BindView(R.id.rl_my_order)
    RelativeLayout rlMyOrder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    Unbinder unbinder;
    @BindView(R.id.rl_my_team)
    RelativeLayout rlMyTeam;
    private MenuAdapter adapter;

    public MyFragment() {
        // Required empty public constructor
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);


        initView();
        initData();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private static final String TAG = "MyFragment";
    private void initData() {
        List<MenuBean> data = new ArrayList<>();
        data.add(new MenuBean("我的发布", MyCircleActivity.class));
        data.add(new MenuBean("设置", SettingActivity.class));
        data.add(new MenuBean("帮助", HelpActivity.class));
        data.add(new MenuBean("关于", AbountActivity.class));
        adapter.setNewData(data);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setUserInfo( UserBean userInfo) {
        initUsetInfo();
        Log.d(TAG, "user");
    }

    private void initUsetInfo() {
        if (MyApp.getApp().getUser() == null) {
            tvUserName.setText("请登录");
            tvUserInfo.setText("点击头像登陆");
        }else{
            String name = MyApp.getApp().getUser().getName();
            tvUserName.setText(TextUtils.isEmpty(name)?MyApp.getApp().getUser().getUsername():name);
            if (MyApp.getApp().getUser().getVip() != null) {
                switch (MyApp.getApp().getUser().getVip()) {
                    case 0:
                        tvUserInfo.setText("普通用户");
                        break;
                    case 1:
                        tvUserInfo.setText("Vip会员");
                        break;
                    case 2:
                        tvUserInfo.setText("团队Vip会员");
                        break;
                }
            }


            GlideLoader.loadAvatar(ivAvatar,MyApp.getApp().getUser().getUseravatar());
            rlMyOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VipActivity.start(getActivity());
                }
            });
            tvUserInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VipActivity.start(getActivity());
                }
            });
        }
    }

    private void initView() {

        initUsetInfo();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MenuAdapter(null);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MenuBean bean = (MenuBean) adapter.getItem(position);

                if (TextUtils.equals(bean.getMenuTitle(), "我的发布")) {
                    if (MyApp.getApp().getUser() == null) {
                        Toast.makeText(getActivity(), "当前未登录！请登录！！！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                startActivtity(bean.getActivityClass());
            }
        });
    }

    public void startActivtity(Class<?> c) {
        if (c != null) {
            Intent intent = new Intent(getActivity(), c);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_avatar, R.id.rc_avatar,R.id.rl_my_team})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                if (MyApp.getApp().getUser() == null) {
                    Login2Activity.start(getActivity());
                }else {
//                    Toast.makeText(getActivity(), "已经登陆", Toast.LENGTH_SHORT).show();
                    EditUserInfoActivity.start(getActivity());
                }
                break;
            case R.id.rc_avatar:
                break;
            case R.id.rl_my_team:
                if (MyApp.getApp().getUser() == null) {
                    if (MyApp.getApp().getUser() == null) {
                        Toast.makeText(getActivity(), "当前未登录！请登录！！！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                BmobUtils.findCurrentUserTeam(false)
                        .subscribe(new Consumer<Team>() {
                            @Override
                            public void accept(Team team) throws Exception {
                                if (TextUtils.isEmpty(team.getObjectId())) {
                                    Toast.makeText(getActivity(), "您还没有团队！请加入团队", Toast.LENGTH_SHORT).show();
                                }else {
                                    TeamActivity.start(getActivity(),team);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: ",throwable );
                                Toast.makeText(getActivity(), "您还没有团队！请加入团队", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}
