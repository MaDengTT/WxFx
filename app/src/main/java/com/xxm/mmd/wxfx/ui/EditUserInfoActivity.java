package com.xxm.mmd.wxfx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.UserBean;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.GlideLoader;
import com.xxm.mmd.wxfx.utils.WaitObserver;
import com.xxm.mmd.wxfx.view.RCRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class EditUserInfoActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.rc_avatar)
    RCRelativeLayout rcAvatar;
    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.tv_wxname)
    TextView tvWxname;
    @BindView(R.id.ed_wxname)
    EditText edWxname;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_woman)
    RadioButton rbWoman;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_label_str)
    TextView tvLabelStr;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.cl_label)
    ConstraintLayout clLabel;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.ed_info)
    EditText edInfo;
    private ImagePicker imagePicker;
    private int REQUEST_CODE_SELECT = 0x22;
    private int REQUEST_CODE_PREVIEW = 0x33;
    private UserBean user;
    private String path;

    public static void start(Context context) {
        Intent starter = new Intent(context, EditUserInfoActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
    }

    public ImagePicker getImagePicker() {

        if (imagePicker == null) {
            imagePicker = ImagePicker.getInstance();
            imagePicker.setImageLoader(new GlideLoader());
        }
        return imagePicker;
    }

    private void initView() {

        tvTitle.setText("个人信息");

        user = MyApp.getApp().getUser();
        com.xxm.mmd.wxfx.glide.GlideLoader.loadAvatar(ivAvatar, user.getUseravatar());

        edName.setText(user.getName());
        edWxname.setText(user.getWeixin());
        edInfo.setText(user.getInfo());

        tvSend.setText("保存");
        tvSend.setVisibility(View.VISIBLE);

        getImagePicker().setCrop(true);
        getImagePicker().setSaveRectangle(true);                   //是否按矩形区域保存
        getImagePicker().setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        getImagePicker().setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        getImagePicker().setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        getImagePicker().setOutPutX(1000);                         //保存文件的宽度。单位像素
        getImagePicker().setOutPutY(1000);                         //保存文件的高度。单位像素


    }


    protected void onPickMultiple(int size, ArrayList<ImageItem> images) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        if (size != 1) {
            imagePicker.setMultiMode(true);
            getImagePicker().setSelectLimit(size);
        } else {
            imagePicker.setMultiMode(false);
        }

        if (images != null) {
            intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (requestCode == REQUEST_CODE_SELECT) {
                if (data != null) {
                    List<ImageItem> images = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    takeSuccess(images);
                } else {
                    //失败或者没有数据
                    takeFail("失败");
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                List<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                previewList(images);
            }
        }
    }

    protected void takeSuccess(List<ImageItem> images) {
        if (images.size() == 0) {
            return;
        }
        path = images.get(0).path;
        com.xxm.mmd.wxfx.glide.GlideLoader.loadAvatar(ivAvatar, path);
    }

    protected void takeFail(String message) {

    }

    protected void previewList(List<ImageItem> images) {

    }

    @OnClick({R.id.tv_send, R.id.rl_avatar})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_send:
                if (TextUtils.isEmpty(edName.getText().toString())) {
                    Toast.makeText(this, "用户名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(path)) {
                    BmobUtils.uploadFiles(new String[]{path})
                            .map(new Function<List<String>, UserBean>() {
                                @Override
                                public UserBean apply(List<String> strings) throws Exception {
                                    UserBean userBean = new UserBean();
                                    userBean.setUseravatar(strings.get(0));
                                    userBean.setName(edName.getText().toString());
                                    userBean.setWeixin(edWxname.getText().toString());
                                    userBean.setInfo(edInfo.getText().toString());

                                    return userBean;
                                }
                            })
                            .flatMap(new Function<UserBean, ObservableSource<String>>() {
                                @Override
                                public ObservableSource<String> apply(final UserBean userBean) throws Exception {
                                    return Observable.create(new ObservableOnSubscribe<String>() {
                                        @Override
                                        public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                                            userBean.update(MyApp.getApp().getUser().getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        emitter.onNext("成功");
                                                        emitter.onComplete();
                                                    } else {
                                                        emitter.onError(e);
                                                    }
                                                }
                                            });

                                        }
                                    });
                                }
                            })
                            .subscribe(new WaitObserver<String>(EditUserInfoActivity.this,"") {

                                @Override
                                public void onNext(String s) {
                                    Toast.makeText(EditUserInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    Toast.makeText(EditUserInfoActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    super.onComplete();
                                    EventBus.getDefault().post(new UserBean());
                                    finish();
                                }
                            });
                }else {
                    Observable.just("")
                            .map(new Function<String, UserBean>() {
                                @Override
                                public UserBean apply(String s) throws Exception {
                                    UserBean userBean = new UserBean();
                                    userBean.setName(edName.getText().toString());
                                    userBean.setWeixin(edWxname.getText().toString());
                                    userBean.setInfo(edInfo.getText().toString());

                                    return userBean;
                                }
                            }).flatMap(new Function<UserBean, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(final UserBean userBean) throws Exception {
                            return Observable.create(new ObservableOnSubscribe<String>() {
                                @Override
                                public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                                    userBean.update(MyApp.getApp().getUser().getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                emitter.onNext("成功");
                                                emitter.onComplete();
                                            } else {
                                                emitter.onError(e);
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    })
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(String s) {
                                    Toast.makeText(EditUserInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("aa", "onError: ", e);
                                    Toast.makeText(EditUserInfoActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    EventBus.getDefault().post(new UserBean());
                                    finish();
                                }
                            });
                }

                break;
            case R.id.rl_avatar:
                onPickMultiple(1, null);
                break;
        }
    }
}
