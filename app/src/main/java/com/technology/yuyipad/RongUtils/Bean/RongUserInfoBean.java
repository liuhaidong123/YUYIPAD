package com.technology.yuyipad.RongUtils.Bean;

import android.net.Uri;

import io.rong.imlib.model.UserInfo;

/**
 * Created by wanyu on 2017/10/11.
 */
//容云用户信息提供者实体类
public class RongUserInfoBean {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(Uri portraitUri) {
        this.portraitUri = portraitUri;
    }

    String name;
    Uri portraitUri;
}
