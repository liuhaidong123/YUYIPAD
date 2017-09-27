package com.technology.yuyipad.fragment.MyFrag;

/**
 * Created by wanyu on 2017/9/27.
 */

public class MyModel {
    IUser iUser;
    //获取用户信息
    public void getUserInfo(IUser iUser){
        this.iUser=iUser;
        if (iUser==null){
            return;
        }
    }
}
