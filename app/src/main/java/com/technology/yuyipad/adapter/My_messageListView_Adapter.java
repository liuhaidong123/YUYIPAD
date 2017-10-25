package com.technology.yuyipad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.UnReadMsgBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by wanyu on 2017/3/2.
 */

public class My_messageListView_Adapter extends BaseAdapter {
    private Context context;
    List<UnReadMsgBean.RowsBean> list;
    public My_messageListView_Adapter(Context context,List<UnReadMsgBean.RowsBean>list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        mp.put("image",imgId[i]+"");
//        mp.put("title",title[i]);
//        mp.put("time",time[i]);
//        mp.put("info",info[i]);
//        mp.put("type",type[i]);
        View view= LayoutInflater.from(context).inflate(R.layout.activity_my_message_listview_item1,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.my_message_listview_item_iamgeview);//公告图片
        TextView my_message_listview_item_msgName= (TextView) view.findViewById(R.id.my_message_listview_item_msgName);//公告名称
        TextView my_message_listview_item_time= (TextView) view.findViewById(R.id.my_message_listview_item_time);//公告时间
        TextView my_message_listview_item_msgInfo= (TextView) view.findViewById(R.id.my_message_listview_item_msgInfo);//公告内容
        int type=list.get(position).getMsgType();
        String title="";
        int resId;//图片id
        switch (type){
            case 1://公告
                title="宇医公告";
                resId=R.mipmap.msg1;
                break;
            case 2://挂号信息
                title="挂号通知";
                resId=R.mipmap.msg3;
                break;
            default:
                title="宇医消息";
                resId=R.mipmap.msg2;
                break;
        }
        imageView.setImageResource(resId);
        my_message_listview_item_msgName.setText(title);

        my_message_listview_item_msgInfo.setText(list.get(position).getContent());
        String data="";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d=format.parse(list.get(position).getCreateTimeString());
            SimpleDateFormat format2=new SimpleDateFormat("HH:mm");
            data=format2.format(d);
        } catch (ParseException e) {
            data=list.get(position).getCreateTimeString();
            e.printStackTrace();
        }
        my_message_listview_item_time.setText(data);
        return view;
    }
}
