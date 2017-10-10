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

    //获取个人信息接口http://localhost:8080/yuyi/personal/get.do?token=C0700876FB2F9BEC156AC039F894E92B
    public final static String interface_UserMsg="personal/get.do?";
    //个人信息修改:http://localhost:8080/yuyi/personal/save.do?token=C0700876FB2F9BEC156AC039F894E92B&idCard=515251635262&age=26
    public final static String interface_UserMsgRevise="personal/save.do?";
}
