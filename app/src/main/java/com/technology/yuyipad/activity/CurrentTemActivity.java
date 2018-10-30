package com.technology.yuyipad.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sst.jkezt.health.utils.BTBpData;
import com.sst.jkezt.health.utils.BTTptData;
import com.sst.jkezt.health.utils.HealthMeasureActivity;
import com.sst.jkezt.health.utils.HealthMeasureListener;
import com.sst.jkezt.health.utils.HealthMeasureState;
import com.sst.jkezt.health.utils.HealthMeasureType;
import com.sst.jkezt.health.utils.JkezAPIMain;
import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.FamilyUserManagerActivity;
import com.technology.yuyipad.adapter.TemAdapter;
import com.technology.yuyipad.bean.UserListBean.Result;
import com.technology.yuyipad.bean.UserListBean.Root;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.lhdUtils.NetWorkUtils;
import com.technology.yuyipad.lhdUtils.SanJiao;
import com.technology.yuyipad.lhdUtils.TherC;
import com.technology.yuyipad.lzhUtils.MyDialog;
import com.technology.yuyipad.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentTemActivity extends HealthMeasureActivity implements View.OnClickListener {
    //重新登录页面
    private RelativeLayout mLogin_rl;
    private TextView mHandInput, mSaveBtn, mDuNum, mPrompt, mDu;
    private ListView mListview;
    private TemAdapter mAdapter;
    private View footer;
    private RelativeLayout mTemRL;
    private Map<String, String> mSubmitMap = new HashMap<>();
    private int mPosintion = -1;
    private HttpTools mHttptools;
    private Map<String, String> mMap = new HashMap<>();
    private List<Result> mList = new ArrayList<>();
    private List<Boolean> showNameList = new ArrayList<>();
    private boolean isSelect = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 35) {//获取用户列表
                Object o = msg.obj;
                if (o != null && o instanceof Root) {
                    Root root = (Root) o;
                    if (root != null && root.getResult() != null) {

                        mList = root.getResult();
                        mListview.removeFooterView(footer);
                        showNameList.clear();
                        for (int i = 0; i < mList.size(); i++) {
                            if (i == 0) {
                                showNameList.add(true);
                            } else {
                                showNameList.add(false);
                            }

                        }
                        mAdapter.setList(mList);
                        mAdapter.setmCheckList(showNameList);
                        mAdapter.notifyDataSetChanged();
                        mPosintion = 0;
                        isSelect = true;

                        if (mList.size() == 6) {
                            mListview.removeFooterView(footer);
                        } else {
                            mListview.addFooterView(footer);
                        }
                    } else {
                        mLogin_rl.setVisibility(View.VISIBLE);
                        Toast.makeText(CurrentTemActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                    }


                }
            } else if (msg.what == 36) {//提交数据接口
                MyDialog.stopDia();
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.SubmitTemBean.Root) {
                    com.technology.yuyipad.bean.SubmitTemBean.Root root = (com.technology.yuyipad.bean.SubmitTemBean.Root) o;
                    if (root.getCode().equals("0")) {
                        Toast.makeText(getApplicationContext(), "提交数据成功", Toast.LENGTH_SHORT).show();
                        MyDialog.stopDia();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "提交数据失败", Toast.LENGTH_SHORT).show();
                        MyDialog.stopDia();
                    }
                }
            } else if (msg.what == 227) {//json解析失败
                MyDialog.stopDia();
                Toast.makeText(getApplicationContext(), "提交数据失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 228) {//提交数据失败
                Toast.makeText(getApplicationContext(), "提交数据失败", Toast.LENGTH_SHORT).show();
                MyDialog.stopDia();
            }
        }
    };

    private HealthMeasureType type;//体温类型
    BluetoothAdapter bluetoothAdapter;
    private float mTemNum=34.5f;//默认体温34.5度，三角指示标志指示在34.5度那里，注意：异常数据时，三角形指示在34.5那里
    private SanJiao mSanjiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NetWorkUtils.isNetWorkConnected(this)) {
            setContentView(R.layout.activity_current_tem);
            //获取用户列表
            mMap.put("token", User.token);
            mHttptools = HttpTools.getHttpToolsInstance();
            mHttptools.getUserLIst(mHandler, mMap);//
            initUI();
        } else {
            setContentView(R.layout.firstpage_newwork);
            TextView textView = (TextView) findViewById(R.id.first_page_tv);
            textView.setText("当前体温");
        }
    }

    private void initUI() {
        JkezAPIMain.openBluetooth();//打开蓝牙
        mLogin_rl = (RelativeLayout) findViewById(R.id.again_login_rl);
        mLogin_rl.setOnClickListener(this);
        mHandInput = (TextView) findViewById(R.id.input_tem_btn);
        mSaveBtn = (TextView) findViewById(R.id.tem_save_btn);

        //手动输入
        mHandInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), InputTemActivity.class));
                finish();
            }
        });
        //保存
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTemData();
            }
        });
        footer = LayoutInflater.from(this).inflate(R.layout.tem_add_item, null);
        //存放温度计
        mTemRL = (RelativeLayout) findViewById(R.id.my_tem_rl);
        TherC therC = new TherC(this);
        mTemRL.addView(therC);
        mSanjiao = new SanJiao(CurrentTemActivity.this);
        mSanjiao.setDuNum(mTemNum);
        mTemRL.addView(mSanjiao);
        mListview = (ListView) findViewById(R.id.tem_user_listview);
        mAdapter = new TemAdapter(this, mList, showNameList);
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (mList.size() == 0 || i == mList.size()) {
                    Intent intent = new Intent(getApplicationContext(), FamilyUserManagerActivity.class);
                    startActivity(intent);
                } else {
                    for (int k = 0; k < mList.size(); k++) {
                        if (k == i) {
                            showNameList.set(i, true);
                        } else {
                            showNameList.set(k, false);
                        }
                    }
                    mAdapter.setmCheckList(showNameList);
                    mAdapter.notifyDataSetChanged();
                    isSelect = true;
                    mPosintion = i;
                }

            }
        });



        mDuNum = (TextView) findViewById(R.id.current_tem);
        mDu = (TextView) findViewById(R.id.du);
        mPrompt = (TextView) findViewById(R.id.tv_prompt);
        type = HealthMeasureType.BTTEMPERATURETYPE;//测量时的类型，这里是体温的类型
        setBleData(type);

    }


    /**
     * 获取体温
     */
    public String getTemData() {
        if (!mDuNum.getText().toString().trim().equals("") && mDuNum.getText().toString() != null && Double.valueOf(mDuNum.getText().toString().trim()) != 0) {
            return mDuNum.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 提交体温
     */
    public void submitTemData() {
        if (!getTemData().equals("")) {//体温不为""；
            if (isSelect) {//选中了某一个用户
                mSubmitMap.put("token", User.token);
                mSubmitMap.put("humeuserId", mList.get(mPosintion).getId() + "");
                mSubmitMap.put("temperaturet", getTemData());
                mHttptools.submitTemData(mHandler, mSubmitMap);
                MyDialog.showDialog(this);
            } else {
                Toast.makeText(this, "请选择用户", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "体温错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void setBleData(HealthMeasureType type1){
        try{
            initBluetooth(type, new HealthMeasureListener() {
                @Override
                public void onHealthNotFindDevice() {
                }

                @Override
                public void onHealthFindDevice(BluetoothDevice bluetoothDevice, HealthMeasureType healthMeasureType) {
                    Log.e("设备名称：", bluetoothDevice.getName());
                }

                /**
                 * 监听蓝牙连接
                 */
                @Override
                public void onHealthConnected() {
                    mPrompt.setText("连接成功，等待测量");

                }

                /**
                 * 监听蓝牙断开
                 */
                @Override
                public void onHealthDeviceDisconnect() {

                    mPrompt.setText("设备已断开");
                    mDuNum.setText("0.0");
                    mSanjiao.setDuNum(34.5);
                    mSanjiao.invalidate();//数据异常时，三角形指示标志指示在32度那里
                }

                @Override
                public void onHealthDeviceReceiveData(HealthMeasureType healthMeasureType, HealthMeasureState healthMeasureState, Object o) {
                    if (healthMeasureType == HealthMeasureType.BTTEMPERATURETYPE) {//接收到体温设备
                        BTTptData tptdata = (BTTptData) o;
                        if (healthMeasureState == HealthMeasureState.ERROR) {//获取异常的内容
                            mPrompt.setText("异常:" + tptdata.getErrtext());
                            mDuNum.setText("0.0");
                            mTemNum=34.5f;
                            mSanjiao.setDuNum(34.5);
                            mSanjiao.invalidate();//数据异常时，三角形指示标志指示在32度那里
                        } else {
                            if (1 == tptdata.getMeaType()) {
                                mTemNum = tptdata.getBody_temperature();
                                if (Double.valueOf(mTemNum) <= 36) {
                                    mPrompt.setText("*当前体温过低,请查看测量部位");
                                    mPrompt.setTextColor(Color.parseColor("#1ebeec"));
                                    mDu.setTextColor(Color.parseColor("#1ebeec"));
                                    mDuNum.setTextColor(Color.parseColor("#1ebeec"));
                                } else if (Double.valueOf(mTemNum) >= 38) {
                                    mPrompt.setText("*当前体温过高,请尽快就医");
                                    mPrompt.setTextColor(Color.parseColor("#f6547a"));
                                    mDu.setTextColor(Color.parseColor("#f6547a"));
                                    mDuNum.setTextColor(Color.parseColor("#f6547a"));
                                } else {
                                    mPrompt.setText("*当前体温正常");
                                    mPrompt.setTextColor(Color.parseColor("#f654f5"));
                                    mDu.setTextColor(Color.parseColor("#f654f5"));
                                    mDuNum.setTextColor(Color.parseColor("#f654f5"));
                                }
                                mDuNum.setText(tptdata.getBody_temperature() + "");
                                //当温度大于等于35小于等于42度时，三角形指示标志指示在正确的刻度位置
                                if (mTemNum>=35&&mTemNum<=42){
                                    mSanjiao.setDuNum(mTemNum);
                                    mSanjiao.invalidate();
                                }else {
                                    mPrompt.setText("异常:" + tptdata.getErrtext());
                                    mDuNum.setText("0.0");
                                    mTemNum=34.5f;
                                    mSanjiao.setDuNum(mTemNum);
                                    mSanjiao.invalidate();//数据异常时，三角形指示标志指示在32度那里
                                }


                            }
                        }
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
