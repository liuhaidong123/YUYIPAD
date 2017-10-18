package com.technology.yuyipad.fragment.MyFrag;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Enum.IntentValue;
import com.technology.yuyipad.Enum.UserSex;
import com.technology.yuyipad.Lview.RoundImageView;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.R;
import com.technology.yuyipad.RongUtils.IGetRongDoctorId;
import com.technology.yuyipad.RongUtils.IGetRongUserTokenError;
import com.technology.yuyipad.RongUtils.RongConnect;
import com.technology.yuyipad.RongUtils.RongUser;
import com.technology.yuyipad.RongUtils.RongUserInfoProvider;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.FamilyUser.FamilyUserManagerActivity;
import com.technology.yuyipad.activity.UserInfo.UserInfoActivity;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.code.RSCode;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.StateBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.callkit.RongCallKit;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment implements IUser,IGetRongDoctorId,IGetRongUserTokenError{
    int RongConnectCount=1;//请求医生信息的次数
    int RongServerConnectCount=1;//链接容云服务器的次数
    String targetIds="";//医生id
    UserBean user;
    Unbinder unbinder;
    MyPresenter presenter;
//    @BindView(R.id.frag_my_userLayout)RelativeLayout frag_my_userLayout;
    @BindView(R.id.frag_my_userImage)RoundImageView userImage;//头像
    @BindView(R.id.frag_my_userName)TextView frag_my_userName;//姓名
    @BindView(R.id.frag_my_userAge)TextView frag_my_userAge;//年龄
    @BindView(R.id.frag_my_userSexImage)ImageView frag_my_userSexImage;//性别image：false女，ture男

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongConnect.getInstance().getTargetDocId(this,"1");
        if (RongUser.isConnectSuccess==false){//没有链接容云服务器时
            RongConnect.getInstance().getRongUserInfo(this,getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my, container, false);
        unbinder= ButterKnife.bind(this,view);
        presenter=new MyPresenter();
        presenter.getUserInfo(this);
        return view;
    }
    @OnClick({R.id.frag_my_userLayout,R.id.frag_my_layout_medical,R.id.frag_my_layout_message,
            R.id.frag_my_layout_member,R.id.frag_my_layout_device,R.id.frag_my_layout_setting})
    public void Click(View vi){

        switch (vi.getId()){
            case R.id.frag_my_userLayout://用户信息
                Intent intent=new Intent();
                intent.setClass(getActivity(), UserInfoActivity.class);
                //1修改0添加
                intent.putExtra("type", IntentValue.UserInfoActivity_Change);
                startActivityForResult(intent,RSCode.rCode);
                break;
            case R.id.frag_my_layout_medical://电子病历

                break;
            case R.id.frag_my_layout_message://消息

                break;
            case R.id.frag_my_layout_member://家庭成员管理
                startActivity(new Intent(getActivity(), FamilyUserManagerActivity.class));
                break;
            case R.id.frag_my_layout_device://用户设备管理

                break;
            case R.id.frag_my_layout_setting://设置

                break;
        }

    }
    //view填充数据
    public void setViewDate(){
        if (user==null){
            return;
        }
        Picasso.with(getActivity()).load(Ip.imagePath+user.getResult().getAvatar()).placeholder(R.mipmap.usererr).error(R.mipmap.usererr).into(userImage);
        frag_my_userName.setText(user.getResult().getTrueName());
        frag_my_userAge.setText(user.getResult().getAge()+"岁");
        frag_my_userSexImage.setSelected(UserSex.getUserSex(user.getResult().getGender())==UserSex.BOY);
    }

    //是否需要刷新用户信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RSCode.rCode){
            if (resultCode==RSCode.sCode){
                presenter.getUserInfo(this);
            }
        }
    }
    //获取用户信息失败
    @Override
    public void onError(String msg,String interfaceName){
        toast.getInstance().text(getActivity(),msg);
        String info= IDbUtlis.getInstance().getOkhttpString(getActivity(),interfaceName);//获取到数据库中保存的字符串信息
        if (Empty.getInstance().notEmptyOrNull(info)){
            user= gson.gson.fromJson(info,UserBean.class);
            setViewDate();
        }
    }
    //获取用户信息成功
    @Override
    public void onSuccess(UserBean userBean){
        this.user=userBean;
        setViewDate();
    }
    //token失效
    @Override
    public void onErrorTokenOut() {
        ExitLogin.getInstance().showLogin(getActivity());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
//获取医生信息失败
    @Override
    public void onError(String msg) {
        if (RongConnectCount==1){
            RongConnectCount=2;
            RongConnect.getInstance().getTargetDocId(this,"1");
        }
        else {
            toast.getInstance().text(getActivity(),msg);
        }
    }

    @Override
    public void onSuccess(String targetId) {
        targetIds=targetId;
        RongUserInfoProvider.getInstance().setUserInfo(targetIds,new UserInfo(targetIds,"医生", Uri.parse("http://a3.qpic.cn/psb?/V10dl1Mt1s0RoL/qvT5ZwDSegULprXup78nlo3*XNUqCRH8shghIkAnQTs!/b/dLMAAAAAAAAA&bo=ewJ7AgAAAAADByI!&rf=viewer_4")));
    }
    //链接容云服务器失败时（在MainActivity中链接，此页面只是检查是否链接成功，失败时才会重新链接）
    @Override
    public void onTokenError(String message) {
        if (RongServerConnectCount==1){//重新链接容云服务器
            RongConnect.getInstance().getRongUserInfo(this,getActivity());
            RongServerConnectCount=2;
        }
        else {
            toast.getInstance().text(getActivity(),message);
            }
    }
}
