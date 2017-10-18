package com.technology.yuyipad.RongUtils;

/**
 * Created by wanyu on 2017/10/11.
 */

public interface IGetRongUserTokenError {
    void onTokenError(String message);//从本地服务器获取到容云的token失败
}
