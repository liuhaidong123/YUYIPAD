package com.technology.yuyipad.Net;

/**
 * Created by wanyu on 2017/9/27.
 */
//定义所有网络请求失败时的返回信息
public interface ErrorText {
    String gsonError="数据异常";
    String netError="网络异常";
    String dateEmpty="没有获取到数据";//数据为null时
}
