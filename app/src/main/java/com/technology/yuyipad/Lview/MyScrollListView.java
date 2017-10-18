package com.technology.yuyipad.Lview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by wanyu on 2017/10/17.
 */

public class MyScrollListView extends ListView implements AbsListView.OnScrollListener{
    int firstVisiblePos=0;//第一个可见的item
    IVisibleListener listener;
    ListAdapter adapter;
    int lastVisibleItem=0;//最后一个可见的item
    public MyScrollListView(Context context) {
        super(context);
    }

    public MyScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter=adapter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float startY=0f;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN://记录按下时的第一个可见的item以及按下的坐标
                lastVisibleItem=getLastVisiblePosition();
                startY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                float sy=ev.getY();
                float y=startY-sy;//差值
                if (y>100){
                    listener.visiable(lastVisibleItem);
                    }
                break;
        }
        return super.onTouchEvent(ev);
    }



    public void setIVisibleListener(IVisibleListener listener){
        if (adapter!=null&&adapter.getCount()>0){
            this.listener=listener;
            listener.visiable(0);
        }
        else {
            Log.e("MyScrollListView","您必须确保adapter的数据源个数大于0个");
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        firstVisiblePos=absListView.getFirstVisiblePosition();
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public interface IVisibleListener{
        void visiable(int pos);
    }
}
