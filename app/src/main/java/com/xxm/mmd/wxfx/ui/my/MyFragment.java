package com.xxm.mmd.wxfx.ui.my;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.MenuAdapter;
import com.xxm.mmd.wxfx.bean.MenuBean;
import com.xxm.mmd.wxfx.ui.AbountActivity;
import com.xxm.mmd.wxfx.ui.HelpActivity;
import com.xxm.mmd.wxfx.ui.LoginActivity;
import com.xxm.mmd.wxfx.ui.SettingActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.xxm.mmd.wxfx.view.RCRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
        initData();
        return view;
    }

    private void initData() {
        List<MenuBean> data = new ArrayList<>();
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

                if (position == 2) {

                    Intent intent = new Intent(getActivity(), ZxingActivity.class);
                    getActivity().startActivity(intent);

                    return;
                }

                MenuBean bean = (MenuBean) adapter.getItem(position);
                startActivtity(bean.getActivityClass());
            }
        });
    }

    private void startActivtity(Class<?> c) {
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

                break;
        }
    }
}
