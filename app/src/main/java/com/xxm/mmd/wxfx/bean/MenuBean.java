package com.xxm.mmd.wxfx.bean;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class MenuBean {

    String menuTitle;

    Class<?> activityClass;

    public MenuBean(String menuTitle, Class<?> activityClass) {
        this.menuTitle = menuTitle;
        this.activityClass = activityClass;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<?> activityClass) {
        this.activityClass = activityClass;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }
}
