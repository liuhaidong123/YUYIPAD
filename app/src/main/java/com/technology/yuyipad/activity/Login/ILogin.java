package com.technology.yuyipad.activity.Login;

/**
 * Created by wanyu on 2017/10/17.
 */

public interface ILogin {
    void getSMSCodeSuccess();
    void getSMSCodeError(String msg);
    void getTimeOut(int count);
    void onLoginSuccess(LoginBean bean);
    void onLoginError(String msg);
}
