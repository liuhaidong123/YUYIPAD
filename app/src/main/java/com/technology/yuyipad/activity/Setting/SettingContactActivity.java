package com.technology.yuyipad.activity.Setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.MyActivity;

import butterknife.BindView;

public class SettingContactActivity extends MyActivity {
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    private String tele="0312-3850331";
    @BindView(R.id.my_settings_contactOur_callPhone)TextView my_settings_contactOur_callPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.content_setting_contact2);
        titleinclude_text.setText("联系我们");
    }

    //拨打电话的按钮
    public void callPhone(View view) {
        if (view!=null){
            if (view.getId()==R.id.my_settings_contactOur_callPhone){
                TelephonyManager manager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (manager==null){
                    Toast.makeText(this,"该设备不支持拨号功能",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    int stata=0;
                    switch(manager.getSimState()){ //getSimState()取得sim的状态  有下面6中状态
                        case TelephonyManager.SIM_STATE_ABSENT :
//                            sb.append("无卡");
                            stata=1;
                            break;
                        case TelephonyManager.SIM_STATE_UNKNOWN :
//                            sb.append("未知状态");
                            stata=1;
                            break;
                        case TelephonyManager.SIM_STATE_NETWORK_LOCKED :
//                            sb.append("需要NetworkPIN解锁");
                            stata=1;
                            break;
                        case TelephonyManager.SIM_STATE_PIN_REQUIRED :
//                            sb.append("需要PIN解锁");
                            stata=1;
                            break;
                        case TelephonyManager.SIM_STATE_PUK_REQUIRED :
//                            sb.append("需要PUK解锁");
                            stata=1;
                            break;
                        case TelephonyManager.SIM_STATE_READY :
//                            sb.append("良好");
                            stata=0;
                            break;
                    }
                    if (stata==1){
                        Toast.makeText(this,"该设备没有SIM卡或SIM异常，无法拨号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent intent = new Intent();//创建一个意图对象，用来激发拨号的Activity
                intent.setAction("android.intent.action.DIAL");//android.intent.action.CALL
                intent.setData(Uri.parse("tel:"+tele));
                startActivity(intent);//方法内部会自动添加类别,android.intent.category.DEFAULT
            }
        }
    }
}
