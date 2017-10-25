package com.technology.yuyipad.Net;

/**
 * Created by wanyu on 2017/9/27.
 */
//定义所有接口
public interface Iport {
    //获取个人信息参数token，method：get／post
    String IgetUserInfo="personal/get.do?";
    //家庭用户列表参数token
    String IgetFamilyUserList="homeuser/findList.do?";

    //获取个人信息接口http://localhost:8080/yuyi/personal/get.do?token=C0700876FB2F9BEC156AC039F894E92B
     String interface_UserMsg="personal/get.do?";
    //个人信息修改:http://localhost:8080/yuyi/personal/save.do?token=C0700876FB2F9BEC156AC039F894E92B&idCard=515251635262&age=26
     String interface_UserMsgRevise="personal/save.do?";

    //获取融云token的请求personal/rongyuToken.do?personalid=18782931356
    String interface_RongToken="personal/rongyuToken.do?";
    //获取融云医生的信息接口（聊天对象的信息）http://192.168.1.37:8080/yuyi/physician/doctory.do?
     String interface_DocInfo="physician/doctory.do?";

    //获取家庭用户电子病历列表（我的家庭用户,不是我的）//http://localhost:8080/yuyi/medical/homeuserMedicalTime.do?id=1
     String interface_famiUserEleList="medical/homeuserMedicalTime.do?";
    //   获取电子病历列表(个人的)http://localhost:8080/yuyi/medical/token.do?token=1EE359830D68AF676396B06029CCFA61
     String interface_medicalRecordList="medical/token.do?";

    //添加家庭用户的接口
    public final static String interface_addFamilyUser="homeuser/save.do?";
    //删除家庭用户http://192.168.1.55:8080/yuyi/homeuser/delete.do?token=6DD620E22A92AB0AED590DB66F84D064&id=123
    String interfce_DeleteFamilyUser="homeuser/delete.do?";
    //获取我的药品状态接口（所有的药品状态）
     String interface_MyDrugStateList="prescription/findList2.do?";
    //登录获取验证码
    String interface_LoginGetSMS="personal/vcode.do";
    //登录
    String interface_Login="personal/login.do";


    //更改绑定手机号修改绑定的手机号 http://192.168.1.168:8082/yuyi/personal/modifymobile.do?token=1213&newMobile=13717883009&vcode=123456
  String interface_ChangePhone="personal/modifymobile.do?";
    //获取验证码http://192.168.1.168:8082/yuyi/personal/vcode.do?id=13717883005
     String interface_GetSMSCode="personal/vcode.do?";

    //是否有未读消息http://192.168.1.168:8082/yuyi/message/hasmessage.do?token=97338E8A81C0CC137FC51C6206681EBB
    String interface_HasUnReadMsg="message/hasmessage.do?";
    //未读消息http://192.168.1.168:8082/yuyi/message/readPage.do?token=97338E8A81C0CC137FC51C6206681EBB&start=0&limit=1
    String interface_getUnReadMsg="message/readPage.do?";

    //意见反馈页面:http://localhost:8080/yuyi/feedback//save.do?content=“”&contact=192873637&token=2E8B4C79121FBC6CB1377B190C663F52
    public final static String interface_User_feedus="feedback//save.do?";

    //获取最新10消息http://192.168.1.55:8080/yuyi/message/findList.do?
    public final static String interface_getPushMsg="message/findList.do?";
}
