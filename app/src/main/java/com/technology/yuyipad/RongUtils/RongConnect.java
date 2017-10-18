package com.technology.yuyipad.RongUtils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.RongUtils.Bean.RongDoctorInfoBean;
import com.technology.yuyipad.RongUtils.Bean.RongTokenBean;
import com.technology.yuyipad.RongUtils.Bean.RongUserInfoBean;
import com.technology.yuyipad.code.ServerCode;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * Created by wanyu on 2017/10/11.
 */
public class RongConnect {
    static  RongConnect connect;
    IGetRongUserTokenError iToken;
    IGetRongDoctorId iGetDocId;//获取医生信息
    RongUserInfoBean info;//容云用户信息
    Context con;
    private RongConnect(){

    }
    public static RongConnect getInstance(){
        if (connect==null){
            connect=new RongConnect();
        }
        return connect;
    }
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    try{
                        RongTokenBean bean= gson.gson.fromJson(resStr,RongTokenBean.class);
                        if (bean!=null){
                            if ("1".equals(bean.getCode())) {
                                RongUser.RongToken = bean.getToken();
                                Log.i("Rong--", bean.getId() + "--" + bean.getTrueName());
                                Log.e("融云用户头像---",Ip.imagePath + bean.getAvatar());
                                info=new RongUserInfoBean();
                                info.setName(bean.getTrueName()+"");
                                info.setPortraitUri(Uri.parse(Ip.imagePath+bean.getAvatar()));
                                RongUser.isRongTokenGet=true;//已经获取到了容云到token
                                connect(con);
                            } else if ("0".equals(bean.getCode())) {
                                Log.e("融云获取token错误--1--main-", "---后台无法返回token----");
                            } else {
                                Log.e("融云获取token错误--2-main-", "---后台无法返回token----");
                            }
                        }
                        else {
                            iToken.onTokenError("无法获取聊天资料");
                        }
                    }
                    catch (Exception e){
                        iToken.onTokenError("数据异常，无法解析聊天资料");
                        Log.e("RongConnect","获取服务器保存的容云token返回数据无法解析："+e.toString());
                    }
                    break;
                case -2://获取
                    iGetDocId.onError("网络异常");
                    break;
                case 2:
                    try{
                        RongDoctorInfoBean bean=gson.gson.fromJson(resStr,RongDoctorInfoBean.class);
                        if (bean!=null){
                            if (ServerCode.successCode.equals(bean.getCode())){
                                iGetDocId.onSuccess(bean.getId());
                            }
                            else if ("-1".equals(bean.getCode())){//医院没有开通咨询服务
                                iGetDocId.onError("医院没有开通咨询服务");
                            }
                            else {
                               iGetDocId.onError("请求医生信息错误，无法启动咨询功能,请稍后重试");
                                }
                        }
                        else {
                            iGetDocId.onError("获取医生信息失败");
                        }
                    }catch (Exception e){
                        iGetDocId.onError("数据错误，无法开启咨询程序");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    //获取融云tokenhttp://localhost:8080/yuyi/personal/post.do?personalid=18881882888
    public void getRongUserInfo(IGetRongUserTokenError iTokens, Context con) {
        if (RongUser.isRongTokenGet){//已经获取到了服务器端存储到用户容云信息，只执行与容云服务器的链接工作不再向本地服务器请求数据
            connect(con);
        }
        else {//还没有向服务器请求过或者没有请求到容云信息
            this.iToken=iTokens;
            this.con=con;
            if (Empty.getInstance().notEmptyOrNull(User.tele)) {
                Map<String, String> mp = new HashMap<>();
                mp.put("personalid", User.tele);
                ok.getCall(Ip.path + Iport.interface_RongToken, mp, ok.OK_POST).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        handler.sendEmptyMessage(0);
                    }
                    @Override
                    public void onResponse(Response response) throws IOException {
                        resStr=response.body().string();
                        Log.i("获取容云token接口",resStr);
                        handler.sendEmptyMessage(1);
                    }
                });
            }
            else {
                iToken.onTokenError("当前用户信息不正确，无法获取聊天室资料");
                Log.e("RongConnect","从本地服务器获取用户的容云token失败：当前用户的userName不正确，无法获取到信息");
            }
        }

    }


    //链接容云服务器
    private void connect( Context context) {
        if (!Empty.getInstance().notEmptyOrNull(RongUser.RongToken)){
            Log.e("RongConnect","错误：本地没有获取到容云的token，无法执行链接容云服务器操作");
            return;
        }
        if (context.getApplicationInfo().packageName.equals(getCurProcessName(context.getApplicationContext())))
            RongIM.connect(RongUser.RongToken, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.e("RongConnect", "错误：本地保存的容云token不正确或已经过期，需要重新设置tokne");
                    RongUser.isConnectSuccess=false;
                }
                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    RongUser.RonguserId = userid;
                    RongUser.isConnectSuccess=true;
                    if (info==null){
                        info=new RongUserInfoBean();
                        Log.e("RongConnect","无法获取到当前用户的其他信息(name与头像)，只获取倒了userid");
                    }
                    info.setId(userid);
                    RongUserInfoProvider.getInstance().setUserInfo(info.getId(),new UserInfo(info.getId(),info.getName(),Uri.parse("http://a3.qpic.cn/psb?/V10dl1Mt1s0RoL/qvT5ZwDSegULprXup78nlo3*XNUqCRH8shghIkAnQTs!/b/dLMAAAAAAAAA&bo=ewJ7AgAAAAADByI!&rf=viewer_4")));
                    Log.i("RongConnect", "链接容云服务器成功，返回的useri："+userid+"");
                }

                /**
                 * 连接融云失败
                 *
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    RongUser.isConnectSuccess=false;
                    Log.e("RongConnect", "错误码："+errorCode.getValue()+",错误信息："+errorCode.getMessage());
                }
            });
    }


    //获取聊天医生的信息cid医院的id（默认值-1）
    public void getTargetDocId(IGetRongDoctorId iGetDocId,String cid){
        this.iGetDocId=iGetDocId;
        if (!"-1".equals(cid)){
            Map<String, String> m = new HashMap<>();
            m.put("cid",cid+"");
            ok.getCall(Ip.path+Iport.interface_DocInfo, m, ok.OK_GET).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(-2);
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    resStr = response.body().string();
                    Log.i("请求医生融云信息---", resStr);
                    handler.sendEmptyMessage(2);
                }
            });
        }
        else {
            iGetDocId.onError("医院没有开通咨询服务，无法启动");
            Log.e("RongConnect","获取聊天医生的容云id失败，医院的id不能是-1，请检查当前医院id是否正确");
        }
    }
}
