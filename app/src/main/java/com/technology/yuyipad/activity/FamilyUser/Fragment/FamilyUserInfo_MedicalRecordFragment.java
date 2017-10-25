package com.technology.yuyipad.activity.FamilyUser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technology.yuyipad.Lview.MyListView;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Adapter.FamilyUserInfo_EleRecordAdapter;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_HomeUserTempAndPress;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Models.FamilyUserMessageModel;
import com.technology.yuyipad.activity.Message.IMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wanyu on 2017/10/13.
 */
//显示电子病历的fragment
public class FamilyUserInfo_MedicalRecordFragment extends Fragment implements FamilyUserMessageModel.IElectMsg{
    Unbinder unbinder;
    String homeUserId;
    FamilyUserMessageModel model;
    @BindView(R.id.familyuser_ele_listView)MyListView familyuser_ele_listView;
    FamilyUserInfo_EleRecordAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi=inflater.inflate(R.layout.frag_familyuserinfo_medicalrecord,null);
        unbinder= ButterKnife.bind(this,vi);
        if (model==null){
            model=new FamilyUserMessageModel(this);
        }
        return vi;
    }

    //
    public void setUserId(String homeUserId,boolean isMySelf){
        this.homeUserId=homeUserId;
        if (model==null){
            model=new FamilyUserMessageModel(this);
        }
        if (isMySelf){
            model.getMyEleMsg();
        }
        else {
            model.getFamilyUserEleMsg(homeUserId);
        }
    }

    @Override
    public void onNetWorkError() {
        familyuser_ele_listView.setEmpey("网络异常！");
    }

    @Override
    public void onGetElectMsg(bean_MedicalRecordList recordList) {
            familyuser_ele_listView.setLoadingComplete();
            if (recordList.getResult()!=null&&recordList.getResult().size()>0){
                adapter=new FamilyUserInfo_EleRecordAdapter(getActivity(),recordList.getResult());
                familyuser_ele_listView.setAdapter(adapter);
            }
        else {
                familyuser_ele_listView.setEmpey("没有病历记录！");
                Log.e("没有电子病历","FamilyUserInfo_MedicalRecordFragment:null");
            }
    }

    @Override
    public void onGetUserTempAndPress(bean_HomeUserTempAndPress bean) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
