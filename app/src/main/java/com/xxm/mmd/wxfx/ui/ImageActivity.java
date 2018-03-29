package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.utils.FileUtils;
import com.xxm.mmd.wxfx.utils.ImageUtils;
import com.xxm.mmd.wxfx.utils.SysUtils;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import java.io.File;

import io.reactivex.functions.Consumer;

public class ImageActivity extends BaseActivity {

    String bitmapS;
    ImageView iv;
    private Bitmap bitmap;

    private Button but_load;
    private Button but_share;
    public static void start(Context context,String s) {
        Intent starter = new Intent(context, ImageActivity.class);
        starter.putExtra("bitmap",s);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        bitmapS = getIntent().getStringExtra("bitmap");
        initView();
    }

    private static final String TAG = "ImageActivity";
    private void initView() {
        setTitleName("我的二维码");
        but_load = findViewById(R.id.but_load);
        but_share = findViewById(R.id.but_share);

        but_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    return;
                }
                FileUtils.saveQRCode(bitmap)
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                WeiXinShareUtil.shareSingleImage(ImageActivity.this,s);
                                Log.d(TAG, "accept: "+s);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: ",throwable );
                            }
                        });

            }
        });

        but_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    return;
                }
                FileUtils.saveQRCode(bitmap)
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
//                                Toast.makeText(ImageActivity.this, "图片保存至" + s, Toast.LENGTH_SHORT).show();
                                FileUtils.saveImageToGallery(ImageActivity.this,new File(s));
                                Toast.makeText(ImageActivity.this, "图片保存至" + s, Toast.LENGTH_SHORT).show();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: ",throwable );
                            }
                        });
            }
        });

        iv = findViewById(R.id.iv_image);
        if (!TextUtils.isEmpty(bitmapS)) {
            String urls = SysUtils.getStringUrl(bitmapS);
            ImageUtils.createImage(urls).subscribe(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    ImageActivity.this.bitmap = bitmap;
                    iv.setImageBitmap(bitmap);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.e(TAG, "accept: ", throwable);
                }
            });
        }
    }



}
