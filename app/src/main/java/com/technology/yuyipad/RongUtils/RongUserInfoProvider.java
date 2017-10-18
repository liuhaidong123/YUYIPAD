package com.technology.yuyipad.RongUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static android.R.id.list;

/**
 * Created by wanyu on 2017/10/11.
 */

public class RongUserInfoProvider implements RongIM.UserInfoProvider{
    static RongUserInfoProvider provider;
    Map<String,UserInfo> mp;
    private RongUserInfoProvider(){
        mp=new HashMap<>();
    }
    public static RongUserInfoProvider getInstance(){
        if (provider==null){
            provider=new RongUserInfoProvider();
        }
        return provider;
    }
    @Override
    public UserInfo getUserInfo(String s) {
        return mp.get(s);
    }
    public void setUserInfo(String ids,UserInfo info){
        if (mp==null){
            mp=new HashMap<>();
        }
        mp.put(ids,info);
    }
}
