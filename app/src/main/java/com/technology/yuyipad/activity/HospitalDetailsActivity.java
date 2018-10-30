package com.technology.yuyipad.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.RongUtils.RongWindow;
import com.technology.yuyipad.bean.DoctorRoot;
import com.technology.yuyipad.bean.Information;
import com.technology.yuyipad.bean.PhysicianList;
import com.technology.yuyipad.fragment.ConsultFragment;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lhdUtils.NetWorkUtils;
import com.technology.yuyipad.user.User;

import java.util.ArrayList;
import java.util.List;

public class HospitalDetailsActivity extends AppCompatActivity {
    private AlertDialog.Builder builder,fuwuBuilder;
    private AlertDialog alertDialog,fuwuAlertDialog;
    private View alertView,fuwuAlertView;
    private int flagPosition = -1;//表示点击某个医生的下标
    private boolean flag = false;//true表示已经获取到医院医生咨询列表
    private ListView doctorListView;
    private List<PhysicianList> docList=new ArrayList<>();
    private DocAdapter docAdapter;
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
                        mHttptools.getDoctorList(handler, hospitalId);//获取医生视频列表
                        mHospital_Name.setText(information.getHospitalName());
                        mHospital_Grade.setText(information.getGradeName());
                        mHospital_Content.setText(information.getIntroduction());
                        Picasso.with(getApplicationContext()).load(UrlTools.BASE + information.getPicture()).error(R.mipmap.errorpicture).into(mImg);
                        mData_rl.setVisibility(View.VISIBLE);
                    } else {
                        mNoData_rl.setVisibility(View.VISIBLE);

                    }

                }
            }else if (msg.what == 44) {///获取医生视频列表
                Object o = msg.obj;
                if (o != null && o instanceof DoctorRoot) {
                    DoctorRoot doctorRoot = (DoctorRoot) o;
                    if (doctorRoot != null && "0".equals(doctorRoot.getCode() + "")) {
                        if (doctorRoot.getPhysicianList() != null) {
                            docList=doctorRoot.getPhysicianList();
                            docAdapter.notifyDataSetChanged();
                            flag = true;
                        }
                    } else {
                        Toast.makeText(HospitalDetailsActivity.this, "获取医生视频列表错误", Toast.LENGTH_SHORT).show();
                    }

                }
            }
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
        //服务弹框
        fuwuBuilder=new AlertDialog.Builder(this);
        fuwuBuilder.setCancelable(false);
        fuwuAlertDialog=fuwuBuilder.create();
        fuwuAlertView=LayoutInflater.from(this).inflate(R.layout.fuwu_alert,null);
        fuwuAlertDialog.setView(fuwuAlertView);
        TextView disagree=fuwuAlertView.findViewById(R.id.fuwu_disagree);//取消
        TextView agree=fuwuAlertView.findViewById(R.id.fuwu_agree);//同意
        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuwuAlertDialog.dismiss();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuwuAlertDialog.dismiss();
                User.saveFuWuFlag(HospitalDetailsActivity.this,true);
                if (flag) {
                    alertDialog.show();
                } else {
                    if (!"".equals(hospitalId)) {
                        mHttptools.getDoctorList(handler, hospitalId);//获取医生视频列表
                    } else {
                        Toast.makeText(HospitalDetailsActivity.this, "医院详情错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        builder = new AlertDialog.Builder(HospitalDetailsActivity.this);
        alertDialog = builder.create();
        alertView = LayoutInflater.from(HospitalDetailsActivity.this).inflate(R.layout.select_doctor, null);
        alertDialog.setView(alertView);
        doctorListView = alertView.findViewById(R.id.doc_listview);
        docAdapter=new DocAdapter();
        doctorListView.setAdapter(docAdapter);
        doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                flagPosition=i;
                alertDialog.dismiss();
                RongWindow.getInstance().showWindow(HospitalDetailsActivity.this, parentView, hospitalId,flagPosition);
            }
        });
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
                if (User.getFuWuFlag(HospitalDetailsActivity.this)){//判断用户有没有同意咨询服务协议

                    if (flag) {
                        alertDialog.show();
                    } else {
                        if (!"".equals(hospitalId)) {
                            mHttptools.getDoctorList(handler, hospitalId);//获取医生视频列表
                        } else {
                            Toast.makeText(HospitalDetailsActivity.this, "医院详情错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    fuwuAlertDialog.show();
                }


            }
        });

    }
    //医生列表适配器
    class DocAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return docList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DocHolder docHolder=null;
            if (view==null){
                view=LayoutInflater.from(HospitalDetailsActivity.this).inflate(R.layout.select_doctor_item,null);
                docHolder=new DocHolder();
                docHolder.doctor=view.findViewById(R.id.doctor1_btn);
                view.setTag(docHolder);
            }else {
                docHolder= (DocHolder) view.getTag();
            }

            docHolder.doctor.setText(docList.get(i).getTrueName());
            return view;
        }

        class  DocHolder{
            TextView doctor;
        }
    }
}
