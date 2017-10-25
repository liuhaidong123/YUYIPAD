package com.technology.yuyipad.RongUtils;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.PopupSettings;

/**
 * Created by wanyu on 2017/10/24.
 */

public class RongWindow implements View.OnClickListener ,IGetRongUserTokenError{
    static RongWindow rWindow;
    private RongWindow(){}
    public static RongWindow getInstance(){
        if (rWindow==null){
            rWindow=new RongWindow();
        }
        return rWindow;
    }
   public  void showWindow(Activity con, View parentView){
       if (RongUser.isRongTokenGet){

       }
       else {
           RongConnect.getInstance().getRongUserInfo(this,con);
       }
       PopupWindow pop=new PopupWindow();
       View vi= LayoutInflater.from(con).inflate(R.layout.rongwindow,null);
       LinearLayout rong_video=vi.findViewById(R.id.rong_video);//视频
       LinearLayout rong_radio=vi.findViewById(R.id.rong_radio);//语音
       LinearLayout rong_write=vi.findViewById(R.id.rong_write);//文字
       rong_video.setOnClickListener(rWindow);
       rong_radio.setOnClickListener(rWindow);
       rong_write.setOnClickListener(rWindow);
       PopupSettings.getInstance().showWindowCenter(con,pop,vi,parentView);
   }

    @Override
    public void onClick(View view) {
            if (view!=null){
                switch (view.getId()){
                    case R.id.rong_video://视频

                        break;
                    case R.id.rong_radio://语音

                        break;
                    case R.id.rong_write://文字

                        break;
                }
            }
    }

    //请求服务器的token异常
    @Override
    public void onTokenError(String message) {

    }
}
