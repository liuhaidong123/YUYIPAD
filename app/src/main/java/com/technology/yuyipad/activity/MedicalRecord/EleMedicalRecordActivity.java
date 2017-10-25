package com.technology.yuyipad.activity.MedicalRecord;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.technology.yuyipad.Lview.MyGirdView;
import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.lzhUtils.MyActivity;

import butterknife.BindView;

public class EleMedicalRecordActivity extends MyActivity {
    bean_MedicalRecordList.ResultBean bean;
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @BindView(R.id.eleMsg_text_creatTime)TextView eleMsg_text_creatTime;//就诊时间
    @BindView(R.id.eleMsg_text_hospitalName)TextView eleMsg_text_hospitalName;//就诊医院
    @BindView(R.id.eleMsg_text_KS)TextView eleMsg_text_KS;//科室
    @BindView(R.id.eleMsg_text_DocName)TextView eleMsg_text_DocName;//医生
    @BindView(R.id.ele_message)TextView ele_message;//病情描述
    @BindView(R.id.ele_gridView)MyGirdView ele_gridView;//图片
    LookElecAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_ele_medical_record);
        bean= (bean_MedicalRecordList.ResultBean) getIntent().getSerializableExtra("result");
        titleinclude_text.setText("病历查看");
        if (bean!=null){
            initView();
        }
    }

    private void initView() {
        eleMsg_text_creatTime.setText(bean.getCreateTimeString());
        eleMsg_text_hospitalName.setText(bean.getHospitalName());
        eleMsg_text_KS.setText(bean.getDepartmentName());
        eleMsg_text_DocName.setText(bean.getPhysicianName());
        String url=bean.getPicture();
        if (url!=null&&!"".equals(url)){
            String[]str=url.split(";");
            adapter=new LookElecAdapter(this,str);
            ele_gridView.setAdapter(adapter);
        }
    }
}
