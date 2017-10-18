package com.technology.yuyipad.activity.Medicinal;

/**
 * Created by wanyu on 2017/10/17.
 */

public class MyMedicalPresneter {
    MyMedicalModel model;
    public void getMedicalList(IMedical iMedical){
        if (model==null){
            model=new MyMedicalModel();
        }
        model.getMedicalList(iMedical);
    }
}
