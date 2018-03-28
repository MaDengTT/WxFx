package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.FunctionBean;

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

    }
}
