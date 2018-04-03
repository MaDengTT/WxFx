package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.FunctionBean;
import com.xxm.mmd.wxfx.glide.GlideLoader;

import java.util.List;

/**
 * Created by MaDeng on 2018/3/27.
 */

public class FunctionAdapter extends BaseQuickAdapter<FunctionBean,BaseViewHolder> {
    public FunctionAdapter(@Nullable List<FunctionBean> data) {
        super(R.layout.item_function_layout,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionBean item) {
        GlideLoader.loadNormal((ImageView) helper.getView(R.id.iv_img),item.getImageId());
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_desc, item.getDesc());
    }
}
