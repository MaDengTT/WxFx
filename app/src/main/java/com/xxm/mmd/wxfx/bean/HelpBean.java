package com.xxm.mmd.wxfx.bean;

/**
 * Created by MaDeng on 2018/3/27.
 */

public class HelpBean {

    String title;

    String imagePath;

    int imageID;

    public boolean isShwoImage = true;

    public HelpBean(String title, int imageID) {
        this.title = title;
        this.imageID = imageID;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public HelpBean(String title, String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
