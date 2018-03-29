package com.xxm.mmd.wxfx.ui;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.xxm.mmd.wxfx.R;

/**
 * Created by MaDeng on 2018/3/27.
 */

public class BaseFrament extends Fragment {

    public void setTitleName(TextView title,String s) {
        if (title != null) {
            title.setText(s);
        }
    }
}
