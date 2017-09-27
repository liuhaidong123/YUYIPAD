package com.technology.yuyipad.fragment.MyFrag;

/**
 * Created by wanyu on 2017/9/27.
 */
//获取用户信息接口
public interface IUser {
    void  onError(String msg,String interfaceName);//获取失败参数为失败原因,接口的名称
    void onSuccess(UserBean userBean);//获取成功
    void onErrorTokenOut();//token失效：未登录或者用户登录状态失效：在其他设备登录
}
