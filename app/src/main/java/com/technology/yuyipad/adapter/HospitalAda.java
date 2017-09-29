package com.technology.yuyipad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.FirstPageInformationTwoData;
import com.technology.yuyipad.httptools.UrlTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/9/28.
 */

public class HospitalAda extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<FirstPageInformationTwoData> list = new ArrayList<>();
    private List<Boolean> checkList=new ArrayList<>();
    public HospitalAda(Context mContext, List<FirstPageInformationTwoData> list) {
        this.mContext = mContext;
        this.list = list;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setList(List<FirstPageInformationTwoData> list,List<Boolean> checkList) {
        this.list = list;
        this.checkList=checkList;
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
        HospitalHolder hospitalHolder = null;
        if (view == null) {
            hospitalHolder = new HospitalHolder();
            view = mInflater.inflate(R.layout.hospital_item, null);
            hospitalHolder.img = view.findViewById(R.id.yu_img_mess);
            hospitalHolder.name_tv = view.findViewById(R.id.yu_tv_title);
            hospitalHolder.grade_tv = view.findViewById(R.id.grade_tv);
            view.setTag(hospitalHolder);
        } else {
            hospitalHolder = (HospitalHolder) view.getTag();
        }
        Picasso.with(mContext).load(UrlTools.BASE + list.get(i).getPicture()).error(R.mipmap.error_big).into(hospitalHolder.img);
        hospitalHolder.name_tv.setText(list.get(i).getHospitalName());
        hospitalHolder.grade_tv.setText(list.get(i).getGradeName());

        if (checkList.get(i)){//点击
            view.setBackgroundResource(R.color.color_f6f6f6);
        }else {//没有点击
            view.setBackgroundResource(R.color.ffffff);
        }
        return view;
    }

    class HospitalHolder {
        ImageView img;
        TextView name_tv;
        TextView grade_tv;
    }
}
