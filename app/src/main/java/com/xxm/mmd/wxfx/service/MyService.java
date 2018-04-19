package com.xxm.mmd.wxfx.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.xxm.mmd.wxfx.MyApp;
import com.xxm.mmd.wxfx.contast.Contast;
import com.xxm.mmd.wxfx.service.content.Constant;
import com.xxm.mmd.wxfx.service.content.IDConstant;
import com.xxm.mmd.wxfx.utils.AccessibilityHelper;
import com.xxm.mmd.wxfx.utils.WeiXinShareUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by MaDeng on 2017/12/11.
 */

public class MyService extends AccessibilityService {

    private static final String TAG = "MyService";
    final String pknWX = "com.tencent.mm";
    final String thisPkn = "com.xxm.mmd.wxfx";
    private String WxId = "";
    private AccessBroadcastReceiver msgReceiver;


    @Override
    public void onCreate() {
        super.onCreate();
////        key = MyApplication.getInstanct().getKEY();
        msgReceiver = new AccessBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contast.accessBroad);
        registerReceiver(msgReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        unregisterReceiver(msgReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onServiceConnected() {

        Log.d(TAG, "onServiceConnected: sever");
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        setServiceInfo(info);
        info.packageNames = new String[]{"com.tencent.mm"};
        setServiceInfo(info);


//        setAutoLike();
        initBoole();

        super.onServiceConnected();
    }


    boolean start = false;

    boolean addNewFriend = false;  //添加新朋

    boolean addNewFriendToPhone = false; //添加新朋友从手机


    boolean addNewFriendPutText = false;

    private boolean addNewFriendToPhoneAdd = true; //详情页添加 false 返回
    int addNewFriendToPhoneItemSize = 0;

    int wxPage = 0; // 1 主页面 2、新的朋友 3 、添加朋友 4、搜索 5、详情页 6、验证申请 7、搜索好友页面 9、朋友圈


    boolean autoLike = false;

    int likeSize = 0;

    private void initBoole() {
        addNewFriend = false;
        addNewFriendToPhone = false;
        addNewFriendPutText = false;
        addNewFriendToPhoneAdd = true;
        addNewFriendToPhoneItemSize = 0;
        autoLike = false;
        likeSize = 0;
        wxPage = 0;
    }

    private void setAutoLike() {
        initBoole();
        autoLike = true;
        start = true;
    }

    public void setAddNewFriendToText() {
        initBoole();
        addNewFriend = true;
        start = true;
    }
    public void setAddNewFriendToPhone() {
        initBoole();
        addNewFriendToPhone = true;
        start = true;
    }

    public void stop() {
        start = false;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();


        if (!start) {
            return;
        }

        try {
            if (event.getSource() != null) {

                CharSequence className = event.getClassName();
                Log.d(TAG, "className:" + className);

                if (className.equals(IDConstant.WxMain)) {
                    wxPage = 1;
                } else if (className.equals(IDConstant.WxMessageConversation)){
                    wxPage = 2;
                }else if (className.equals(IDConstant.WxAddMoreFriends)) {
                    wxPage = 3;
                } else if (className.equals(IDConstant.WxSerachFriends)) {
                    wxPage = 4;
                } else if (className.equals(IDConstant.WxContactInfo)) {
                    wxPage = 5;
                } else if (className.equals(IDConstant.WxHiWithSns)||className.equals("com.tencent.mm.ui.base.r")) {
                    wxPage = 6;
                } else if (className.equals(IDConstant.WxFTSMain)) {
                    wxPage = 7;
                } else if (className.equals(IDConstant.WxSnsTimeLine)) {
                    wxPage = 9;
                }

                if (addNewFriend) {
                    if (wxPage == 1) {
                        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/gx");
                        if (nodeInfosById != null) {
                            List<AccessibilityNodeInfo> text = nodeInfosById.findAccessibilityNodeInfosByText("搜索");
                            if (text != null) {
                                AccessibilityHelper.performClick(text.get(0));
                                addNewFriendPutText = false;
                            }
                        }


//                        performSerachToMain();
                    } else if (wxPage == 2) {
                        performNode("添加朋友");
                    } else if (wxPage == 3) {
                        perforNode("com.tencent.mm:id/ht");  //点击不了 微信号/QQ号/手机号
                    } else if (wxPage == 7) {

                        if (!addNewFriendToPhoneAdd) {
                            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/ht");
                            AccessibilityHelper.putStringToNodeInfo(nodeInfosById,"");
                            addNewFriendToPhoneAdd = true;
                        }

                        if (!addNewFriendPutText) {
                            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/hx");
                            AccessibilityHelper.putStringToNodeInfo(nodeInfosById,"");
                            AccessibilityHelper.putStringToNodeInfo(nodeInfosById,WxId);
                            addNewFriendPutText = true;
                        }else {
                            perforNode("com.tencent.mm:id/baz");
                            addNewFriendToPhoneAdd = true;
                        }
                    }else if (wxPage == 5) {

                        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/anc");
                        if (nodeInfosById != null && nodeInfosById.getText().equals("发消息")) {
                            perforNode("com.tencent.mm:id/hx"); //返回
                            addNewFriendToPhoneAdd =false;
                        }

                        if (addNewFriendToPhoneAdd) {
//                            perforNode("com.tencent.mm:id/an_");   //添加通訊錄
                            performNode("添加到通讯录");
                        }else {
                            perforNode("com.tencent.mm:id/i2"); //返回
                            addNewFriendToPhoneAdd = true;
                        }
                    } else if (wxPage == 6) {
                        perforNode("com.tencent.mm:id/hh"); //發送
//                        performNode("发送");
                        addNewFriendToPhoneAdd = false;
                    }
                    sleepTime(1000);
                }

                if (addNewFriendToPhone) {
                    if (wxPage == 1) {
                        performNode("新的朋友");
                        performNode("通讯录");
                        addNewFriendToPhoneItemSize = 0;
                    } else if (wxPage == 2) {
//                        performNode("添加朋友");
                        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/b8u");
                        if (nodeInfosById != null) {
//                            List<AccessibilityNodeInfo> infos = nodeInfosById.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b8m");
                            List<AccessibilityNodeInfo> infos = nodeInfosById.findAccessibilityNodeInfosByText("手机联系人");
                            if (infos.size() <= addNewFriendToPhoneItemSize) {
                                nodeInfosById.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                addNewFriendToPhoneItemSize = 0;
                            }else {
                                try {
                                    AccessibilityHelper.performClick(infos.get(addNewFriendToPhoneItemSize));
                                } catch (Exception e) {
                                    nodeInfosById.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                    addNewFriendToPhoneItemSize = 0;
                                }
                                addNewFriendToPhoneItemSize++;
                                addNewFriendToPhoneAdd = true;
                            }
                        }
                    } else if (wxPage == 5) {
//                        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/anb");
//                        if (nodeInfosById == null) {
////                            perforNode("com.tencent.mm:id/hx"); //返回
//                            AccessibilityHelper.performBack(this);
//                            addNewFriendToPhoneAdd = true;
//                        }

                        if (addNewFriendToPhoneAdd) {
                            performNode("添加到通讯录");   //添加通訊錄
                        }else {
//                            perforNode("com.tencent.mm:id/hx"); //返回
                            AccessibilityHelper.performBack(this);
                            addNewFriendToPhoneAdd = true;
                        }
                    } else if (wxPage == 6) {
                        perforNode("com.tencent.mm:id/hh"); //發送
//                        performNode("发送");
                        addNewFriendToPhoneAdd = false;
                    }
                    sleepTime(1000);
                }

            }

            if (autoLike) {
                if (wxPage == 1) {
                    performNode("朋友圈");
                    performNode("发现");
                    addNewFriendToPhoneItemSize = 0;
                } else if (wxPage == 9) {

                    performNode("赞");

                    List<AccessibilityNodeInfo> infos = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dao");
                    if (infos != null) {
                        if (infos.size() > likeSize) {
                            AccessibilityNodeInfo like = infos.get(likeSize);
                            AccessibilityHelper.performClick(like);
                            likeSize++;
                          sleepTime(2000);
                        }else {
                            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/ddn");
                            if (nodeInfosById != null) {
                                nodeInfosById.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                likeSize = 0;
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            Log.e(TAG, "onAccessibilityEvent: ", e);
        }

    }

    private void performSerachToMain() {

//        AccessibilityNodeInfo aainfos = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/ge");
//        if (aainfos != null) {
//            AccessibilityHelper.performClick(aainfos);
//        }

        performNode("添加朋友");

        Log.d(TAG, "performSerachToMain: ToMain");
        AccessibilityNodeInfo infos = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/gd");
        if(infos!=null)
            AccessibilityHelper.performClick(infos);
//        if(infos!=null) Log.d(TAG, "performSerachToMain: ToMain2");
//        AccessibilityNodeInfo parentParent = infos.getParent();
//        AccessibilityNodeInfo parentParentParent = parentParent.getParent();
//
//        if (parentParentParent != null) {
//            AccessibilityNodeInfo child = parentParentParent.getChild(0);
//            CharSequence contentDescription = child.getContentDescription();
////            Log.d(TAG, contentDescription.toString());
//            AccessibilityHelper.performClick(child);
//        }
    }

    private void performNode(String text) {


        AccessibilityNodeInfo infos = AccessibilityHelper.findNodeInfosByText(getRootInActiveWindow(), text);
        if (infos != null) {
            Log.d(TAG, text);
            AccessibilityHelper.performClick(infos);
        }
    }

    private void perforNode(String id) {

        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), id);
        if (nodeInfosById != null) {
            Log.d(TAG, "nodeInfosById:" + id);
            AccessibilityHelper.performClick(nodeInfosById);
        }

    }
    private void perforNode(String id,String fClassName) {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), id);
        AccessibilityHelper.findParentNodeInfosByClassName(nodeInfosById, fClassName);
        if (nodeInfosById != null) {
            Log.d(TAG, "perforNode:" + id);
            AccessibilityHelper.performClick(nodeInfosById);
        }

    }
    private void perforNode(String id,int parentSize) {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), id);
        AccessibilityNodeInfo parent = getParent(nodeInfosById, parentSize);
        if (parent != null) {
            Log.d(TAG, "perforNodeP:" +parent.getChildCount());
            AccessibilityHelper.performClick(parent);
        }
    }

    private AccessibilityNodeInfo getParent(AccessibilityNodeInfo nodeInfo, int size) {
        AccessibilityNodeInfo parent = nodeInfo.getParent();
        if (parent != null) {
            if (size == 0) {
                return parent;
            }else {
                size = size-1;
                return getParent(nodeInfo, size);
            }
        }else {
            return nodeInfo;
        }
    }

    private void performNewFriend() {
        AccessibilityNodeInfo infos = AccessibilityHelper.findNodeInfosByText(getRootInActiveWindow(), "新的朋友");
        if (infos != null) {
            AccessibilityHelper.performClick(infos);
        }
    }

    private void performTXL() {
        AccessibilityNodeInfo text = AccessibilityHelper.findNodeInfosByText(getRootInActiveWindow(), "通讯录");
        if (text != null) {
            AccessibilityHelper.performClick(text);
        }
    }



    private void showToast(final String message) {
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onInterrupt() {

    }
    /**
     * 睡眠
     *
     * @param millis
     */
    public void sleepTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public class  AccessBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(Contast.BroadType, 0);
            switch (type) {
                case Contast.addFriendPhone:         //通讯录添加好友
                    setAddNewFriendToPhone();
                    WeiXinShareUtil.openAppWx(context);
                    break;

                case Contast.addFriendText:         //添加好友
                    String name = intent.getStringExtra(Contast.BroadNameID);
                    WxId = name;
                    setAddNewFriendToText();
                    WeiXinShareUtil.openAppWx(context);
                    ClipboardManager clipboard = (ClipboardManager) MyApp.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("",name);
                    clipboard.setPrimaryClip(clip);
                    showToast("微信号码以复制至粘贴板");
                    break;

                case Contast.autolike:         //自动点赞
                    setAutoLike();
                    WeiXinShareUtil.openAppWx(context);
                    break;

                case 0:
                    stop();
                    break;
            }
        }
    }



}
