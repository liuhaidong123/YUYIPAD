package com.technology.yuyipad.lzhUtils;

import android.app.Activity;
import android.os.Bundle;

import butterknife.Unbinder;

public class MyActivity extends Activity {
    public Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

//    @Override
//    public void setRequestedOrientation(int requestedOrientation) {
//        return;
//    }
}
