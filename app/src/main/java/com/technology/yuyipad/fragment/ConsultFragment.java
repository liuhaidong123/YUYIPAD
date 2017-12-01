package com.technology.yuyipad.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.RongUtils.RongWindow;
import com.technology.yuyipad.adapter.AskListViewAdapter;
import com.technology.yuyipad.bean.FirstPageInformationTwoData;
import com.technology.yuyipad.bean.FirstPageInformationTwoDataRoot;
import com.technology.yuyipad.bean.Information;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lzhUtils.MyDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultFragment extends Fragment {
    View three;
    String hospitalId = "";
    //医院列表
    private ListView mHospital_ListView;
    private View footer;
    private ProgressBar footerBar;
    private AskListViewAdapter mAdapter;
    private List<FirstPageInformationTwoData> mList = new ArrayList<>();
    private int mPosition = 0;
    private boolean isOnce = true;
    //医院详情
    private ImageView mImg;
    private TextView mHospital_Name, mHospital_Grade, mHospital_Content, mAsk_Btn;
    private HttpTools mHttptools;
    private int mStart = 0;
    private int mAddNum = 10;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyDialog.stopDia();
            if (msg.what == 24) {
                Object o = msg.obj;
                if (o != null && o instanceof FirstPageInformationTwoDataRoot) {
                    FirstPageInformationTwoDataRoot root = (FirstPageInformationTwoDataRoot) o;
                    if (root != null && root.getRows() != null && root.getRows().size() != 0) {
                        List<FirstPageInformationTwoData> list = new ArrayList<>();
                        list = root.getRows();
                        mHospital_ListView.removeFooterView(footer);
                        mList.addAll(list);
                        mAdapter.setmList(mList);
                        mAdapter.notifyDataSetChanged();

                        if (isOnce) {//刚进来显示第一个医院的详情
                            mHttptools.getAskDataMessage(handler, list.get(mPosition).getId());//请求医院详情
                            isOnce = false;
                        }

                        mData_LL.setVisibility(View.VISIBLE);
                        mNoData_RL.setVisibility(View.GONE);

                        if (list.size() == 10) {
                            mHospital_ListView.addFooterView(footer);
                            footerBar.setVisibility(View.INVISIBLE);
                        } else {
                            mHospital_ListView.removeFooterView(footer);
                        }
                    } else {
                        mData_LL.setVisibility(View.GONE);
                        mNoData_RL.setVisibility(View.VISIBLE);
                    }

                }
            } else if (msg.what == 205) {
                mHospital_ListView.removeFooterView(footer);
                mData_LL.setVisibility(View.GONE);
                mNoData_RL.setVisibility(View.VISIBLE);
            } else if (msg.what == 206) {
                mHospital_ListView.removeFooterView(footer);
                mData_LL.setVisibility(View.GONE);
                mNoData_RL.setVisibility(View.VISIBLE);

            } else if (msg.what == 25) {//医院详情
                Object o = msg.obj;
                if (o != null && o instanceof Information) {
                    Information information = (Information) o;
                    hospitalId = information.getId() + "";
                    mHospital_Name.setText(information.getHospitalName());
                    mHospital_Grade.setText(information.getGradeName());
                    mHospital_Content.setText(information.getIntroduction());
                    Picasso.with(getActivity()).load(UrlTools.BASE + information.getPicture()).error(R.mipmap.errorpicture).into(mImg);
                }
            }
        }
    };

    private LinearLayout mData_LL;
    private RelativeLayout mNoData_RL;

    public ConsultFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {

        mData_LL = view.findViewById(R.id.consult_data_ll);
        mNoData_RL = view.findViewById(R.id.consult_nodata_rl);
        mNoData_RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.showDialog(getContext());
                mHttptools.getAskData(handler, 0, 10);
            }
        });
        three = view.findViewById(R.id.three);
        mHttptools = HttpTools.getHttpToolsInstance();//医院列表
        mHttptools.getAskData(handler, 0, 10);
        //医院列表
        mHospital_ListView = view.findViewById(R.id.hospital_listview);
        footer = LayoutInflater.from(getContext()).inflate(R.layout.circle_listview_footer, null);
        footerBar = footer.findViewById(R.id.pbLocate);
        mAdapter = new AskListViewAdapter(getActivity(), mList);
        mHospital_ListView.setAdapter(mAdapter);
        mHospital_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mList.size()) {//点击footer加载更多
                    footerBar.setVisibility(View.VISIBLE);
                    mStart += 10;
                    mHttptools.getAskData(handler, mStart, mAddNum);
                } else {
                    mHttptools.getAskDataMessage(handler, mList.get(i).getId());//请求医院详情
                    mPosition = i;
                }

            }
        });

        //医院详情
        mImg = view.findViewById(R.id.hospital_img);
        mHospital_Name = view.findViewById(R.id.hospital_name);
        mHospital_Grade = view.findViewById(R.id.hospital_grade);
        mHospital_Content = view.findViewById(R.id.hospital_content);

        mAsk_Btn = view.findViewById(R.id.bottomBtn);//咨询语音视频
        mAsk_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongWindow.getInstance().showWindow(getActivity(), three, hospitalId);
            }
        });
    }
}
