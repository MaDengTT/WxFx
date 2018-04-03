package com.xxm.mmd.wxfx.bean;

/**
 * Created by MaDeng on 2018/3/27.
 */

public class FunctionBean {

    int imageId;
    String title;
    String desc;

    Class<?> aClass;

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public FunctionBean(int imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }    public FunctionBean(int imageId, String title, String desc,Class<?> aClass) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
        this.aClass = aClass;
    }
}
