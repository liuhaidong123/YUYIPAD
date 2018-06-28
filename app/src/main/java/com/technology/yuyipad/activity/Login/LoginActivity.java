package com.technology.yuyipad.activity.Login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Enum.IntentValue;
import com.technology.yuyipad.JPushUtils.JpRegister;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.Main.MainActivity;
import com.technology.yuyipad.activity.UserInfo.UserInfoActivity;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyActivity;
import com.technology.yuyipad.lzhUtils.MyToast;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends MyActivity implements ILogin {
    @BindView(R.id.titleinclude_text)
    TextView titleinclude_text;
    @BindView(R.id.my_userlogin_edit_name)
    EditText my_userlogin_edit_name;//手机号输入框
    @BindView(R.id.my_userlogin_edit_smdCode)
    EditText my_userlogin_edit_smdCode;//验证码输入框
    @BindView(R.id.my_userlogin_getSMScode)
    TextView my_userlogin_getSMScode;//获取验证码按钮
    @BindView(R.id.my_userlogin_logninButton)
    TextView my_userlogin_logninButton;
    LoginPresenter presenter;


    private EditText mMyStatus_Num;
    private ImageView mMyStatus_Img;
    private long mCurrentMillis;
    private String myCooike = "-null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_login);
        titleinclude_text.setText("登录");
        presenter = new LoginPresenter();
        mCurrentMillis = System.currentTimeMillis();
        mMyStatus_Num = findViewById(R.id.my_status_num_edit);
        mMyStatus_Img = findViewById(R.id.my_status_num_img);
        //获取动态验证码
        getDynamicNumAndCookie();
        //重新获取动态验证码
        mMyStatus_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentMillis = System.currentTimeMillis();
                getDynamicNumAndCookie();
            }
        });

    }

    @OnClick({R.id.my_userlogin_logninButton, R.id.my_userlogin_getSMScode})
    public void 点击事件(View vi) {
        switch (vi.getId()) {
            case R.id.my_userlogin_getSMScode://获取验证码
                my_userlogin_getSMScode.setClickable(false);
                my_userlogin_getSMScode.setBackground(getResources().getDrawable(R.drawable.my_userlogin_unclick));
                 presenter.getSmsCode(my_userlogin_edit_name.getText().toString(), String.valueOf(mCurrentMillis), mMyStatus_Num.getText().toString(), myCooike, this);
                break;
            case R.id.my_userlogin_logninButton://登录
                my_userlogin_logninButton.setClickable(false);
                presenter.onLogin(my_userlogin_edit_name.getText().toString(), my_userlogin_edit_smdCode.getText().toString(), this);
                break;
        }
    }

    @Override
    public void getSMSCodeSuccess() {
        presenter.onTimer();
    }

    @Override
    public void getSMSCodeError(String msg) {
        MyToast.toast(this, msg);
        my_userlogin_getSMScode.setBackground(getResources().getDrawable(R.drawable.btnlogin));
        my_userlogin_getSMScode.setClickable(true);
        my_userlogin_getSMScode.setText("发送验证码");
    }

    //验证码计时
    @Override
    public void getTimeOut(int count) {
        if (my_userlogin_getSMScode == null) {
            return;
        }
        if (count > 0) {
            my_userlogin_getSMScode.setText("剩余 " + count + "s");
        } else if (count == 0) {
            my_userlogin_getSMScode.setText("发送验证码");
        } else {
            my_userlogin_getSMScode.setClickable(true);
            my_userlogin_getSMScode.setBackground(getResources().getDrawable(R.drawable.btnlogin));
        }
    }

    @Override
    public void onLoginSuccess(LoginBean bean) {
        //        激光注册标签
        presenter.stopTimer();
        MyToast.toast(this, "登录成功");
        JpRegister.getInstance().setAlias(this, bean.getPersonal().getId() + "");
        User.saveLogin(this, bean.getResult(), bean.getPersonal().getId() + "");
        Intent intent = new Intent();
        intent.putExtra("type", IntentValue.UserInfoActivity_Add);
        LoginBean.PersonalBean be = bean.getPersonal();
        if (be != null) {
            try {
                //当前用户不是新用户
                if (Empty.getInstance().notEmptyOrNull(be.getTrueName()) && Empty.getInstance().notEmptyOrNull(be.getGender() + "") && !"0".equals(be.getAge() + "")) {
                    intent.setClass(this, MainActivity.class);
                } else {
                    intent.setClass(this, UserInfoActivity.class);
                }
            } catch (Exception e) {
                intent.setClass(this, UserInfoActivity.class);
            }

        } else {
            //d点击登录注释
            intent.setClass(this, UserInfoActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String msg) {
        MyToast.toast(this, msg);
        my_userlogin_logninButton.setClickable(true);
    }

    /**
     * 获取动态验证码以及cookie
     */
    public void getDynamicNumAndCookie() {

        Call call = ok.getCall(UrlTools.BASE + UrlTools.URL_GET_DYNAMIC_NUM + "ts=" + mCurrentMillis, null, 0);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("获取动态验证码错误", e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    myCooike = response.headers().get("Set-Cookie");
                    //Log.e("动态验证码myCooike=", myCooike);
                    InputStream inputStream = response.body().byteStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap!=null){
                                mMyStatus_Img.setImageBitmap(bitmap);
                            }else {
                                mMyStatus_Img.setBackgroundResource(R.color.color_cecece);
                            }

                        }
                    });
                } else {
                    Log.e("onResponse--", "获取动态验证码错误");
                }

            }
        });
    }
}
