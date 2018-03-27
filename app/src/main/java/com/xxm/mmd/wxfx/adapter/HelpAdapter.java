package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.HelpBean;

import java.util.List;

/**
 * Created by MaDeng on 2018/3/27.
 */

public class HelpAdapter extends BaseQuickAdapter<HelpBean,BaseViewHolder> {

    public HelpAdapter(@Nullable List<HelpBean> data) {
        super(R.layout.item_help_layout,data );
    }

    @Override
    protected void convert(BaseViewHolder helper, HelpBean item) {
        helper.setText(R.id.tv_help, item.getTitle());
        ImageView view = (ImageView) helper.getView(R.id.iv_help_image);
        Glide.with(mContext).load(item.getImageID()).into(view);
        helper.setVisible(R.id.iv_help_image, item.isShwoImage);
    }
}
