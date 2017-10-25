package com.technology.yuyipad.activity.Message;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanyu on 2017/10/20.
 */

public class MessageListModel {
    IMessage ims;
    private String resStr;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ims.onGetMessageError("网络异常！");
                    break;
                case 1:
                    try {
                        bean_MyMessage myMessage = gson.gson.fromJson(resStr, bean_MyMessage.class);
                        if (myMessage!=null&&myMessage.getRows()!=null&&myMessage.getRows().size()>0){
                            ims.onGetMessageSuccess(myMessage.getRows());
                        }
                        else {
                            ims.onGetMessageError("没有查询到数据！");
                        }
                    } catch (Exception e) {
                        ims.onGetMessageError("数据异常！");
                        e.printStackTrace();

                    }
                    break;
            }
        }
    };
    public void getMessageList(int start,int limit,IMessage iMsg){
        this.ims=iMsg;
        Map<String,String> mp=new HashMap<>();
        mp.put("token", User.token);
        mp.put("start",start+"");
        mp.put("limit",limit+"");
        ok.getCall(Ip.path+ Iport.interface_getUnReadMsg,mp,ok.OK_GET).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                Log.i("消息---",resStr);
                handler.sendEmptyMessage(1);
            }
        });
    }
}
