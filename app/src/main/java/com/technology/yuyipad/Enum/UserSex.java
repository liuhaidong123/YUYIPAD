package com.technology.yuyipad.Enum;

/**
 * Created by wanyu on 2017/9/27.
 */
//用户性别
public enum  UserSex {
    GIRL,BOY;//女，男
    //int转男女类型
    public static UserSex getUserSex(int sex){
        UserSex sx;
        switch (sex){
            case 0:
                sx=GIRL;
                break;
            case 1:
                sx=BOY;
                break;
            default:
                sx=GIRL;
        }
        return sx;
    }
}
