package com.technology.yuyipad.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.FamilyUserManagerActivity;
import com.technology.yuyipad.adapter.RecycleAdapter;
import com.technology.yuyipad.adapter.RecycleViewBlood;
import com.technology.yuyipad.bean.UserListBean.Result;
import com.technology.yuyipad.bean.UserListBean.Root;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.lhdUtils.MyDialog;
import com.technology.yuyipad.lhdUtils.NetWorkUtils;
import com.technology.yuyipad.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentBloodActivity extends AppCompatActivity {
    private TextView mHeightBlood_Num, mLowBlood_Num, mHandInput_Num, mSave_Btn;

    private RecyclerView mRecycleView;
    private RecycleViewBlood mAdapter;
    private List<Result> mList = new ArrayList<>();
    private List<Boolean> isFlagList = new ArrayList<>();
    private HttpTools mHttptools;
    private int mPosintion = -1;//点击某个用户的下标
    private boolean isSelect = false;
    private Map<String, String> mMap = new HashMap<>();
    private Map<String, String> mSubmitMap = new HashMap<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 35) {//获取用户列表
                Object o = msg.obj;
                if (o != null && o instanceof Root) {
                    Root root = (Root) o;
                    mList = root.getResult();
                    isFlagList.clear();
                    for (int i = 0; i < mList.size(); i++) {
                        if (i == 0) {
                            isFlagList.add(true);
                        } else {
                            isFlagList.add(false);
                        }

                    }
                    mAdapter.setList(mList);
                    mAdapter.setIsFlagList(isFlagList);
                    mAdapter.notifyDataSetChanged();
                    mPosintion = 0;
                    isSelect = true;
                    Log.e("用户下标-", mPosintion + "");
                }
            } else if (msg.what == 37) {//提交血压数据
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.SubmitTemBean.Root) {
                    com.technology.yuyipad.bean.SubmitTemBean.Root root = (com.technology.yuyipad.bean.SubmitTemBean.Root) o;
                    if (root.getCode().equals("0")) {
                        Toast.makeText(CurrentBloodActivity.this, "提交数据成功", Toast.LENGTH_SHORT).show();
                        MyDialog.stopDialog();
                        finish();
                    } else {
                        Toast.makeText(CurrentBloodActivity.this, "提交数据失败", Toast.LENGTH_SHORT).show();
                        MyDialog.stopDialog();
                    }
                }
            } else if (msg.what == 229) {//json解析失败
                MyDialog.stopDialog();
            } else if (msg.what == 230) {//提交数据失败
                Toast.makeText(CurrentBloodActivity.this, "提交数据失败", Toast.LENGTH_SHORT).show();
                MyDialog.stopDialog();
            }
        }
    };

    private RelativeLayout mAllData_Blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (NetWorkUtils.isNetWorkConnected(this)) {
            setContentView(R.layout.activity_current_blood);
            initUI();
        } else {
            setContentView(R.layout.firstpage_newwork);
            TextView textView = (TextView) findViewById(R.id.first_page_tv);
            textView.setText("当前血压");
        }

    }

    private void initUI() {
        mAllData_Blood = (RelativeLayout) findViewById(R.id.all_data_blood);
        //请求用户列表
        mMap.put("token", User.token);
        Log.e("token=", User.token);
        mHttptools = HttpTools.getHttpToolsInstance();
        //异常提示
        mRecycleView = (RecyclerView) findViewById(R.id.recycle_view_person);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(OrientationHelper.HORIZONTAL);//水平走向
        mRecycleView.setLayoutManager(manager);
        mAdapter = new RecycleViewBlood(this, mList, isFlagList);
        mRecycleView.setAdapter(mAdapter);
        //选择头像
        mAdapter.setOnItemClickLitener(new RecycleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mList.size() == 0) {//只有添加按钮
                    Intent intent = new Intent(getApplicationContext(), FamilyUserManagerActivity.class);
                    // intent.putExtra("type", "0");
                    startActivity(intent);
                } else {
                    if (position == mList.size()) {//最后一个是添加按钮
                        Intent intent = new Intent(getApplicationContext(), FamilyUserManagerActivity.class);
                        //intent.putExtra("type", "0");
                        startActivity(intent);

                    } else {
                        //用户信息不完善
                        if (mList.get(0).getAge() == 0 | mList.get(0).getTrueName().equals("") | mList.get(0).getGender() == null) {
                            //mSureAlertDialog.show();
                            Toast.makeText(CurrentBloodActivity.this, "用户信息不完善", Toast.LENGTH_SHORT).show();
                        } else {//点击的是某个用户头像
                            for (int i = 0; i < mList.size(); i++) {
                                if (position == i) {
                                    isFlagList.set(position, true);
                                } else {
                                    isFlagList.set(i, false);
                                }
                            }

                            mAdapter.setIsFlagList(isFlagList);
                            mAdapter.notifyDataSetChanged();
                            isSelect = true;
                            mPosintion = position;
                            Log.e("name", mList.get(position).getTrueName());
                        }
                    }

                }
            }
        });

        mHeightBlood_Num = (TextView) findViewById(R.id.blood_height_num);
        mLowBlood_Num = (TextView) findViewById(R.id.blood_low_num);
        mHandInput_Num = (TextView) findViewById(R.id.blood_input_tv);
        mSave_Btn = (TextView) findViewById(R.id.save_blood_data);
        //手动输入
        mHandInput_Num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CurrentBloodActivity.this, InputBloodActivity.class));
                finish();
            }
        });
        //保存血压数据
        mSave_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitBloodData();
            }
        });
    }

    /**
     * 获取高压
     *
     * @return
     */
    public String getHeightBlood() {
        if (!mHeightBlood_Num.getText().toString().trim().equals("") && mHeightBlood_Num.getText().toString() != null && Integer.valueOf(mHeightBlood_Num.getText().toString().trim()) != 0) {
            return mHeightBlood_Num.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 获取低压
     *
     * @return
     */
    public String getLowBlood() {
        if (!mLowBlood_Num.getText().toString().trim().equals("") && mLowBlood_Num.getText().toString() != null && Integer.valueOf(mLowBlood_Num.getText().toString().trim()) != 0) {
            return mLowBlood_Num.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 判断输入的血压是否在范围之内
     */
    public boolean checkHeightBlood() {
        if (Integer.valueOf(getHeightBlood()) > 180 || Integer.valueOf(getHeightBlood()) < 40 || Integer.valueOf(getLowBlood()) < 40 || Integer.valueOf(getLowBlood()) > 180) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 提交
     */
    public void submitBloodData() {
        if (!getHeightBlood().equals("")) {//高压
            if (!getLowBlood().equals("")) {//低压
                if (checkHeightBlood()) {//判断输入的数据是否在规定范围内
                    if (isSelect) {//选中用户
                        mSubmitMap.put("token", User.token);
                        mSubmitMap.put("humeuserId", mList.get(mPosintion).getId() + "");
                        mSubmitMap.put("systolic", getHeightBlood());
                        mSubmitMap.put("diastolic", getLowBlood());
                        mHttptools.submitBloodData(mHandler, mSubmitMap);
                        MyDialog.showPopuWindow(this, mAllData_Blood);
                    } else {
                        Toast.makeText(CurrentBloodActivity.this, "请选择用户", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请传入正确的血压数据", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(CurrentBloodActivity.this, "低压数据错误", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(CurrentBloodActivity.this, "高压数据错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHttptools.getUserLIst(mHandler, mMap);//
        Log.e("onResume", "====onResume");
    }


    /**
     * 判断血压,体温设置提示
     * height 高压
     * low 低压
     *
     */
//    public void checkBlood(int height, int low) {
//
//        if (height == 0 && low == 0 ) {
//
//            mDataMsg_tv.setText("*当前数据为空");
//            mDataMsg_tv.setTextColor(ContextCompat.getColor(this,R.color.navigate_tv_select));
//        }
//
//        //显示不正常
//        else if (height > 139 || height < 90 || low > 89 || low < 60 ) {
//
//            mDataMsg_tv.setText("*当前数据异常");
//            mDataMsg_tv.setTextColor(ContextCompat.getColor(this,R.color.color_red));
//
//        } else {
//
//            mDataMsg_tv.setText("*当前数据正常");
//            mDataMsg_tv.setTextColor(ContextCompat.getColor(this,R.color.normal_data));
//        }
//
//    }
}
