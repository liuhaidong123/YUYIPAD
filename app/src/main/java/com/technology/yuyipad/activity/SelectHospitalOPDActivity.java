package com.technology.yuyipad.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.FamilyUserManagerActivity;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter.IFamilyUserAdd;
import com.technology.yuyipad.adapter.DepartmentAda;
import com.technology.yuyipad.adapter.DoctorAda;
import com.technology.yuyipad.adapter.HospitalAda;
import com.technology.yuyipad.adapter.OPDAda;
import com.technology.yuyipad.adapter.RecycleAdapter;
import com.technology.yuyipad.adapter.SelectRegisterPersonGridViewAlertAdapter;
import com.technology.yuyipad.bean.FirstPageInformationTwoData;
import com.technology.yuyipad.bean.FirstPageInformationTwoDataRoot;
import com.technology.yuyipad.bean.HospitalDepartmentMessage;
import com.technology.yuyipad.bean.HospitalDepartmentRoot;
import com.technology.yuyipad.bean.HospitalOutPatient;
import com.technology.yuyipad.bean.SelectDoctor.DatenumberList;
import com.technology.yuyipad.bean.SelectDoctor.Result;
import com.technology.yuyipad.bean.SelectDoctor.Root;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.lhdUtils.InformationListView;
import com.technology.yuyipad.lhdUtils.NetWorkUtils;
import com.technology.yuyipad.lhdUtils.UseDrugGridView;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyDialog;
import com.technology.yuyipad.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectHospitalOPDActivity extends AppCompatActivity implements View.OnClickListener {
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
    //没有数据
    private RelativeLayout mNoData_ll;
    //显示医生数据
    private LinearLayout mDoctor_ll, mDepartmentAndOpd_ll;
    private ImageView mLfetDate_img, mRightDate_img;//左右日期
    private TextView mMorning_tv, mAfternoon_tv, mName_ke;//上午下午
    private RecyclerView mRecycleView;
    private RecycleAdapter mRecycleAda;
    private int mDepartmentPosition = 0;//点击科室的时候获取的下标
    private UseDrugGridView mGridView;
    private DoctorAda mDoctorAda;
    private List<DatenumberList> mDoctorList = new ArrayList<>();
    private boolean isFlag = true;//默认true上午, false 下午
    private List<Result> mRList = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat;
    private Calendar mCalender;
    private int hour;
    private List<String> mDateList = new ArrayList<>();
    private List<Boolean> isClickDateList = new ArrayList<>();
    private int datePosition = 0;//日期下表，默认第一个日期下的医生列表


    private PopupWindow mPatientPop;//挂号患者列表弹框
    private View mPatientView;
    private ImageView mCancle_alert_img;
    private TextView mSureSelectPatient;
    private GridView mAlertGridView;
    private SelectRegisterPersonGridViewAlertAdapter mRegisterAdapter;
    private List<com.technology.yuyipad.bean.UserListBean.Result> userList = new ArrayList<>();
    //获取取用户列表的所有数据
    private Map<String, String> map = new HashMap<>();

    private long homeuserId;//患者id
    private int mPosition = -1;//患者下标，默认没有选择挂号人，只有点击才会变化
    private Map<String, String> registerMap = new HashMap();
    private int numHao;
    private long docID;
    private int mPositionDoc = -1;
    private RelativeLayout mSearch_Hospital_Rl, no_department_data_rl;
    //重新登录页面
    private RelativeLayout mLogin_rl;
    private RelativeLayout no_doctor_rl;

    //医院列表
    private ListView mHospital_listview;
    private HospitalAda mHospitalAda;
    private List<FirstPageInformationTwoData> mList = new ArrayList<>();
    private List<Boolean> mHospitalCheckList = new ArrayList<>();
    private View footer;
    private ProgressBar footerBar;
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
                        if (mList.size() != 0) {
                            mHospital_listview.removeFooterView(footer);
                            footerBar.setVisibility(View.INVISIBLE);
                            mAllData.setVisibility(View.VISIBLE);
                            mNoData_ll.setVisibility(View.GONE);
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
                                //默认请求第一个医院的科室
                                mHttptools.getHospitalDepartmentData(mHandler, mList.get(0).getId());
                            } else {//点击了加载更多
                                for (int i = 0; i < list.size(); i++) {
                                    mHospitalCheckList.add(false);
                                }
                                mHospitalAda.setList(mList, mHospitalCheckList);
                                mHospitalAda.notifyDataSetChanged();
                            }


                            if (list.size() == 10) {
                                mHospital_listview.addFooterView(footer);
                            } else {
                                mHospital_listview.removeFooterView(footer);
                            }
                        } else {
                            mAllData.setVisibility(View.GONE);
                            mNoData_ll.setVisibility(View.VISIBLE);
                        }

                    }
                }
            } else if (msg.what == 213) {
                footerBar.setVisibility(View.INVISIBLE);
            } else if (msg.what == 214) {
                footerBar.setVisibility(View.INVISIBLE);
            } else if (msg.what == 29) {//获取科室
                MyDialog.stopDia();
                Object o = msg.obj;
                if (o != null && o instanceof HospitalDepartmentRoot) {
                    HospitalDepartmentRoot root = (HospitalDepartmentRoot) o;
                    if (root != null && root.getCode().equals("0") && root.getResult() != null) {
                        mDepartmentList = root.getResult();

                        if (mDepartmentList.size() != 0) {
                            no_department_data_rl.setVisibility(View.GONE);
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
                        } else {
                            no_department_data_rl.setVisibility(View.VISIBLE);
                            Toast.makeText(SelectHospitalOPDActivity.this, "该医院目前没有科室", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SelectHospitalOPDActivity.this, "获取科室失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SelectHospitalOPDActivity.this, "获取科室失败", Toast.LENGTH_SHORT).show();

                }
            }
            //获取所有的挂号医生
            else if (msg.what == 30) {
                Object o = msg.obj;
                if (o != null && o instanceof Root) {
                    Root root = (Root) o;
                    if (root.getCode().equals("0")) {
                        int month = 0;
                        int day = 0;
                        mRList = root.getResult();
                        if (mRList.size() != 0) {
                            //设置日期
                            for (int i = 0; i < mRList.size(); i++) {
                                try {
                                    Date date = simpleDateFormat.parse(mRList.get(i).getDatastr());
                                    mCalender.setTime(date);
                                    month = mCalender.get(Calendar.MONTH) + 1;
                                    day = mCalender.get(Calendar.DAY_OF_MONTH);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String date = month + "/" + day;
                                mDateList.add(date);
                                if (i == 0) {//默认第一个日期字体比较大，背景深
                                    isClickDateList.add(true);
                                } else {
                                    isClickDateList.add(false);
                                }

                            }

                            //设置日期数据
                            mRecycleAda.setIsFlagList(isClickDateList);
                            mRecycleAda.setmList(mDateList);
                            mRecycleAda.notifyDataSetChanged();

                            //刚进页面显示第一个日期，上午的医生数据
                            mDoctorList = mRList.get(datePosition).getDatenumberList();
                            if (mDoctorList.size() != 0) {
                                mDoctorAda.setmListDoctor(mDoctorList);
                                mDoctorAda.setFlag(isFlag);
                                mDoctorAda.notifyDataSetChanged();
                                mGridView.setVisibility(View.VISIBLE);
                                no_doctor_rl.setVisibility(View.GONE);
                            } else {
                                no_doctor_rl.setVisibility(View.VISIBLE);
                                mGridView.setVisibility(View.GONE);
                                Toast.makeText(SelectHospitalOPDActivity.this, "此门诊目前没有医生信息", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            no_doctor_rl.setVisibility(View.VISIBLE);
                            Toast.makeText(SelectHospitalOPDActivity.this, "此门诊目前没有医生信息", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            } else if (msg.what == 35) {
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.UserListBean.Root) {
                    com.technology.yuyipad.bean.UserListBean.Root root = (com.technology.yuyipad.bean.UserListBean.Root) o;
                    if (root != null && root.getResult() != null) {
                        userList = root.getResult();
                        if (userList.size() != 0) {
                            mRegisterAdapter.setmList(userList);
                            mRegisterAdapter.notifyDataSetChanged();
                        }
                    } else {//重新登录

                        Toast.makeText(SelectHospitalOPDActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                        mLogin_rl.setVisibility(View.VISIBLE);


                    }

                }
            } else if (msg.what == 226) {
                Toast.makeText(SelectHospitalOPDActivity.this, "获取列表失败", Toast.LENGTH_SHORT).show();
                //挂号是否成功
            } else if (msg.what == 40) {
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.RegisterResult.Root) {
                    com.technology.yuyipad.bean.RegisterResult.Root root = (com.technology.yuyipad.bean.RegisterResult.Root) o;
                    //判断返回的结果
                    if (root.getCode().equals("0")) {//挂号成功
                        if (isFlag) {
                            mDoctorList.get(mPositionDoc).setBeforNum(mDoctorList.get(mPositionDoc).getBeforNum() - 1);
                        } else {
                            mDoctorList.get(mPositionDoc).setAfterNum(mDoctorList.get(mPositionDoc).getAfterNum() - 1);
                        }
                        mDoctorAda.setmListDoctor(mDoctorList);
                        mDoctorAda.setFlag(isFlag);
                        mDoctorAda.notifyDataSetChanged();
                        Toast.makeText(SelectHospitalOPDActivity.this, root.getResult(), Toast.LENGTH_SHORT).show();
                    } else if (root.getCode().equals("10101")) {//一天之内只能挂3个人的号
                        Toast.makeText(SelectHospitalOPDActivity.this, "挂号失败，一天之内只能挂3个人的号", Toast.LENGTH_SHORT).show();

                    } else if (root.getCode().equals("10102")) {//请选择挂号的家庭成员
                        Toast.makeText(SelectHospitalOPDActivity.this, root.getResult(), Toast.LENGTH_SHORT).show();

                    } else if (root.getCode().equals("10103")) {//用户信息不完整，无法挂号
                        Toast.makeText(SelectHospitalOPDActivity.this, "此用户信息不完整，无法挂号", Toast.LENGTH_SHORT).show();

                    } else if (root.getCode().equals("10104")) {//没有选择挂号门诊的医生
                        Toast.makeText(SelectHospitalOPDActivity.this, root.getResult(), Toast.LENGTH_SHORT).show();

                    } else if (root.getCode().equals("10105")) {//请选择上午还是下午
                        Toast.makeText(SelectHospitalOPDActivity.this, root.getResult(), Toast.LENGTH_SHORT).show();
                    } else if (root.getCode().equals("10106")) {//您选择的医生已经没号了
                        Toast.makeText(SelectHospitalOPDActivity.this, root.getResult(), Toast.LENGTH_SHORT).show();
                    } else if (root.getCode().equals("10107")) {//已经挂过该医生的号了
                        Toast.makeText(SelectHospitalOPDActivity.this, "您已经挂过该医生的好了，亲", Toast.LENGTH_SHORT).show();

                    } else if (root.getCode().equals("10108")) {//该医生不出诊
                        Toast.makeText(SelectHospitalOPDActivity.this, root.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetWorkUtils.isNetWorkConnected(this)) {
            setContentView(R.layout.activity_select_hospital_opd);
            //获取首页数据
            mHttptools = HttpTools.getHttpToolsInstance();
            mHttptools.getAppintmentData(mHandler, 0, 10);//医院列表
            map.put("token", User.token);
            mHttptools.getUserLIst(mHandler, map);//获取所有挂号人
            initUI();
        } else {
            setContentView(R.layout.firstpage_newwork);
            TextView textView = (TextView) findViewById(R.id.first_page_tv);
            textView.setText("预约挂号");
        }


    }

    /**
     * 初始化UI
     */
    private void initUI() {
        no_doctor_rl= (RelativeLayout) findViewById(R.id.no_doctor_rl);//没有医生是显示的图片
        mLogin_rl = (RelativeLayout) findViewById(R.id.again_login_rl);
        mLogin_rl.setOnClickListener(this);
        //搜索医院
        mSearch_Hospital_Rl = (RelativeLayout) findViewById(R.id.search_relative);
        mSearch_Hospital_Rl.setOnClickListener(this);

        mAllData = (LinearLayout) findViewById(R.id.all_data_register_ll);
        mNoData_ll = (RelativeLayout) findViewById(R.id.nodata_rl_register);
        no_department_data_rl = (RelativeLayout) findViewById(R.id.no_department_data_rl);
        //医院
        mHospital_listview = (ListView) findViewById(R.id.hospital_listview);
        mHospitalAda = new HospitalAda(this, mList);
        mHospital_listview.setAdapter(mHospitalAda);
        footer = LayoutInflater.from(this).inflate(R.layout.circle_listview_footer, null);
        footerBar = footer.findViewById(R.id.pbLocate);

        //点击医院显示科室和默认第一个科室下的所有门诊
        mHospital_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mList.size()) {
                    isMore = true;
                    footerBar.setVisibility(View.VISIBLE);
                    mStart += 10;
                    mHttptools.getAppintmentData(mHandler, mStart, mAddNum);


                } else {
                    mHospitalCheckList.clear();
                    mDepartmentPosition = 0;
                    datePosition = 0;//将默认日期下标回归0
                    mRecycleView.smoothScrollToPosition(datePosition);//日期滚动到第一个日期
                    mDoctor_ll.setVisibility(View.GONE);//隐藏医生
                    mDepartmentAndOpd_ll.setVisibility(View.VISIBLE);//显示科室门诊
                    try{
                        MyDialog.showDialog(SelectHospitalOPDActivity.this);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //变化背景
                    for (int k = 0; k < mList.size(); k++) {
                        if (k == i) {
                            mHospitalCheckList.add(true);
                        } else {
                            mHospitalCheckList.add(false);
                        }
                    }
                    mHospitalAda.notifyDataSetChanged();

                    //请求当前医院下的科室
                    mDeparmentCheckList.clear();
                    mHttptools.getHospitalDepartmentData(mHandler, mList.get(i).getId());
                    mDepartment_gridview.setVisibility(View.GONE);
                    mOPD_gridview.setVisibility(View.GONE);
                }
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
                    mDepartmentPosition = i;
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
                mDateList.clear();
                isClickDateList.clear();
                Log.e("门诊下标", i + "");
                Log.e("科室下标", mDepartmentPosition + "");
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
        mGridView = (UseDrugGridView) findViewById(R.id.doctor_gridview);
        mDoctorAda = new DoctorAda(this, mDoctorList, isFlag);
        mGridView.setAdapter(mDoctorAda);
        //点击医生显示挂号人弹框
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.yu_num);
                if (mDoctorList.size() != 0) {
                    docID = mDoctorList.get(i).getId();
                    Log.e("docid", docID + "");
                    numHao = Integer.valueOf(textView.getText().toString());
                    mPositionDoc = i;
                    showPatient();
                }

            }
        });
        //时间
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        mCalender = Calendar.getInstance();
        hour = mCalender.get(Calendar.HOUR_OF_DAY);
        //日期列表
        mRecycleView = (RecyclerView) findViewById(R.id.recycle_date_id);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(OrientationHelper.HORIZONTAL);//水平走向
        mRecycleView.setLayoutManager(manager);
        mRecycleAda = new RecycleAdapter(mDateList, isClickDateList, this);
        mRecycleView.setAdapter(mRecycleAda);
        //点击日期时候
        mRecycleAda.setOnItemClickLitener(new RecycleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < isClickDateList.size(); i++) {
                    if (position == i) {
                        isClickDateList.set(position, true);
                    } else {
                        isClickDateList.set(i, false);
                    }
                }
                mRecycleAda.setIsFlagList(isClickDateList);
                mRecycleAda.notifyDataSetChanged();
                //  Toast.makeText(SelectHospitalOPDActivity.this, "日期下标" + position, Toast.LENGTH_SHORT).show();
                datePosition = position;
                mDoctorList = mRList.get(datePosition).getDatenumberList();
                mDoctorAda.setmListDoctor(mDoctorList);
                mDoctorAda.setFlag(isFlag);
                mDoctorAda.notifyDataSetChanged();
            }
        });

        //挂号时患者弹框

        mPatientView = LayoutInflater.from(this).inflate(R.layout.alert_select_register_person, null);
        mPatientPop = new PopupWindow(mPatientView);
        mCancle_alert_img = mPatientView.findViewById(R.id.cancle_alert_img);//取消
        mSureSelectPatient = mPatientView.findViewById(R.id.alert_sure);
        mCancle_alert_img.setOnClickListener(this);
        mSureSelectPatient.setOnClickListener(this);
        //弹框设置适配器
        mAlertGridView = (GridView) mPatientView.findViewById(R.id.be_select_gridview);
        mAlertGridView.setSelector(R.drawable.nopress_bgcolor);
        mRegisterAdapter = new SelectRegisterPersonGridViewAlertAdapter(SelectHospitalOPDActivity.this, userList);
        mAlertGridView.setAdapter(mRegisterAdapter);
        mAlertGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //添加
                if (position == userList.size()) {
                    Intent intent = new Intent(SelectHospitalOPDActivity.this, FamilyUserManagerActivity.class);
                    startActivity(intent);
                    mPatientPop.dismiss();
                } else {//选择某一个联系人
                    homeuserId = userList.get(position).getId();
                    mPosition = position;
                }

            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mLfetDate_img.getId()) {//左日期
            if (datePosition == 0) {
                Toast.makeText(getApplicationContext(), "抱歉，只显示当天的数据", Toast.LENGTH_SHORT).show();
            } else {
                datePosition -= 1;
                mDoctorList = mRList.get(datePosition).getDatenumberList();
                mDoctorAda.setmListDoctor(mDoctorList);
                mDoctorAda.setFlag(isFlag);
                mDoctorAda.notifyDataSetChanged();

                mRecycleView.smoothScrollToPosition(datePosition);
                for (int i = 0; i < isClickDateList.size(); i++) {
                    if (datePosition == i) {
                        isClickDateList.set(datePosition, true);
                    } else {
                        isClickDateList.set(i, false);
                    }
                }
                mRecycleAda.setIsFlagList(isClickDateList);
                mRecycleAda.notifyDataSetChanged();
            }

        } else if (id == mRightDate_img.getId()) {//右日期
            if (datePosition == mDateList.size() - 1) {
                Toast.makeText(getApplicationContext(), "抱歉，最多显示一周的数据", Toast.LENGTH_SHORT).show();
            } else {
                datePosition += 1;
                mDoctorList = mRList.get(datePosition).getDatenumberList();
                mDoctorAda.setmListDoctor(mDoctorList);
                mDoctorAda.setFlag(isFlag);
                mDoctorAda.notifyDataSetChanged();

                mRecycleView.smoothScrollToPosition(datePosition);
                for (int i = 0; i < isClickDateList.size(); i++) {
                    if (datePosition == i) {
                        isClickDateList.set(datePosition, true);
                    } else {
                        isClickDateList.set(i, false);
                    }
                }
                mRecycleAda.setIsFlagList(isClickDateList);
                mRecycleAda.notifyDataSetChanged();
            }
        } else if (id == mMorning_tv.getId()) {//上午
            mMorning_tv.setTextColor(ContextCompat.getColor(this, R.color.ffffff));
            mMorning_tv.setBackgroundResource(R.drawable.morning_bg);
            mAfternoon_tv.setTextColor(ContextCompat.getColor(this, R.color.color_drumall));
            mAfternoon_tv.setBackgroundResource(R.drawable.afternoon_cancle_bg);
            isFlag = true;
            mDoctorAda.setFlag(isFlag);
            mDoctorAda.notifyDataSetChanged();
        } else if (id == mAfternoon_tv.getId()) {//下午

            mMorning_tv.setTextColor(ContextCompat.getColor(this, R.color.color_drumall));
            mMorning_tv.setBackgroundResource(R.drawable.morning_cancle_bg);
            mAfternoon_tv.setTextColor(ContextCompat.getColor(this, R.color.ffffff));
            mAfternoon_tv.setBackgroundResource(R.drawable.afternoon_bg);
            isFlag = false;
            mDoctorAda.setFlag(isFlag);
            mDoctorAda.notifyDataSetChanged();
        } else if (id == mCancle_alert_img.getId()) {//取消挂号人弹框
            mPatientPop.dismiss();
        } else if (id == mSureSelectPatient.getId()) {//确定挂号人弹框
            if (datePosition == 0 && isFlag && hour >= 11) {//过了今天上午11点不能挂上午的号
                Toast.makeText(getApplicationContext(), "抱歉,挂号时间已过", Toast.LENGTH_SHORT).show();
            } else if (datePosition == 0 && !isFlag && hour >= 17) {//过了今天下午17点不能挂下午的号
                Toast.makeText(getApplicationContext(), "抱歉,挂号时间已过", Toast.LENGTH_SHORT).show();
            } else {
                if (mDoctorList.size() != 0) {
                    if (mPosition != -1) {
                        if (numHao > 0) {
                            registerMap.put("datenumberId", String.valueOf(docID));//确定预约挂号时的参数
                            registerMap.put("isAm", String.valueOf(isFlag));//确定预约挂号时的参数
                            registerMap.put("homeuserId", String.valueOf(homeuserId));
                            registerMap.put("token", User.token);
                            mHttptools.sureRegister(mHandler, registerMap);
                            Log.e("homeuserId", homeuserId + "");
                            Log.e("Id", docID + "");
                            Log.e("isAm", isFlag + "");
                            Log.e("token", User.token + "");
                            mPatientPop.dismiss();
                            mPosition = -1;
                        } else {
                            Toast.makeText(this, "无余号，请重新选择", Toast.LENGTH_SHORT).show();
                            mPatientPop.dismiss();
                            mPosition = -1;
                        }
                    } else {
                        Toast.makeText(this, "请选择挂号人", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else if (id == mSearch_Hospital_Rl.getId()) {
            startActivity(new Intent(getApplicationContext(), SearchHospitalActivity.class));
        }
    }

    //挂号患者弹框
    private void showPatient() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //设置透明度
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);

        //设置弹框的款，高
        mPatientPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPatientPop.setWidth(mName_ke.getWidth());
        mPatientPop.setFocusable(true);//如果有交互需要设置焦点为true
        mPatientPop.setOutsideTouchable(false);//设置内容外可以点击
        mPatientPop.setBackgroundDrawable(new ColorDrawable(Color.argb(000, 255, 255, 255)));
        //相对于父控件的位置
        // mPatientPop.showAtLocation(mDoctor_ll, Gravity.CENTER, 0, 0);
        mPatientPop.showAsDropDown(mName_ke);
        //当弹框销毁时，将透明度初始化，否则弹框销毁后，所依附的activity页面背景将会改变
        mPatientPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SelectHospitalOPDActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
                mPosition = -1;//取消弹框以后，患者下标回到初始值
                //Toast.makeText(SelectHospitalOPDActivity.this, "取消患者弹框", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
