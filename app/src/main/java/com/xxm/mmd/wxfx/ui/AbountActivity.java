package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xxm.mmd.wxfx.R;

public class AbountActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AbountActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_abount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
    }
}
