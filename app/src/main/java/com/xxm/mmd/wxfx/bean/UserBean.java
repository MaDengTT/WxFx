package com.xxm.mmd.wxfx.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by MaDeng on 2018/3/28.
 */
public class UserBean extends BmobUser{

    String useravatar;
    Integer points;

    Team team; //团队

    Boolean postPerToTeam;

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
