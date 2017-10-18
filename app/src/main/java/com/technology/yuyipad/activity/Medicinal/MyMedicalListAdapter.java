package com.technology.yuyipad.activity.Medicinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technology.yuyipad.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanyu on 2017/10/17.
 */
//我的药品列表适配器
public class MyMedicalListAdapter extends BaseAdapter{
    Context con;
    List<BeanDrugStates.ResultBean> list;
    public MyMedicalListAdapter(Context con, List<BeanDrugStates.ResultBean> list){
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
            view= LayoutInflater.from(con).inflate(R.layout.item_mydedical_list,null);
            hodler=new ViewHodler(view);
            view.setTag(hodler);
        }
        else {
            hodler= (ViewHodler) view.getTag();
        }
        try{
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=format.parse(list.get(i).getCreateTimeString());
            Calendar c=Calendar.getInstance();
            c.setTime(date);
            hodler.medical_list_text.setText(c.get(Calendar.YEAR)+""+(c.get(Calendar.MONTH)+1)+""+c.get(Calendar.DAY_OF_MONTH)+"-"+list.get(i).getTitle());
        }
        catch (Exception e){
            hodler.medical_list_text.setText(list.get(i).getCreateTimeString()+"-"+list.get(i).getTitle());
            e.printStackTrace();
        }
        if (list.get(i).isSelectMedical){
            hodler.medical_list_text.setSelected(true);
        }
        else {
            hodler.medical_list_text.setSelected(false);
        }
        return view;
    }
    class ViewHodler{
        public ViewHodler(View vi){
            ButterKnife.bind(this,vi);
        }
        @BindView(R.id.medical_list_text)TextView medical_list_text;
    }
}
