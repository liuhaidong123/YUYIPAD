package com.technology.yuyipad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.HospitalDepartmentMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/9/28.
 * 科室
 */

public class DepartmentAda extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<HospitalDepartmentMessage> mList = new ArrayList();
    private List<Boolean> checklist = new ArrayList<>();

    public DepartmentAda(Context mContext, List<HospitalDepartmentMessage> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(this.mContext);

    }

    public void setmList(List<HospitalDepartmentMessage> mList, List<Boolean> checklist) {
        this.mList = mList;
        this.checklist = checklist;
    }

    @Override
    public int getCount() {
        return mList.size()==0?0:mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DepartmentHolder departmentHolder = null;
        if (view == null) {
            departmentHolder = new DepartmentHolder();
            view = mInflater.inflate(R.layout.select_department_item, null);
            departmentHolder.textView = view.findViewById(R.id.department_id);
            view.setTag(departmentHolder);
        } else {
            departmentHolder = (DepartmentHolder) view.getTag();

        }
        departmentHolder.textView.setText(mList.get(i).getDepartmentName());
        if (checklist.get(i)) {//点击背景和文字颜色变化
            departmentHolder.textView.setBackgroundResource(R.color.color_f6f6f6);
            departmentHolder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_department_tv));
        } else {
            departmentHolder.textView.setBackgroundResource(R.color.ffffff);
            departmentHolder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
        }
        return view;
    }


    class DepartmentHolder {
        TextView textView;
    }
}
