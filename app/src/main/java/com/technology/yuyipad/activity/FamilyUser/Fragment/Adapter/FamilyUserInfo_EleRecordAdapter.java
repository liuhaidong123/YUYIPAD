package com.technology.yuyipad.activity.FamilyUser.Fragment.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.Lview.MyGirdView;
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
 * Created by wanyu on 2017/10/20.
 */

public class FamilyUserInfo_EleRecordAdapter extends BaseAdapter{
    Context con;
    List<bean_MedicalRecordList.ResultBean> li;//数据源
    public FamilyUserInfo_EleRecordAdapter(Context con,List<bean_MedicalRecordList.ResultBean> li){
        this.con=con;
        this.li=li;
    }
    @Override
    public int getCount() {
        return li==null?0:li.size();
    }

    @Override
    public Object getItem(int i) {
        return li.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder hodler;
        if (view==null){
            view= LayoutInflater.from(con).inflate(R.layout.item_familyuser_ele,null);
            hodler=new ViewHolder(view);
            view.setTag(hodler);
        }else {
            hodler= (ViewHolder) view.getTag();
        }
        if (!"".equals(li.get(i).getCreateTimeString())&&!TextUtils.isEmpty(li.get(i).getCreateTimeString())){
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d= null;
            try {
                d = format.parse(li.get(i).getCreateTimeString());
                Calendar c=Calendar.getInstance();
                c.setTime(d);
                hodler.hospitalTime_year.setText(c.get(Calendar.YEAR)+"");
                hodler.hospitalTime_month.setText((c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                hodler.hospitalTime_year.setText("日期");
                hodler.hospitalTime_month.setText("");
                e.printStackTrace();
            }
        }
        hodler.hospicalKS.setText(li.get(i).getDepartmentName()+"／"+li.get(i).getPhysicianName());
        hodler.hospicalName.setText(li.get(i).getHospitalName());
        hodler.hospical_msgInfo.setText(li.get(i).getMedicalrecord());
        String url=li.get(i).getPicture();
        if (url!=null&&!"".equals(url)) {
            hodler.myGridView.setVisibility(View.VISIBLE);
            String[] str = url.split(";");
            LookElecAdapter adapter = new LookElecAdapter(con, str);
            hodler.myGridView.setAdapter(adapter);
        }
        else {
            hodler.myGridView.setVisibility(View.GONE);
        }
//        int count=i%3;
//        switch (count){
//            case 0:
//                String[] str0=new String[]{"2","3"};
//                LookElecAdapter adapter0=new LookElecAdapter(con,str0);
//                hodler.myGridView.setAdapter(adapter0);
//                break;
//            case 1:
//                String[] str1=new String[]{"2","3","4"};
//                LookElecAdapter adapter1=new LookElecAdapter(con,str1);
//                hodler.myGridView.setAdapter(adapter1);
//                break;
//            case 2:
//                String[] str2=new String[]{"2","3","4","5"};
//                LookElecAdapter adapter2=new LookElecAdapter(con,str2);
//                hodler.myGridView.setAdapter(adapter2);
//                break;
//        }

        return view;
    }

    class ViewHolder{
        public ViewHolder(View vi){
            ButterKnife.bind(this,vi);
        }
       @BindView(R.id.hospitalTime_year) TextView hospitalTime_year;//NIAN
        @BindView(R.id.hospitalTime_month) TextView hospitalTime_month;//yue
        @BindView(R.id.hospicalKS) TextView hospicalKS;//科室+医生名称
        @BindView(R.id.hospicalName) TextView hospicalName;//医院
        @BindView(R.id.hospical_msgInfo)TextView hospical_msgInfo;//病情描述
        @BindView(R.id.myGridView)MyGirdView myGridView;//图片
    }
}
