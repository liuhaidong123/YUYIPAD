package com.technology.yuyipad.activity.FamilyUser.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.Enum.UserSex;
import com.technology.yuyipad.Lview.RoundImageView;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Photo.PhotoPictureUtils;
import com.technology.yuyipad.Photo.PhotoRSCode;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.FamilyUser.Bean.FamilyUserListBean;
import com.technology.yuyipad.activity.FamilyUser.FamilyUserManagerActivity;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter.FamilyUserAddPresenter;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter.IFamilyUserAdd;
import com.technology.yuyipad.lzhUtils.BitmapTobase64;
import com.technology.yuyipad.lzhUtils.DialogUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wanyu on 2017/10/12.
 */
//添加家庭用户的fragment
public class FamilyUserAddFragment extends Fragment implements IFamilyUserAdd,PhotoPictureUtils.OnSavePictureListener{
    FamilyUserAddPresenter presenter;
    Unbinder unbinder;
    @BindView(R.id.familyUserAdd_textV_Name)TextView familyUserAdd_textV_Name;//添加／编辑
    @BindView(R.id.familyUserAdd_textV_delete)TextView familyUserAdd_textV_delete;//删除的按钮
    @BindView(R.id.familyUserAdd_textV_changePhoto)TextView familyUserAdd_textV_changePhoto;//上传头像的文字说明
    @BindView(R.id.familyUserAdd_textV_UserImg)RoundImageView familyUserAdd_textV_UserImg;//头像
    @BindView(R.id.familyUserAdd_edit_rela)EditText familyUserAdd_edit_rela;//关系
    @BindView(R.id.familyUserAdd_edit_age)EditText familyUserAdd_edit_age;//年龄
    @BindView(R.id.familyUserAdd_edit_name)EditText familyUserAdd_edit_name;//姓名
    @BindView(R.id.familyUserAdd_edit_tele)EditText familyUserAdd_edit_tele;//手机号
    @BindView(R.id.familyUserAdd_image)ImageView familyUserAdd_image;//同意手机号查看账户的image
    @BindView(R.id.familyUserAdd_subimt)TextView familyUserAdd_subimt;//提交的按钮
    @BindView(R.id.add_family_group_sex)RadioGroup add_family_group_sex;
    @BindView(R.id.add_fami_boy)RadioButton add_fami_boy;
    @BindView(R.id.add_fami_gril)RadioButton add_fami_gril;
    FamilyUserListBean.ResultBean bean;//用户信息实体
    String bit64;//头像的bit64
    boolean isBitChange=false;//是否改变了头像（添加时默认为true，必须有值）
    UserSex sex=UserSex.GIRL;//性别，默认女
    File outImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi=inflater.inflate(R.layout.frag_familyuser_add,null);
        unbinder= ButterKnife.bind(this,vi);
        familyUserAdd_image.setSelected(true);//默认同意账户查看
        presenter=new FamilyUserAddPresenter();
        add_family_group_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.add_fami_boy://男1
                        sex=UserSex.BOY;
                        break;
                    case R.id.add_fami_gril://女0
                        sex=UserSex.GIRL;
                        break;
                }
            }
        });
        return vi;
    }
    @OnClick({R.id.familyUserAdd_textV_delete,R.id.familyUserAdd_image,R.id.familyUserAdd_subimt,R.id.familyUserAdd_textV_UserImg})
    public void 点击事件(View vi){
        switch (vi.getId()){
            case R.id.familyUserAdd_textV_delete://删除家庭用
                DialogUtils.showDialog();
                String id=bean.getId()+"";
                Log.e("id===",id);
                presenter.deleteUser(bean==null?"":bean.getId()+"",this);
                break;
            case R.id.familyUserAdd_image://是否同意手机号查看我的家庭成员信息
                familyUserAdd_image.setSelected(!familyUserAdd_image.isSelected());
                break;
            case R.id.familyUserAdd_subimt://确定按钮
                DialogUtils.showDialog();
                presenter.onSubmit(familyUserAdd_edit_rela.getText().toString(),familyUserAdd_edit_name.getText().toString(),familyUserAdd_edit_age.getText().toString()
                ,familyUserAdd_edit_tele.getText().toString(),sex,bit64,isBitChange,bean==null?"":bean.getId()+"",this);
                break;
            case R.id.familyUserAdd_textV_UserImg://上传头像
                presenter.showWindow(this,outImage);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== PhotoRSCode.requestCode_SearchPermission){//选取图片的权限请求
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                PhotoPictureUtils.getInstance().searchPictureFragment(this);
            }
            else {
                Toast.makeText(getActivity(),"请打开存储卡权限！",Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode==PhotoRSCode.requestCode_CameraPermission){//拍照的权限请求
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                PhotoPictureUtils.getInstance().takePhotoFragment(this);
            }
            else {
                Toast.makeText(getActivity(),"请打开相机权限！",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case PhotoRSCode.requestCode_Search://相册选取返回
                    PhotoPictureUtils.getInstance().savaPictureSearch(data.getData(),this,getActivity());
                    break;
                case PhotoRSCode.requestCode_Camera://拍照
                    //cameraFile为保存后的文件，mImg：需要显示图片的ImageView
                    PhotoPictureUtils.getInstance().savaPictureCamera(this,getActivity());
                    break;
            }
        }
    }

    //设置当前的用户信息
    public void setUserInfo(FamilyUserListBean.ResultBean bean){
            this.bean=bean;
            if (bean!=null){
                initView(1);//修改
            }
            else{
                initView(0);//添加
            }
    }
    //初始化view：tp=0添加用户，tp=1修改用户
    private void initView(int tp){
        switch (tp){
            case 0://添加
                familyUserAdd_textV_Name.setText("添加");
                familyUserAdd_textV_delete.setVisibility(View.GONE);
                familyUserAdd_subimt.setText("确定");
                familyUserAdd_textV_UserImg.setImageResource(R.mipmap.f1);
                familyUserAdd_textV_changePhoto.setVisibility(View.VISIBLE);//隐藏上传头像的文字说明
                familyUserAdd_edit_name.setText("");
                familyUserAdd_edit_age.setText("");
                familyUserAdd_edit_rela.setText("");
                familyUserAdd_edit_tele.setText("");
                add_fami_gril.setChecked(true);
                add_fami_boy.setChecked(false);
                sex=UserSex.GIRL;
                isBitChange=true;
                break;
            case 1://修改
                isBitChange=false;
                familyUserAdd_textV_Name.setText("编辑");
                familyUserAdd_textV_delete.setVisibility(View.VISIBLE);
                familyUserAdd_subimt.setText("保存");
                if (bean!=null){
                    familyUserAdd_edit_name.setText(bean.getTrueName());
                    familyUserAdd_edit_age.setText(bean.getAge()+"");
                    familyUserAdd_edit_rela.setText(bean.getNickName());
                    Picasso.with(getActivity()).load(Ip.imagePath+bean.getAvatar()).placeholder(R.mipmap.usererr).error(R.mipmap.usererr).into(familyUserAdd_textV_UserImg);
                    familyUserAdd_textV_changePhoto.setVisibility(View.GONE);//隐藏上传头像的文字说明
                    familyUserAdd_edit_tele.setText(bean.getTelephone()==0?"":bean.getTelephone()+"");
                    sex=UserSex.getUserSex(bean.getGender());
                    switch (sex){
                        case BOY:
                            add_fami_gril.setChecked(false);
                            add_fami_boy.setChecked(true);
                            break;
                        case GIRL:
                            add_fami_gril.setChecked(true);
                            add_fami_boy.setChecked(false);
                            break;
                    }
                }
                break;
        }
    }

    //添加修改成功／删除成功
    @Override
    public void onSuccess() {
        DialogUtils.stopDialog();
        FamilyUserManagerActivity ac= (FamilyUserManagerActivity) getActivity();
        ac.getData();//刷新数据
    }
    //添加／修改／删除失败
    @Override
    public void onError(String msg) {
        DialogUtils.stopDialog();
        toast.getInstance().text(getActivity(),msg);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public void onSavePicture(boolean isSuccess, File result) {
            if (isSuccess){
                isBitChange=true;
                Bitmap bt=BitmapFactory.decodeFile(result.getAbsolutePath());
                bit64= BitmapTobase64.bitmapToBase64(bt);
                familyUserAdd_textV_UserImg.setImageBitmap(bt);
            }
        else {
                toast.getInstance().text(getActivity(),"选取图片失败！");
            }
    }
}
