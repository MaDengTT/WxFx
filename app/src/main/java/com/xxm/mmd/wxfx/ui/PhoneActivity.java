package com.xxm.mmd.wxfx.ui;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.xxm.mmd.wxfx.R;
import com.xxm.mmd.wxfx.adapter.PhoneAdapter;
import com.xxm.mmd.wxfx.daoman.bean.CityPhoneBean;
import com.xxm.mmd.wxfx.daoman.dao.CityPhoneDao;
import com.xxm.mmd.wxfx.utils.PrefUtils;
import com.xxm.mmd.wxfx.utils.WaitObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PhoneActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_opera)
    TextView tvOpera;
    @BindView(R.id.mobile)
    RadioButton mobile;
    @BindView(R.id.unicom)
    RadioButton unicom;
    @BindView(R.id.telecom)
    RadioButton telecom;
    @BindView(R.id.rg_opera)
    RadioGroup rgOpera;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_address_info)
    TextView tvAddressInfo;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.but_create)
    Button butCreate;
    @BindView(R.id.but_putBook)
    Button butPutBook;
    @BindView(R.id.but_clearBook)
    Button butClearBook;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private List<String> phoneNumber = new ArrayList<>();
    private PhoneAdapter phoneAdapter;

    CityPickerView mPicker = new CityPickerView();

    public static void start(Context context) {
        Intent starter = new Intent(context, PhoneActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mPicker.init(this);
        initView();
    }

    private void initView() {
        rgOpera.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.mobile:
                        PrefUtils.putString(PhoneActivity.this, "phone_opera", "移动");
                        break;

                    case R.id.telecom:
                        PrefUtils.putString(PhoneActivity.this, "phone_opera", "电信");
                        break;

                    case R.id.unicom:
                        PrefUtils.putString(PhoneActivity.this, "phone_opera", "联通");
                        break;
                }
            }
        });
        PrefUtils.putString(PhoneActivity.this, "phone_opera", "移动");
        String phone_area = PrefUtils.getString(PhoneActivity.this, "phone_area", null);
        if (!TextUtils.isEmpty(phone_area)) {
            tvAddressInfo.setText(phone_area);
        } else {
            tvAddressInfo.setText("郑州市");
            PrefUtils.putString(PhoneActivity.this, "phone_area", "郑州");
        }
        recycler.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        phoneAdapter = new PhoneAdapter(null);
        recycler.setAdapter(phoneAdapter);
    }

    @OnClick({R.id.tv_address_info, R.id.but_create, R.id.but_putBook, R.id.but_clearBook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_address_info:
                CityConfig cityConfig = new CityConfig.Builder().setCityWheelType(CityConfig.WheelType.PRO_CITY).build();

                mPicker.setConfig(cityConfig);
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        super.onSelected(province, city, district);
                        if (!TextUtils.isEmpty(city.getName())) {
                            tvAddressInfo.setText(city.getName());
                            PrefUtils.putString(PhoneActivity.this, "phone_area", city.getName().substring(0, city.getName().length() - 1));
                            Log.d("PhoneActivity", PrefUtils.getString(PhoneActivity.this, "phone_area", null));

                        }
                    }
                });
                mPicker.showCityPicker();
                break;
            case R.id.but_create:
                createPhone();
                break;
            case R.id.but_putBook:

                Observable.just(phoneNumber)
                        .map(new Function<List<String>, String>() {
                            @Override
                            public String apply(List<String> strings) throws Exception {
                                testAddContactsInTransaction(strings);
                                return "";
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())

                        .subscribe(new WaitObserver<String>(this, "") {
                            @Override
                            public void onNext(String s) {

                            }
                        });

                break;
            case R.id.but_clearBook:
                Observable.just(phoneNumber)
                        .map(new Function<List<String>, String>() {
                            @Override
                            public String apply(List<String> strings) throws Exception {
                                clearContent();
                                return "";
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                Log.d("PhoneActivity", "开始");
                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.d("PhoneActivity", "结束");
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())

                        .subscribe(new WaitObserver<String>(this, "") {
                            @Override
                            public void onNext(String s) {

                            }
                        });

                break;
        }
    }

    public void createPhone() {
        phoneNumber.clear();
        CityPhoneDao cityPhoneDao = new CityPhoneDao(this);
        List<CityPhoneBean> all = cityPhoneDao.findAll();

        if (all != null) {
            Log.d("PhoneActivity", "all.size():" + all.size());
        }

        for (int i = 0; i < all.size(); i++) {
            for (int j = 0; j < 5; j++) {
                String phone = all.get(i).getName() + (int) (Math.random() * 9000 + 1000);
                Log.d("PhoneActivity", phone);
                phoneNumber.add(phone);
            }
        }

        phoneAdapter.setNewData(phoneNumber);
        tvEmpty.setVisibility(View.GONE);
    }

    /**
     * 清空系统通信录数据
     */
    public void clearContent() {
        ContentResolver cr = this.getContentResolver();// 获取
        // ContentResolver对象查询在ContentProvider里定义的共享对象
        // 根据URI对象ContactsContract.Contacts.CONTENT_URI查询所有联系人
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        try {
            // 如果记录不为空
            if (cursor != null) {
                // 游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断
                // 下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
                // 循环
                while (cursor.moveToNext()) {
                    String name = cursor
                            .getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // 根据姓名求id
                    Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");

                    Cursor cursor1 = cr.query(uri, new String[]{ContactsContract.Data._ID}, "display_name=?", new String[]{name},
                            null);
                    // 除去姓名对应的号码
                    if (cursor1.moveToFirst()) {
                        int id = cursor1.getInt(0);
                        cr.delete(uri, "display_name=?", new String[]{name});
                        // 根据id删除data中的相应数据
                        uri = Uri.parse("content://com.android.contacts/data");
                        cr.delete(uri, "raw_contact_id=?", new String[]{id + ""});
                    }
                    cursor1.close();// Cursor循环内再申请Cursor，记得将内部申请的每个Cursor都加上close
                }
                cursor.close();

            } else {
                Toast.makeText(PhoneActivity.this, "通讯录为空", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 批量添加联系人到通讯录中
     *
     * @throws Exception
     */
    public void testAddContactsInTransaction(List<String> phoneNumber) throws Exception {
        ArrayList<ContentProviderOperation> mOperations = new ArrayList<>();
        ContentResolver resolver = PhoneActivity.this.getContentResolver();
        int rawContactInsertIndex;
        // 循环添加
        for (int i = 0; i < phoneNumber.size(); i++) {
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            rawContactInsertIndex = mOperations.size();// 这句好很重要，有了它才能给真正的实现批量添加。
            // 向raw_contact表添加一条记录
            // 此处.withValue("account_name", null)一定要加，不然会抛NullPointerException
            ContentProviderOperation operation1 = ContentProviderOperation.newInsert(uri)
                    .withValue("account_name", null).build();
            mOperations.add(operation1);
            // 向data添加数据
            uri = Uri.parse("content://com.android.contacts/data");
            // 添加姓名
            ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", rawContactInsertIndex)
                    // withValueBackReference的第二个参数表示引用operations[0]的操作的返回id作为此值
                    .withValue("mimetype", "vnd.android.cursor.item/name").withValue("data2", phoneNumber.get(i))
                    .withYieldAllowed(true).build();
            mOperations.add(operation2);
            // 添加手机数据
            ContentProviderOperation operation3 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", rawContactInsertIndex)
                    .withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data2", "2")
                    .withValue("data1", phoneNumber.get(i)).withYieldAllowed(true).build();
            mOperations.add(operation3);

        }
        try {
            // 这里才调用的批量添加
            resolver.applyBatch("com.android.contacts", mOperations);

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
