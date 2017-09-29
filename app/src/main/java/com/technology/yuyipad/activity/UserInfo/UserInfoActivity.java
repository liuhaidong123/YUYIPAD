package com.technology.yuyipad.activity.UserInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.technology.yuyipad.Lview.MyListView;
import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//用户信息
public class UserInfoActivity extends MyActivity {
    @BindView(R.id.listviewa)MyListView listView;
    MyAdapter adapter;
    List<String> list;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_user_info);
        list=new ArrayList<>();
        addList();
        adapter=new MyAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnScrollBottomListener(new MyListView.IonScrollBottomListener() {
            @Override
            public void onScrollBottom() {
                if (count>20){
                    Log.e("111111","11111111");
//                    listView.setLoadingComplete();
                    list.clear();
                    adapter.notifyDataSetChanged();
                    listView.setEmpey("没有数据了");
                    return;
                }
                Log.e("2222222","2222222");
                listView.setScroll(true);
                addList();
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void addList() {
        for (int i=count;i<count+10;i++){
            list.add("当前："+i);
        }
        count+=10;
    }
}
