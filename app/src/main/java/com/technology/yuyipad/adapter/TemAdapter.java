package com.technology.yuyipad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.UserListBean.Result;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lhdUtils.RoundImageView;

/**
 * Created by liuhaidong on 2017/10/11.
 */

public class TemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContent;
    private List<Result> list = new ArrayList<>();
    private List<Boolean> mCheckList = new ArrayList<>();

    public TemAdapter(Context mContent, List<Result> list, List<Boolean> mCheckList) {
        this.mContent = mContent;
        this.list = list;
        this.mCheckList = mCheckList;
        mInflater=LayoutInflater.from(mContent);
    }

    public void setmCheckList(List<Boolean> mCheckList) {
        this.mCheckList = mCheckList;
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

        TemHolder temHolder = null;
        if (view == null) {
            temHolder = new TemHolder();
            view = mInflater.inflate(R.layout.tem_user_listview_item, null);
            temHolder.img = view.findViewById(R.id.tem_user_head_img);
            temHolder.name = view.findViewById(R.id.tem_user_name);
            temHolder.sanjiao = view.findViewById(R.id.sanjiao);
            view.setTag(temHolder);
        } else {
            temHolder = (TemHolder) view.getTag();
        }

        Picasso.with(mContent).load(UrlTools.BASE + list.get(i).getAvatar()).error(R.mipmap.usererr).into(temHolder.img);

        //点击了头像
        if (mCheckList.get(i)) {
            temHolder.name.setVisibility(View.VISIBLE);
            temHolder.name.setText(list.get(i).getTrueName());
            temHolder.sanjiao.setVisibility(View.VISIBLE);
        } else {
            temHolder.name.setVisibility(View.INVISIBLE);
            temHolder.name.setBackgroundResource(R.color.color_383838);
            temHolder.sanjiao.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    class TemHolder {
        LinearLayout name_ll;
        RoundImageView img;
        TextView name;
        ImageView sanjiao;
    }

}
