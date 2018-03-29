package com.xxm.mmd.wxfx.ui.function;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.FunctionAdapter;
import com.xxm.mmd.wxfx.bean.FunctionBean;
import com.xxm.mmd.wxfx.ui.BaseFrament;
import com.xxm.mmd.wxfx.utils.SysUtils;
import com.xxm.mmd.wxfx.view.GridMarginDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FunctionFragment extends BaseFrament {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    private View rootView;
    private RecyclerView recyclerView;
    private FunctionAdapter adapter;

    public FunctionFragment() {
        // Required empty public constructor
    }

    public static FunctionFragment newInstance() {
        FunctionFragment fragment = new FunctionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_function, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        initData();
        return rootView;
    }

    private void initData() {
        List<FunctionBean> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new FunctionBean(0, "", ""));
            data.add(new FunctionBean(0, "", ""));
        }

        adapter.setNewData(data);
    }

    private void initView() {
        setTitleName(tvTitle,"功能");
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridMarginDecoration(SysUtils.dp2px(getContext(), 16), SysUtils.dp2px(getContext(), 16)));
        adapter = new FunctionAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
