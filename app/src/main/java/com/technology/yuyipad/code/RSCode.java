package com.technology.yuyipad.code;

/**
 * Created by wanyu on 2017/9/27.
 */
//定义使用到的请求码与对应的结果吗(r开头的是请求吗，s开头的是结果吗)
public interface RSCode{
    //公用的请求码与结果吗
    int rCode=100;
    int sCode=200;

    //私有请求吗与结果码
    int rCode_SearchPicture=101;//浏览系统相册
    int rCode_TakePhoto=102;//拍砸后
    int rCode_CutPicture=103;//
    //权限请求码
    int priCode_SearchPicture=201;//浏览图库权限
    int priCode_TakePhoto=202;//拍照权限
}
