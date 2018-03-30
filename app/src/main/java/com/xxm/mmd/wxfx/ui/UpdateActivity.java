package com.xxm.mmd.wxfx.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xxm.mmd.wxfx.BuildConfig;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.ImageAdapter;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.GlideLoader;

import java.util.ArrayList;
import java.util.List;

import abc.abc.abc.nm.sp.SpotListener;
import abc.abc.abc.nm.sp.SpotManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class UpdateActivity extends BaseActivity {

    @BindView(R.id.sw_to_square)
    Switch swToSquare;
    @BindView(R.id.sw_to_team)
    Switch swToTeam;
    private int IMAGE_PICKER = 0x55;

    private List<String> images;

    RecyclerView recyclerView;
    private ImageAdapter adapter;
    private ImagePicker imagePicker;

    private EditText editText;

    private TextView send;

    ProgressDialog dialog;
    private Unbinder bind;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind = ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        dialog.setTitle("请稍后");
        dialog.setCanceledOnTouchOutside(false);
        images = new ArrayList<>();
        images.add("0000");
        initView();
        initImagePicker();

        initYouMi();
    }


    @Override
    public void onBackPressed() {
        if (SpotManager.getInstance(this).isSlideableSpotShowing()) {
            SpotManager.getInstance(this).hideSlideableSpot();
//            return;
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(this).onDestroy();
        bind.unbind();
//        SpotManager.getInstance(this).onAppExit();
//        BannerManager.getInstance(this).onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initYouMi();
    }

    private void initYouMi() {
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);
        SpotManager.getInstance(this).showSlideableSpot(this, new SpotListener() {
            @Override
            public void onShowSuccess() {
                Log.d("MainActivity", "showSuccess".toString());
            }

            @Override
            public void onShowFailed(int i) {
                Log.d("MainActivity", "i:" + i);
            }

            @Override
            public void onSpotClosed() {
                if (BuildConfig.DEBUG) Log.d("MainActivity", "结束");
            }

            @Override
            public void onSpotClicked(boolean b) {
                Log.d("MainActivity", "isClicked:" + b);
            }
        });
    }

    private static final String TAG = "UpdateActivity";

    private void initView() {

        setTitleName("发送朋友圈");
        editText = findViewById(R.id.et_content);
        recyclerView = findViewById(R.id.recycler);
        send = findViewById(R.id.tv_send);
        send.setVisibility(View.VISIBLE);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getImageSize() == 9) {
                    Toast.makeText(UpdateActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (images.get(images.size() - 1).equals("0000")) {
                    images.remove(images.size() - 1);
                }
                if (dialog != null) {
                    dialog.show();
                }
                BmobUtils.PostData(editText.getText().toString(), images,swToSquare.isChecked(),swToTeam.isChecked())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        }).doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                ImageActivity.start(UpdateActivity.this, s);
                                finish();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: ", throwable);
                            }
                        });
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        adapter = new ImageAdapter(images, this, new ImageAdapter.ItemOnClickListent() {
            @Override
            public void onClick(View view, int position) {
                if (view.getId() == R.id.sdv) {
                    if (images.get(position).equals("0000")) {
                        startImage(getImageSize());
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setDataToAdapter(List<String> datas) {
        if (images.get(images.size() - 1).equals("0000")) {
            images.remove(images.size() - 1);
        }
        images.addAll(datas);
        if (images.size() < 9) {
            images.add("0000");
        }
        adapter.notifyDataSetChanged();
    }


    private int getImageSize() {

        if (images.get(images.size() - 1).equals("0000")) {
            return 9 - images.size() + 1;
        } else {
            return 0;
        }
    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
//        startImage();
    }

    private void startImage(int size) {
        imagePicker.setSelectLimit(size);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                MyAdapter adapter = new MyAdapter(images);
//                gridView.setAdapter(adapter);
//                Log.d("UpdateActivity", images.toString());
                List<String> list = new ArrayList<>();
                for (ImageItem item : images) {
                    list.add(item.path);
                }
                setDataToAdapter(list);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
