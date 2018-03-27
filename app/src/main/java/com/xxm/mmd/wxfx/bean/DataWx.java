package com.xxm.mmd.wxfx.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by MaDeng on 2018/3/19.
 */

public class DataWx extends BmobObject{
    String text;
    List<String> image;

    @Override
    public String toString() {
        return "DataWx{" +
                "text='" + text + '\'' +
                ", image=" + image +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
