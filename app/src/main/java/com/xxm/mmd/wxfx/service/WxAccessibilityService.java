package com.xxm.mmd.wxfx.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xxm.mmd.wxfx.service.content.IDConstant;
import com.xxm.mmd.wxfx.utils.AccessibilityHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by MaDeng on 2018/4/9.
 */
public class WxAccessibilityService extends AccessibilityService{

    int addTask = 1;

    private ArrayList<String> tasks = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();

        if (tasks.size() != 0) {
            return;
        }
        if (event.getSource() != null) {

            CharSequence className = event.getClassName();

            if (className.equals(IDConstant.WxMain)) {
                mainToUi(tasks.get(tasks.size()-1));
            }
            if(className.equals(IDConstant.WxMessageConversation)){ //新的朋友

            }
            if (className.equals(IDConstant.WxSerachFriends)) { //搜索朋友

            }
        }

    }

    private void serachFriendsUi(String s) {
        if (s.equals("搜索添加->输入字符")) {
            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/ht");
            if (nodeInfosById != null) {
                AccessibilityHelper.putStringToNodeInfo(nodeInfosById,"aa");
            }
            tasks.add("输入字符->点击搜索");
        } else if (s.equals("输入字符->点击搜索")) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void messageConverUi(String s){
        sleepTime(1000);
        if (s.equals("新的朋友->搜素")) {
            //点击搜索
            AccessibilityHelper.WXfindIdTextAndClick(getRootInActiveWindow(),"hc","添加朋友",0);
            tasks.add("搜索->添加朋友");
        } else if (s.equals("搜索->添加朋友")) {
            AccessibilityNodeInfo byId = AccessibilityHelper.findNodeInfosById(getRootInActiveWindow(), "com.tencent.mm:id/j3");
            if (byId != null) {
                AccessibilityHelper.performClick(byId);
            }
            tasks.add("搜索添加->输入字符");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void mainToUi(String s) {
        sleepTime(1000);
        if (s.equals("主界面->通讯录")) {
            AccessibilityHelper.WXfindIdTextAndClick(getRootInActiveWindow(),IDConstant.MAIN_BUTTON, "通讯录", 1);
            tasks.add("通讯录->新的朋友");
        }else if(s.equals("通讯录->新的朋友")){
            AccessibilityNodeInfo newFirend = AccessibilityHelper.findNodeInfosByText(getRootInActiveWindow(), "新的朋友");
            if (newFirend != null) {
                AccessibilityHelper.performClick(newFirend);
                tasks.add("新的朋友->搜索");
            }else {

            }
        }
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
}
