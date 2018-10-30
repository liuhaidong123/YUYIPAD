package com.technology.yuyipad.activity.Setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.Login.LoginActivity;
import com.technology.yuyipad.lzhUtils.MyActivity;
import com.technology.yuyipad.lzhUtils.MyApplication;
import com.technology.yuyipad.user.User;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends MyActivity {
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_setting);
        titleinclude_text.setText("设置");
    }
    @OnClick({R.id.setting_exit,R.id.setting_contactus,R.id.setting_feedback,R.id.setting_aboutus, R.id.setting_changeTelePhone})
    public void 事件(View view){
        switch (view.getId()){
            case R.id.setting_contactus://联系我们
                startActivity(new Intent(this,SettingContactActivity.class));
                break;
            case R.id.setting_feedback://意见反馈
                startActivity(new Intent(this,SettingFeedActivity.class));
                break;
            case R.id.setting_aboutus://关于我们
                startActivity(new Intent(this,SettingAboutActivity.class));
                break;
            case R.id.setting_changeTelePhone://修改绑定手机号
                startActivity(new Intent(this,Setting_ChangePhoneActivity.class));
                break;
            case R.id.setting_exit://退出
                exitApp();
                break;
        }
    }

    private void exitApp() {
        User.clearLogin(this);
        User.clearFuWu(this);
        MyApplication.removeActivity();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
