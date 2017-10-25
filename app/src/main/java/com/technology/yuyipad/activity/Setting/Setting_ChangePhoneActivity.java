package com.technology.yuyipad.activity.Setting;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.Setting.Bean.BeanChangePhone;
import com.technology.yuyipad.activity.Setting.Presenter.ChangePhonePresenter;
import com.technology.yuyipad.lzhUtils.MyActivity;
import com.technology.yuyipad.user.User;

import butterknife.BindView;
import butterknife.OnClick;

public class Setting_ChangePhoneActivity extends MyActivity implements ChangePhonePresenter.IChangePhone{
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @BindView(R.id.settingChangePhone_teleNum)TextView settingChangePhone_teleNum;//显示当前手机号
    @BindView(R.id.settingChangePhone_newTeleNum)EditText settingChangePhone_newTeleNum;//新手机号输入框
    @BindView(R.id.settingChangePhone_smsCode)EditText settingChangePhone_smsCode;//验证码输入框
    @BindView(R.id.settingChangePhone_btn)TextView settingChangePhone_btn;//发送验证码
    @BindView(R.id.settingChangePhone_submit)TextView settingChangePhone_submit;
    ChangePhonePresenter phonePresenter=new ChangePhonePresenter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_setting__change_phone);
        titleinclude_text.setText("修改绑定手机号");
        settingChangePhone_teleNum.setText("当前绑定手机号："+User.tele);
    }
    @OnClick({R.id.settingChangePhone_submit,R.id.settingChangePhone_btn})
    public void click(View vi){
        switch (vi.getId()){
            case R.id.settingChangePhone_btn://获取验证码
                settingChangePhone_btn.setClickable(false);
                settingChangePhone_btn.setBackground(getResources().getDrawable(R.drawable.editrightclick));
                phonePresenter.getSmSCode(settingChangePhone_newTeleNum.getText().toString(),this);
                break;
            case R.id.settingChangePhone_submit://提交修改
                settingChangePhone_submit.setClickable(false);
                phonePresenter.onChangePhone(settingChangePhone_newTeleNum.getText().toString(),settingChangePhone_smsCode.getText().toString(),this);
                break;
        }
    }

    @Override
    public void onGetSmSSuccess() {
        phonePresenter.onTimer();
    }

    @Override
    public void getTimeOut(int count) {
        if (settingChangePhone_btn==null){
            return;
        }
        if (count>0){
            settingChangePhone_btn.setText("剩余 "+count+"s");
        }
        else if (count==0){
            settingChangePhone_btn.setText("发送验证码");
        }
        else {
            settingChangePhone_btn.setBackground(getResources().getDrawable(R.drawable.editright));
            settingChangePhone_btn.setClickable(true);
            }
    }

    @Override
    public void onGetSmSError(String msg) {
        toast.getInstance().text(this,msg);
        settingChangePhone_btn.setText("发送验证码");
        settingChangePhone_btn.setClickable(true);
        settingChangePhone_btn.setBackground(getResources().getDrawable(R.drawable.editright));
    }

    @Override
    public void onChangeSuccess(String newPhone, BeanChangePhone bean) {
        toast.getInstance().text(this,"修改成功");
        settingChangePhone_newTeleNum.setText("");
        settingChangePhone_smsCode.setText("");
        settingChangePhone_submit.setClickable(true);
        User.tele=newPhone;
        Log.i("更改绑定手机号：",User.token.equals(bean.getResult())+"==bean1==="+bean.getResult()+"==User2==="+User.token);
        User.saveLogin(this,bean.getResult(),bean.getPersonal().getId()+"");
        User.removeJPSH(this);
        settingChangePhone_teleNum.setText("当前绑定手机号："+newPhone);
    }

    @Override
    public void onChangeError(String msg) {
        settingChangePhone_submit.setClickable(true);
        toast.getInstance().text(this,msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (phonePresenter!=null){
            phonePresenter.stopTimer();
        }
    }
}
