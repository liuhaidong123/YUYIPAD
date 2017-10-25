package com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;

import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_HomeUserTempAndPress;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.activity.FamilyUser.Fragment.FamilyUserInfo_DataNalFragment;
import com.technology.yuyipad.activity.FamilyUser.Fragment.FamilyUserInfo_MedicalRecordFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wanyu on 2017/10/12.
 */
//用户信息显示的fragment逻辑
public class FamilyUserInfoPresenter {
    TextView textVData,textVRecord;//数据分析，电子病历
    FragmentManager manager;
    FamilyUserInfo_MedicalRecordFragment fragMedical;//病历档案的frag
    FamilyUserInfo_DataNalFragment fragDataNa;//数据分析的frag
    Fragment fragCurrent;//当前的fragment
    //字体颜色切换
    public void initText(TextView textData,TextView textRecord){
        this.textVData=textData;
        this.textVRecord=textRecord;
        textVData.setSelected(true);
        textVRecord.setSelected(false);
    }
    //设置被选中的选项
    private void setSelect(int pos){
        switch (pos){
            case 0://数据分析
                textVData.setSelected(true);
                textVRecord.setSelected(false);
                break;
            case 1://电子病历
                textVData.setSelected(false);
                textVRecord.setSelected(true);
                break;
            default:
                Log.e("FamilyUserInfoPresenter","下标越界：最大只能是1，当前传递的参数下标为："+pos);
                break;
        }
    }
    //初始化fragment
    public void initFragment(FragmentManager manager,int resId){
        this.manager=manager;
        fragMedical=new FamilyUserInfo_MedicalRecordFragment();
        fragDataNa=new FamilyUserInfo_DataNalFragment();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(resId,fragDataNa);
        transaction.add(resId,fragMedical);
        fragCurrent=fragDataNa;
        transaction.hide(fragDataNa).hide(fragMedical).show(fragCurrent).commit();
    }
    //切换fragment
    public void showFragment(FragType tp){
        manager.beginTransaction().hide(fragCurrent).commit();
        switch (tp){
            case MEDICAL://电子病历
                fragCurrent=fragMedical;
                setSelect(1);
                break;
            case DATA://数据分析
                fragCurrent=fragDataNa;
                setSelect(0);
                break;
        }
        manager.beginTransaction().show(fragCurrent).commit();
    }
    //给下级页面传递当前的用户信息
    public void setUserInfo(String homeUserId,boolean isMySelf){
            fragDataNa.setUserId(homeUserId);
            fragMedical.setUserId(homeUserId,isMySelf);
    }
    public enum FragType{
        MEDICAL,DATA;//电子病历，数据分析
    }
}
