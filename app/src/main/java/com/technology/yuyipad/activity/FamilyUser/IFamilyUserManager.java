package com.technology.yuyipad.activity.FamilyUser;

import com.technology.yuyipad.activity.FamilyUser.Bean.FamilyUserListBean;

/**
 * Created by wanyu on 2017/9/29.
 */

public interface IFamilyUserManager {
    void onSuccess(FamilyUserListBean bean);
    void onError(String msg,String interfaceName);
    void onTokenError(String msg);
}
