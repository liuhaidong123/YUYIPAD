package com.technology.yuyipad.JPushUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by wanyu on 2017/10/10.
 */
//激光注册
public class JpRegister {
    static JpRegister jpRegister;
    public static JpRegister getInstance(){
        if (jpRegister==null){
            jpRegister=new JpRegister();
        }
        return jpRegister;
    }
    //设置别名(单一个体别名)
    public  void setAlias(final Context context, String phon){
        JPushInterface.setAlias(context, phon,new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                SharedPreferences preferences=context.getSharedPreferences("JPSH",Context.MODE_APPEND);
                SharedPreferences.Editor editor=preferences.edit();
                if (i==0){
                    Log.i("JPsh注册别名成功-alias-"+s,"--------------");
                    editor.putBoolean("Succ",true);
                    editor.commit();
                }
                else {
                    Log.e("JPsh注册别名alias失败--"+s,"----------");
                    editor.putBoolean("Succ",false);
                    editor.commit();
                }
            }
        });
    }
    //设置标签(群组)
    public  void setTag(Context context,Set<String>tag){
        JPushInterface.setTags(context, tag, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i==0){
                    Log.i("JPsh注册Tags成功----"+s,"--------------");
                }
                else {
                    Log.e("JPsh注册别名Tags失败--"+s,"----------"+set.iterator().next());
                }
            }
        });
    }
    //是否注册过jpsh
    public  Boolean isJPSHSucc(Context context){
        SharedPreferences preferences=context.getSharedPreferences("JPSH",Context.MODE_APPEND);
        return preferences.getBoolean("Succ",false);
    }
}
