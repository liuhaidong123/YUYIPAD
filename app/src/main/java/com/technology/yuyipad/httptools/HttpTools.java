package com.technology.yuyipad.httptools;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.technology.yuyipad.bean.AdBean.Root;
import com.technology.yuyipad.bean.FirstPageDrugSixDataRoot;
import com.technology.yuyipad.bean.FirstPageInformationTwoDataRoot;
import com.technology.yuyipad.bean.HospitalDepartmentRoot;
import com.technology.yuyipad.bean.Information;
import com.technology.yuyipad.bean.LoginSuccess;
import com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.UpdatedInformation;
import com.technology.yuyipad.bean.UserMessage;
import com.technology.yuyipad.bean.ValidateCodeRoot;


import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * Created by liuhaidong on 2017/3/7.
 */

public class HttpTools {

    private FinalHttp mFinalHttp;
    private static HttpTools mHttpTools;
    private Gson mGson = new Gson();

    private HttpTools() {
        if (mFinalHttp == null) {
            mFinalHttp = new FinalHttp();
        }
    }

    //获取本类的实例对象，并且初始化FinalHttp类
    public static HttpTools getHttpToolsInstance() {
        if (mHttpTools == null) {
            //当初始化本类的时候，会初始化mFinalHttp
            mHttpTools = new HttpTools();
        }
        return mHttpTools;
    }

