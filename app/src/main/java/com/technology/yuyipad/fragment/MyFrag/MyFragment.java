package com.technology.yuyipad.fragment.MyFrag;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Lview.RoundImageView;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.code.RSCode;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.StateBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment implements IUser{
    UserBean user;
    Unbinder unbinder;
    MyPresenter presenter;
    @BindView(R.id.frag_my_userImage)RoundImageView userImage;//头像
    @BindView(R.id.frag_my_userName)TextView frag_my_userName;//姓名
    @BindView(R.id.frag_my_userAge)TextView frag_my_userAge;//年龄
    @BindView(R.id.frag_my_userSexImage)TextView frag_my_userSexImage;//性别image：false女，ture男
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my, container, false);
        unbinder= ButterKnife.bind(this,view);
        presenter=new MyPresenter();
        presenter.getUserInfo(this);
        return view;
    }
    @OnClick({R.id.frag_my_userLayout})
    public void Click(View vi){
        switch (vi.getId()){
            case R.id.frag_my_userLayout:

                break;
        }
    }
    //view填充数据
    public void setViewDate(){
        if (user==null){
            return;
        }
    }

    //是否需要刷新用户信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RSCode.rCode){
            if (resultCode==RSCode.sCode){

            }
        }
    }
    //获取用户信息失败
    @Override
    public void onError(String msg,String interfaceName) {
        toast.getInstance().text(getActivity(),msg);
        String info= IDbUtlis.getInstance().getOkhttpString(getActivity(),interfaceName);//获取到数据库中保存的字符串信息
        if (Empty.getInstance().isEmptyOrNull(info)){
            user= gson.gson.fromJson(info,UserBean.class);
            setViewDate();
        }
    }
    //获取用户信息成功
    @Override
    public void onSuccess(UserBean userBean) {
        this.user=userBean;
        setViewDate();
    }
    //token失效
    @Override
    public void onErrorTokenOut() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
