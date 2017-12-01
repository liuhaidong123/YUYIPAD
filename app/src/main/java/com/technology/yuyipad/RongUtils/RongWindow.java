package com.technology.yuyipad.RongUtils;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.MyApplication;
import com.technology.yuyipad.lzhUtils.PopupSettings;

import io.rong.callkit.RongCallKit;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by wanyu on 2017/10/24.
 */

public class RongWindow implements View.OnClickListener ,IGetRongUserTokenError,IGetRongDoctorId{
    PopupWindow pop;
    Activity ac;
    String cID;
    View parenteV;
    String docId="";
    static RongWindow rWindow;
    private RongWindow(){}
    public static RongWindow getInstance(){
        if (rWindow==null){
            rWindow=new RongWindow();
        }
        return rWindow;
    }
    //cid医院的id
   public  void showWindow(Activity con, View parentView,String cid){
       if (Empty.getInstance().notEmptyOrNull(cid)){
           this.cID=cid;
           docId="";//重制docId
           this.ac=con;
           this.parenteV=parentView;
           RongConnect.getInstance().getTargetDocId(this,cid);
       }
       else {
           toast.getInstance().text(con,"医院信息错误，无法启动咨询程序");
       }
   }

    private void show(Activity con, View parentView){
        if (pop==null) {
            pop = new PopupWindow();
            }
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
                        RongCallKit.startSingleCall(ac,docId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
                        break;
                    case R.id.rong_radio://语音
                        RongCallKit.startSingleCall(ac,docId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
                        break;
                    case R.id.rong_write://文字
                        RongIM.getInstance().startPrivateChat(ac,docId, "咨询");
                        break;
                }
                if (pop!=null&&pop.isShowing()){
                    pop.dismiss();
                }
            }
    }

    //请求服务器的token异常
    @Override
    public void onTokenError(String message) {
        showWindow(ac,parenteV,cID);//获取容云token失败或者链接容云服务器失败时执行一次重新链接
    }

    @Override
    public void onTokenSucc() {
        showWindow(ac,parenteV,cID);
    }

    //获取医生信息
    @Override
    public void onError(String msg) {
        toast.getInstance().text(ac,"咨询程序启动失败："+msg);
    }

    @Override
    public void onSuccess(String targetId) {
        this.docId=targetId;
        RongUserInfoProvider.getInstance().setUserInfo(targetId,new UserInfo(targetId,"医生",
                Uri.parse("http://a3.qpic.cn/psb?/V10dl1Mt1s0RoL/qvT5ZwDSegULprXup78nlo3*XNUqCRH8shghIkAnQTs!/b/dLMAAAAAAAAA&bo=ewJ7AgAAAAADByI!&rf=viewer_4")));
        show(ac,parenteV);
    }
}