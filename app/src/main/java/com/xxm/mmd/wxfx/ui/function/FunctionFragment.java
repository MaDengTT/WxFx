package com.xxm.mmd.wxfx.ui.function;


import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.FunctionAdapter;
import com.xxm.mmd.wxfx.bean.FunctionBean;
import com.xxm.mmd.wxfx.ui.AddFriendActivity;
import com.xxm.mmd.wxfx.ui.AddFriendPhoneActivity;
import com.xxm.mmd.wxfx.ui.AutoLikeActivity;
import com.xxm.mmd.wxfx.ui.BaseFrament;
import com.xxm.mmd.wxfx.ui.MainActivity;
import com.xxm.mmd.wxfx.ui.PhoneActivity;
import com.xxm.mmd.wxfx.ui.UpdateActivity;
import com.xxm.mmd.wxfx.ui.ZxingActivity;
import com.xxm.mmd.wxfx.utils.DialogHelp;
import com.xxm.mmd.wxfx.utils.ServiceHelper;
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
        data.add(new FunctionBean(R.drawable.ic_update, "上传", "上传朋友圈至服务器", UpdateActivity.class));
        data.add(new FunctionBean(R.drawable.ic_scan, "扫一扫", "打开扫一扫功能", ZxingActivity.class));
        data.add(new FunctionBean(R.drawable.ic_scan, "通讯录", "生成通讯录号码", PhoneActivity.class));
        data.add(new FunctionBean(R.drawable.ic_scan, "自动加好友", "自动从通讯录添加好友", AddFriendPhoneActivity.class));
        data.add(new FunctionBean(R.drawable.ic_scan, "精准加好友", "精准添加好友", AddFriendActivity.class));
        data.add(new FunctionBean(R.drawable.ic_scan, "自动点赞", "朋友圈自动点赞", AutoLikeActivity.class));

        adapter.setNewData(data);
    }

    private void initView() {
        setTitleName(tvTitle,"功能");
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridMarginDecoration(SysUtils.dp2px(getContext(), 16), SysUtils.dp2px(getContext(), 16)));
        adapter = new FunctionAdapter(null);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FunctionBean item = (FunctionBean) adapter.getItem(position);
                if (item.getaClass() != null) {
                    if (item.getaClass().getName().equals(ZxingActivity.class.getName())) {
                        ((MainActivity)getActivity()).startZxing();
                    }else {

                        if (!ServiceHelper.AccessibilityIsRunning(getContext())) {
                            ServiceHelper.startAccessibilityService(getActivity());
                        }else {
                            ((MainActivity)getActivity()).startActivtity(item.getaClass());
                        }

                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }




}
