package com.technology.yuyipad.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.RongUtils.IGetRongDoctorId;
import com.technology.yuyipad.RongUtils.IGetRongUserTokenError;
import com.technology.yuyipad.RongUtils.RongConnect;
import com.technology.yuyipad.RongUtils.RongUser;
import com.technology.yuyipad.RongUtils.RongUserInfoProvider;
import com.technology.yuyipad.RongUtils.RongWindow;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.lzhUtils.StateBarUtils;

import io.rong.imlib.model.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultFragment extends Fragment implements View.OnClickListener,IGetRongDoctorId,IGetRongUserTokenError{
    int RongConnectCount=1;
    int RongServerConnectCount=1;
    TextView information_title;
    View parentView;
    public ConsultFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_consult, container, false);
        parentView=view.findViewById(R.id.three);
        information_title=view.findViewById(R.id.information_title);
        information_title.setOnClickListener(this);
        if (RongUser.isRongTargetIdGet==false){
            RongConnect.getInstance().getTargetDocId(this,"1");
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if (RongUser.isConnectSuccess==false){//没有链接容云服务器时
            RongConnect.getInstance().getRongUserInfo(this,getActivity());
        }
        else {
            RongWindow.getInstance().showWindow(getActivity(),parentView);
        }
    }

    //获取医生信息失败
    @Override
    public void onError(String msg) {
        if (RongConnectCount==1){
            RongConnectCount=2;
            RongConnect.getInstance().getTargetDocId(this,"1");
        }
        else {
            toast.getInstance().text(getActivity(),msg);
        }
    }

    @Override
    public void onSuccess(String targetId) {
        RongUser.isRongTargetIdGet=true;
        RongUser.RongTargetId=targetId;
        RongUserInfoProvider.getInstance().setUserInfo(RongUser.RongTargetId,new UserInfo(RongUser.RongTargetId,"医生", Uri.parse("http://a3.qpic.cn/psb?/V10dl1Mt1s0RoL/qvT5ZwDSegULprXup78nlo3*XNUqCRH8shghIkAnQTs!/b/dLMAAAAAAAAA&bo=ewJ7AgAAAAADByI!&rf=viewer_4")));
    }

    //链接容云服务器失败时（在MainActivity中链接，此页面只是检查是否链接成功，失败时才会重新链接）
    @Override
    public void onTokenError(String message) {
        if (RongServerConnectCount==1){//重新链接容云服务器
            RongConnect.getInstance().getRongUserInfo(this,getActivity());
            RongServerConnectCount=2;
        }
        else {
            toast.getInstance().text(getActivity(),message);
        }
    }
}
