package com.technology.yuyipad.activity.FamilyUser.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.Lview.FormView;
import com.technology.yuyipad.R;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wanyu on 2017/10/13.
 */

public class FamilyUserInfo_Data_PagerFragment extends Fragment{
    Unbinder unbinder;

    @BindView(R.id.formview) FormView formview;
    @BindView(R.id.data_image) ImageView data_image;//显示状态的view（正常，异常，待测，过低，过高）
    @BindView(R.id.data_text_preL) TextView data_text_preL;//低压显示的view
    @BindView(R.id.data_text_preH)TextView data_text_preH;//，高压显示的view
    @BindView(R.id.data_textV_tem)TextView data_textV_tem;//温度显示的view
    @BindView(R.id.data_textV_title) TextView data_textV_title;//当前显示的是血压或者体温的title

    @BindView(R.id.data_rela_press) RelativeLayout data_rela_press;//血压数值所在的layout（体温时应隐藏）
    @BindView(R.id.data_lin_pressNomalInfo) LinearLayout data_lin_pressNomalInfo;//提示正常血压范围的layout（体温时应隐藏）

    @BindView(R.id.data_rela_tem) RelativeLayout data_rela_tem;//温度数值所在的layout（血压时应该隐藏）
    @BindView(R.id.data_lin_temNormalInfo) LinearLayout data_lin_temNormalInfo;//提示正常血压范围的layout（血压时应该隐藏）

    FormType tp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi=inflater.inflate(R.layout.familyuserpager_dataview,null);
        unbinder= ButterKnife.bind(this,vi);
        return vi;
    }
    public void init(FormType tp){
        this.tp=tp;
        switch (tp){
            case PRESS:
                initPress();
                break;
            case TEM:
                initTem();
                break;
        }
    }



    //初始化血压的数据
    private void initPress(){
        data_textV_title.setText("血压");
        data_rela_tem.setVisibility(View.GONE);
        data_lin_temNormalInfo.setVisibility(View.GONE);

        formview.drawRightTextView(new String[]{"高压","低压"});//血压
        List<Integer> li=new ArrayList<>();//血压Y轴数据源
        int min=40;//最低血压
        for (int i=0;i<8;i++){
            li.add(min);
            min+=20;
        }
        formview.drawTopView(li);
        List<Float>lit=new ArrayList<>();
        float minx=40;
        for (int i=0;i<7;i++){
            lit.add(minx);
            minx+=20;
        }
    }
    //初始化温度的数据
    private void initTem(){
        data_textV_title.setText("体温");
        data_rela_press.setVisibility(View.GONE);
        data_lin_pressNomalInfo.setVisibility(View.GONE);


        formview.drawRightTextView(new String[]{"体温"});//温度
        List<Integer>lt=new ArrayList<>();
        int minTemp=35;//最低温度
        for (int i=0;i<8;i++){
            lt.add(minTemp);
            minTemp+=1;
        }
        formview.setColor(Color.parseColor("#7ed66b"));
        formview.drawTopView(lt);
    }
    //设置血压数据(时间，高压，低压，最后一条数据的低压，高压)
    public void setPress(List<String>listTime,List<Float>listSource,List<Float>listOtherSource,Float lowPress,Float highPress){
        formview.drawBottomView(listTime);
        formview.drawFirstDataView(listSource);
        formview.drawOtherDataView(listOtherSource);
        data_text_preL.setText(getText(lowPress));
        data_text_preH.setText(getText(highPress));
        data_image.setImageResource(getInfoRes(highPress,lowPress,0));
    }
    //设置温度数据（时间，数据，最后一条的温度）
    public void setTem(List<String>listTime,List<Float>listSource,Float tem){
        formview.drawBottomView(listTime);
        formview.drawFirstDataView(listSource);
        data_textV_tem.setText(getText(tem));
        data_image.setImageResource(getInfoRes(0,0,tem));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    public enum FormType{
        TEM,PRESS;//温度，血压
    }

    public String getText(float tex){
        String text=tex+"";
        String tt=text.substring(0,text.indexOf("."));
        String last=text.substring(text.indexOf(".")+1,text.length());
        if ("0".equals(last)|"00".equals(last)){
            text=tt;
        }
        Log.i("text===",text);
        return text;
    }

    public int getInfoRes(float highP,float lowP,float tem){
        int[] imageResid={R.mipmap.a_unmesure,R.mipmap.a_low,R.mipmap.a_normal,R.mipmap.a_high,R.mipmap.a_error};//待测，偏低，正常，偏高，异常
//        体温范围：36.1~37.2
//        血压范围：maxP收缩压／高压90~140，minP舒张压／低压60~90
        int pos=-1;
        switch (tp){
            case PRESS:
                if (lowP==0|highP==0){
                    pos=0;//待测
                }
                else if (lowP<60|highP<90){
                    pos=1;//偏低
                }
                else if (lowP>90|highP>140){
                    pos=3;//偏高
                }
                else if (lowP>=60&&lowP<=90&&highP>=90&&highP<=140){
                    pos=2;//正常
                }

                break;
            case TEM:
                if (tem==0){
                    pos=0;//待测
                }
                else if (tem<=34){
                    pos=4;//异常
                }
                else if (tem>34&&tem<36.1){
                    pos=1;//偏低
                }
                else if (tem>=36.1&&tem<=37.2){
                    pos=2;//正常
                }
                else if (tem>37.2&&tem<=42){
                    pos=3;//偏高
                }
                else {
                    pos=4;//异常
                }
                break;
        }
        if (pos==-1){
            Log.e("getInfoRes错误","FamilyUserInfo_Data_PagerFragment：下表不正确");
            return imageResid[2];
        }
        return imageResid[pos];
    }
}
