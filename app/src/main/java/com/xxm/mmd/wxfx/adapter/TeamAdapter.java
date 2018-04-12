package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.UserBean;
import com.xxm.mmd.wxfx.glide.GlideLoader;

import java.util.List;

/**
 * Created by MaDeng on 2018/4/2.
 */
public class TeamAdapter extends BaseQuickAdapter<UserBean,BaseViewHolder> {

    public TeamAdapter(@Nullable List<UserBean> data) {
        super(R.layout.item_team_layout,data);
    }

    private String adminid;

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        helper.setText(R.id.tv_user_name,item.getUsername());
        GlideLoader.loadNormal((ImageView) helper.getView(R.id.iv_avatar),item.getUseravatar());
//        SwitchCompat view = helper.getView(R.id.switch_post);
//        view.setChecked(item.getPostPerToTeam()==null?false:true);

        if (TextUtils.equals(item.getObjectId(), adminid)) {
            helper.setText(R.id.tv_del,"管理员");

        }else {
            if (TextUtils.equals(adminid, MyApp.getApp().getUser().getObjectId())) {
                helper.setText(R.id.tv_del,"删除");
                helper.addOnClickListener(R.id.tv_del);
            }else {
                helper.setText(R.id.tv_del, "会员");
            }
        }
//        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//            }
//        });

    }
}
