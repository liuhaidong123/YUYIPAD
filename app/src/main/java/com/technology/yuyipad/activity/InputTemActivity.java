package com.technology.yuyipad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.FamilyUserManagerActivity;
import com.technology.yuyipad.adapter.TemAdapter;
import com.technology.yuyipad.bean.UserListBean.Result;
import com.technology.yuyipad.bean.UserListBean.Root;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.lhdUtils.NetWorkUtils;
import com.technology.yuyipad.lhdUtils.SanJiaoHand;
import com.technology.yuyipad.lhdUtils.TherC;
import com.technology.yuyipad.lzhUtils.MyDialog;
import com.technology.yuyipad.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InputTemActivity extends AppCompatActivity implements View.OnClickListener{

    //重新登录页面
    private RelativeLayout mLogin_rl;
    private TextView  mSaveBtn, mDuNum, mDuFuHao, mPromptTv;
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
                        showNameList.clear();
                        mListview.removeFooterView(footer);
                        mList = root.getResult();
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
                    }else {
                        mLogin_rl.setVisibility(View.VISIBLE);
                        Toast.makeText(InputTemActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (msg.what == 36) {//提交数据接口
                Object o = msg.obj;
                MyDialog.stopDia();
                if (o != null && o instanceof com.technology.yuyipad.bean.SubmitTemBean.Root) {
                    com.technology.yuyipad.bean.SubmitTemBean.Root root = (com.technology.yuyipad.bean.SubmitTemBean.Root) o;
                    if (root.getCode().equals("0")) {
                        Toast.makeText(getApplicationContext(), "提交数据成功", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "提交数据失败", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetWorkUtils.isNetWorkConnected(this)) {
            setContentView(R.layout.activity_input_tem);
            //获取用户列表
            mMap.put("token", User.token);
            mHttptools = HttpTools.getHttpToolsInstance();
            mHttptools.getUserLIst(mHandler, mMap);//
            initUI();
        } else {
            setContentView(R.layout.firstpage_newwork);
            TextView textView = (TextView) findViewById(R.id.first_page_tv);
            textView.setText("手动输入体温");
        }
    }

    private void initUI() {
        mLogin_rl = (RelativeLayout) findViewById(R.id.again_login_rl);
        mLogin_rl.setOnClickListener(this);
        mSaveBtn = (TextView) findViewById(R.id.tem_save_btn);
        mDuNum = (TextView) findViewById(R.id.current_tem);
        mDuFuHao = (TextView) findViewById(R.id.du);
        mPromptTv = (TextView) findViewById(R.id.tv_prompt);

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
        SanJiaoHand sanJiao = new SanJiaoHand(this, 39, mDuNum, mDuFuHao, mPromptTv);
        mTemRL.addView(therC);
        mTemRL.addView(sanJiao);

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
                com.technology.yuyipad.lzhUtils.MyDialog.showDialog(this);
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
}
