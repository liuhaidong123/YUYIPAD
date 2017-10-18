package com.technology.yuyipad.activity.FamilyUser.Fragment.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import com.technology.yuyipad.activity.FamilyUser.Fragment.FamilyUserInfo_Data_PagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanyu on 2017/10/13.
 */
//显示血压，温度的viewpager的适配器
public class FamilyUserInfo_DataNalPagerAdapter extends FragmentPagerAdapter{
    List<FamilyUserInfo_Data_PagerFragment>list;
    public FamilyUserInfo_DataNalPagerAdapter(FragmentManager fm,List<FamilyUserInfo_Data_PagerFragment>list) {
        super(fm);
        this.list=list;
        Log.e("-------","-----------------------------------------");
    }

    @Override
    public Fragment getItem(int position) {
        return list==null?null:list.get(position);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
//    public void setListData(List<FamilyUserInfo_Data_PagerFragment>li){
//        if (li==null){
//            Log.e("errorA:","FamilyUserInfo_DataNalPagerAdapter:setList中数据为null");
//            return;
//        }
//        if (list==null){
//            this.list=li;
//            Log.e("errorB:","FamilyUserInfo_DataNalPagerAdapter:setList中数据为null");
//            return;
//        }
//        notifyDataSetChanged();
//    }
}
