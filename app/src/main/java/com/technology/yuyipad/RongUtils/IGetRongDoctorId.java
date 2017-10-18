package com.technology.yuyipad.RongUtils;

/**
 * Created by wanyu on 2017/10/11.
 */
//获取医生id接口
public interface IGetRongDoctorId {
    void onError(String msg);//获取失败
    void onSuccess(String targetId);//获取成功
}
