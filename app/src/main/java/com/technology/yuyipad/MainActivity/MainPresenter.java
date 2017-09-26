package com.technology.yuyipad.MainActivity;

import com.technology.yuyipad.MainActivity.HomeRelativeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanyu on 2017/9/25.
 */

public class MainPresenter {
    List<HomeRelativeBean> li;
    public MainPresenter(){
        li=new ArrayList<>();
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
