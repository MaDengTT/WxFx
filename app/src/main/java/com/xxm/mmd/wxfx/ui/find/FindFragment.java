package com.xxm.mmd.wxfx.ui.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.CircleAdapter;
import com.xxm.mmd.wxfx.ui.BaseFrament;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FindFragment extends BaseFrament {


    Unbinder unbinder;
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    private CircleAdapter adapter;

    public FindFragment() {
        // Required empty public constructor
    }


    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ivTitleBack.setVisibility(View.GONE);
        setTitleName(tvTitle, "发现");
        viewPage.setAdapter(new FindFragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPage);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    public class FindFragmentAdapter extends FragmentPagerAdapter {

        public FindFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? CircleFragment.newInstance(String.valueOf(position), "")
                    : position == 1 ? CircleFragment.newInstance(String.valueOf(position), "")
                    : CircleFragment.newInstance("", "");
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "信息广场" : position == 1 ? "我的团队" : "";
        }
    }

}
