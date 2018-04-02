package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.glide.GlideApp;
import com.xxm.mmd.wxfx.utils.GlideLoader;
import com.xxm.mmd.wxfx.utils.SysUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BigImageActivity extends AppCompatActivity {


    @BindView(R.id.viewPage)
    ViewPager viewPage;

    public static void start(Context context,String[] urls,int postion) {
        Intent starter = new Intent(context, BigImageActivity.class);
        starter.putExtra("urls",urls);
        starter.putExtra("postion", postion);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ButterKnife.bind(this);

        setData(getIntent().getStringArrayExtra("urls"));

    }

    private void setData(final String[] images) {
        viewPage.setPageMargin(SysUtils.px2dp(getBaseContext(), 15));
        viewPage.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                PhotoView view = new PhotoView(BigImageActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.CENTER);
                com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(view,images[position]);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        viewPage.setCurrentItem(getIntent().getIntExtra("postion",0));
    }

}
