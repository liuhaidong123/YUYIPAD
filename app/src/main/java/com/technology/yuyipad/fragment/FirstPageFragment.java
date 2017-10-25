package com.technology.yuyipad.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.InformationDetailsActivity;
import com.technology.yuyipad.activity.Medicinal.MyMedicalActivity;
import com.technology.yuyipad.activity.LocationActivity;
import com.technology.yuyipad.activity.Message.MessageListActivity;
import com.technology.yuyipad.activity.SearchHospitalActivity;
import com.technology.yuyipad.activity.SelectHospitalOPDActivity;
import com.technology.yuyipad.adapter.BloodTemViewPagerAda;
import com.technology.yuyipad.adapter.FirstPageListViewAdapter;
import com.technology.yuyipad.adapter.My_messageListView_Adapter;
import com.technology.yuyipad.adapter.PatientAda;
import com.technology.yuyipad.bean.FirstPageUserDataBean.BloodpressureList;
import com.technology.yuyipad.bean.FirstPageUserDataBean.TemperatureList;
import com.technology.yuyipad.bean.HasMsgBean;
import com.technology.yuyipad.bean.UnReadMsgBean;
import com.technology.yuyipad.bean.UserListBean.Result;
import com.technology.yuyipad.bean.UserListBean.Root;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lhdUtils.BloodView;
import com.technology.yuyipad.lhdUtils.InformationListView;
import com.technology.yuyipad.lhdUtils.RoundImageView;
import com.technology.yuyipad.lhdUtils.TemView;
import com.technology.yuyipad.lzhUtils.PopupSettings;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPageFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout wrap;//RelativeLyout是scrollview中的容器，
    private RelativeLayout mSelect_patient_rl;
    LinearLayout drugmall_ll;//我的药品
    private RoundImageView mHead_img;
    private TextView mName;
    private PopupWindow mPopupwindow;
    private ListView mPatient_Listview;
    private PatientAda mPatientAda;
    private View mView;
    private View footer;
    ImageView message_img;//消息
    View message_unRead;//消息圆点
    boolean hasUnReadMsg=true;//是否有未读消息
    int msgTotalCount=0;//未读消息的总个数
    View parentView;//弹窗的容器
    String resStr;
    My_messageListView_Adapter adapter;
    private List<com.technology.yuyipad.bean.FirstPageUserDataBean.Result> mList = new ArrayList<>();
    private HttpTools mHttptools;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 31) {//广告接口
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.AdBean.Root) {
                    com.technology.yuyipad.bean.AdBean.Root root = (com.technology.yuyipad.bean.AdBean.Root) o;
                    if (root.getResult() != null && root.getResult().getRows().size() != 0) {
                        mListAd = root.getResult().getRows();
                        mRollPagerView.setAdapter(new RollAdapter(mRollPagerView));
                    }
                }
            }//首页资讯2条数据
            else if (msg.what == 22) {
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Root) {
                    com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Root root = (com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Root) o;
                    if (root != null && root.getRows() != null) {
                        mInforList = root.getRows();
                        mListViewAdapter.setList(mInforList);
                        mListViewAdapter.notifyDataSetChanged();
                    }

                }
            } else if (msg.what == 38) {//首页用户列表及默认数据
                heightBloodData.clear();
                lowBloodData.clear();
                XdateNum.clear();
                temData.clear();
                XTemdateNum.clear();
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.FirstPageUserDataBean.Root) {
                    com.technology.yuyipad.bean.FirstPageUserDataBean.Root root = (com.technology.yuyipad.bean.FirstPageUserDataBean.Root) o;
                    if (root != null && root.getResult() != null) {
                        mList = root.getResult();
                        mPatientAda.setList(mList);
                        mPatientAda.notifyDataSetChanged();

                        //默认主用户头像和姓名
                        if (mList.size() != 0) {
                            Picasso.with(getActivity()).load(UrlTools.BASE + mList.get(0).getAvatar()).error(R.mipmap.usererr).into(mHead_img);
                            mName.setText(mList.get(0).getTrueName());
                            initUserMessage();//初始化默认主用户用户的头像和昵称，绘制折线图
                        }

                        //如果数据为6条，则隐藏添加按钮
                        if (mList.size() == 6) {
                            mPatient_Listview.removeFooterView(footer);
                        }
                    }

                }


            } else if (msg.what == 39) {//点击首页用户头像
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.FirstPageClickUserBean.Root) {
                    com.technology.yuyipad.bean.FirstPageClickUserBean.Root root = (com.technology.yuyipad.bean.FirstPageClickUserBean.Root) o;
                    XdateNum.clear();
                    heightBloodData.clear();
                    lowBloodData.clear();
                    XTemdateNum.clear();
                    temData.clear();
                    int month = 0;
                    int day = 0;

                    List<com.technology.yuyipad.bean.FirstPageClickUserBean.BloodpressureList> bloodlist = root.getResult().getBloodpressureList();
                    List<com.technology.yuyipad.bean.FirstPageClickUserBean.TemperatureList> temlist = root.getResult().getTemperatureList();
                    //血压
                    if (bloodlist.size() != 0) {
                        for (int i = 0; i < bloodlist.size(); i++) {
                            try {
                                Date date = simpleDateFormat.parse(bloodlist.get(i).getCreateTimeString());
                                month = date.getMonth() + 1;
                                day = date.getDate();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String date = month + "月" + day + "日";
                            XdateNum.add(date);
                            heightBloodData.add(bloodlist.get(i).getSystolic());//高
                            lowBloodData.add(bloodlist.get(i).getDiastolic());
                        }

                    }

                    //填补血压日期
                    if (XdateNum.size() != 7) {
                        Calendar calendarBlood = Calendar.getInstance();
                        int dayNum = 7 - XdateNum.size();
                        if (XdateNum.size() == 0) {
                            for (int i = 0; i < dayNum; i++) {
                                int month2 = calendarBlood.get(Calendar.MONTH) + 1;
                                int day2 = calendarBlood.get(Calendar.DAY_OF_MONTH);
                                String date2 = month2 + "月" + day2 + "日";
                                XdateNum.add(date2);
                                calendarBlood.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        } else {
                            for (int i = 0; i < dayNum; i++) {
                                calendarBlood.add(Calendar.DAY_OF_MONTH, 1);
                                int month2 = calendarBlood.get(Calendar.MONTH) + 1;
                                int day2 = calendarBlood.get(Calendar.DAY_OF_MONTH);
                                String date2 = month2 + "月" + day2 + "日";
                                XdateNum.add(date2);
                            }
                        }

                    }
                    // 体温
                    if (temlist.size() != 0) {
                        for (int i = 0; i < temlist.size(); i++) {
                            try {
                                Date date = simpleDateFormat.parse(temlist.get(i).getCreateTimeString());
                                month = date.getMonth() + 1;
                                day = date.getDate();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String date = month + "月" + day + "日";
                            XTemdateNum.add(date);
                            temData.add(temlist.get(i).getTemperaturet());//体温

                        }

                    }

                    //填补体温日期
                    if (XTemdateNum.size() != 7) {
                        Calendar calendarTem = Calendar.getInstance();
                        int dayNum = 7 - XTemdateNum.size();
                        //没有数据时，从当前日期开始
                        if (XTemdateNum.size() == 0) {
                            for (int i = 0; i < dayNum; i++) {
                                int month2 = calendarTem.get(Calendar.MONTH) + 1;
                                int day2 = calendarTem.get(Calendar.DAY_OF_MONTH);
                                String date2 = month2 + "月" + day2 + "日";
                                XTemdateNum.add(date2);
                                calendarTem.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            //有数据时，从当前日期的下一天开始
                        } else {
                            for (int i = 0; i < dayNum; i++) {
                                calendarTem.add(Calendar.DAY_OF_MONTH, 1);
                                int month2 = calendarTem.get(Calendar.MONTH) + 1;
                                int day2 = calendarTem.get(Calendar.DAY_OF_MONTH);
                                String date2 = month2 + "月" + day2 + "日";
                                XTemdateNum.add(date2);
                            }
                        }

                    }
                    mBloodView.setInfo(YbloodNum, XdateNum, heightBloodData, lowBloodData);
                    mBloodView.invalidate();
                    mTemView.setTemInfo(YTemData, XTemdateNum, temData);
                    mTemView.invalidate();

                    //判断数据是否正常，设置文字图片提示
                    if (bloodlist.size() != 0 && temlist.size() != 0) {
                        checkBlood(bloodlist.get(bloodlist.size() - 1).getSystolic(), bloodlist.get(bloodlist.size() - 1).getDiastolic(), temlist.get(temlist.size() - 1).getTemperaturet());
                    } else if (bloodlist.size() == 0 && temlist.size() != 0) {
                        checkBlood(0, 0, temlist.get(temlist.size() - 1).getTemperaturet());
                    } else if (bloodlist.size() != 0 && temlist.size() == 0) {
                        checkBlood(bloodlist.get(bloodlist.size() - 1).getSystolic(), bloodlist.get(bloodlist.size() - 1).getDiastolic(), 0);
                    } else {
                        checkBlood(0, 0, 0);
                    }

                }
            }

            else if (msg.what==-10){//获取有无未读消息网络异常
                hasUnReadMsg=false;
            }
            else if (msg.what==-11){//获取有无未读消息
                hasUnReadMsg=false;
                try{
                    HasMsgBean hasMsgBean= gson.gson.fromJson(resStr,HasMsgBean.class);
                    if (hasMsgBean!=null){
                        if ("0".equals(hasMsgBean.getCode())){
                            msgTotalCount= hasMsgBean.getHasMessage();
                            if (msgTotalCount>0){
                                hasUnReadMsg=true;
                                message_unRead.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (msg.what==-13){//获取有无未读消息网络异常
                startActivity(new Intent(getActivity(), MessageListActivity.class));
            }
            else if (msg.what==-14){//获取有无未读消息
                hasUnReadMsg=false;
                msgTotalCount=0;
                message_unRead.setVisibility(View.GONE);
                try{
                    UnReadMsgBean hasMsgBean= gson.gson.fromJson(resStr,UnReadMsgBean.class);
                    if (hasMsgBean!=null&&hasMsgBean.getRows()!=null&&hasMsgBean.getRows().size()>0){
                        showUnReadMsgList(hasMsgBean.getRows());
                    }
                    else {
                        startActivity(new Intent(getActivity(), MessageListActivity.class));
                    }
                }
                catch (Exception e){
                    startActivity(new Intent(getActivity(), MessageListActivity.class));
                    e.printStackTrace();
                }
            }
        }

    };

    //广告
    private RollPagerView mRollPagerView;
    private List<com.technology.yuyipad.bean.AdBean.Rows> mListAd = new ArrayList<>();
    //资讯
    private InformationListView mInformationTwoListview;
    private List<com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Rows> mInforList = new ArrayList<>();
    FirstPageListViewAdapter mListViewAdapter;

    //血压体温
    private ViewPager mBloodTem_ViewPager;
    private List<View> mBloodTemViewList = new ArrayList<>();
    private BloodView mBloodView;
    private TemView mTemView;
    private BloodTemViewPagerAda mBloodTemViewPagerAda;
    //血压数据
    private ArrayList<Integer> YbloodNum = new ArrayList<>();//y轴血压数据
    private ArrayList<String> XdateNum = new ArrayList<>();//x轴日期数据
    private ArrayList<Integer> heightBloodData = new ArrayList<>();  //血压高压数据
    private ArrayList<Integer> lowBloodData = new ArrayList<>();//血压低压数据
    //体温数据
    private ArrayList<Integer> YTemData = new ArrayList<>();//Y轴温度
    private ArrayList<String> XTemdateNum = new ArrayList<>(); //x轴日期数据
    private ArrayList<Float> temData = new ArrayList<>(); //体温数据
    private SimpleDateFormat simpleDateFormat;
    private ImageView mPromptImg;
    private TextView mPromptTv;
    private String grayColor = "#6a6a6a";
    private TextView mHeightBloodTv, mLowBloodTv, mTem;

    //资讯
    private RelativeLayout mInformation_Rl;

    //预约挂号
    private LinearLayout mRegister_ll;

    //定位按钮
    private TextView mLocation_tv;

    //搜索
    private ImageView mSearch_img;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public FirstPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        initUI(view);
        hasUnReadMsg();
        return view;
    }

    /**
     * 初始化UI
     *
     * @param view
     */
    private void initUI(View view) {
        message_unRead=view.findViewById(R.id.message_unRead);
        message_unRead.setVisibility(View.GONE);
        message_img=view.findViewById(R.id.message_img);
        message_img.setOnClickListener(this);
        //搜索
        mSearch_img = view.findViewById(R.id.search_img);
        mSearch_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchHospitalActivity.class));
            }
        });
        //定位
        mLocation_tv = view.findViewById(R.id.location_tv);
        mLocation_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivityForResult(intent, 66);
            }
        });
        ///定位
       checkPermission();
        //将右边scrollView定位到最顶端：
        wrap = (RelativeLayout) view.findViewById(R.id.wrap);
        wrap.setFocusable(true);
        wrap.setFocusableInTouchMode(true);
        wrap.requestFocus();


        mHttptools = HttpTools.getHttpToolsInstance();
        mHttptools.getAdData(mHandler);//广告接口
        mHttptools.getFirstPageInformationTwoData(mHandler, 0, 2);//首页资讯2条数据
        mHttptools.getFirstPageUserDataData(mHandler, User.token);//首页用户列表数据u
        //选择患者查看数据
        mSelect_patient_rl = view.findViewById(R.id.select_patient_rl);
        mSelect_patient_rl.setOnClickListener(this);

        //患者列表
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.patient_listview, null);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.patient_footer, null);
        mPatient_Listview = mView.findViewById(R.id.patient_listview);
        mPatient_Listview.addFooterView(footer);
        mPatientAda = new PatientAda(getActivity(), mList);
        mPatient_Listview.setAdapter(mPatientAda);

        mHead_img = view.findViewById(R.id.patient_head_img);
        mName = view.findViewById(R.id.patient_name_tv);
        //点击患者
        mPatient_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mList.size() != 0 && i != mList.size()) {
                    Picasso.with(getActivity()).load(UrlTools.BASE + mList.get(i).getAvatar()).error(R.mipmap.usererr).into(mHead_img);
                    mName.setText(mList.get(i).getTrueName());
                    mPopupwindow.dismiss();
                    mHttptools.getClickUserDataData(mHandler, User.token, mList.get(i).getId());//请求某个患者数据
                } else {
                    //添加患者
                    Toast.makeText(getActivity(), "添加患者", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //广告轮播
        mRollPagerView = view.findViewById(R.id.ad_viewpager);
        mRollPagerView.setPlayDelay(3000);//切换图片的间隔时间
        mRollPagerView.setAnimationDurtion(500);
        mRollPagerView.setHintView(new IconHintView(this.getActivity(), R.mipmap.selected, R.mipmap.unselected));
        mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), InformationDetailsActivity.class);
                intent.putExtra("tag", position);//
                startActivity(intent);
            }
        });
        //资讯俩条数据
        mInformationTwoListview = view.findViewById(R.id.listview_firstpage);
        //给资讯设置adapter
        mListViewAdapter = new FirstPageListViewAdapter(this.getActivity(), mInforList);
        mInformationTwoListview.setAdapter(mListViewAdapter);
        //点击资讯跳转列表和详情
        mInformationTwoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), InformationDetailsActivity.class);
                intent.putExtra("tag", i);//
                startActivity(intent);
            }
        });

        //血压体温走势图
        initBloodData();
        initTemData();
        mBloodView = new BloodView(getActivity());
        mTemView = new TemView(getActivity());
        mBloodTemViewList.add(mBloodView);
        mBloodTemViewList.add(mTemView);
        mBloodTem_ViewPager = view.findViewById(R.id.blood_tem_viewpager);
        mBloodTemViewPagerAda = new BloodTemViewPagerAda(mBloodTemViewList, getActivity());
        mBloodTem_ViewPager.setAdapter(mBloodTemViewPagerAda);
        //时间
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mPromptImg = (ImageView) view.findViewById(R.id.normal_btn_img);
        mPromptTv = (TextView) view.findViewById(R.id.normal_tv);
        mHeightBloodTv = (TextView) view.findViewById(R.id.heightPress_message_tv);
        mLowBloodTv = (TextView) view.findViewById(R.id.lowPress_message_tv);
        mTem = (TextView) view.findViewById(R.id.temperature_message_tv);

        //点击资讯按钮跳转列表和详情
        mInformation_Rl = view.findViewById(R.id.information_rl);
        mInformation_Rl.setOnClickListener(this);

        //预约挂号
        mRegister_ll = view.findViewById(R.id.register_ll);
        mRegister_ll.setOnClickListener(this);
        //我的药品
        drugmall_ll=view.findViewById(R.id.drugmall_ll);
        drugmall_ll.setOnClickListener(this);
    }


    //定位
    public void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setInterval(1000);//设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        mLocation_tv.setText(aMapLocation.getDistrict());
                    } else {
                        mLocation_tv.setText("未定位");

                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError", "location Error, ErrCode:"
//                                + aMapLocation.getErrorCode() + ", errInfo:"
//                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    public static final int LOCATION_CODE = 123;
    public void checkPermission() {
        //sdk版本>=23时，
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
            //如果定位权限没有授权
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //请求授权， 点击允许或者拒绝时会回调onRequestPermissionsResult（），
                //注意 ：如果是在fragment中申请权限，不要使用ActivityCompat.requestPermissions，
                //直接使用requestPermissions （new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_READ_PHONE）
                //否则不会调用onRequestPermissionsResult（）方法。
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);//
                return;

            } else {//如果已经授权，执行业务逻辑
               initLocation();
            }

        } else { //版本小于23时，不需要判断敏感权限，执行业务逻辑
            initLocation();

        }
    }

    //请求授权， 点击允许或者拒绝时会回调onRequestPermissionsResult（），
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                //点击了允许，授权成功
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getActivity(), "定位授权成功", Toast.LENGTH_SHORT).show();
                    initLocation();
                    //点击了拒绝，授权失败
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "定位授权失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mSelect_patient_rl.getId()) {//选择患者查看数据
            showPatientBox();
        } else if (id == mInformation_Rl.getId()) {//点击资讯跳转列表和详情
            Intent intent = new Intent(getActivity(), InformationDetailsActivity.class);
            intent.putExtra("tag", -1);//
            startActivity(intent);
        } else if (id == mRegister_ll.getId()) {
            startActivity(new Intent(getActivity(), SelectHospitalOPDActivity.class));
        }
        else if (id==drugmall_ll.getId()){
            startActivity(new Intent(getActivity(), MyMedicalActivity.class));
        }
        else if (id==message_img.getId()){//点击消息
            if (hasUnReadMsg&&msgTotalCount>0){//有未读消息时显示消息列表
                getUnReadMsg();
            }
            else {//没有未读消息时跳转到消息页面
                startActivity(new Intent(getActivity(), MessageListActivity.class));
                }
        }
    }

    public void showUnReadMsgList(List<UnReadMsgBean.RowsBean>list){
        PopupWindow pop=new PopupWindow();
        View vi=LayoutInflater.from(getActivity()).inflate(R.layout.msglist,null);
        ListView msgListView=vi.findViewById(R.id.msgListView);
        adapter=new My_messageListView_Adapter(getActivity(),list);
        msgListView.setAdapter(adapter);
        PopupSettings.getInstance().showWindowRight(getActivity(),pop,vi,message_img);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 患者弹框
     */
    public void showPatientBox() {
        //设置透明度
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.7f;
        getActivity().getWindow().setAttributes(params);
        //存放popupWindow的容器
        // ViewGroup container = (ViewGroup) findViewById(R.id.activity_money_input_sure);
        mPopupwindow = new PopupWindow(mView);
        //设置弹框的款，高
        mPopupwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupwindow.setWidth(mSelect_patient_rl.getWidth());
        mPopupwindow.setFocusable(true);//如果有交互需要设置焦点为true
        mPopupwindow.setOutsideTouchable(true);//设置内容外可以点击

        //mPopupwindow.setAnimationStyle(R.style.popup3_anim);
        //相对于父控件的位置
        mPopupwindow.showAsDropDown(mSelect_patient_rl);
        //当弹框销毁时，将透明度初始化，否则弹框销毁后，所依附的activity页面背景将会改变
        mPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });

    }


    class RollAdapter extends LoopPagerAdapter {
        // private int[] imgs = {R.mipmap.img1, R.mipmap.img2, R.mipmap.img3};

        public RollAdapter(RollPagerView viewPager) {
            super(viewPager);
        }


        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            Picasso.with(getActivity()).load(UrlTools.BASE + mListAd.get(position).getPicture()).error(R.mipmap.error_big).into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return mListAd.size();
        }

    }

    /**
     * 初始化血压折线图y轴数据
     */
    public void initBloodData() {
        //y轴血压数据
        for (int i = 40; i <= 180; i += 20) {
            YbloodNum.add(i);
        }
    }

    /**
     * 初始化体温折线图y轴数据
     */

    public void initTemData() {
        for (int i = 35; i < 43; i++) {
            YTemData.add(i);
        }

    }


    /**
     * 初始化用户数据
     */
    public void initUserMessage() {

        if (mList.size() != 0) {
            //为默认用户初始化数据
            List<BloodpressureList> listBlood = mList.get(0).getBloodpressureList();
            List<TemperatureList> listTem = mList.get(0).getTemperatureList();
            //默认用户血压数据
            if (listBlood.size() != 0) {
                int month = 0;
                int day = 0;
                for (int i = 0; i < listBlood.size(); i++) {
                    heightBloodData.add(listBlood.get(i).getSystolic());//高
                    lowBloodData.add(listBlood.get(i).getDiastolic());
                    try {
                        Date date = simpleDateFormat.parse(listBlood.get(i).getCreateTimeString());
                        month = date.getMonth() + 1;
                        day = date.getDate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String date = month + "月" + day + "日";
                    XdateNum.add(date);
                }

            }


            //填补血压日期
            if (XdateNum.size() != 7) {
                int dayNum = 7 - XdateNum.size();
                Calendar calendarBlood = Calendar.getInstance();

                if (XdateNum.size() == 0) {
                    for (int i = 0; i < dayNum; i++) {
                        int month2 = calendarBlood.get(Calendar.MONTH) + 1;
                        int day2 = calendarBlood.get(Calendar.DAY_OF_MONTH);
                        String date2 = month2 + "月" + day2 + "日";
                        XdateNum.add(date2);
                        calendarBlood.add(Calendar.DAY_OF_MONTH, 1);
                    }
                } else {
                    for (int i = 0; i < dayNum; i++) {
                        calendarBlood.add(Calendar.DAY_OF_MONTH, 1);
                        int month2 = calendarBlood.get(Calendar.MONTH) + 1;
                        int day2 = calendarBlood.get(Calendar.DAY_OF_MONTH);
                        String date2 = month2 + "月" + day2 + "日";
                        XdateNum.add(date2);
                    }
                }

            }


            //默认用户体温数据
            if (listTem.size() != 0) {
                int month = 0;
                int day = 0;
                for (int i = 0; i < listTem.size(); i++) {
                    temData.add(listTem.get(i).getTemperaturet());
                    try {
                        Date date = simpleDateFormat.parse(listTem.get(i).getCreateTimeString());
                        month = date.getMonth() + 1;
                        day = date.getDate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String date = month + "月" + day + "日";
                    XTemdateNum.add(date);
                }
            }


            //填补体温日期
            if (XTemdateNum.size() != 7) {
                Calendar calendarTem = Calendar.getInstance();
                int dayNum = 7 - XTemdateNum.size();
                //如果日期数据为0，日期从当天开始
                if (XTemdateNum.size() == 0) {
                    for (int i = 0; i < dayNum; i++) {
                        int month2 = calendarTem.get(Calendar.MONTH) + 1;
                        int day2 = calendarTem.get(Calendar.DAY_OF_MONTH);
                        String date2 = month2 + "月" + day2 + "日";
                        XTemdateNum.add(date2);
                        calendarTem.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    //如果日期数据不为0，日期从当天的下一天开始
                } else {
                    for (int i = 0; i < dayNum; i++) {
                        calendarTem.add(Calendar.DAY_OF_MONTH, 1);
                        int month2 = calendarTem.get(Calendar.MONTH) + 1;
                        int day2 = calendarTem.get(Calendar.DAY_OF_MONTH);
                        String date2 = month2 + "月" + day2 + "日";
                        XTemdateNum.add(date2);
                    }
                }

            }
            //判断数据是否正常，设置文字图片提示
            if (listBlood.size() != 0 && listTem.size() != 0) {
                checkBlood(listBlood.get(listBlood.size() - 1).getSystolic(), listBlood.get(listBlood.size() - 1).getDiastolic(), listTem.get(listTem.size() - 1).getTemperaturet());
            } else if (listBlood.size() == 0 && listTem.size() != 0) {
                checkBlood(0, 0, listTem.get(listTem.size() - 1).getTemperaturet());
            } else if (listBlood.size() != 0 && listTem.size() == 0) {
                checkBlood(listBlood.get(listBlood.size() - 1).getSystolic(), listBlood.get(listBlood.size() - 1).getDiastolic(), 0);
            } else {
                checkBlood(0, 0, 0);
            }
        }
        mBloodView.setInfo(YbloodNum, XdateNum, heightBloodData, lowBloodData);
        mBloodView.invalidate();
        mTemView.setTemInfo(YTemData, XTemdateNum, temData);
        mTemView.invalidate();


    }


    /**
     * 判断血压,体温设置提示
     * height 高压
     * low 低压
     * tem 体温
     */


    public void checkBlood(int height, int low, float tem) {

        if (height == 0 && low == 0 && tem == 0) {
            mPromptImg.setVisibility(View.GONE);
            mPromptTv.setText("待测");
            mPromptTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            mPromptTv.setVisibility(View.VISIBLE);
            mPromptTv.setTextColor(Color.parseColor(grayColor));
        }

        //显示不正常
        else if (height > 139 || height < 90 || low > 89 || low < 60 || tem < 36 || tem > 37.5) {
            mPromptImg.setVisibility(View.VISIBLE);
            mPromptImg.setImageResource(R.mipmap.normal_error);
            mPromptTv.setVisibility(View.GONE);
        } else {
            mPromptImg.setVisibility(View.VISIBLE);
            mPromptImg.setImageResource(R.mipmap.normal);
            mPromptTv.setVisibility(View.GONE);
        }
        mHeightBloodTv.setText(height + "");
        mLowBloodTv.setText(low + "");
        mTem.setText(tem + "°C");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRollPagerView.pause();
        mRollPagerView.resume();
        mRollPagerView.isPlaying();
    }

    //是否有未读消息
    public void hasUnReadMsg(){
        Map<String,String>mp=new HashMap<>();
        mp.put("token",User.token);
        ok.getCall(Ip.path+ Iport.interface_HasUnReadMsg,mp,ok.OK_POST).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                    mHandler.sendEmptyMessage(-10);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                Log.e("有无未读消息",resStr);
                mHandler.sendEmptyMessage(-11);
            }
        });
    }
    //获取未读消息http://192.168.1.168:8082/yuyi/message/readPage.do?token=97338E8A81C0CC137FC51C6206681EBB&start=0&limit=1
    public void getUnReadMsg(){
        Map<String,String>mp=new HashMap<>();
        mp.put("token",User.token);
        mp.put("start","0");
        mp.put("limit",msgTotalCount>15?15+"":msgTotalCount+"");
        ok.getCall(Ip.path+ Iport.interface_getUnReadMsg,mp,ok.OK_POST).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(-13);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                Log.i("未读消息",resStr);
                mHandler.sendEmptyMessage(-14);
            }
        });
    }
}

