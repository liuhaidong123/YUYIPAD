package com.technology.yuyipad.activity.FamilyUser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.FamilyUser.Bean.FamilyUserListBean;
import com.technology.yuyipad.activity.FamilyUser.FamilyUserManagerActivity;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_HomeUserTempAndPress;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Models.FamilyUserMessageModel;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter.FamilyUserInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wanyu on 2017/10/12.
 */
//显示家庭用户信息
public class FamilyUserInfoFragment extends Fragment {
    Unbinder unbinder;
    FamilyUserInfoPresenter presenter;
    FamilyUserListBean.ResultBean bean;//保存的当前的用户信息实体
    @BindView(R.id.familyUserInfo_textV_Name)TextView familyUserInfo_textV_Name;//显示姓名与昵称
    @BindView(R.id.familyUserInfo_textV_dataNalysis)TextView familyUserInfo_textV_dataNalysis;
    @BindView(R.id.familyUserInfo_textV_medicalRecord)TextView familyUserInfo_textV_medicalRecord;
    @BindView(R.id.familyUserInfo_fragLayout)RelativeLayout familyUserInfo_fragLayout;//用户显示数据分析与电子档案的fragnment的lyaout
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi=inflater.inflate(R.layout.frag_familyuser_info,null);
        unbinder= ButterKnife.bind(this,vi);
        presenter=new FamilyUserInfoPresenter();
        presenter.initText(familyUserInfo_textV_dataNalysis,familyUserInfo_textV_medicalRecord);
        presenter.initFragment(getFragmentManager(),R.id.familyUserInfo_fragLayout);
        return vi;
    }
    @OnClick({R.id.familyUserInfo_imgV_Edit,R.id.familyUserInfo_textV_dataNalysis,R.id.familyUserInfo_textV_medicalRecord})
    public void 点击方法(View vi){
        switch (vi.getId()){
            case R.id.familyUserInfo_imgV_Edit://编辑用户信息
                FamilyUserManagerActivity ac= (FamilyUserManagerActivity) getActivity();
                ac.showFragChange();//显示修改的fargment
                break;
            case R.id.familyUserInfo_textV_dataNalysis://数据分析
                presenter.showFragment(FamilyUserInfoPresenter.FragType.DATA);
                break;
            case R.id.familyUserInfo_textV_medicalRecord://电子病历
                presenter.showFragment(FamilyUserInfoPresenter.FragType.MEDICAL);
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
    //接收用户信息实体类
    public void setUserData(FamilyUserListBean.ResultBean bean){
        if (bean!=null){
            if (this.bean!=null&&this.bean.equals(bean)){//防止重复请求数据
                return;
            }
            this.bean=bean;
            presenter.setUserInfo(bean.getId()+"");
            familyUserInfo_textV_Name.setText(bean.getTrueName()+"  "+"("+bean.getNickName()+")");
        }
    }
}
