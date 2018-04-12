package com.xxm.mmd.wxfx.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by MaDeng on 2018/4/11.
 */
public class FCode extends BmobObject {

    String FCode;

    Integer vipnum;

    Boolean isTrue;

    public Boolean getTrue() {
        return isTrue;
    }

    public void setTrue(Boolean aTrue) {
        isTrue = aTrue;
    }

    public Integer getVipnum() {
        return vipnum;
    }

    public void setVipnum(Integer vipnum) {
        this.vipnum = vipnum;
    }

    public String getFCode() {
        return FCode;
    }

    public void setFCode(String FCode) {
        this.FCode = FCode;
    }
}
