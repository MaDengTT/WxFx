package com.xxm.mmd.wxfx.ui.find;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.CircleAdapter;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.bean.DataWx;
import com.xxm.mmd.wxfx.bean.Team;
import com.xxm.mmd.wxfx.ui.BaseFrament;
import com.xxm.mmd.wxfx.ui.CreateTeamActivity;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class CircleFragment extends BaseFrament {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.but_create)
    Button butCreate;
    @BindView(R.id.but_add)
    Button butAdd;
    @BindView(R.id.cl_empty)
    ConstraintLayout clEmpty;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private String mParam1;
    private String mParam2;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private CircleAdapter adapter;

    private int pagesize = 10;
    private int pageno = 0;

    Team team;

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
        if (mParam1.equals("0")) {
            clEmpty.setVisibility(View.GONE);
            initData("");
        } else if (mParam1.equals("1")) {
            clEmpty.setVisibility(View.VISIBLE);
            initTeam();
        }
        return view;
    }

    private void initTeam() {
        BmobUtils.findCurrentUserTeam(false)
                .subscribe(new Consumer<Team>() {
                    @Override
                    public void accept(Team team) throws Exception {
                        if (team != null) {
                            CircleFragment.this.team = team;
                            initData(team.getObjectId());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ", throwable);
                    }
                });
    }


    private static final String TAG = "CircleFragment";

    private void initData(String teamID) {
        BmobUtils.getDataWxs(teamID, pagesize, pageno)
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
                                Log.d(TAG, dataWx.getUser().getMobilePhoneNumber() + "");
                            }
                            cirlceBeanList.add(bean);
                        }
                        return Observable.just(cirlceBeanList);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new Consumer<List<CirlceBean>>() {
                    @Override
                    public void accept(List<CirlceBean> cirlceBeans) throws Exception {
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                        setData(cirlceBeans);
                        pageno++;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (srl.isRefreshing()) {
                            srl.setRefreshing(false);
                        }
                        Log.e(TAG, "accept: ", throwable);
                        if (adapter.isLoadMoreEnable()) {
                            adapter.loadMoreFail();
                        }
                    }
                });
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CircleAdapter(null);
        adapter.setLoadMoreView(new SimpleLoadMoreView());
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mParam1.equals("0")) {
                    initData("");
                } else if (mParam1.equals("1")) {
                    initData(team.getObjectId());
                }
            }
        }, recyclerView);
        recyclerView.setAdapter(adapter);

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

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageno = 0;
                if (mParam1.equals("0")) {
                    initData("");
                } else if (mParam1.equals("1")) {
                    initTeam();
                }
            }
        });
    }

    private void setData(List<CirlceBean> data) {

        clEmpty.setVisibility(View.GONE);
        if (pageno == 0) {
            adapter.setNewData(data);
        } else {
            adapter.addData(data);
        }
        if (data.size() == pagesize) {
            adapter.loadMoreComplete();
        } else {
            adapter.loadMoreEnd();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.but_create, R.id.but_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.but_create:
                CreateTeamActivity.start(getActivity());
                break;
            case R.id.but_add:

                BmobUtils.addTotoTeam("07820a5ed9")
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.d(TAG, s);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: ", throwable);
                            }
                        });

                break;
        }
    }
}
