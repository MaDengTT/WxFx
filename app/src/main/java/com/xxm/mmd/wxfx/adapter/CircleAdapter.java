package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.glide.GlideLoader;
import com.xxm.mmd.wxfx.ui.BigImageActivity;
import com.xxm.mmd.wxfx.view.MultiImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class CircleAdapter extends BaseQuickAdapter<CirlceBean,BaseViewHolder> {

    boolean ifDel = false;

    public boolean isIfDel() {
        return ifDel;
    }

    public void setIfDel(boolean ifDel) {
        this.ifDel = ifDel;
    }

    public CircleAdapter(@Nullable List<CirlceBean> data) {
        super(R.layout.item_circle,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CirlceBean item) {
        MultiImageView multiImage = (MultiImageView)helper.getView(R.id.multiImage);

        helper.setText(R.id.tv_user_name, item.getUserName()).setText(R.id.tv_content,item.getContent());
        multiImage.setList(item.getImageUrls());
        GlideLoader.loadAvatar((ImageView) helper.getView(R.id.iv_user_avatar),item.getAvatarUrl());
        helper.addOnClickListener(R.id.tv_share)
                .addOnClickListener(R.id.tv_del)
                .setVisible(R.id.tv_del, ifDel)
        .setText(R.id.tv_time,item.getTime());

        multiImage.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String[] s = new String[item.getImageUrls().size()];
                item.getImageUrls().toArray(s);
                BigImageActivity.start(mContext,s,position);
            }
        });

//        List<String> imageUrls = item.getImageUrls();
//        if (imageUrls == null) {
//            imageUrls = new ArrayList<>();
//        }
//        for(int i = 0;i<helper.getAdapterPosition();i++) {
//            imageUrls.add("http://bmob-cdn-17447.b0.upaiyun.com/2018/03/29/6c2150a6e1d54b80902f45cf2f8e5092.jpg");
//        }
//        multiImage.setList(imageUrls);
    }
}
