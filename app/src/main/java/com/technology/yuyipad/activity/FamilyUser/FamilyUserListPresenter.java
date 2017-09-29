package com.technology.yuyipad.activity.FamilyUser;

/**
 * Created by wanyu on 2017/9/29.
 */

public class FamilyUserListPresenter {
    FamilyUserListModel model;
    //获取家庭用户列表数据
    public void getData(IFamilyUserList ilist){
        if (model==null){
            model=new FamilyUserListModel();
        }
        model.getUserList(ilist);
    }
}
