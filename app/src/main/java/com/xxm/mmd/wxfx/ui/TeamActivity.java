package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.TeamAdapter;
import com.xxm.mmd.wxfx.bean.Team;
import com.xxm.mmd.wxfx.bean.UserBean;
import com.xxm.mmd.wxfx.utils.BmobUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class TeamActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private TeamAdapter teamAdapter;

    Team team;

    int pageSize = 10;
    int pageNo = 0;

    public static void start(Context context, Team team) {
        Intent starter = new Intent(context, TeamActivity.class);
        starter.putExtra("teamId", team);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        team = (Team) getIntent().getSerializableExtra("teamId");
        initView();
        initData();
    }

    private static final String TAG = "TeamActivity";
    private void initData() {
        BmobUtils.findTeamUsers(team,pageSize,pageNo)
                .subscribe(new Consumer<List<UserBean>>() {
                    @Override
                    public void accept(List<UserBean> userBeans) throws Exception {
                        setData(userBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ",throwable );
                    }
                });
    }

    private void initView() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        teamAdapter = new TeamAdapter(null);
        recycler.setAdapter(teamAdapter);

        teamAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.tv_del:
                        UserBean item = teamAdapter.getItem(position);
                        BmobUtils.removeUserToTeam(item.getObjectId())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Toast.makeText(TeamActivity.this, s, Toast.LENGTH_SHORT).show();
                                        adapter.remove(position);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e(TAG, "accept: ",throwable );
                                        Toast.makeText(TeamActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                }
            }
        });
    }

    public void setData(List<UserBean> data) {
        teamAdapter.setNewData(data);
    }
}
