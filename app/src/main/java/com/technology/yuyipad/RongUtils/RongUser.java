package com.technology.yuyipad.RongUtils;

/**
 * Created by wanyu on 2017/10/11.
 */
//容云用户信息
public class RongUser {
    public static boolean isConnectSuccess=false;//是否已经链接到容云服务器成功
    public static boolean isRongTokenGet=false;//是否已经得到了服务器返回的token
    public static boolean isRongTargetIdGet=false;//医生的id是否已经获取
   public static String RongToken="";//当前登录用户的token
   public static  String RonguserId="";//当前登录用户的userId
    public static String RongTargetId="";//医生的userId
}
