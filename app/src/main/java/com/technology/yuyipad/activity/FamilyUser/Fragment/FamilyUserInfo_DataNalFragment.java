package com.technology.yuyipad.activity.FamilyUser.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Adapter.FamilyUserInfo_DataNalPagerAdapter;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_HomeUserTempAndPress;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Models.FamilyUserMessageModel;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter.FamilyUserInfo_DataNalPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wanyu on 2017/10/13.
 */
//显示我的数据分析(温度，血压)的fragment（外层）
public class FamilyUserInfo_DataNalFragment extends Fragment implements FamilyUserMessageModel.IElectMsg,ViewPager.OnPageChangeListener{
    String homeUserId;
    Unbinder unbinder;
    FamilyUserMessageModel model;
    FamilyUserInfo_DataNalPresenter presenter;
    @BindView(R.id.familyuser_dataNalysis_viewpter)ViewPager familyuser_dataNalysis_viewpter;//数据分析的pager
    @BindView(R.id.imagA)ImageView imagA;
    @BindView(R.id.imagB)ImageView imagB;

    bean_HomeUserTempAndPress bean;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                presenter.initDataView(getActivity(),getChildFragmentManager(),familyuser_dataNalysis_viewpter);
                if (bean!=null){//此处是为了防止当网速过快时请求到了数据但此时viewpager但绘制还没有完成，数据源没有绑定到viewpager的fragment中，在此处重新绑定
                    presenter.setTmeAndPress(bean);
                }
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi=inflater.inflate(R.layout.frag_familyuserinfo_data,null);
        unbinder= ButterKnife.bind(this,vi);
        presenter=new FamilyUserInfo_DataNalPresenter();
        new Thread(new Runnable() {//此处是为了解决viewpager中的内容绘制与当前fragment的绘制抢夺主线程资源，出现阻塞的问题（先绘制fragment，延时绘制viewpager中的内容）
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        model=new FamilyUserMessageModel(this);
        familyuser_dataNalysis_viewpter.addOnPageChangeListener(this);
        imagA.setSelected(true);
        imagB.setSelected(false);
        return vi;
    }

    public void setUserId(String homeUserId){
        this.homeUserId=homeUserId;
        model.getTempAndPress(homeUserId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public void onNetWorkError() {

    }

    @Override
    public void onGetElectMsg(bean_MedicalRecordList recordList) {

    }
    //获取温度与血压成功
    @Override
    public void onGetUserTempAndPress(bean_HomeUserTempAndPress bean) {
            if (bean!=null){
                    this.bean=bean;
                    presenter.setTmeAndPress(bean);
                    Log.e("aaaaa1111","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                imagA.setSelected(true);
                imagB.setSelected(false);
                break;
            case 1:
                imagA.setSelected(false);
                imagB.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
