package com.technology.yuyipad.code;

import android.content.Context;

/**
 * Created by wanyu on 2017/9/27.
 */
//token失效重新登录
public class ExitLogin {
    static  ExitLogin login;
    private ExitLogin(){

    }
    public static ExitLogin getInstance(){
        if (login==null){
            login=new ExitLogin();
        }
        return login;
    }
    public void showLogin(Context con){

    }
}
