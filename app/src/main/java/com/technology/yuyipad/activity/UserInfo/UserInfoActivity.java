package com.technology.yuyipad.activity.UserInfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Enum.IntentValue;
import com.technology.yuyipad.Enum.UserSex;
import com.technology.yuyipad.Lview.RoundImageView;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Photo.PhotoPictureUtils;
import com.technology.yuyipad.Photo.PhotoRSCode;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.Main.MainActivity;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.code.RSCode;
import com.technology.yuyipad.lzhUtils.BitmapTobase64;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

//用户信息
public class UserInfoActivity extends MyActivity implements Iuser,IuserChange,PhotoPictureUtils.OnSavePictureListener {
    @BindView(R.id.userInfo_submit)TextView userInfo_submit;
    @BindView(R.id.userInfo_image)RoundImageView userInfo_image;//touxiang
    @BindView(R.id.userInfo_userName)EditText userInfo_userName;//用户名
    @BindView(R.id.userInfo_sex_women)ImageView userInfo_sex_women;//女性的选择按钮
    @BindView(R.id.userInfo_sex_man)ImageView userInfo_sex_man;//男性的select按钮
    @BindView(R.id.userInfo_userAge)EditText userInfo_userAge;//用户年龄
    UserBean user;//用户信息实体类
    String type="1";//0添加用户信息，1修改用户信息（默认是修改）
    UserInfoPresenter presenter;
    String bitmap64;
    File outImage;
    boolean isPhotoChange=false;//头像是否更换过
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_user_info);
        userInfo_sex_man.setSelected(false);
        userInfo_sex_women.setSelected(true);
        //性别默认女
        type=getIntent().getStringExtra("type");//
        presenter=new UserInfoPresenter();
        if (type.equals(IntentValue.UserInfoActivity_Change)){
            presenter.getUserDate(this);
        }
        else{
            isPhotoChange=true;//添加时必须修改用户头像
        }
    }
    @OnClick({R.id.userInfo_submit,R.id.userInfo_LayoutChangePhoto,R.id.userInfo_layout_sexWomen,R.id.userInfo_layout_sexMan})
    public void click(View view){
        switch (view.getId()){
            case R.id.userInfo_submit://提交
                if (IntentValue.UserInfoActivity_Change.equals(type)){
                    presenter.saveUserDate(this,bitmap64,userInfo_userName.getText().toString(),userInfo_userAge.getText().toString(),userInfo_sex_man.isSelected()?UserSex.BOY:UserSex.GIRL,isPhotoChange,this);
                }
                else {//新添加用户信息的时候bit64不能为空
                    if (Empty.getInstance().notEmptyOrNull(bitmap64)){
                        presenter.saveUserDate(this,bitmap64,userInfo_userName.getText().toString(),userInfo_userAge.getText().toString(),userInfo_sex_man.isSelected()?UserSex.BOY:UserSex.GIRL,isPhotoChange,this);
                    }
                    else {
                        toast.getInstance().text(this,"您还没有添加头像！");
                    }
                }
                break;
            case R.id.userInfo_LayoutChangePhoto://修改头像的按钮
                presenter.showWindow(this,outImage);
                break;
            case R.id.userInfo_layout_sexWomen://选择性别女
                userInfo_sex_man.setSelected(false);
                userInfo_sex_women.setSelected(true);
                break;
            case R.id.userInfo_layout_sexMan://选择性别男
                userInfo_sex_man.setSelected(true);
                userInfo_sex_women.setSelected(false);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== PhotoRSCode.requestCode_SearchPermission){//选取图片的权限请求
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                PhotoPictureUtils.getInstance().searchPicture(this);
            }
            else {
                Toast.makeText(this,"请打开存储卡权限！",Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode==PhotoRSCode.requestCode_CameraPermission){//拍照的权限请求
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                PhotoPictureUtils.getInstance().takePhoto(this);
            }
            else {
                Toast.makeText(this,"请打开相机权限！",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case PhotoRSCode.requestCode_Search://相册选取返回
                    PhotoPictureUtils.getInstance().savaPictureSearch(data.getData(),this,this);
                    break;
                case PhotoRSCode.requestCode_Camera://拍照
                    //cameraFile为保存后的文件，mImg：需要显示图片的ImageView
                    PhotoPictureUtils.getInstance().savaPictureCamera(this,this);
                    break;
            }
        }
    }
    //---------获取用户信息------
    @Override
    public void onError(String msg, String interfaceName) {
        toast.getInstance().text(this,msg);
        String resStr= IDbUtlis.getInstance().getOkhttpString(this,interfaceName);
        if (Empty.getInstance().notEmptyOrNull(resStr)){
            try{
                this.user= gson.gson.fromJson(resStr,UserBean.class);
                initUserDate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTokenError() {
        ExitLogin.getInstance().showLogin(this);
    }

    @Override
    public void onSuccess(UserBean user) {
        this.user=user;
        initUserDate();
    }
    //---------获取用户信息------


    //---------修改信息
    @Override
    public void onChangeSuccess() {
        if (IntentValue.UserInfoActivity_Change.equals(type)){
            Intent intent=new Intent(this, MainActivity.class);
            setResult(RSCode.sCode,intent);
            finish();
        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onChangeError(String msg) {
        toast.getInstance().text(this,"修改失败："+msg);
    }
    //---------修改信息

    //设置数据
    public void initUserDate(){
        if (user==null){
            return;
        }
        userInfo_userName.setText(user.getResult().getTrueName());
        Picasso.with(this).load(Ip.imagePath+user.getResult().getAvatar()).placeholder(R.mipmap.usererr).error(R.mipmap.usererr).into(userInfo_image);
        userInfo_userAge.setText(user.getResult().getAge()+"");
        int gender=user.getResult().getGender();
        if (UserSex.getUserSex(gender)==UserSex.BOY){
            userInfo_sex_women.setSelected(false);
            userInfo_sex_man.setSelected(true);
        }
        else if (UserSex.getUserSex(gender)==UserSex.GIRL){
            userInfo_sex_women.setSelected(true);
            userInfo_sex_man.setSelected(false);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            if (IntentValue.UserInfoActivity_Change.equals(type)){
                finish();
            }
            else if (IntentValue.UserInfoActivity_Add.equals(type)){
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
        }
        return false;
    }

    @Override
    public void onSavePicture(boolean isSuccess, File result) {
        if (isSuccess){
            isPhotoChange=true;
            bitmap64= BitmapTobase64.bitmapToBase64(BitmapFactory.decodeFile(result.getAbsolutePath()));
            userInfo_image.setImageBitmap(BitmapFactory.decodeFile(result.getAbsolutePath()));
        }
        else {
            toast.getInstance().text(this,"更换失败！");
        }
    }
}
