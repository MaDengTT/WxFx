package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;

import java.util.List;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class CircleImageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    public CircleImageAdapter(@Nullable List<String> data) {
        super(R.layout.item_circle_img,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
