package com.xxm.mmd.wxfx.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.HomePage;
import com.xxm.mmd.wxfx.bean.UserBean;
import com.xxm.mmd.wxfx.glide.GlideLoader;

import java.util.List;

/**
 * Created by MaDeng on 2018/4/11.
 */
public class UserCardAdapter extends BaseQuickAdapter<HomePage,BaseViewHolder> {
    public UserCardAdapter(@Nullable List<HomePage> data) {
        super(R.layout.item_user_card,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePage item) {
        helper.setText(R.id.tv_user_name, item.getCardName())
                .setText(R.id.tv_user_info, item.getCardInfo())
                .setText(R.id.tv_user_wx, item.getWxInfo());
        GlideLoader.loadAvatar((ImageView) helper.getView(R.id.iv_avatar),item.getUser().getUseravatar());
        helper.addOnClickListener(R.id.but_add);
    }
}
