package com.technology.yuyipad.activity.Setting.Bean;

/**
 * Created by wanyu on 2017/3/20.
 */
//获取验证码返回
public class bean_SMScode {

    /**
     * result : 216101
     * code : 0
     */

    private String result;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
