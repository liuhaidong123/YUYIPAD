package com.technology.yuyipad.activity.FamilyUser;

/**
 * Created by wanyu on 2017/9/29.
 */

public interface IFamilyUserList {
    void onSuccess(FamilyUserListBean bean);
    void onError(String msg,String interfaceName);
    void onTokenError(String msg);
}
