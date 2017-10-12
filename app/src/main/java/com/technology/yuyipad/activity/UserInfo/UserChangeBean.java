package com.technology.yuyipad.activity.UserInfo;

/**
 * Created by wanyu on 2017/10/9.
 */
//更改用户信息实体类
public class UserChangeBean {
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * code : 0
     */
    String message;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
