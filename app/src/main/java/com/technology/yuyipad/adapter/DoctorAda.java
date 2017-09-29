package com.technology.yuyipad.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.SelectDoctor.DatenumberList;
import com.technology.yuyipad.httptools.UrlTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhaidong on 2017/9/29.
 */

public class DoctorAda extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DatenumberList> mListDoctor = new ArrayList<>();
    private boolean flag;

    public DoctorAda(Context mContext, List<DatenumberList> mListDoctor, boolean flag) {
        this.mContext = mContext;
        this.mListDoctor = mListDoctor;
        this.flag = flag;
        mInflater = LayoutInflater.from(this.mContext);

    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setmListDoctor(List<DatenumberList> mListDoctor) {
        this.mListDoctor = mListDoctor;
    }

    @Override
    public int getCount() {
        return mListDoctor.size();
    }

    @Override
    public Object getItem(int i) {
        return mListDoctor.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.register_doctor_item, null);
            viewHolder.register_rl = (RelativeLayout) view.findViewById(R.id.register_btn_rl);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.dortor_head_img);
            viewHolder.doc_name = (TextView) view.findViewById(R.id.doctor_name);
            viewHolder.doc_job = (TextView) view.findViewById(R.id.doctor_job);
            viewHolder.num_tv = (TextView) view.findViewById(R.id.yu_num);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Picasso.with(mContext).load(UrlTools.BASE + mListDoctor.get(i).getAvatar()).error(R.mipmap.usererr).into(viewHolder.imageView);
        viewHolder.doc_name.setText(mListDoctor.get(i).getTrueName());
        viewHolder.doc_job.setText(mListDoctor.get(i).getTitle());

        //上午
        if (flag) {
            Log.e("重新设置数据", "上午");
            viewHolder.num_tv.setText("" + mListDoctor.get(i).getBeforNum());
        }
        //下午
        if (!flag) {
            Log.e("重新设置数据", "下午");
            viewHolder.num_tv.setText("" + mListDoctor.get(i).getAfterNum());
        }
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView doc_name;
        TextView doc_job;
        TextView num_tv;
        RelativeLayout register_rl;
    }
}
