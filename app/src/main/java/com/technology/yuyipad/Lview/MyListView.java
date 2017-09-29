package com.technology.yuyipad.Lview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.technology.yuyipad.R;

import java.util.ArrayList;

/**
 * Created by wanyu on 2017/9/6.
 */
    //下拉到底部自动刷新到listview(调用方法setOnScrollBottomListener),
    // 特别注意1：listview外层必须加一层独立的layout包住listview（不然会显示异常，设置空数据时，页面可能无法占完，有空白）
    //注意2：如果litview中没有数据（adapter==null,或者adapter。getCount==0）时不会响应调用方法OnScrollBottomListener
    //注意3：listview的OnScrollBottomListener方法监听器响应一次后，需要二次调用setScroll()方法才会在下拉到底部时继续响应,否则只会响应一次下拉到底部的动作
    //注意4：不能给此listview添加其他的headview或者footview，不能与scrollow嵌套（无法响应OnScrollListener做出刷新操作）
public class MyListView extends ListView implements AbsListView.OnScrollListener{
    Context context;
    //空数据相关
    View emptyView;
    ViewGroup parentView;
    boolean canListenerScrollBottom=true;//是否能够在有监听器的情况下，下拉到底部时响应监听器OnScrollBottomListener（true响应，false不响应,默认true）
    TextView emptyTextView;

    //底部加载更多相关
    IonScrollBottomListener listener;
    View footerView;
    ProgressBar loading_progressBar;
    TextView loading_textView;
    ListAdapter adapter;
    int footerViewHeight;
    float startY;//按下时候的Y数值
    public MyListView(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }
    public void init(){
        footerView=LayoutInflater.from(context).inflate(R.layout.footerview,null,false);
        loading_progressBar= (ProgressBar) footerView.findViewById(R.id.loading_progressBar);
        loading_textView= (TextView) footerView.findViewById(R.id.loading_textView);
        addFooterView(footerView,null,false);
    }

    //空数据（每个页面的listview只能公用一个emptyView：相同listview的适配器只能是同一个，如果不同的话，至少他的emptyview是一摸一样的，包含emptyview的文字和图片都必须一致）
    public void setEmpey(String emText){
        if (this.getAdapter()!=null&&this.getAdapter().getCount()>0){
            if (this.getAdapter().getCount()==this.getFooterViewsCount()){
                removeFooterView(footerView);
            }
            else{
                return;
            }
        }
        if (this.getAdapter()==null){
            this.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,new ArrayList<String>()));
        }
        if (emptyView==null){
            emptyView= LayoutInflater.from(context).inflate(R.layout.headview,null);
            emptyView.setLayoutParams(new ViewGroup.LayoutParams(this.getMeasuredWidth(),this.getMeasuredHeight()));
            emptyTextView= (TextView) emptyView.findViewById(R.id.MyListView_HeadView_Text);
            emptyTextView.setText(emText);
            ((ViewGroup)getParent()).addView(emptyView);
            }
        setEmptyView(emptyView);
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter=adapter;
        setOnScrollListener(this);
    }
    //已经没有数据了
    public void setLoadingComplete(){
        canListenerScrollBottom=false;//不在响应底部监听
        loading_progressBar.setVisibility(GONE);
        loading_textView.setText("加载完毕");
    }
    //是否继续响应加载更多
    public void setScroll(boolean flag){
        canListenerScrollBottom=flag;
    }
    public void setOnScrollBottomListener(IonScrollBottomListener lis){
        this.listener=lis;
    }
    //设置不显示footView，到底部时自动刷新
    public void setFooterViewVisiable(boolean visi){
        if (!visi){
            removeFooterView(footerView);
            }
        else {
            if (getFooterViewsCount()==0){
                addFooterView(footerView);
            }
        }
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.e("onScrollStateChanged:","state=="+scrollState);
                if (scrollState==OnScrollListener.SCROLL_STATE_IDLE){
//                    Log.e("onScrollStateChanged:","state=="+"停止");
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {//当前显示的是最后一个
                        if (listener!=null){
                            if (canListenerScrollBottom){//响应一次后不在响应，需要调用setScroll()才能在下拉到底部时继续响应
                                canListenerScrollBottom=false;
                                listener.onScrollBottom();
                            }

                        }
                    }
                }
            }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }
    public interface IonScrollBottomListener{
        void onScrollBottom();//
    }
}
