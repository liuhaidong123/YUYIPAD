package com.technology.yuyipad.activity.UserInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.util.List;

/**
 * Created by wanyu on 2017/9/28.
 */

public class MyAdapter extends BaseAdapter{
    Context con;
    List<String> li;
    public MyAdapter(Context con, List<String> li){
        this.con=con;
        this.li=li;
    }
    @Override
    public int getCount() {
        return li.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi= LayoutInflater.from(con).inflate(R.layout.listite,null);
        TextView text=vi.findViewById(R.id.text);
        text.setText(li.get(i));
        return vi;
    }
}
