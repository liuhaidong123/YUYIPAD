package com.technology.yuyipad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.HospitalOutPatient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/9/28.
 */

public class OPDAda extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<HospitalOutPatient> mList = new ArrayList();

    public OPDAda(Context mContext, List<HospitalOutPatient> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater=LayoutInflater.from(this.mContext);
    }

    public void setmList(List<HospitalOutPatient> mList) {
        this.mList = mList;
        if (mList.size()==0){
            Toast.makeText(this.mContext,"暂无门诊信息",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
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
        OPDHolder opdHolder=null;
        if (view==null){
            opdHolder=new OPDHolder();
            view=mInflater.inflate(R.layout.select_opd_item,null);
            opdHolder.textView=view.findViewById(R.id.opd_id);
            view.setTag(opdHolder);
        }else {
           opdHolder= (OPDHolder) view.getTag();
        }
        opdHolder.textView.setText(mList.get(i).getClinicName());

        return view;
    }

    class OPDHolder{
        TextView textView;
    }
}
