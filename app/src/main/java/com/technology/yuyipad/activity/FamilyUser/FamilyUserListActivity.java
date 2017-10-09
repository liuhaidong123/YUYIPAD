package com.technology.yuyipad.activity.FamilyUser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.R;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.code.RSCode;
import com.technology.yuyipad.code.ServerCode;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.util.List;

//家庭成员列表
public class FamilyUserListActivity extends MyActivity implements IFamilyUserList{
    FamilyUserListPresenter presenter;
    List<FamilyUserListBean.ResultBean> listFamilyUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_family_user_list);
        presenter=new FamilyUserListPresenter();
        presenter.getData(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RSCode.rCode){
            if (resultCode==RSCode.sCode){
                presenter.getData(this);
            }
        }
    }
    //成功的时候
    @Override
    public void onSuccess(FamilyUserListBean bean) {
        listFamilyUser=bean.getResult();
        if (listFamilyUser!=null&&listFamilyUser.size()>0){

        }
        else {

        }
    }
    //失败的时候
    @Override
    public void onError(String msg,String interfaceName) {
//        String result= IDbUtlis.getInstance().getOkhttpString(this,interfaceName);
    }
    //token失效
    @Override
    public void onTokenError(String msg) {
        ExitLogin.getInstance().showLogin(this);
    }
}
