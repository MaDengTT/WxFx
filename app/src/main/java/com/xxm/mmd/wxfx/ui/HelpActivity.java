package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.HelpAdapter;
import com.xxm.mmd.wxfx.bean.HelpBean;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends BaseActivity {

    RecyclerView recyclerView;

    public static void start(Context context) {
        Intent starter = new Intent(context, HelpActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private List<HelpBean> getData() {
        List<HelpBean> data = new ArrayList<>();
        data.add(new HelpBean("1、如何生成朋友圈", R.drawable.help_1));
        data.add(new HelpBean("2、如何发送朋友圈", R.drawable.help_2));
        data.add(new HelpBean("3、如何获得二维码", R.drawable.help_3));
        data.add(new HelpBean("4、如何扫描二维码", R.drawable.help_4));
        data.add(new HelpBean("5、如何发送朋友圈", R.drawable.help_5));

        return data;
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        HelpAdapter adapter = new HelpAdapter(null);
        recyclerView.setAdapter(adapter);

        adapter.setNewData(getData());
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener(){
//
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                HelpBean item = (HelpBean) adapter.getItem(position);
//                item.isShwoImage = !item.isShwoImage;
//                adapter.notifyItemChanged(position);
//            }
//        });
    }
}
