package com.technology.yuyipad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.httptools.UrlTools;


import java.util.ArrayList;
import java.util.List;

/**
 * 指定泛型是你自己的holder
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyRecycleHolder> {
    private List<String> mList = new ArrayList<>();
    private List<Boolean> isFlagList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;


    /**
     * ItemClick的回调接口
     *
     * @author zhy
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
        Log.e("实例化接口", "");
    }

    public void setIsFlagList(List<Boolean> isFlagList) {
        this.isFlagList = isFlagList;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    public RecycleAdapter(List<String> mList, List<Boolean> isFlagList, Context mContext) {
        this.mList = mList;
        this.isFlagList = isFlagList;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(this.mContext);
        Log.e("初始化Adapter", "");
    }

    //返回数据多少
    @Override
    public int getItemCount() {
        Log.e("返回数据多少", "");
        return mList.size();

    }

    //初始化viewholder,并且设置item布局
    @Override
    public RecycleAdapter.MyRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item, null);
        MyRecycleHolder myRecycleHolder = new MyRecycleHolder(view);
        Log.e("==", "初始化viewholder,并且设置item布局");
        return myRecycleHolder;
    }

    //设置数据
    @Override
    public void onBindViewHolder(final MyRecycleHolder holder, final int position) {
        Log.e("设置数据", "");

        holder.date.setText(mList.get(position));

        if (isFlagList.get(position)){
            holder.date.setBackgroundResource(R.color.color_eeeeee);
            holder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        }else {
            holder.date.setBackgroundResource(R.color.color_f6f6f6);
            holder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
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


    //初始化控件
    class MyRecycleHolder extends RecyclerView.ViewHolder {

        TextView date;

        public MyRecycleHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }

}
