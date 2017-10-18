package com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.technology.yuyipad.Enum.UserSex;
import com.technology.yuyipad.PermissionCheck.PicturePhotoUtils;
import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Models.FamilyUserAddModel;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.PhoneUtils;
import com.technology.yuyipad.lzhUtils.PopupSettings;

import java.io.File;

/**
 * Created by wanyu on 2017/10/16.
 */

public class FamilyUserAddPresenter implements View.OnClickListener{
    FamilyUserAddModel model;
    Fragment activity;
    PopupWindow pop;
    File outImage;
    //弹窗选择
    public void showWindow(Fragment acs, File out){
        this.outImage=out;
        this.activity=acs;
        View vi= LayoutInflater.from(activity.getActivity()).inflate(R.layout.pop_photoselect,null);
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
        View parentView=activity.getActivity().findViewById(R.id.activity_family_user_list);
        PopupSettings.getInstance().showWindowCenter(activity.getActivity(),pop,vi,parentView);
        ViewGroup.LayoutParams param=usereditor_pop_layout.getLayoutParams();
        param.width=(int)(activity.getActivity().getWindowManager().getDefaultDisplay().getWidth()*0.7);
        usereditor_pop_layout.setLayoutParams(param);
    }

    //提交信息（关系，姓名，年龄，电话号，性别，头像，是否同意手机号查看,头像是否更换）
    public void onSubmit(String rela, String name, String age, String tele, UserSex sex, String bit64, boolean isBitChange,String id,IFamilyUserAdd iAdd){
        if (Empty.getInstance().notEmptyOrNull(rela)&&Empty.getInstance().notEmptyOrNull(name)&&Empty.getInstance().notEmptyOrNull(age)){//姓名，年龄，关系都正确
            if (Integer.parseInt(age)>0&&Integer.parseInt(age)<120){//大与0小与120
                if (isBitChange){//头像更换时
                    if (Empty.getInstance().notEmptyOrNull(bit64)){
                            if (Empty.getInstance().notEmptyOrNull(tele)){//手机号不为null时需要检查手机号的完整性
                                if (PhoneUtils.isPhone(tele)){
                                    submit(rela,name,age,tele,sex,bit64,isBitChange,id,iAdd);
                                }
                                else {
                                    iAdd.onError("手机号码不正确");
                                }
                            }
                        else {
                                submit(rela,name,age,tele,sex,bit64,isBitChange,id,iAdd);
                            }

                    }
                    else {
                        iAdd.onError("您还没有上传头像");
                    }
                }


                else {//头像未更换
                    if (Empty.getInstance().notEmptyOrNull(tele)){//手机号不为null时检查手机号是否正确
                        if (PhoneUtils.isPhone(tele)){
                            submit(rela,name,age,tele,sex,bit64,isBitChange,id,iAdd);
                        }
                        else {
                            iAdd.onError("手机号码不正确");
                        }
                    }
                    else{////手机号为null时不检查手机号是否正确
                        submit(rela,name,age,tele,sex,bit64,isBitChange,id,iAdd);
                    }
                }
            }
            else {//年龄大约120或者小与0
                iAdd.onError("输入的年龄不正确");
            }
        }

        else{//姓名，年龄，关系（有一个为null）
            iAdd.onError("信息不完整，请检查");
        }
    }
    //添加／修改信息
    private void submit(String rela,String name,String age,String tele, UserSex sex,String bit64,boolean isBitChange,String id,IFamilyUserAdd iAdd){
        if (model==null){
            model=new FamilyUserAddModel();
        }
        model.addFamilyUser(rela,name,age,tele,sex,bit64,isBitChange,id,iAdd);
    }

    public void deleteUser(String userId,IFamilyUserAdd iAdd){
        if (model==null){
            model=new FamilyUserAddModel();
        }
        if (Empty.getInstance().notEmptyOrNull(userId)){
            model.deleteUser(userId,iAdd);

        }
        else {
            iAdd.onError("删除失败，当前用户信息不存在");
        }
    }

    @Override
    public void onClick(View view) {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
        switch (view.getId()) {
            case R.id.usereditor_textv_picture://图库选取头像
                PicturePhotoUtils.getInstance().searchPhtoFrag(activity, outImage);
                break;
            case R.id.usereditor_textv_camera://拍照头像
                PicturePhotoUtils.getInstance().takePhotoFrag(activity, outImage);
                break;
            case R.id.usereditor_textv_cancle://取消
                break;
        }
    }
}
