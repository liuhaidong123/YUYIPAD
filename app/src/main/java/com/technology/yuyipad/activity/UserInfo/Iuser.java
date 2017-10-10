package com.technology.yuyipad.activity.UserInfo;

/**
 * Created by wanyu on 2017/10/9.
 */
//获取用户信息
public interface Iuser {
    void onError(String msg,String interfaceName);
    void onTokenError();
    void onSuccess(UserBean user);
}
