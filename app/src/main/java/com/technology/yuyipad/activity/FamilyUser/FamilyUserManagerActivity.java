package com.technology.yuyipad.activity.FamilyUser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Lview.FamilyUserListView;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.FamilyUser.Adapter.FamilyUserListAdapter;
import com.technology.yuyipad.activity.FamilyUser.Bean.FamilyUserListBean;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.lzhUtils.ListChangePosition;
import com.technology.yuyipad.lzhUtils.MyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//家庭成员列表
public class FamilyUserManagerActivity extends MyActivity implements AdapterView.OnItemClickListener,IFamilyUserManager {
    FamilyUserManagerPresenter presenter;
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;//标题
    @BindView(R.id.familyusermanager_listview)FamilyUserListView familyusermanager_listview;//用户列表
    List<FamilyUserListBean.ResultBean>li;
    FamilyUserListAdapter adapter;//家庭用户列表的list适配器
    FamilyUserListBean.ResultBean beanAdd;//添加按钮
    boolean flag=true;//是否能够进入下级页面（网络未响应）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_family_user_list);
        titleinclude_text.setText("家庭用户管理");
        presenter=new FamilyUserManagerPresenter();
        presenter.initFragment(getSupportFragmentManager(),R.id.familyusermanager_fragmentLayout);
        presenter.getData(this);//获取家庭用户列表
        initData();
    }
    //设置用户列表的listview相关的适配器以及数据源
    private void initData() {
        //添加按钮作为一条假的bean数据加入
        beanAdd=new FamilyUserListBean.ResultBean();
        beanAdd.setAdd(true);
        beanAdd.setMySelf(false);
        beanAdd.setTrueName("添加");
        beanAdd.setAvatar(""+R.mipmap.addfamilyuser);
        li=new ArrayList<>();
        li.add(beanAdd);
        adapter=new FamilyUserListAdapter(li,this);
        familyusermanager_listview.setAdapter(adapter);
        familyusermanager_listview.setOnItemClickListener(this);
    }
    //item点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i!=0){//点击的不是第一项
            if (!li.get(i).isAdd()){//不是添加按钮时，向fragment传递数据
                presenter.showFragment(FamilyUserManagerPresenter.FragmentType.USERINFO,null);//显示用户信息的fragment
                presenter.setUserData(li.get(i));
            }
            else {//添加按钮时
                presenter.showFragment(FamilyUserManagerPresenter.FragmentType.USERADD,null);//显示用户添加／修改的fragment
                }
            if (li.get(0).isAdd()&&i!=li.size()-1){//第一项是添加按钮时
                ListChangePosition.getInstance().changeList(li,0,li.size()-1);//先把添加按钮换到最后
                }
            ListChangePosition.getInstance().changeList(li,i,0);
            adapter.notifyDataSetChanged();
            }
        else  {//点击的是第一项
                if (!li.get(i).isAdd()){//第一项不是添加按钮才处理
                    presenter.showFragment(FamilyUserManagerPresenter.FragmentType.USERINFO,null);//显示用户信息的fragment
                    presenter.setUserData(li.get(i));
                }
                else {
                    presenter.showFragment(FamilyUserManagerPresenter.FragmentType.USERADD,null);//显示用户添加／修改的fragment
                }
            }
    }
    //获取列表成功的时候
    @Override
    public void onSuccess(FamilyUserListBean bean) {
        initListAdata(bean);
    }
    //获取家庭用户列表失败的时候
    @Override
    public void onError(String msg,String interfaceName) {
        flag=false;
        toast.getInstance().text(this,msg);
        String result= IDbUtlis.getInstance().getOkhttpString(this,interfaceName);
        FamilyUserListBean bean= gson.gson.fromJson(result,FamilyUserListBean.class);
        if (bean!=null){
            initListAdata(bean);
        }
    }
    //token失效
    @Override
    public void onTokenError(String msg) {
        ExitLogin.getInstance().showLogin(this);
    }

    //刷新数据（网络请求到数据或者本地取到数据时）
    public void initListAdata(FamilyUserListBean bean){
        List<FamilyUserListBean.ResultBean>listFamilyUser=bean.getResult();
        if (listFamilyUser!=null&&listFamilyUser.size()>0){
            li.clear();
            adapter.notifyDataSetChanged();
            listFamilyUser.get(0).setMySelf(true);//第一个是用户自己
            li.addAll(listFamilyUser);
            li.add(beanAdd);
            adapter.notifyDataSetChanged();
            presenter.showFragment(FamilyUserManagerPresenter.FragmentType.USERINFO,null);
            presenter.setUserData(li.get(0));
        }
        else {
            Log.i("没有家庭用户","该用户还没有添加过家庭用户（自己的也没有添加上去）");
        }
    }

    public void getData(){
        presenter.getData(this);
    }
    //切换到修改信息的fragment
    public void showFragChange(){
        presenter.showFragment(FamilyUserManagerPresenter.FragmentType.USERCHANGE,li.get(0));
    }

}
