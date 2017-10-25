package com.technology.yuyipad.activity.Setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.bean.BeanCode;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyActivity;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFeedActivity extends MyActivity {
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @BindView(R.id.my_settings_idea_editIdea)EditText my_settings_idea_editIdea;//意见
    @BindView(R.id.my_settings_idea_editContact)EditText my_settings_idea_editContact;//联系方式
    @BindView(R.id.settingFeed_submit)TextView settingFeed_submit;//提交
    @BindView(R.id.my_settings_idea_textNum)TextView my_settings_idea_textNum;//文字计数器
    boolean isClickable=true;
    private String resultStr;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isClickable=true;
                    toast.getInstance().text(SettingFeedActivity.this,"网络异常");
                    break;
                case 1:
                    isClickable=true;
                    try{
                        BeanCode feadus= gson.gson.fromJson(resultStr,BeanCode.class);
                        String code=feadus.getCode();
                        if ("-1".equals(code)){
                            ExitLogin.getInstance().showLogin(SettingFeedActivity.this);
                        }
                        else if ("0".equals(code)){
                            my_settings_idea_editIdea.setText("");
                            toast.getInstance().text(SettingFeedActivity.this,"提交成功");
                        }
                    }
                    catch (Exception e){
                        toast.getInstance().text(SettingFeedActivity.this,"数据异常");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.content_setting_contact);
        titleinclude_text.setText("意见反馈");
        my_settings_idea_editIdea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (Empty.getInstance().notEmptyOrNull(text)){
                    int length=text.length();
                    my_settings_idea_textNum.setText(length+"/200");
                }
                else {
                    my_settings_idea_textNum.setText("0/200");
                }
            }
        });
    }
        @OnClick(R.id.settingFeed_submit)
        public void 事件(){
                String msg=my_settings_idea_editIdea.getText().toString();
                String feed=my_settings_idea_editContact.getText().toString();
               if (Empty.getInstance().notEmptyOrNull(msg)){
                   if (Empty.getInstance().notEmptyOrNull(feed)){
                       submitIdea(msg,feed);
                   }
                   else {
                       toast.getInstance().text(this,"联系方式不能为空");
                    }
               }
            else {
                   toast.getInstance().text(this,"意见不能为空");
               }
        }

    //提交意见和联系方式
    public void submitIdea(String idea,String contact) {
                    if (isClickable){
                        isClickable=false;
                        Map<String,String> mp=new HashMap<>();
                        mp.put("content",idea);
                        mp.put("token", User.token);
                        mp.put("contact",contact);
                        ok.getCall(Ip.path+ Iport.interface_User_feedus,mp,ok.OK_GET).enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                handler.sendEmptyMessage(0);
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                resultStr=response.body().string();
                                handler.sendEmptyMessage(1);
                                Log.i("意见反馈返回的数据---",""+resultStr);
                            }
                        });
                    }
                     else {
                        toast.getInstance().text(this,"正在提交，请稍后。。。");
                    }

    }
}
