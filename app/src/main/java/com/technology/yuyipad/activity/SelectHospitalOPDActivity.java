package com.technology.yuyipad.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.adapter.DepartmentAda;
import com.technology.yuyipad.adapter.DoctorAda;
import com.technology.yuyipad.adapter.HospitalAda;
import com.technology.yuyipad.adapter.OPDAda;
import com.technology.yuyipad.bean.FirstPageInformationTwoData;
import com.technology.yuyipad.bean.FirstPageInformationTwoDataRoot;
import com.technology.yuyipad.bean.HospitalDepartmentMessage;
import com.technology.yuyipad.bean.HospitalDepartmentRoot;
import com.technology.yuyipad.bean.HospitalOutPatient;
import com.technology.yuyipad.bean.SelectDoctor.DatenumberList;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.lhdUtils.InformationListView;
import com.technology.yuyipad.lhdUtils.UseDrugGridView;

import java.util.ArrayList;
import java.util.List;

public class SelectHospitalOPDActivity extends AppCompatActivity implements View.OnClickListener{
    //医院列表
    private InformationListView mHospital_listview;
    private HospitalAda mHospitalAda;
    private List<FirstPageInformationTwoData> mList = new ArrayList<>();
    private List<Boolean> mHospitalCheckList = new ArrayList<>();
    private RelativeLayout mMany_more;
    private ProgressBar mBar;
    private boolean isMore = false;//true表示点击了加载更多
    private int mStart = 0;
    private int mAddNum = 10;
    private HttpTools mHttptools;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 28) {//医院列表
                Object o = msg.obj;
                if (o != null && o instanceof FirstPageInformationTwoDataRoot) {
                    FirstPageInformationTwoDataRoot root = (FirstPageInformationTwoDataRoot) o;
                    List<FirstPageInformationTwoData> list = new ArrayList<>();
                    if (root != null && root.getRows() != null) {
                        list = root.getRows();
                        mList.addAll(list);

                        if (!isMore) {//没有点击加载更多医院
                            for (int i = 0; i < mList.size(); i++) {
                                if (i == 0) {//默认点击了第一个医院，背景颜色变化
                                    mHospitalCheckList.add(true);
                                } else {
                                    mHospitalCheckList.add(false);
                                }
                            }
                            mHospitalAda.setList(mList, mHospitalCheckList);
                            mHospitalAda.notifyDataSetChanged();

                            if (mList.size() != 0) {//默认请求第一个医院的科室
                                mHttptools.getHospitalDepartmentData(mHandler, mList.get(0).getId());
                            }
                        } else {//点击了加载更多
                            for (int i = 0; i < list.size(); i++) {
                                mHospitalCheckList.add(false);
                            }
                            mHospitalAda.setList(mList, mHospitalCheckList);
                            mHospitalAda.notifyDataSetChanged();
                        }

                        mBar.setVisibility(View.INVISIBLE);
                        if (list.size() == 10) {
                            mMany_more.setVisibility(View.VISIBLE);
                        } else {
                            mMany_more.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (msg.what == 213) {
                mBar.setVisibility(View.INVISIBLE);
            } else if (msg.what == 214) {
                mBar.setVisibility(View.INVISIBLE);
            } else if (msg.what == 29) {//获取科室
                Object o = msg.obj;
                if (o != null && o instanceof HospitalDepartmentRoot) {
                    HospitalDepartmentRoot root = (HospitalDepartmentRoot) o;
                    if (root != null && root.getCode().equals("0") && root.getResult() != null) {
                        mDepartmentList = root.getResult();
                        //默认点击了第一个医院的第一个科室，背景颜色变化
                        for (int i = 0; i < mDepartmentList.size(); i++) {
                            if (i == 0) {
                                mDeparmentCheckList.add(true);
                            } else {
                                mDeparmentCheckList.add(false);
                            }
                        }
                        mDepartmentAda.setmList(mDepartmentList, mDeparmentCheckList);
                        mDepartmentAda.notifyDataSetChanged();
                        mAllData.setVisibility(View.VISIBLE);//显示数据
                        mPopupwindow.dismiss();
                        mDepartment_gridview.setVisibility(View.VISIBLE);
                        mOPD_gridview.setVisibility(View.VISIBLE);
                        //提取每个科室中的门诊集合
                        for (int i = 0; i < mDepartmentList.size(); i++) {
                            mRightData.add(mDepartmentList.get(i).getClinicList());
                        }
                        // 刚进页面，将第一个科室的所有门诊数据显示
                        if (mRightData.size() != 0) {
                            mOPDAda = new OPDAda(SelectHospitalOPDActivity.this, (List<HospitalOutPatient>) mRightData.get(0));
                            mOPD_gridview.setAdapter(mOPDAda);
                            mOPDAda.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    };

    //科室
    private UseDrugGridView mDepartment_gridview;
    private DepartmentAda mDepartmentAda;
    private List<HospitalDepartmentMessage> mDepartmentList = new ArrayList();
    private List<Boolean> mDeparmentCheckList = new ArrayList<>();
    //门诊
    private UseDrugGridView mOPD_gridview;
    private OPDAda mOPDAda;
    private List<List<HospitalOutPatient>> mRightData = new ArrayList<>();//门诊集合

    //所有数据
    private LinearLayout mAllData;

    //加载
    private PopupWindow mPopupwindow;

    //显示医生数据
    private LinearLayout mDoctor_ll, mDepartmentAndOpd_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hospital_opd);
        //获取首页数据
        mHttptools = HttpTools.getHttpToolsInstance();
        mHttptools.getAppintmentData(mHandler, 0, 10);//医院列表

        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        mAllData = (LinearLayout) findViewById(R.id.all_data_register_ll);
        View view = LayoutInflater.from(this).inflate(R.layout.loading_box, null);
        mPopupwindow = new PopupWindow(view);
        //医院
        mHospital_listview = (InformationListView) findViewById(R.id.hospital_listview);
        mHospitalAda = new HospitalAda(this, mList);
        mHospital_listview.setAdapter(mHospitalAda);
        mMany_more = (RelativeLayout) findViewById(R.id.hospital_many_relative);
        mBar = (ProgressBar) findViewById(R.id.in_pbLocate);
        //医加载更多
        mMany_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMore = true;
                mBar.setVisibility(View.VISIBLE);
                mStart += 10;
                mHttptools.getAppintmentData(mHandler, mStart, mAddNum);
            }
        });
        //点击医院显示科室和默认第一个科室下的所有门诊
        mHospital_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mHospitalCheckList.clear();
                mDepartmentPosition=0;
                mDoctor_ll.setVisibility(View.GONE);//隐藏医生
                mDepartmentAndOpd_ll.setVisibility(View.VISIBLE);//显示科室门诊
                showPopuWindow();
                //变化背景
                for (int k = 0; k < mList.size(); k++) {
                    if (k == i) {
                        mHospitalCheckList.add(true);
                    } else {
                        mHospitalCheckList.add(false);
                    }
                }
                mHospitalAda.setList(mList, mHospitalCheckList);
                mHospitalAda.notifyDataSetChanged();

                //请求当前医院下的科室
                mDeparmentCheckList.clear();
                mHttptools.getHospitalDepartmentData(mHandler, mList.get(i).getId());
                mDepartment_gridview.setVisibility(View.GONE);
                mOPD_gridview.setVisibility(View.GONE);
            }
        });

        //科室
        mDepartment_gridview = (UseDrugGridView) findViewById(R.id.girdview_Department);
        mDepartmentAda = new DepartmentAda(this, mDepartmentList);
        mDepartment_gridview.setAdapter(mDepartmentAda);
        //点击科室获取门诊
        mDepartment_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //默认点击了第一个医院的第一个科室，背景颜色变化
                mDeparmentCheckList.clear();
                for (int k = 0; k < mDepartmentList.size(); k++) {
                    if (k == i) {
                        mDeparmentCheckList.add(true);
                    } else {
                        mDeparmentCheckList.add(false);
                    }
                }
                mDepartmentAda.setmList(mDepartmentList, mDeparmentCheckList);
                mDepartmentAda.notifyDataSetChanged();

                // 点击某个科室获取对应门诊集合
                if (mRightData.size() != 0) {
                    mDepartmentPosition=i;
                    mOPDAda.setmList((List<HospitalOutPatient>) mRightData.get(i));
                    mOPDAda.notifyDataSetChanged();
                }
            }
        });

        //门诊
        mOPD_gridview = (UseDrugGridView) findViewById(R.id.girdview_opd);
        //mOPDAda = new OPDAda(this, mRightData);
        //医生布局和科室门诊布局
        mDoctor_ll = (LinearLayout) findViewById(R.id.select_doctor_ll);
        mDepartmentAndOpd_ll = (LinearLayout) findViewById(R.id.department_ll);

        //点击门诊获取医生
        mOPD_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDoctor_ll.setVisibility(View.VISIBLE);//显示医生
                mDepartmentAndOpd_ll.setVisibility(View.GONE);//隐藏所有科室门诊
                mName_ke.setText(mRightData.get(mDepartmentPosition).get(i).getClinicName());
                Log.e("门诊下标",i+"");
                Log.e("科室下标",mDepartmentPosition+"");
                mHttptools.getUserRegisterData(mHandler, mRightData.get(mDepartmentPosition).get(i).getId());//获取所有医生列表
            }
        });

        //左右日期按钮
        mLfetDate_img = (ImageView) findViewById(R.id.prior_date_btn);
        mRightDate_img = (ImageView) findViewById(R.id.next_date_btn);
        mLfetDate_img.setOnClickListener(this);
        mRightDate_img.setOnClickListener(this);

        //上下午
        mMorning_tv = (TextView) findViewById(R.id.morning_btn);
        mAfternoon_tv = (TextView) findViewById(R.id.afternoon_btn);
        mMorning_tv.setOnClickListener(this);
        mAfternoon_tv.setOnClickListener(this);
        //点击某个门诊名称
        mName_ke = (TextView) findViewById(R.id.opd_name_tv);
        //医生列表
        mGridView= (UseDrugGridView) findViewById(R.id.doctor_gridview);
        mDoctorAda=new DoctorAda(this, mDoctorList, isFlag);
        mGridView.setAdapter(mDoctorAda);
    }
    private ImageView mLfetDate_img, mRightDate_img;//左右日期
    private TextView  mMorning_tv, mAfternoon_tv,mName_ke;//上午下午
    private int mDepartmentPosition=0;//点击科室的时候获取的下标
    private UseDrugGridView mGridView;
    private DoctorAda mDoctorAda;
    private List<DatenumberList> mDoctorList = new ArrayList<>();
    private boolean isFlag = true;//默认true上午, false 下午
    //加载弹框
    private void showPopuWindow() {
        //设置透明度
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);

        //设置弹框的款，高
        mPopupwindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupwindow.setFocusable(true);//如果有交互需要设置焦点为true
        mPopupwindow.setOutsideTouchable(false);//设置内容外可以点击

        //相对于父控件的位置
        mPopupwindow.showAtLocation(mAllData, Gravity.CENTER, 0, 0);
        //当弹框销毁时，将透明度初始化，否则弹框销毁后，所依附的activity页面背景将会改变
        mPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        if (id==mLfetDate_img.getId()){//左日期

        }else if (id==mRightDate_img.getId()){//右日期

        }else if (id==mMorning_tv.getId()){//上午
            mMorning_tv.setTextColor(ContextCompat.getColor(this,R.color.ffffff));
            mMorning_tv.setBackgroundResource(R.drawable.morning_bg);
            mAfternoon_tv.setTextColor(ContextCompat.getColor(this,R.color.color_drumall));
            mAfternoon_tv.setBackgroundResource(R.drawable.afternoon_cancle_bg);

        }else if (id==mAfternoon_tv.getId()){//下午

            mMorning_tv.setTextColor(ContextCompat.getColor(this,R.color.color_drumall));
            mMorning_tv.setBackgroundResource(R.drawable.morning_cancle_bg);
            mAfternoon_tv.setTextColor(ContextCompat.getColor(this,R.color.ffffff));
            mAfternoon_tv.setBackgroundResource(R.drawable.afternoon_bg);
        }
    }
}
