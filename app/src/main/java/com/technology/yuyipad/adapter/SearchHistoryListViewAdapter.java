package com.technology.yuyipad.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technology.yuyipad.R;

import com.technology.yuyipad.activity.HospitalDetailsActivity;
import com.technology.yuyipad.activity.SearchHospitalActivity;
import com.technology.yuyipad.bean.SearchDrugBean.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/3/1.
 */

public class SearchHistoryListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<com.technology.yuyipad.bean.SearchHospital.Result> mList = new ArrayList();
    private int mPositon;



    public SearchHistoryListViewAdapter(Context mContext, List mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = mInflater.inflate(R.layout.search_history_listview_item, null);
            viewHodler.name = (TextView) convertView.findViewById(R.id.name);

        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        //医院
        if (mList.get(position) instanceof com.technology.yuyipad.bean.SearchHospital.Result) {
            viewHodler.name.setText(((com.technology.yuyipad.bean.SearchHospital.Result) mList.get(position)).getHospitalName());
        }

        mPositon = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchHospitalActivity searchActivity = (SearchHospitalActivity) mContext;
                Intent intent=new Intent(mContext,HospitalDetailsActivity.class);
                intent.putExtra("HospitalID",mList.get(mPositon).getId());
                searchActivity.startActivity(intent);
                searchActivity.  finish();
            }
        });
        convertView.setTag(viewHodler);
        return convertView;
    }

    class ViewHodler {
        TextView name;
    }
}
