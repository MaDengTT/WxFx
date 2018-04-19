package com.xxm.mmd.wxfx.bean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by MaDeng on 2018/3/21.
 */

public class BannerImage extends BmobObject {
    String imageUrl;

    ArrayList<String> ImageUrls;

    public ArrayList<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        ImageUrls = imageUrls;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
