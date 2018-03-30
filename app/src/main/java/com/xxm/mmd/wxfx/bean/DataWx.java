package com.xxm.mmd.wxfx.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by MaDeng on 2018/3/19.
 */

public class DataWx extends BmobObject{
    String text;
    List<String> image;

    UserBean user;  //用户
    Boolean ifShowTeam;    //是否发布至团队
    Boolean ifShowSquare;   //是否发布至广场

    public Boolean getIfShowTeam() {
        return ifShowTeam;
    }

    public void setIfShowTeam(Boolean ifShowTeam) {
        this.ifShowTeam = ifShowTeam;
    }

    public Boolean getIfShowSquare() {
        return ifShowSquare;
    }

    public void setIfShowSquare(Boolean ifShowSquare) {
        this.ifShowSquare = ifShowSquare;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

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
