package com.technology.yuyipad.activity.MedicalRecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.technology.yuyipad.Lview.MyListView;
import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Adapter.FamilyUserInfo_EleRecordAdapter;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.activity.Medicinal.BeanDrugStates;
import com.technology.yuyipad.activity.Medicinal.IMedical;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EleMedicalRecordListActivity extends MyActivity implements EleMedicalRecordListModel.IMedicalRecord ,AdapterView.OnItemClickListener{
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @BindView(R.id.ele_listView)MyListView ele_listView;
    EleMedicalRecordListModel model;
    EleMedicalReocrdListAdapter adapter;
    List<bean_MedicalRecordList.ResultBean> lis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_ele_medical_record_list);
        titleinclude_text.setText("电子病历");
        model=new EleMedicalRecordListModel();
        model.getMyEleMsg(this);
        lis=new ArrayList<>();
        adapter=new EleMedicalReocrdListAdapter(this,lis);
        ele_listView.setAdapter(adapter);
        ele_listView.setOnItemClickListener(this);
    }


    @Override
    public void onError(String msg) {
        ele_listView.setEmpey(msg);
    }

    @Override
    public void onTokenError() {
        ExitLogin.getInstance().showLogin(this);
    }

    @Override
    public void onSuccess(bean_MedicalRecordList bList) {
        ele_listView.setLoadingComplete();
        if (bList.getResult()!=null&&bList.getResult().size()>0){
            lis.addAll(bList.getResult());
            adapter.notifyDataSetChanged();
        }
        else {
            ele_listView.setEmpey("没有病历记录！");
            Log.e("没有电子病历","FamilyUserInfo_MedicalRecordFragment:null");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,EleMedicalRecordActivity.class);
        intent.putExtra("result",lis.get(i));
        startActivity(intent);
    }
}
