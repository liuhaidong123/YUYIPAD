package com.technology.yuyipad.activity.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.technology.yuyipad.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanyu on 2017/10/20.
 */

public class MessageListAdapter extends BaseAdapter{
    Context con;
    List<bean_MyMessage.RowsBean> list;
    public  MessageListAdapter(Context con, List<bean_MyMessage.RowsBean> list){
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
            view= LayoutInflater.from(con).inflate(R.layout.messagelistitem,null);
            hodler=new ViewHodler(view);
            view.setTag(hodler);
        }
        else {
            hodler= (ViewHodler) view.getTag();
        }
        int resId;//图片id
        String title="";
        int type=list.get(i).getMsgType();
        String msgTitle="";
        switch (type){
            case 1://公告
                title="宇医公告";
                resId=R.mipmap.msg1;
                msgTitle="重要通知";
                break;
            case 2://挂号信息
                title="挂号通知";
                msgTitle="挂号信息";
                resId=R.mipmap.msg3;
                break;
            default:
                title="宇医消息";
                msgTitle="重要通知";
                resId=R.mipmap.msg2;
                break;
        }
        hodler.messageListitem_image.setImageResource(resId);
        hodler.messageListitem_title.setText(title);
        hodler.messageListitem_info.setText(msgTitle);
        hodler.messageListitem_msg.setText(list.get(i).getContent());
        String data="";
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d=format.parse(list.get(i).getCreateTimeString());
            SimpleDateFormat format2=new SimpleDateFormat("HH:mm");
            data=format2.format(d);
        } catch (ParseException e) {
            data=list.get(i).getCreateTimeString();
            e.printStackTrace();
        }
        hodler.messageListitem_time.setText(data);
        return view;
    }

    class ViewHodler{
        public ViewHodler(View vi){
            ButterKnife.bind(this,vi);
        }
        @BindView(R.id.messageListitem_image)ImageView messageListitem_image;//图片
        @BindView(R.id.messageListitem_title)TextView messageListitem_title;//所属类型
        @BindView(R.id.messageListitem_time)TextView messageListitem_time;//时间
        @BindView(R.id.messageListitem_info)TextView messageListitem_info;//内容标题
        @BindView(R.id.messageListitem_msg)TextView messageListitem_msg;//内容
    }
}
