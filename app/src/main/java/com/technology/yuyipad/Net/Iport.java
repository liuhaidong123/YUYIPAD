package com.technology.yuyipad.Net;

/**
 * Created by wanyu on 2017/9/27.
 */
//定义所有接口
public interface Iport {
    //获取个人信息参数token，method：get／post
    String IgetUserInfo="personal/get.do?";
    //家庭用户列表参数token
    String IgetFamilyUserList="homeuser/findList.do?";
}
