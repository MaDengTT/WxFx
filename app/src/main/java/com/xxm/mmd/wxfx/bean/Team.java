package com.xxm.mmd.wxfx.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by MaDeng on 2018/3/30.
 */
public class Team extends BmobObject {
    String name;
    UserBean AdminUser;

    String AdminId;

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserBean getAdminUser() {
        return AdminUser;
    }

    public void setAdminUser(UserBean adminUser) {
        AdminUser = adminUser;
    }
}
