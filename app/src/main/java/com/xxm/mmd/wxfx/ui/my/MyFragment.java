package com.xxm.mmd.wxfx.ui.my;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.xxm.mmd.wxfx.ui.HelpActivity;
import com.xxm.mmd.wxfx.ui.LoginActivity;
import com.xxm.mmd.wxfx.ui.MyCircleActivity;
import com.xxm.mmd.wxfx.ui.SettingActivity;
import com.xxm.mmd.wxfx.ui.TeamActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.view.RCRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
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

        initView();
        initUserInfo();
        initData();
        return view;
    }

    private void initUserInfo() {
//        MyApp.getApp().UpdateUser();
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.include("team");

        UserBean user = MyApp.getApp().getUser();
        if (user != null) {
            tvUserName.setText(user.getUsername());
            GlideLoader.loadAvatar(ivAvatar,user.getUseravatar());
        }
        BmobUtils.findCurrentUserTeam(false).subscribe(new Consumer<Team>() {
            @Override
            public void accept(Team team) throws Exception {
                Log.d("MyFragment", team.getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: ",throwable );
            }
        });

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

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MenuAdapter(null);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                if (position == 2) {
//
//                    Intent intent = new Intent(getActivity(), ZxingActivity.class);
//                    getActivity().startActivity(intent);
//
//                    return;
//                }

                MenuBean bean = (MenuBean) adapter.getItem(position);
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
                LoginActivity.start(getActivity());
                break;
            case R.id.rc_avatar:
                break;
            case R.id.rl_my_team:
                BmobUtils.findCurrentUserTeam(false)
                        .subscribe(new Consumer<Team>() {
                            @Override
                            public void accept(Team team) throws Exception {
                                TeamActivity.start(getActivity(),team);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: ",throwable );
                                Toast.makeText(getActivity(), "没有团队", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}
