package com.xxm.mmd.wxfx.bean;

import java.util.List;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class CirlceBean {

    String avatarUrl;
    String userName;
    String content;
    List<String> imageUrls;

    String objeId;

    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObjeId() {
        return objeId;
    }

    public void setObjeId(String objeId) {
        this.objeId = objeId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
