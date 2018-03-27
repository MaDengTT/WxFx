package com.xxm.mmd.wxfx.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by MaDeng on 2018/3/20.
 */

public class UpdateSys extends BmobObject{
    String UpdateP;
    String vesion;
    boolean isTrue;
    String message;

    @Override
    public String toString() {
        return "UpdateSys{" +
                "UpdateP='" + UpdateP + '\'' +
                ", vesion='" + vesion + '\'' +
                ", isTrue=" + isTrue +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUpdateP() {
        return UpdateP;
    }

    public void setUpdateP(String updateP) {
        UpdateP = updateP;
    }

    public String getVesion() {
        return vesion;
    }

    public void setVesion(String vesion) {
        this.vesion = vesion;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}
