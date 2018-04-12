package com.xxm.mmd.wxfx.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by MaDeng on 2018/4/12.
 */
public class OrderBean extends BmobObject {

    UserBean user;
    String orderid;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