    /**
     * 获取首页常用药品6条数据
     */
    public void getFirstSixDrugData(final Handler handler) {
        String url = UrlTools.BASE + UrlTools.URL_FIRST_PAGE_SIX_DATA;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始药品6条数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功6条数据：", s);
                try {
                    FirstPageDrugSixDataRoot root = mGson.fromJson(s, FirstPageDrugSixDataRoot.class);
                    Message message = new Message();
                    message.what = 21;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    handler.sendEmptyMessage(200);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败6条数据：", strMsg.toString());
                handler.sendEmptyMessage(201);
            }
        });
    }


    /**
     * 获取首页资讯2条数据
     */
    public void getFirstPageInformationTwoData(final Handler handler, int start, int limit) {
        String url = UrlTools.BASE + UrlTools.URL_UPDATEA_FIRST + "start=" + start + "&limit=" + limit;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始资讯2条数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功资讯2条数据：", s);
                try {
                    com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.UpdatedFirstPageTwoDataBean.Root.class);
                    Message message = new Message();
                    message.what = 22;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(202);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败资讯2条数据：", strMsg);
                handler.sendEmptyMessage(203);
            }
        });
    }

    /**
     * 获取首页资讯2条数据的详情
     */
    public void getFirstPageInformationTwoDataMessage(final Handler handler, long id) {
        String url = UrlTools.BASE + UrlTools.URL_UPDATEA_FIRST_MESSAGE + "id=" + id;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始资讯2条数据详情");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功资讯2条数据详情：", s);
                try {
                    UpdatedInformation root = mGson.fromJson(s, UpdatedInformation.class);
                    Message message = new Message();
                    message.what = 23;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(204);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败资讯2条数据详情：", strMsg);
            }
        });
    }


    /**
     * 获取咨询数据
     */
    public void getAskAllData(final Handler handler, int start, int limit) {
        String url = UrlTools.BASE + UrlTools.URL_FIRST_PAGE_TWO_DATA + "start=" + start + "&limit=" + limit;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始资讯2条数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功资讯2条数据：", s);
                try {
                    FirstPageInformationTwoDataRoot root = mGson.fromJson(s, FirstPageInformationTwoDataRoot.class);
                    Message message = new Message();
                    message.what = 22;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(202);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败资讯2条数据：", strMsg);
                handler.sendEmptyMessage(203);
            }
        });
    }



    /**
     * 获取咨询页面数据
     */
    public void getAskData(final Handler handler, int start, int limit) {
        String url = UrlTools.BASE + UrlTools.URL_FIRST_PAGE_TWO_DATA + "start=" + start + "&limit=" + limit;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始咨询页面数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功咨询页面数据：", s);
                try {
                    FirstPageInformationTwoDataRoot root = mGson.fromJson(s, FirstPageInformationTwoDataRoot.class);
                    Message message = new Message();
                    message.what = 24;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    handler.sendEmptyMessage(205);
                    Log.e("错误码", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败咨询页面数据：", strMsg);
                handler.sendEmptyMessage(206);
            }
        });
    }


    /**
     * 获取咨询页面数据详情
     */
    public void getAskDataMessage(final Handler handler, int id) {
        String url = UrlTools.BASE + UrlTools.URL_FIRST_PAGE_TWO_DATA_MESSAGE + "id=" + id;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始咨询数据详情");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功咨询数据详情：", s);
                try {
                    Information root = mGson.fromJson(s, Information.class);
                    Message message = new Message();
                    message.what = 25;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败咨询据详情：", strMsg);
                handler.sendEmptyMessage(207);
            }
        });
    }


    /**
     * 获取验证码接口
     */
    public void getValidateCode(final Handler handler, Map<String, String> map) {
        String url = UrlTools.BASE + UrlTools.URL_GET_VALIDATE_CODE;

        mFinalHttp.post(url, new AjaxParams(map), new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "获取验证码开始");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess：", "获取验证码成功:" + s);
                try {
                    ValidateCodeRoot root = mGson.fromJson(s, ValidateCodeRoot.class);
                    Message m = new Message();
                    m.what = 26;
                    m.obj = root;
                    handler.sendMessage(m);
                } catch (Exception e) {
                    handler.sendEmptyMessage(208);
                    Log.e("错误码：", e.toString());
                }


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure：", "获取验证码失败" + strMsg);
                handler.sendEmptyMessage(209);
            }
        });

    }

    /**
     * 登录接口
     */
    public void login(final Handler handler, Map<String, String> map) {
        String url = UrlTools.BASE + UrlTools.URL_LOGIN;
        mFinalHttp.post(url, new AjaxParams(map), new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "登录开始");

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onStart：", "登录成功" + s);
                try {
                    LoginSuccess root = mGson.fromJson(s, LoginSuccess.class);
                    Message m = new Message();
                    m.what = 27;
                    m.obj = root;
                    handler.sendMessage(m);
                } catch (Exception e) {
                    handler.sendEmptyMessage(210);
                    Log.e("错误码：", e.toString());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                handler.sendEmptyMessage(211);
                Log.e("onFailure：", "登录失败" + strMsg);
            }
        });

    }

    /**
     * 获取我的页面用户信息
     */
    public void getUserMessage(final Handler handler, String token) {
        String url = UrlTools.BASE + UrlTools.URL_USER_MESSAGE + "token=" + token;
        Log.e("onSuccess：",UrlTools.BASE + UrlTools.URL_USER_MESSAGE+token );
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "获取用户信息开始");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess：", "获取用户信息成功" + s);

                try {
                    UserMessage root = mGson.fromJson(s, UserMessage.class);
                    Message m = new Message();
                    m.what = 28;
                    m.obj = root;
                    handler.sendMessage(m);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure：", "获取用户信息失败" + strMsg);
                handler.sendEmptyMessage(212);
            }
        });

    }


    /**
     * 预约挂号接口
     */

    public void getAppintmentData(final Handler handler, int start, int limit) {
        String url = UrlTools.BASE + UrlTools.URL_FIRST_PAGE_TWO_DATA + "start=" + start + "&limit=" + limit;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始预约挂号数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功预约挂号数据：", s);
                try {
                    FirstPageInformationTwoDataRoot root = mGson.fromJson(s, FirstPageInformationTwoDataRoot.class);
                    Message message = new Message();
                    message.what = 28;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(213);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败预约挂号数据：", strMsg);
                handler.sendEmptyMessage(214);
            }
        });


    }

    /**
     * 医院科室接口
     */

    public void getHospitalDepartmentData(final Handler handler, int hid) {
        String url = UrlTools.BASE + UrlTools.URL_HOSPITAL_DEPARTMENT + "hid=" + hid;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始医院科室数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功医院科室数据：", s);
                try {
                    HospitalDepartmentRoot root = mGson.fromJson(s, HospitalDepartmentRoot.class);
                    Message message = new Message();
                    message.what = 29;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(215);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                handler.sendEmptyMessage(216);
                Log.e("onFailure请求失败医院科室数据：", strMsg);
            }
        });
    }


    /**
     * 挂号选择上午，下午，医生
     */
    public void getUserRegisterData(final Handler handler, int cid) {
        String url = UrlTools.BASE + UrlTools.URL_USER_REGISTER + "clinicId=" + cid;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始医院科室数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功医院科室数据：", s);
                try {
                    com.technology.yuyipad.bean.SelectDoctor.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.SelectDoctor.Root.class);
                    Message message = new Message();
                    message.what = 30;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(217);
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败医院科室数据：", strMsg+"");
                handler.sendEmptyMessage(218);
            }
        });
    }

    /**
     * 首页轮播广告接口
     */
    public void getAdData(final Handler handler) {
        String url = UrlTools.BASE + UrlTools.URL_AD;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始轮播广告接口");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功轮播广告接口数据：", s);
                try {
                    Root root = mGson.fromJson(s, Root.class);
                    Message message = new Message();
                    message.what = 31;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败轮播广告接口数据：", strMsg);
                handler.sendEmptyMessage(219);
            }
        });
    }


    /**
     * 搜索药品接口
     */
    public void getSearchDrugData(final Handler handler, String vague) {
        String url = UrlTools.BASE + UrlTools.URL_SEARCH_DRUG + "vague=" + vague;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求查找药品接口");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功查找药品数据：", s);
                try {
                    com.technology.yuyipad.bean.SearchDrugBean.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.SearchDrugBean.Root.class);
                    Message message = new Message();
                    message.what = 32;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(220);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败查找药品数据：", strMsg);
                handler.sendEmptyMessage(221);
            }
        });
    }

    /**
     * 搜索医院接口
     */
    public void getSearchHospitalData(final Handler handler, String vague) {
        String url = UrlTools.BASE + UrlTools.URL_SEARCH_HOSPITAL + "vague=" + vague;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求查找医院接口");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功查找医院数据：", s);
                try {
                    com.technology.yuyipad.bean.SearchHospital.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.SearchHospital.Root.class);
                    Message message = new Message();
                    message.what = 33;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码", e.toString());
                    handler.sendEmptyMessage(222);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败查找医院数据：", strMsg);
                handler.sendEmptyMessage(223);
            }
        });
    }


    /**
     * 广告详情
     */
    public void getAdMessageData(final Handler handler, int id) {
        String url = UrlTools.BASE + UrlTools.URL_AD_MEssage + "id=" + id;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始轮播广告详情接口");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功轮播广告详情数据：", s);
                try {
                    com.technology.yuyipad.bean.ADmessageBean.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.ADmessageBean.Root.class);
                    Message message = new Message();
                    message.what = 34;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码：", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败轮播广告详情数据：", strMsg);
                handler.sendEmptyMessage(224);
            }
        });
    }

    /**
     * 获取用户列表接口
     */
    public void getUserLIst(final Handler handler, Map<String, String> map) {
        String url = UrlTools.BASE + UrlTools.URL_USER_LIST;
        mFinalHttp.post(url, new AjaxParams(map), new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "获取用户列表开始");

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess：", "获取用户列表成功" + s);
                try {
                    com.technology.yuyipad.bean.UserListBean.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.UserListBean.Root.class);
                    Message m = new Message();
                    m.what = 35;
                    m.obj = root;
                    handler.sendMessage(m);
                } catch (Exception e) {
                    handler.sendEmptyMessage(225);
                    Log.e("错误码==", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure：", "获取用户列表失败" + strMsg);
                handler.sendEmptyMessage(226);
            }
        });

    }

    /**
     * 提交用户体温接口
     */
    public void submitTemData(final Handler handler, final Map<String, String> map) {
        String url = UrlTools.BASE + UrlTools.URL_SUBMIT_TEM;
        mFinalHttp.post(url, new AjaxParams(map), new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "提交体温开始开始");

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess：", "提交体温成功" + s);
                try {
                    com.technology.yuyipad.bean.SubmitTemBean.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.SubmitTemBean.Root.class);
                    Message message = new Message();
                    message.what = 36;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    handler.sendEmptyMessage(227);
                    Log.e("提交体温错误码==", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure：", "提交体温失败" + strMsg);
                handler.sendEmptyMessage(228);
            }
        });

    }


    /**
     * 提交用户血压接口
     */
    public void submitBloodData(final Handler handler, final Map<String, String> map) {
        String url = UrlTools.BASE + UrlTools.URL_SUBMIT_BLOOD;
        mFinalHttp.post(url, new AjaxParams(map), new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "提交血压开始");

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess：", "提交血压成功" + s);
                try {
                    com.technology.yuyipad.bean.SubmitTemBean.Root root = mGson.fromJson(s, com.technology.yuyipad.bean.SubmitTemBean.Root.class);
                    Message message = new Message();
                    message.what = 37;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码==", e.toString());
                    handler.sendEmptyMessage(229);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure：", "提交血压失败" + strMsg);
                handler.sendEmptyMessage(230);
            }
        });

    }


    /**
     * 获取首页用户列表以及默认用户的数据
     */
    public void getFirstPageUserDataData(final Handler handler, String token) {
        String url = UrlTools.BASE + UrlTools.URL_FIRST_PAGE_USER_DATA + "token=" + token;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求开始获取首页用户列表以及默认用户的数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功首页用户列表数据：", s);
                try {
                    com.technology.yuyipad.bean.FirstPageUserDataBean.Root root=mGson.fromJson(s, com.technology.yuyipad.bean.FirstPageUserDataBean.Root.class);
                    Message message = new Message();
                    message.what = 38;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("错误码：", e.toString());
                    handler.sendEmptyMessage(231);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求失败首页用户列表数据：", strMsg);
                handler.sendEmptyMessage(232);
            }
        });
    }
    /**
     * 点击首页用户头像用户的数据
     */
    public void getClickUserDataData(final Handler handler, String token, long humeuserId) {
        String url = UrlTools.BASE + UrlTools.URL_CLICK_USER_HEAD_FIRST_PAGE + "token=" + token+"&humeuserId="+humeuserId;
        mFinalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "请求点击用户的数据");
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess请求成功点击数据：", s);
                try {
                    com.technology.yuyipad.bean.FirstPageClickUserBean.Root root=mGson.fromJson(s, com.technology.yuyipad.bean.FirstPageClickUserBean.Root.class);
                    Message message = new Message();
                    message.what = 39;
                    message.obj = root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    handler.sendEmptyMessage(234);
                    Log.e("错误码：", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure请求点击失败：", strMsg);
                handler.sendEmptyMessage(233);
            }
        });
    }

    /**
     * 确定挂号
     */
    public void sureRegister(final Handler handler, Map<String, String> map){
        String url = UrlTools.BASE + UrlTools.URL_REGISTER;
        mFinalHttp.post(url, new AjaxParams(map), new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart：", "提交挂号开始");

            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("onSuccess：", "提交挂号成功" + s);
                try {
                    com.technology.yuyipad.bean.RegisterResult.Root root=mGson.fromJson(s,com.technology.yuyipad.bean.RegisterResult.Root.class);
                    Message message=new Message();
                    message.what=40;
                    message.obj=root;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    handler.sendEmptyMessage(235);
                    Log.e("提交挂号错误码==", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (strMsg==null){
                    strMsg="--null";
                }
                Log.e("onFailure：", "提交挂号失败" + strMsg);
                handler.sendEmptyMessage(236);
            }
        });


    }
}
