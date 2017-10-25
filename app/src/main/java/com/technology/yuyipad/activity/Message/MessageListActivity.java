package com.technology.yuyipad.activity.Message;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.technology.yuyipad.Lview.MyListView;
import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageListActivity extends MyActivity implements IMessage,MyListView.IonScrollBottomListener {
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @BindView(R.id.msgListiew)MyListView msgListiew;
    MessageListAdapter adapter;
    MessageListPresenter presenter;
    List<bean_MyMessage.RowsBean> list;
    int start=0;
    int limit=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_message_list);
        titleinclude_text.setText("消息");
        list=new ArrayList<>();
        adapter=new MessageListAdapter(this,list);
        msgListiew.setAdapter(adapter);
        msgListiew.setOnScrollBottomListener(this);
        presenter=new MessageListPresenter();
        presenter.getMessage(start,limit,this);
    }

   //只回传大于0的情况（listBean==null回传到onGetMessageError方法）
    @Override
    public void onGetMessageSuccess(List<bean_MyMessage.RowsBean> listBean) {
            list.addAll(listBean);
            adapter.notifyDataSetChanged();
            start+=listBean.size();
            if (listBean.size()==limit){//还有数据，可以继续加载更多
                msgListiew.setScroll(true);
            }
            else {//没有数据了
                msgListiew.setLoadingComplete();
            }
    }

    @Override
    public void onGetMessageError(String msg) {
        msgListiew.setLoadingComplete();
        msgListiew.setEmpey(msg);
    }

    @Override
    public void onScrollBottom() {
        Log.e("滚动到底部：","当前数据量："+start);
        presenter.getMessage(start,limit,this);
    }
}
