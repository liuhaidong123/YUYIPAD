package com.technology.yuyipad.activity.Start;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.Login.LoginActivity;
import com.technology.yuyipad.activity.Main.MainActivity;
import com.technology.yuyipad.lzhUtils.MyActivity;
import com.technology.yuyipad.user.User;

public class StartActivity extends MyActivity {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                Intent intent=new Intent();
                if (User.isLogin(StartActivity.this)){
                    intent.setClass(StartActivity.this,MainActivity.class);
                }else {
                    intent.setClass(StartActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
