package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;

import java.util.List;

/**
 * Created by MaDeng on 2018/4/10.
 */
public class PhoneAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public PhoneAdapter(@Nullable List<String> data) {
        super(R.layout.item_text,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_phone, helper.getAdapterPosition() + "、" + item);
    }
}
