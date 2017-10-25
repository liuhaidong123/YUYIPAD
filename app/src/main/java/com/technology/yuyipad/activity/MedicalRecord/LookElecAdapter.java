package com.technology.yuyipad.activity.MedicalRecord;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.R;


/**
 * Created by wanyu on 2017/8/21.
 */

public class LookElecAdapter extends BaseAdapter {
    Activity con;
    String[]url;
    public LookElecAdapter(Activity con, String[]url){
        this.con=con;
        this.url=url;
    }
    @Override
    public int getCount() {
        return url==null?0:url.length;
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
        ViewHodler hodler;
        if (convertView==null){
            convertView= LayoutInflater.from(con).inflate(R.layout.ele_item,null);
            hodler=new ViewHodler();
            hodler.imageView= (ImageView) convertView.findViewById(R.id.ele_image);
            convertView.setTag(hodler);
        }
        else {
            hodler= (ViewHodler) convertView.getTag();
        }
        Log.e("url--", Ip.imagePath+url[position]);
        Log.e("length--",url.length+"--");
        Picasso.with(con).load(Ip.imagePath+url[position]).placeholder(R.mipmap.errorpicture).error(R.mipmap.errorpicture).into(hodler.imageView);
        return convertView;
    }
    class ViewHodler{
        ImageView imageView;
    }
}
