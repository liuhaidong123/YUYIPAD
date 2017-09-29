package com.technology.yuyipad.DbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by wanyu on 2017/9/27.
 */

public class IDbUtlis {
    static IDbUtlis dbUtils;
    IOpenHelper helper;
    private IDbUtlis(){
    }
    public static IDbUtlis getInstance(){
        if (dbUtils==null){
            dbUtils=new IDbUtlis();
        }
        return dbUtils;
    }

    //根据url获取到缓存的网络数据
    public String getOkhttpString(Context context, String url){
        helper=new IOpenHelper(context, "network.db", null, 1);
        SQLiteDatabase db =helper.getReadableDatabase();
        String netString="";
        Cursor cursor=db.rawQuery("select value from inter where name=?",new String[]{url});
        if (cursor!=null){
            while (cursor.moveToNext()){
                netString=cursor.getString(cursor.getColumnIndex("value"));
            }
            Log.i("IDbUtlis","getOkhttpString获取缓存数据--："+url+"==="+netString);
        }
        else {
            Log.e("IDbUtlis","getOkhttpString获取缓存数据--："+url+"===获取失败：该接口没有数据或其他原因导致无法获取到该接口缓存的数据");
        }
        db.close();
        return netString;
    }
    //插入／替换
    public boolean saveOkhttpString(Context context ,String url,String netString){
        helper=new IOpenHelper(context, "network.db", null, 1);
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",url);
        contentValues.put("value",netString);
        long l=db.replace("inter",null,contentValues);
        db.close();
        if (l>-1){
            Log.i("IDbUtlis","保存网络数据-成功-："+url+"==="+netString);
            return true;
        }
        Log.e("IDbUtlis","saveOkhttpString保存网络数据-失败-："+url+"==="+netString);
        return false;
    }
}
