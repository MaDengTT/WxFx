package com.xxm.mmd.wxfx.ui;



import android.content.Context;

import android.content.Intent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import com.xxm.mmd.wxfx.R;

import com.xxm.mmd.wxfx.bean.UpdateSys;
import com.xxm.mmd.wxfx.ui.find.FindFragment;
import com.xxm.mmd.wxfx.ui.function.FunctionFragment;
import com.xxm.mmd.wxfx.ui.home.HomeFragment;
import com.xxm.mmd.wxfx.ui.my.MyFragment;
import com.xxm.mmd.wxfx.utils.BmobUtils;

import com.xxm.mmd.wxfx.utils.SysUtils;
import com.xxm.mmd.wxfx.utils.UpdateManager;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity{

    BottomNavigationBar bottomNavigationBar;

    HomeFragment homeFragment;

    MyFragment myFragment;

    FindFragment findFragment;
    FunctionFragment functionFragment;
    private Fragment[] fragments;

    private int lastindex = 0;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    public static int REQUEST_CODE = 0x222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        update();
        initFragment();
        initView();
    }

    private void initFragment() {
        homeFragment = HomeFragment.newInstance();
        myFragment = MyFragment.newInstance();
        findFragment = FindFragment.newInstance();
        functionFragment = FunctionFragment.newInstance();
        fragments = new Fragment[]{homeFragment, functionFragment,findFragment,myFragment};
    }

    private void initView() {
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        BottomNavigationItem home = new BottomNavigationItem(R.drawable.ic_home_c, "主页")
                .setInactiveIconResource(R.drawable.ic_home_un);
        BottomNavigationItem function = new BottomNavigationItem(R.drawable.ic_function_c, "功能")
                .setInactiveIconResource(R.drawable.ic_function_un);
        BottomNavigationItem find = new BottomNavigationItem(R.drawable.ic_find_c, "发现")
                .setInactiveIconResource(R.drawable.ic_find_un);
        BottomNavigationItem my = new BottomNavigationItem(R.drawable.ic_my_c, "我的")
                .setInactiveIconResource(R.drawable.ic_my_un);


        bottomNavigationBar
                .addItem(home)
                .addItem(function)
                .addItem(find)
                .addItem(my)
                .initialise();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, homeFragment).show(homeFragment).commit();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

                if (lastindex != position) {
                    switchFrament(lastindex,position);
                    lastindex = position;
                }
//                switch (position) {
//                    case 0:
//                        if (lastindex != 0) {
//                            switchFrament(lastindex,0);
//                            lastindex = 0;
//                        }
//                        break;
//                    case 1:
//                        if (lastindex != 1) {
//                            switchFrament(lastindex,1);
//                            lastindex = 1;
//                        }
//                        break;
//                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        bottomNavigationBar.selectTab(0,false);
    }


    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void switchFrament(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.fragment, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {

    }



    private void update() {

        BmobUtils.loadSysDataToBmob()
                .subscribe(new Consumer<UpdateSys>() {
                    @Override
                    public void accept(UpdateSys updateSys) throws Exception {
                        if (updateSys != null) {
                            update(updateSys);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private void update(UpdateSys sys) {
        UpdateManager manager = new UpdateManager(this);

        manager.setClientVersion(SysUtils.getVersionCode(getBaseContext()));
        manager.setServerVersion(Integer.valueOf(sys.getVesion()));
        manager.setForceUpdate(sys.isTrue());
        manager.setUpdateDescription(sys.getMessage());
        manager.setApkUrl(sys.getUpdateP());
        manager.setVersionNum(sys.getVesion());
        manager.showNoticeDialog(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    BmobUtils.addTotoTeam(result)
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
//                                    Log.d(TAG, s);
                                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Log.e("aa", "accept: ", throwable);
                                    Log.d("MainActivity", "加入失败");
                                }
                            });
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    public void startActivtity(Class<?> c) {
        if (c != null) {
            Intent intent = new Intent(this, c);
            startActivity(intent);
        }
    }
    public void startZxing() {

    }

    public void startZxingToTeam() {
        Intent intent = new Intent(this, ZxingActivity.class);
        intent.putExtra(ZxingActivity.TEAM_S, ZxingActivity.TEAM_);
        this.startActivityForResult(intent, MainActivity.REQUEST_CODE);
    }


}
