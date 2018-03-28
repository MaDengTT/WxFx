package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.MenuBean;

import java.util.List;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class MenuAdapter extends BaseQuickAdapter<MenuBean,BaseViewHolder> {

    public MenuAdapter(@Nullable List<MenuBean> data) {
        super(R.layout.item_menu_layout,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuBean item) {
        helper.setText(R.id.tv_menu, item.getMenuTitle());
    }
}
