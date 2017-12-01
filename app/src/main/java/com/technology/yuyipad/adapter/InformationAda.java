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
import com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Rows;
import com.technology.yuyipad.httptools.UrlTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/9/27.
 */

public class InformationAda extends BaseAdapter {
    private LayoutInflater mInfllater;
    private Context mContext;
    private List<Boolean> checkList = new ArrayList<>();
    private List<com.technology.yuyipad.bean.NewInformationList.Rows> list = new ArrayList<>();


    public InformationAda(Context mContext, List<com.technology.yuyipad.bean.NewInformationList.Rows> list) {
        this.mContext = mContext;
        this.list = list;
        this.mInfllater = LayoutInflater.from(this.mContext);
    }

    public void setList(List<com.technology.yuyipad.bean.NewInformationList.Rows> list, List<Boolean> checkList) {
        this.list = list;
        this.checkList = checkList;
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
        InformationHolder informationHolder = null;
        if (view == null) {
            informationHolder = new InformationHolder();
            view = mInfllater.inflate(R.layout.information_listview_item, null);
            informationHolder.img = view.findViewById(R.id.infor_img_mess);
            informationHolder.title = view.findViewById(R.id.infor_tv_title);
            view.setTag(informationHolder);
        } else {
            informationHolder = (InformationHolder) view.getTag();
        }

        Picasso.with(mContext).load(UrlTools.BASE + list.get(i).getPicture()).error(R.mipmap.errorpicture).into(informationHolder.img);
        informationHolder.title.setText(list.get(i).getTitle());

        if (checkList.get(i)) {
            view.setBackgroundResource(R.color.color_click_bg);
        } else {
            view.setBackgroundResource(R.color.ffffff);
        }
        return view;
    }

    class InformationHolder {
        ImageView img;
        TextView title;
    }
}
