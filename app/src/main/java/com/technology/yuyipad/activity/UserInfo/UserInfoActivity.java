package com.technology.yuyipad.activity.UserInfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.technology.yuyipad.PermissionCheck.PicturePhotoUtils;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.Main.MainActivity;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.code.RSCode;
import com.technology.yuyipad.lzhUtils.BitmapTobase64;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

//用户信息
public class UserInfoActivity extends MyActivity implements Iuser,IuserChange{
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
                outImage= new File(getExternalFilesDir("DCIM").getAbsolutePath(), new Date().getTime() + ".jpg");
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RSCode.priCode_SearchPicture://图库
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    PicturePhotoUtils.getInstance().searchPhto(this,outImage);
                    }
                else {
                    toast.getInstance().text(this,"存储权限被禁用，无法获取相册信息");
                    }
                break;
            case RSCode.priCode_TakePhoto://拍照
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    PicturePhotoUtils.getInstance().takePhoto(this,outImage);
                    }
                else {
                    toast.getInstance().text(this,"请打开相机权限");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case RSCode.rCode_SearchPicture://浏览相册
                    outImage=new File(getExternalFilesDir("DCIM").getAbsolutePath(),new Date().getTime()+".jpg");
                    PicturePhotoUtils.getInstance().cutPhoto_Search(this,outImage,data);
                    break;
                case RSCode.rCode_TakePhoto://拍照
                    Uri uri=Uri.fromFile(outImage);
                    outImage=new File(getExternalFilesDir("DCIM").getAbsolutePath(),new Date().getTime()+".jpg");
                    PicturePhotoUtils.getInstance().cutPhoto_Camera(this,uri,outImage);
                    break;
                case RSCode.rCode_CutPicture://裁剪
                    try{
                        //将output_image.jpg对象解析成Bitmap对象，然后设置到ImageView中显示出来
                        Bitmap bitmap = BitmapFactory.decodeFile(outImage.getAbsolutePath());
                        if (bitmap!=null){
                            isPhotoChange=true;
                            userInfo_image.setImageBitmap(bitmap);
                            bitmap64= BitmapTobase64.bitmapToBase64(bitmap);
                        }
                        else {
                            Toast.makeText(this,"照片截取失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this,"照片截取失败",Toast.LENGTH_SHORT).show();
                    }
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
                startActivity(new Intent(this,MainActivity.class));
                finish();

            }
            else if (IntentValue.UserInfoActivity_Add.equals(type)){
                finish();
            }
        }
        return false;
    }
}
