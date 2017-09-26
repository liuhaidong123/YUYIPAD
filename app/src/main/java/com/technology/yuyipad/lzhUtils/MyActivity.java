package com.technology.yuyipad.lzhUtils;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.technology.yuyipad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyActivity extends FragmentActivity {
    public Unbinder unbinder;
    RelativeLayout rela;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        rela=findViewById(R.id.base_rela);
    }
    public void setChilidView(int resId){
        View child= LayoutInflater.from(this).inflate(resId,null);
        rela.addView(child);
        unbinder=ButterKnife.bind(this,child);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        return;
    }
}
