package com.technology.yuyipad.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.RongUtils.RongWindow;
import com.technology.yuyipad.bean.Information;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lhdUtils.NetWorkUtils;

public class HospitalDetailsActivity extends AppCompatActivity {
    //医院详情
    private RelativeLayout mNoData_rl, mData_rl;
    View parentView;
    private ImageView mImg;
    private TextView mHospital_Name, mHospital_Grade, mHospital_Content, mAsk_Btn;
    String hospitalId = "";
    private HttpTools mHttptools;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 25) {//医院详情
                Object o = msg.obj;
                if (o != null && o instanceof Information) {
                    Information information = (Information) o;
                    if (information != null) {
                        hospitalId = information.getId() + "";
                        mHospital_Name.setText(information.getHospitalName());
                        mHospital_Grade.setText(information.getGradeName());
                        mHospital_Content.setText(information.getIntroduction());
                        Picasso.with(getApplicationContext()).load(UrlTools.BASE + information.getPicture()).error(R.mipmap.errorpicture).into(mImg);
                        mData_rl.setVisibility(View.VISIBLE);
                    } else {
                        mNoData_rl.setVisibility(View.VISIBLE);

                    }

                }
            }//
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetWorkUtils.isNetWorkConnected(this)) {
            setContentView(R.layout.activity_hospital_details);
            initUI();
        } else {
            setContentView(R.layout.firstpage_newwork);
            TextView textView = (TextView) findViewById(R.id.first_page_tv);
            textView.setText("医院详情");
        }


    }

    private void initUI() {
        mNoData_rl = (RelativeLayout) findViewById(R.id.h_no_data);
        mData_rl = (RelativeLayout) findViewById(R.id.h_all_data);
        parentView = findViewById(R.id.activity_hospital_details);
        mHttptools = HttpTools.getHttpToolsInstance();//医院列表
        mHttptools.getAskDataMessage(handler, getIntent().getIntExtra("id", -1));//请求医院详情
        //医院详情
        mImg = (ImageView) findViewById(R.id.hospital_img);
        mHospital_Name = (TextView) findViewById(R.id.hospital_name);
        mHospital_Grade = (TextView) findViewById(R.id.hospital_grade);
        mHospital_Content = (TextView) findViewById(R.id.hospital_content);

        mAsk_Btn = (TextView) findViewById(R.id.bottomBtn);//咨询语音视频
        mAsk_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongWindow.getInstance().showWindow(HospitalDetailsActivity.this, parentView, hospitalId);
            }
        });

    }

}
