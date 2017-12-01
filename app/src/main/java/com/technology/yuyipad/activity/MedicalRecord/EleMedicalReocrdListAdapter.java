package com.technology.yuyipad.activity.MedicalRecord;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanyu on 2017/10/23.
 */

public class EleMedicalReocrdListAdapter extends BaseAdapter{
    Context con;
    List<bean_MedicalRecordList.ResultBean>list;
    public EleMedicalReocrdListAdapter( Context con,List<bean_MedicalRecordList.ResultBean>list){
        this.con=con;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
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
        ViewHodler hodler;
        if (view==null){
            view= LayoutInflater.from(con).inflate(R.layout.elelist_item,null);
            hodler=new ViewHodler(view);
            view.setTag(hodler);
        }
        else {
            hodler= (ViewHodler) view.getTag();
        }
        hodler.ele_KS.setText(list.get(i).getDepartmentName());
        hodler.ele_HosName.setText(list.get(i).getHospitalName());
        hodler.ele_info.setText(list.get(i).getMedicalrecord());
        if (!"".equals(list.get(i).getCreateTimeString())&&!TextUtils.isEmpty(list.get(i).getCreateTimeString())){
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d= null;
            try {
                d = format.parse(list.get(i).getCreateTimeString());
                Calendar c=Calendar.getInstance();
                c.setTime(d);
                hodler.ele_TimeYear.setText(c.get(Calendar.YEAR)+"");
                hodler.ele_TimeMonth.setText((c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                hodler.ele_TimeYear.setText("日期");
                hodler.ele_TimeMonth.setText("");
                e.printStackTrace();
            }
        }
        return view;
    }
    class ViewHodler{
        public ViewHodler(View vi){
            ButterKnife.bind(this,vi);
        }
        @BindView(R.id.ele_TimeYear)TextView ele_TimeYear;//年份
        @BindView(R.id.ele_TimeMonth)TextView ele_TimeMonth;//月份
        @BindView(R.id.ele_KS)TextView ele_KS;//科室
        @BindView(R.id.ele_HosName)TextView ele_HosName;//医院
        @BindView(R.id.ele_info)TextView ele_info;//病情描述
    }
}
