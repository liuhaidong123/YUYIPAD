package com.technology.yuyipad.activity.Medicinal;

/**
 * Created by wanyu on 2017/10/17.
 */

public interface IMedical {
    void onGetMedicalListSuccess( BeanDrugStates bean);//获取我的药品列表成功
    void onGetMedicalListError(String msg,String interfaceName);//获取我的药品列表失败（失败原因，接口名）
    void onTokenError();//token失效
}
