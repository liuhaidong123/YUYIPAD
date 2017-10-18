package com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Adapter.FamilyUserInfo_DataNalPagerAdapter;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_HomeUserTempAndPress;
import com.technology.yuyipad.activity.FamilyUser.Fragment.FamilyUserInfo_Data_PagerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wanyu on 2017/10/13.
 */
//我的血压，温度状态的fragment
public class FamilyUserInfo_DataNalPresenter {
    ViewPager pager;
    FamilyUserInfo_DataNalPagerAdapter adapter;
    List<FamilyUserInfo_Data_PagerFragment>li;
    public void initDataView(Context con, FragmentManager manager,ViewPager pager){
        this.pager=pager;
        li=new ArrayList<>();
        FamilyUserInfo_Data_PagerFragment fPress=new FamilyUserInfo_Data_PagerFragment();

        FamilyUserInfo_Data_PagerFragment fTem=new FamilyUserInfo_Data_PagerFragment();

        li.add(fPress);
        li.add(fTem);
        adapter=new FamilyUserInfo_DataNalPagerAdapter(manager,li);
        pager.setAdapter(adapter);
        fPress.init(FamilyUserInfo_Data_PagerFragment.FormType.PRESS);
        fTem.init(FamilyUserInfo_Data_PagerFragment.FormType.TEM);
    }

    //获取温度与血压
    public void setTmeAndPress(bean_HomeUserTempAndPress bean) {
        if ("0".equals(bean.getCode())){
            bean_HomeUserTempAndPress.ResultBean result= bean.getResult();
            if (result!=null){
                //最后一条数据的高压，低压，体温值（用于页面显示）
                float HighPress=0.0f;
                float LowPress=0.0f;
                float Temp=0.0f;
                List<bean_HomeUserTempAndPress.ResultBean.BloodpressureListBean>listBlood=result.getBloodpressureList();//血压
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
                //处理血压中的数据
                if (listBlood!=null&&listBlood.size()>0){
                    LowPress=listBlood.get(listBlood.size()-1).getDiastolic();
                    HighPress=listBlood.get(listBlood.size()-1).getSystolic();
                    List<String>listTime=new ArrayList<>();//血压测量的日期的集合
                    List<Float>listSource=new ArrayList<>();//高压集合值
                    List<Float>listOhtherSource=new ArrayList<>();//低压集合值
                    for (int i=0;i<listBlood.size();i++){
                        try{
                            String dstr=listBlood.get(i).getCreateTimeString();
                            Date date=sdf.parse(dstr);
                            SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                            String time=format.format(date);
                            listTime.add(time);
                            listSource.add(Float.parseFloat(listBlood.get(i).getSystolic()+""));//高压
                            listOhtherSource.add(Float.parseFloat(listBlood.get(i).getDiastolic()+""));//低压
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (listTime.size()<7){//补全不足7天的日期
                        try{
                            int tp=7-listTime.size();
                            SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                            Calendar c = Calendar.getInstance();
                            String dstr=listBlood.get(listBlood.size()-1).getCreateTimeString();
                            Date date=sdf.parse(dstr);
                            c.setTime(date);//以最后一个测量日期为准加够7天
                            for (int i=0;i<tp;i++){//填充日期，最后一个日期加1天
                                c.add(Calendar.DAY_OF_MONTH,1);
                                listTime.add(format.format(c.getTime()));
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (listSource.size()<7){//补全不足7天的高压
                        int tp=7-listSource.size();
                        for (int i=0;i<tp;i++){
                            listSource.add(-1f);
                        }
                    }
                    if (listOhtherSource.size()<7){//补全不足7天的低压
                        int tp=7-listOhtherSource.size();
                        for (int i=0;i<tp;i++){
                            listOhtherSource.add(-1f);
                        }
                    }
                    li.get(0).setPress(listTime,listSource,listOhtherSource,LowPress,HighPress);
                }
                else {
                    li.get(0).setPress(null,null,null,0.0f,0.0f);
                }
                //处理温度的中的数据
                List<bean_HomeUserTempAndPress.ResultBean.TemperatureListBean>listTemp=result.getTemperatureList();//温度
                if (listTemp!=null&&listTemp.size()>0){
                    Temp=listTemp.get(listTemp.size()-1).getTemperaturet();
                    List<String>listTime=new ArrayList<>();//测量的日期
                    List<Float>listSource=new ArrayList<>();//测量到的血压数据
                    for (int i=0;i<listTemp.size();i++){
                        try{
                            String dstr=listTemp.get(i).getCreateTimeString();
                            Date date=sdf.parse(dstr);
                            SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                            String time=format.format(date);
                            listTime.add(time);
                            listSource.add(Float.parseFloat(listTemp.get(i).getTemperaturet()+""));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    if (listTime!=null&&listTime.size()<7){//补全不足7天的日期
                        try{
                            int tp=7-listTime.size();
                            SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                            Calendar c = Calendar.getInstance();
                            String dstr=listTemp.get(listTemp.size()-1).getCreateTimeString();
                            Date date=sdf.parse(dstr);
                            c.setTime(date);//以最后一个测量日期为准加够7天
                            for (int i=0;i<tp;i++){//填充日期，最后一个日期加1天
                                c.add(Calendar.DAY_OF_MONTH,1);
                                listTime.add(format.format(c.getTime()));
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            int tp=7-listTime.size();
                            SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                            Calendar c = Calendar.getInstance();
                            String dstr=listTemp.get(listTemp.size()-1).getCreateTimeString();
                            Date date=sdf.parse(dstr);
                            c.setTime(date);//以最后一个测量日期为准加够7天
                            for (int i=0;i<tp;i++){//填充日期，最后一个日期加1天
                                c.add(Calendar.DAY_OF_MONTH,1);
                                listTime.add(format.format(c.getTime()));
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (listSource!=null&&listSource.size()<7){//补全不足7天的体温
                        int tp=7-listSource.size();
                        for (int i=0;i<tp;i++){
                            listSource.add(-1f);//-1f表示当天没有数据
                        }
                    }
                    li.get(1).setTem(listTime,listSource,Temp);
                }
                else {
                    li.get(1).setTem(null,null,0.0f);
                    }
            }
            adapter.notifyDataSetChanged();
        }
    }

    public String getText(float tex){
        String text=tex+"";
        String tt=text.substring(0,text.indexOf("."));
        String last=text.substring(text.indexOf(".")+1,text.length());
        if ("0".equals(last)|"00".equals(last)){
            text=tt;
        }
        return text;
    }

}
