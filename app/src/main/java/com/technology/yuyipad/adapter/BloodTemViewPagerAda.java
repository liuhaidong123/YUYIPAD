package com.technology.yuyipad.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.technology.yuyipad.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/9/27.
 */

public class BloodTemViewPagerAda extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<View> mList=new ArrayList<>();//血压图和体温图


    public BloodTemViewPagerAda(List<View> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        this.mInflater=LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.blood_and_tem_view, null);
        ((RelativeLayout) view.findViewById(R.id.relative_blood_tem)).addView(mList.get(position));
        ((ViewPager) container).addView(view);
        return view;
    }
}
