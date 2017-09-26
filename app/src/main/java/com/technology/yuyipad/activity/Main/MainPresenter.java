package com.technology.yuyipad.activity.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.technology.yuyipad.fragment.ConsultFragment;
import com.technology.yuyipad.fragment.FirstPageFragment;
import com.technology.yuyipad.fragment.MeasureFragment;
import com.technology.yuyipad.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanyu on 2017/9/25.
 */

public class MainPresenter {
    Fragment fragment;//当前选中的fragment
    ConsultFragment cFragment;//咨询
    FirstPageFragment fFragment;//首页
    MeasureFragment meFrgament;//测量
    MyFragment myFragment;//我的
    List<HomeRelativeBean> li;
    FragmentManager manager;
    public MainPresenter(){
        li=new ArrayList<>();
    }
    public void initFragment( FragmentManager manager,int LayoutId){
        this.manager=manager;
        cFragment=new ConsultFragment();
        fFragment=new FirstPageFragment();
        meFrgament=new MeasureFragment();
        myFragment=new MyFragment();


        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(LayoutId,fFragment);
        transaction.add(LayoutId,meFrgament);
        transaction.add(LayoutId,cFragment);
        transaction.add(LayoutId,myFragment);
        fragment=fFragment;
        transaction.hide(fFragment).hide(meFrgament).hide(cFragment).hide(myFragment).show(fFragment).commit();
    }

    public void ShowFragment(int pos){
        switch (pos){
            case 0:
                manager.beginTransaction().hide(fragment).show(fFragment).commit();
                fragment=fFragment;
                break;
            case 1:
                manager.beginTransaction().hide(fragment).show(meFrgament).commit();
                fragment=meFrgament;
                break;
            case 2:
                manager.beginTransaction().hide(fragment).show(cFragment).commit();
                fragment=cFragment;
                break;
            case 3:
                manager.beginTransaction().hide(fragment).show(myFragment).commit();
                fragment=myFragment;
                break;
        }
    }

    //首页，测量页面，咨询页面，我的页面
    public void addList( HomeRelativeBean HomePage,HomeRelativeBean MeasurePage,HomeRelativeBean CounselingPage,HomeRelativeBean Minepage){
        li.add(HomePage);
        li.add(MeasurePage);
        li.add(CounselingPage);
        li.add(Minepage);
    }
    public void setSelect(int pos){
        for(int i=0;i<li.size();i++){
            li.get(i).setSelect(false);
        }
        li.get(pos).setSelect(true);
    }
}
