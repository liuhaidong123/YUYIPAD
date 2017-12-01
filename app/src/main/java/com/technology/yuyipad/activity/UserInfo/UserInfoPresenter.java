package com.technology.yuyipad.activity.UserInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.technology.yuyipad.Enum.UserSex;
import com.technology.yuyipad.Photo.PhotoPictureUtils;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.PopupSettings;

import java.io.File;


/**
 * Created by wanyu on 2017/10/9.
 */

public class UserInfoPresenter implements View.OnClickListener{
    Activity activity;
    PopupWindow pop;
    UserInfoModel model;
    File outImage;
    public void getUserDate(Iuser iuser){
        if (model==null){
            model=new UserInfoModel();
        }
        model.getUserData(iuser);
    }
    //头像的bit64，用户名，年龄，性别，头像是否更换过
    public void saveUserDate(Activity ac, String bit64, String name, String age, UserSex sex, boolean isPhotoChange,IuserChange iuser){
        if (Empty.getInstance().notEmptyOrNull(name)&&Empty.getInstance().notEmptyOrNull(age)){
            if (Integer.parseInt(age)<1|Integer.parseInt(age)>120){
                toast.getInstance().text(ac,"年龄不正确");
                return;
            }
            if (model==null){
                model=new UserInfoModel();
            }
            model.saveUserDate(bit64,name,age,sex,isPhotoChange,iuser);
        }
        else {
            toast.getInstance().text(ac,"用户信息不完整");
        }
    }
    //弹窗选择
    public void showWindow(Activity acs,File out){
        this.outImage=out;
        this.activity=acs;
        View vi= LayoutInflater.from(activity).inflate(R.layout.pop_photoselect,null);
        LinearLayout usereditor_pop_layout= (LinearLayout) vi.findViewById(R.id.usereditor_pop_layout);
        TextView usereditor_textv_cancle= (TextView) vi.findViewById(R.id.usereditor_textv_cancle);
        TextView usereditor_textv_picture= (TextView) vi.findViewById(R.id.usereditor_textv_picture);
        TextView usereditor_textv_camera= (TextView) vi.findViewById(R.id.usereditor_textv_camera);
        usereditor_textv_cancle.setOnClickListener(this);
        usereditor_textv_camera.setOnClickListener(this);
        usereditor_textv_picture.setOnClickListener(this);
        if (pop==null){
            pop=new PopupWindow();
        }
        View parentView=activity.findViewById(R.id.activity_user_info);
        PopupSettings.getInstance().showWindowCenter(activity,pop,vi,parentView);
        ViewGroup.LayoutParams param=usereditor_pop_layout.getLayoutParams();
        param.width=(int)(activity.getWindowManager().getDefaultDisplay().getWidth()*0.7);
        usereditor_pop_layout.setLayoutParams(param);
    }

    @Override
    public void onClick(View view) {
        if (pop!=null&&pop.isShowing()){
            pop.dismiss();
        }
        switch (view.getId()){
            case R.id.usereditor_textv_picture://图库选取头像
                PhotoPictureUtils.getInstance().searchPicture(activity);
//                PicturePhotoUtils.getInstance().searchPhto(activity,outImage);
                break;
            case R.id.usereditor_textv_camera://拍照头像
                PhotoPictureUtils.getInstance().takePhoto(activity);
                break;
            case R.id.usereditor_textv_cancle://取消
                break;
        }
    }
}
