package com.technology.yuyipad.activity.Setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.MyActivity;

import butterknife.BindView;

public class SettingAboutActivity extends MyActivity {
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_setting_about);
        titleinclude_text.setText("关于我们");
    }
}
