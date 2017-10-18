package com.technology.yuyipad.activity.FamilyUser.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technology.yuyipad.R;

/**
 * Created by wanyu on 2017/10/13.
 */
//显示电子病历的fragment
public class FamilyUserInfo_MedicalRecordFragment extends Fragment{
    String homeUserId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi=inflater.inflate(R.layout.frag_familyuserinfo_medicalrecord,null);
        return vi;
    }

    //
    public void setUserId(String homeUserId){
        this.homeUserId=homeUserId;
    }
}
