package com.technology.yuyipad.activity.FamilyUser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.technology.yuyipad.activity.FamilyUser.Bean.FamilyUserListBean;
import com.technology.yuyipad.activity.FamilyUser.Fragment.FamilyUserAddFragment;
import com.technology.yuyipad.activity.FamilyUser.Fragment.FamilyUserInfoFragment;

/**
 * Created by wanyu on 2017/9/29.
 */

public class FamilyUserManagerPresenter {
    String TAG=getClass().getSimpleName();
    FamilyUserManagerModel model;
    FragmentManager manager;
    FamilyUserAddFragment fragAdd;//添加／修改家庭用户的fragment
    FamilyUserInfoFragment fragInfo;//显示家庭用户信息的fragment
    Fragment fragCurrent;//当前显示的fragment
    //获取家庭用户列表数据
    public void getData(IFamilyUserManager ilist){
        if (model==null){
            model=new FamilyUserManagerModel();
        }
        model.getUserList(ilist);
    }
    //初始化fragment
    public void initFragment(FragmentManager manager,int resId){
        this.manager=manager;
        fragAdd=new FamilyUserAddFragment();
        fragInfo=new FamilyUserInfoFragment();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(resId,fragAdd);
        transaction.add(resId,fragInfo);
        fragCurrent=fragInfo;
        transaction.hide(fragAdd).hide(fragInfo).show(fragCurrent).commit();
    }
    public void showFragment(FragmentType tp,FamilyUserListBean.ResultBean bean){
        manager.beginTransaction().hide(fragCurrent).commit();
        switch (tp){
            case USERINFO:
                fragCurrent=fragInfo;
                break;
            case USERADD:
                fragAdd.setUserInfo(null);
                fragCurrent=fragAdd;
                break;
            case USERCHANGE://修改信息
                fragAdd.setUserInfo(bean);
                fragCurrent=fragAdd;
                break;
        }
        manager.beginTransaction().show(fragCurrent).commit();
    }

    public void setUserData(FamilyUserListBean.ResultBean bean){
        if (bean!=null){
            fragInfo.setUserData(bean);
        }
        else {
            Log.e(TAG,"setUserData()方法错误：参数bean不能为null");
        }
    }

    public enum FragmentType{
        USERINFO,USERADD,USERCHANGE;//用户信息，添加，修改信息
    }
}
