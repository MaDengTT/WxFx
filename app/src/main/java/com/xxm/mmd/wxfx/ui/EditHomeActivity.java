package com.xxm.mmd.wxfx.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.bean.HomePage;
import com.xxm.mmd.wxfx.utils.BmobUtils;
import com.xxm.mmd.wxfx.utils.DialogHelp;
import com.xxm.mmd.wxfx.utils.GlideLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EditHomeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.ed_card_name)
    EditText edCardName;
    @BindView(R.id.tv_card_info)
    TextView tvCardInfo;
    @BindView(R.id.ed_card_info)
    EditText edCardInfo;
    @BindView(R.id.tv_wx_name)
    TextView tvWxName;
    @BindView(R.id.ed_wx_name)
    EditText edWxName;
    @BindView(R.id.tv_phone_name)
    TextView tvPhoneName;
    @BindView(R.id.ed_phone_name)
    EditText edPhoneName;
    @BindView(R.id.but_send)
    Button butSend;
    private ImagePicker imagePicker;

    List<String> images  = new ArrayList<>();
    private ProgressDialog waitDialog;
    private HomePage homePage;

    public static void start(Context context) {
        Intent starter = new Intent(context, EditHomeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        ButterKnife.bind(this);
        initImagePicker();
        getCard();
    }

    private void getCard() {
        BmobUtils.getMyHomePage()
                .subscribe(new Observer<HomePage>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (waitDialog == null) {
                            waitDialog = DialogHelp.getWaitDialog(EditHomeActivity.this, "请稍后");
                        }
                        waitDialog.show();
                    }

                    @Override
                    public void onNext(HomePage homePage) {
                        initView(homePage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("aa", "onError: ", e);
                        if (waitDialog.isShowing()) {
                            waitDialog.dismiss();
                        }
                        initView(null);
                    }

                    @Override
                    public void onComplete() {
                        if (waitDialog.isShowing()) {
                            waitDialog.dismiss();
                        }
                    }
                });
    }

    private void initView(HomePage homePage) {

        this.homePage = homePage;

        if (TextUtils.isEmpty(this.homePage.getObjectId())) {
            this.homePage = null;
        }

        if (homePage != null) {
            setDataToImageNet(homePage.getImages());
            edCardName.setText(homePage.getCardName());
            edCardInfo.setText(homePage.getCardInfo());
            edWxName.setText(homePage.getWxInfo());
            edPhoneName.setText(homePage.getPhoneNum());
            butSend.setText("更新名片");
        }else {
            edCardName.setText(MyApp.getApp().getUser().getName());
            edCardInfo.setText(MyApp.getApp().getUser().getInfo());
            edWxName.setText(MyApp.getApp().getUser().getWeixin());
            edPhoneName.setText(MyApp.getApp().getUser().getMobilePhoneNumber());
            butSend.setText("发布名片");
        }

    }


    @OnClick({R.id.iv1, R.id.iv2, R.id.iv3, R.id.but_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv1:
            case R.id.iv2:
            case R.id.iv3:
                startImage(3);
                break;
            case R.id.but_send:
                String cardName = edCardName.getText().toString();
                String cardInfo = edCardInfo.getText().toString();
                String cardWx = edWxName.getText().toString();
                String cardPhone = edPhoneName.getText().toString();

                if (TextUtils.isEmpty(cardName)) {
                    Toast.makeText(this, "名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cardWx)) {
                    Toast.makeText(this, "微信号不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(cardInfo)) {
                    Toast.makeText(this, "描述不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                BmobUtils.putHomePage(homePage != null?homePage.getObjectId():"",images, cardName, cardInfo, cardWx, cardPhone)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (waitDialog == null) {
                            waitDialog = DialogHelp.getWaitDialog(EditHomeActivity.this, "请稍后");
                        }
                        waitDialog.show();
                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(EditHomeActivity.this, "发布成功", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: ",e );
                        Toast.makeText(EditHomeActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        if (waitDialog.isShowing()) {
                            waitDialog.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (waitDialog.isShowing()) {
                            waitDialog.dismiss();
                        }
                        finish();
                    }
                });
                break;
        }
    }

    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(3);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        imagePicker.setMultiMode(true);
//        startImage();
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
                setDataToImage(list);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private int IMAGE_PICKER = 0x55;
    public void setDataToImage(List<String> dataToImage) {
        if (dataToImage == null) {
            return;
        }
        images = dataToImage;
        for(int i = 0;i<images.size();i++) {
            switch (i) {
                case 0:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv1,images.get(i));
                    break;
                case 1:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv2,images.get(i));
                    break;
                case 2:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv3,images.get(i));
                    break;
            }
        }
    }    public void setDataToImageNet(List<String> dataToImage) {
        if (dataToImage == null) {
            return;
        }
        for(int i = 0;i<dataToImage.size();i++) {
            switch (i) {
                case 0:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv1,dataToImage.get(i));
                    break;
                case 1:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv2,dataToImage.get(i));
                    break;
                case 2:
                    com.xxm.mmd.wxfx.glide.GlideLoader.loadNormal(iv3,dataToImage.get(i));
                    break;
            }
        }
    }
    private void startImage(int size) {
        imagePicker.setSelectLimit(size);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }
}
