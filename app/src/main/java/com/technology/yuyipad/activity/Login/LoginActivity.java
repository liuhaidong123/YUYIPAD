package com.technology.yuyipad.activity.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.yuyipad.Enum.IntentValue;
import com.technology.yuyipad.JPushUtils.JpRegister;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.Main.MainActivity;
import com.technology.yuyipad.activity.UserInfo.UserInfoActivity;
import com.technology.yuyipad.bean.LoginSuccess;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyActivity;
import com.technology.yuyipad.lzhUtils.MyToast;
import com.technology.yuyipad.user.User;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends MyActivity implements ILogin{
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @BindView(R.id.my_userlogin_edit_name)EditText my_userlogin_edit_name;//手机号输入框
    @BindView(R.id.my_userlogin_edit_smdCode)EditText my_userlogin_edit_smdCode;//验证码输入框
    @BindView(R.id.my_userlogin_getSMScode)TextView my_userlogin_getSMScode;//获取验证码按钮
    @BindView(R.id.my_userlogin_logninButton)TextView my_userlogin_logninButton;
    LoginPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_login);
        titleinclude_text.setText("登录");
        presenter=new LoginPresenter();
    }
    @OnClick({R.id.my_userlogin_logninButton,R.id.my_userlogin_getSMScode})
    public void 点击事件(View vi){
        switch (vi.getId()){
            case R.id.my_userlogin_getSMScode://获取验证码
                my_userlogin_getSMScode.setClickable(false);
                my_userlogin_getSMScode.setBackground(getResources().getDrawable(R.drawable.my_userlogin_unclick));
                presenter.getSmsCode(my_userlogin_edit_name.getText().toString(),this);
                break;
            case R.id.my_userlogin_logninButton://登录
                my_userlogin_logninButton.setClickable(false);
                presenter.onLogin(my_userlogin_edit_name.getText().toString(),my_userlogin_edit_smdCode.getText().toString(),this);
                break;
        }
    }

    @Override
    public void getSMSCodeSuccess() {
        presenter.onTimer();
    }

    @Override
    public void getSMSCodeError(String msg) {
        MyToast.toast(this,msg);
        my_userlogin_getSMScode.setBackground(getResources().getDrawable(R.drawable.btnlogin));
        my_userlogin_getSMScode.setClickable(true);
        my_userlogin_getSMScode.setText("发送验证码");
    }
    //验证码计时
    @Override
    public void getTimeOut(int count) {
        if (my_userlogin_getSMScode==null){
            return;
        }
        if (count>0){
            my_userlogin_getSMScode.setText("剩余 "+count+"s");
        }
        else if (count==0){
            my_userlogin_getSMScode.setText("发送验证码");
        }
        else {
            my_userlogin_getSMScode.setClickable(true);
            my_userlogin_getSMScode.setBackground(getResources().getDrawable(R.drawable.btnlogin));
        }
    }

    @Override
    public void onLoginSuccess(LoginBean bean) {
        //        激光注册标签
        presenter.stopTimer();
        MyToast.toast(this,"登录成功");
        JpRegister.getInstance().setAlias(this,bean.getPersonal().getId()+"");
        User.saveLogin(this,bean.getResult(),bean.getPersonal().getId()+"");
        Intent intent = new Intent();
        intent.putExtra("type", IntentValue.UserInfoActivity_Add);
        LoginBean.PersonalBean be=bean.getPersonal();
        if (be!=null){
            try{
                //当前用户不是新用户
                if (Empty.getInstance().notEmptyOrNull(be.getTrueName())&&Empty.getInstance().notEmptyOrNull(be.getGender()+"")&&!"0".equals(be.getAge()+"")){
                    intent.setClass(this, MainActivity.class);
                }
                else {
                    intent.setClass(this,UserInfoActivity.class);
                }
            }
            catch (Exception e){
                intent.setClass(this, UserInfoActivity.class);
            }

        }
        else {
            //d点击登录注释
            intent.setClass(this, UserInfoActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String msg) {
        MyToast.toast(this,msg);
        my_userlogin_logninButton.setClickable(true);
    }
}
