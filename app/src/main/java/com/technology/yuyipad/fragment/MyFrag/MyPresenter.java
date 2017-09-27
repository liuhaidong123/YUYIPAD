package com.technology.yuyipad.fragment.MyFrag;

/**
 * Created by wanyu on 2017/9/27.
 */

public class MyPresenter {
    IUser iUser;
    MyModel model;
    //后去用户信息
    public void getUserInfo(IUser iUser){
        this.iUser=iUser;
        if (model==null){
            model=new MyModel();
        }
        model.getUserInfo(iUser);
    }
}
