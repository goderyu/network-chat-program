package com.goderyu.db;

/**
 * 获取个人好友列表信息类
 *
 * @author goderyu
 * @create 2017-11-10 下午7:31
 **/

public class UserInfo {
    private String uid;
    private String nickname;
    private String description;
    private String img;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
