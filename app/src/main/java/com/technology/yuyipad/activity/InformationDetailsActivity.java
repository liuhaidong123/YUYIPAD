package com.technology.yuyipad.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.RongUtils.RongWindow;
import com.technology.yuyipad.adapter.InformationAda;
import com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.UpdatedInformation;
import com.technology.yuyipad.httptools.HttpTools;

import java.util.ArrayList;
import java.util.List;

import com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Root;
import com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Rows;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lhdUtils.InformationListView;

public class InformationDetailsActivity extends AppCompatActivity {
    private RelativeLayout mAllData_Rl;
    private ImageView mImg;
    private TextView mTitle, mGrade, mContent;
    private InformationListView information_listview;
    private InformationAda mAdapter;
    private List<Rows> mList = new ArrayList<>();
    private List<Boolean> checkBgList = new ArrayList<>();
    private int mStart = 0;
    private int mAddNum = 10;
    private RelativeLayout mMany_more;
    private ProgressBar mBar;
    private HttpTools mHttptools;
    private boolean isMore=false;//true 代表点击了加载跟多
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 22) {
                Object o = msg.obj;
                if (o != null && o instanceof Root) {
                    Root root = (Root) o;
                    if (root != null && root.getRows() != null && root.getRows().size() != 0) {

                        List<Rows> list = new ArrayList<>();
                        list = root.getRows();
                        mList.addAll(list);

                        if (!isMore){//没有点击加载更多

                            if (tag == -1) {//请求默认第一条数据
                                for (int i = 0; i < mList.size(); i++) {
                                    if (i == 0) {
                                        checkBgList.add(true);
                                    } else {
                                        checkBgList.add(false);
                                    }
                                }
                                mAdapter.setList(mList, checkBgList);
                                mAdapter.notifyDataSetChanged();
                                mHttptools.getFirstPageInformationTwoDataMessage(handler, mList.get(0).getId());
                            } else if (tag == -2) {
                                Toast.makeText(InformationDetailsActivity.this, "详情错误", Toast.LENGTH_SHORT).show();
                            } else {//请求上一页点击的某条资讯详情

                                for (int i = 0; i < mList.size(); i++) {
                                    if (i == tag) {
                                        checkBgList.add(true);
                                    } else {
                                        checkBgList.add(false);
                                    }
                                }
                                mAdapter.setList(mList, checkBgList);
                                mAdapter.notifyDataSetChanged();
                                mHttptools.getFirstPageInformationTwoDataMessage(handler, mList.get(tag).getId());
                            }
                        }else {//点击了加载更多
                            for (int i=0;i<list.size();i++){
                                checkBgList.add(false);
                            }
                            mAdapter.setList(mList, checkBgList);
                            mAdapter.notifyDataSetChanged();
                        }

                        mBar.setVisibility(View.INVISIBLE);
                        if (list.size() == 10) {
                            mMany_more.setVisibility(View.VISIBLE);
                        } else {
                            mMany_more.setVisibility(View.GONE);
                        }
                    }else {
                        mMany_more.setVisibility(View.GONE);
                    }
                }
            } else if (msg.what == 23) {//详情
                Object o = msg.obj;
                if (o != null && o instanceof UpdatedInformation) {
                    UpdatedInformation information = (UpdatedInformation) o;
                    if (information != null) {
                        Picasso.with(InformationDetailsActivity.this).load(UrlTools.BASE + information.getPicture()).into(mImg);
                        mTitle.setText(information.getTitle());
                        mGrade.setText(information.getSmalltitle());
                        mContent.setText(information.getArticleText());
                        mAllData_Rl.setVisibility(View.VISIBLE);
                    }
                }
            } else if (msg.what == 204) {//医院详情失败
                Toast.makeText(InformationDetailsActivity.this, "请求详情错误", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private int tag;//-1表示请求全部资讯列表，默认详情为第一条数据，-2表示数据错误，其他的表示请求上一页点击的某条资讯详情

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_details);
        //获取资讯页面数据
        mHttptools = HttpTools.getHttpToolsInstance();
        mHttptools.getFirstPageInformationTwoData(handler, 0, 10);
        initUI();
    }

    private void initUI() {
        tag = getIntent().getIntExtra("tag", -2);
        mAllData_Rl = (RelativeLayout) findViewById(R.id.all_information_data_rl);
        mImg = (ImageView) findViewById(R.id.information_details_img);
        mTitle = (TextView) findViewById(R.id.information_title_tv);
        mGrade = (TextView) findViewById(R.id.information_details_grade);
        mContent = (TextView) findViewById(R.id.information_content);

        information_listview = (InformationListView) findViewById(R.id.information_details_listview);
        mAdapter = new InformationAda(this, mList);
        information_listview.setAdapter(mAdapter);

        mMany_more = (RelativeLayout) findViewById(R.id.in_many_relative);
        mBar = (ProgressBar) findViewById(R.id.in_pbLocate);
        //加载更多
        mMany_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMore=true;
                mBar.setVisibility(View.VISIBLE);
                mStart += 10;
                mHttptools.getFirstPageInformationTwoData(handler, mStart, mAddNum);

            }
        });

        //详情
        information_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mHttptools.getFirstPageInformationTwoDataMessage(handler, mList.get(i).getId());
                checkBgList.clear();
                for (int k = 0; k < mList.size(); k++) {
                    if (k == i) {
                        checkBgList.add(i,true);
                    } else {
                        checkBgList.add(false);
                    }
                }
                mAdapter.setList(mList, checkBgList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
