package com.xxm.mmd.wxfx.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.CircleAdapter;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.WaitObserver;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyCircleActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private CircleAdapter adapter;

    int pageSize = 10;
    int pageNo = 0;
    private Observer<List<DataWx>> observer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_circle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initData() {
        observer = new Observer<List<DataWx>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("MyCircleActivity", "onSubscribe.currentThreadTimeMillis():" + SystemClock.currentThreadTimeMillis());
            }

            @Override
            public void onNext(List<DataWx> dataWxList) {
                setData(BmobUtils.dataWxToCirlceBean(dataWxList));
                pageNo++;
            }

            @Override
            public void onError(Throwable e) {
                Log.e("aaa", "accept: ", e);
                adapter.loadMoreFail();
            }

            @Override
            public void onComplete() {
                Log.d("MyCircleActivity", "onComplete.currentThreadTimeMillis():" + SystemClock.currentThreadTimeMillis());
            }
        };
        BmobUtils.findDataWxToUser(MyApp.getApp().getUser().getObjectId(),pageSize,pageNo)
                .subscribe(observer);

    }

    private void initView() {

//        tvSend.setText("编辑");
//        tvSend.setVisibility(View.VISIBLE);
//        tvSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CircleAdapter(null);
        adapter.setIfDel(true);
        recycler.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                CirlceBean item = (CirlceBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_del:
                        BmobUtils.removeDataWx(item.getObjeId()).subscribe(new WaitObserver<String>(MyCircleActivity.this, "") {

                            @Override
                            public void onNext(String s) {
                                adapter.remove(position);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Toast.makeText(MyCircleActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    case R.id.tv_share:
                        WeiXinShareUtil.shareDataToWx(item,MyCircleActivity.this);
                        break;
                }
            }
        });
        adapter.setLoadMoreView(new SimpleLoadMoreView());
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initData();
            }
        },recycler);
    }

    public void setData(List<CirlceBean> data) {
        if (pageNo == 0) {
            adapter.setNewData(data);
        }else {
            adapter.addData(data);
        }

        if (data.size() == pageSize) {
            adapter.loadMoreComplete();
        }else {
            adapter.loadMoreEnd();
        }
    }
}
