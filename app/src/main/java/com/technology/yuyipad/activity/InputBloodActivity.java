package com.technology.yuyipad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.technology.yuyipad.lhdUtils.NetWorkUtils;
import com.technology.yuyipad.lzhUtils.MyDialog;
import com.technology.yuyipad.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputBloodActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mHeightBlood_Num, mLowBlood_Num;
    private TextView mSave_Btn;
    //重新登录页面
    private RelativeLayout mLogin_rl;
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
                    if (root != null && root.getResult() != null) {
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
                    } else {
                        mLogin_rl.setVisibility(View.VISIBLE);
                        Toast.makeText(InputBloodActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                    }

                }
            } else if (msg.what == 37) {//提交血压数据
                Object o = msg.obj;
                MyDialog.stopDia();
                if (o != null && o instanceof com.technology.yuyipad.bean.SubmitTemBean.Root) {
                    com.technology.yuyipad.bean.SubmitTemBean.Root root = (com.technology.yuyipad.bean.SubmitTemBean.Root) o;
                    if (root.getCode().equals("0")) {
                        Toast.makeText(InputBloodActivity.this, "提交数据成功", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(InputBloodActivity.this, "提交数据失败", Toast.LENGTH_SHORT).show();
                        MyDialog.stopDia();
                    }
                }
            } else if (msg.what == 229) {//json解析失败
                MyDialog.stopDia();
                Toast.makeText(InputBloodActivity.this, "提交数据失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 230) {//提交数据失败
                MyDialog.stopDia();
                Toast.makeText(InputBloodActivity.this, "提交数据失败", Toast.LENGTH_SHORT).show();

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (NetWorkUtils.isNetWorkConnected(this)) {
            setContentView(R.layout.activity_input_blood);
            initUI();
        } else {
            setContentView(R.layout.firstpage_newwork);
            TextView textView = (TextView) findViewById(R.id.first_page_tv);
            textView.setText("手动输入血压");
        }
    }
    private void initUI() {
        mLogin_rl = (RelativeLayout) findViewById(R.id.again_login_rl);
        mLogin_rl.setOnClickListener(this);
        //请求用户列表
        mMap.put("token", User.token);
        Log.e("token=", User.token);
        mHttptools = HttpTools.getHttpToolsInstance();
        mHttptools.getUserLIst(mHandler, mMap);//
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
                    startActivity(intent);
                } else {
                    if (position == mList.size()) {//最后一个是添加按钮
                        Intent intent = new Intent(getApplicationContext(), FamilyUserManagerActivity.class);
                        startActivity(intent);
                    } else {
                        //用户信息不完善
                        if (mList.get(0).getAge() == 0 | mList.get(0).getTrueName().equals("") | mList.get(0).getGender() == null) {
                            //mSureAlertDialog.show();
                            Toast.makeText(InputBloodActivity.this, "用户信息不完善", Toast.LENGTH_SHORT).show();
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

        mHeightBlood_Num = (EditText) findViewById(R.id.blood_height_num);
        mLowBlood_Num = (EditText) findViewById(R.id.blood_low_num);
        mSave_Btn = (TextView) findViewById(R.id.save_blood_data);

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
                if (checkHeightBlood()) {
                    if (isSelect) {//选中用户
                        mSubmitMap.put("token", User.token);
                        mSubmitMap.put("humeuserId", mList.get(mPosintion).getId() + "");
                        mSubmitMap.put("systolic", getHeightBlood());
                        mSubmitMap.put("diastolic", getLowBlood());
                        mHttptools.submitBloodData(mHandler, mSubmitMap);
                        MyDialog.showDialog(this);
                    } else {
                        Toast.makeText(InputBloodActivity.this, "请选择用户", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入正确的血压数据", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(InputBloodActivity.this, "低压数据错误", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(InputBloodActivity.this, "高压数据错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
