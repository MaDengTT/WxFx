package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.CirlceBean;
import com.xxm.mmd.wxfx.view.MultiImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class CircleAdapter extends BaseQuickAdapter<CirlceBean,BaseViewHolder> {
    public CircleAdapter(@Nullable List<CirlceBean> data) {
        super(R.layout.item_circle,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CirlceBean item) {
        MultiImageView multiImage = (MultiImageView)helper.getView(R.id.multiImage);

        List<String> imageUrls = item.getImageUrls();
        if (imageUrls == null) {
            imageUrls = new ArrayList<>();
        }
        for(int i = 0;i<helper.getAdapterPosition();i++) {
            imageUrls.add("http://bmob-cdn-17447.b0.upaiyun.com/2018/03/27/5f34db0b67f947eaa05922c8bb1a9e2d.jpg");
        }
        multiImage.setList(imageUrls);
    }
}
