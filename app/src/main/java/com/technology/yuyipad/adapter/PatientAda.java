package com.technology.yuyipad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.FirstPageUserDataBean.Result;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lhdUtils.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/9/26.
 */

public class PatientAda extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContent;
    private List<Result> list = new ArrayList<>();

    public PatientAda(Context mContent, List<Result> list) {
        this.mContent = mContent;
        this.list = list;
        mInflater = LayoutInflater.from(mContent);
    }

    public void setList(List<Result> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PatientHolder patientHolder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.patient_listview_item, null);
            patientHolder = new PatientHolder();
            patientHolder.name = view.findViewById(R.id.name_tv);
            patientHolder.head = view.findViewById(R.id.head_img);
            view.setTag(patientHolder);
        } else {
            patientHolder = (PatientHolder) view.getTag();
        }
        patientHolder.name.setText(list.get(i).getTrueName());
        Picasso.with(mContent).load(UrlTools.BASE + list.get(i).getAvatar()).error(R.mipmap.usererr).into(patientHolder.head);
        return view;
    }

    class PatientHolder {
        TextView name;
        RoundImageView head;
    }
}
