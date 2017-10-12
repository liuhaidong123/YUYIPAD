package com.technology.yuyipad.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.UserListBean.Result;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lhdUtils.RoundImageView;

/**
 * Created by liuhaidong on 2017/10/10.
 */

public class RecycleViewBlood extends RecyclerView.Adapter<RecycleViewBlood.BloodHolder> {
    private LayoutInflater mInflater;
    private Context mContent;
    private List<Result> list = new ArrayList<>();
    private List<Boolean> isFlagList = new ArrayList<>();

    public void setList(List<Result> list) {
        this.list = list;
    }

    public void setIsFlagList(List<Boolean> isFlagList) {
        this.isFlagList = isFlagList;
    }


    public RecycleViewBlood(Context mContent, List<Result> list, List<Boolean> isFlagList) {
        this.mContent = mContent;
        this.list = list;
        this.isFlagList = isFlagList;
        this.mInflater = LayoutInflater.from(mContent);
    }

    /**
     * ItemClick的回调接口
     *
     * @author zhy
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private RecycleAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(RecycleAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
        Log.e("实例化接口", "");
    }

    @Override
    public BloodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleview_blood_item, null);
        BloodHolder bloodHolder = new BloodHolder(view);
        return bloodHolder;
    }


    @Override
    public void onBindViewHolder(final BloodHolder holder, final int position) {
        Log.e("设置数据", "");
        if (list.size() == 0) {
            holder.imageView.setImageResource(R.mipmap.blood_add);
            holder.textView.setText("添加人员");
            holder.textView.setTextColor(ContextCompat.getColor(mContent, R.color.color_999999));
        } else {
            if (position == list.size()) {
                holder.imageView.setImageResource(R.mipmap.blood_add);
                holder.textView.setText("添加人员");
                holder.textView.setTextColor(ContextCompat.getColor(mContent, R.color.color_999999));
            } else {
                Picasso.with(mContent).load(UrlTools.BASE + list.get(position).getAvatar()).error(R.mipmap.usererr).into(holder.imageView);
                if (isFlagList.get(position)) {

                    holder.textView.setText(list.get(position).getTrueName());
                    holder.textView.setTextColor(ContextCompat.getColor(mContent, R.color.color_department_tv));
                } else {
                    holder.textView.setText(list.get(position).getTrueName());
                    holder.textView.setTextColor(ContextCompat.getColor(mContent, R.color.color_333333));
                }
            }
        }

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                    Log.e("点击回调", "");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size() + 1;
    }

    class BloodHolder extends RecyclerView.ViewHolder {
        RoundImageView imageView;
        TextView textView;

        public BloodHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.blood_user_head);
            textView = itemView.findViewById(R.id.blood_user_name);
        }
    }
}
