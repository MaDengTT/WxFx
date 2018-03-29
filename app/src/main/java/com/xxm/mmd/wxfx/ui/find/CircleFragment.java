package com.xxm.mmd.wxfx.ui.find;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.CircleAdapter;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.ui.BaseFrament;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CircleFragment extends BaseFrament {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;



    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private CircleAdapter adapter;

    public CircleFragment() {
        // Required empty public constructor
    }


    public static CircleFragment newInstance(String param1, String param2) {
        CircleFragment fragment = new CircleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        List<CirlceBean> data = new ArrayList<>();
        for(int i = 0;i<10;i++) {
            data.add(new CirlceBean());
        }

        setData(data);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CircleAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    private void setData(List<CirlceBean> data) {
        adapter.setNewData(data);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
