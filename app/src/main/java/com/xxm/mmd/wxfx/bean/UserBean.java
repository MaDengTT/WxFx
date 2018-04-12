package com.xxm.mmd.wxfx.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class UserBean extends BmobUser{

    String useravatar;
    Integer points;

    Team team; //团队

    Boolean postPerToTeam;

    String weixin;

    BmobDate vipdate;

    Integer vip;  // 1,普通会员 2，Vip会员

    String name;

    String info;    //描述

    public BmobDate getVipdate() {
        return vipdate;
    }

    public void setVipdate(BmobDate vipdate) {
        this.vipdate = vipdate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }



    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Boolean getPostPerToTeam() {
        return postPerToTeam;
    }

    public void setPostPerToTeam(Boolean postPerToTeam) {
        this.postPerToTeam = postPerToTeam;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
