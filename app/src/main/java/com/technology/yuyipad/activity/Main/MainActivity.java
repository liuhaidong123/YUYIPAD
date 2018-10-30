package com.technology.yuyipad.activity.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sst.jkezt.health.utils.JkezAPIMain;
import com.technology.yuyipad.JPushUtils.JpRegister;
import com.technology.yuyipad.R;
import com.technology.yuyipad.RongUtils.IGetRongUserTokenError;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.Login.LoginActivity;
import com.technology.yuyipad.bean.VersionRoot;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.lzhUtils.MyActivity;
import com.technology.yuyipad.user.User;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends MyActivity implements IGetRongUserTokenError {
    MainPresenter presenter;
    //首页
    @BindView(R.id.main_HomePage_rela)
    RelativeLayout main_HomePage_rela;
    @BindView(R.id.main_HomePage_image)
    ImageView main_HomePage_image;
    @BindView(R.id.main_HomePage_text)
    TextView main_HomePage_text;
    //测量
    @BindView(R.id.main_MeasurePage_rela)
    RelativeLayout main_MeasurePage_rela;
    @BindView(R.id.main_MeasurePage_image)
    ImageView main_MeasurePage_image;
    @BindView(R.id.main_MeasurePage_text)
    TextView main_MeasurePage_text;
    //咨询
    @BindView(R.id.main_CounselingPage_rela)
    RelativeLayout main_CounselingPage_rela;
    @BindView(R.id.main_CounselingPage_image)
    ImageView main_CounselingPage_image;
    @BindView(R.id.main_CounselingPage_text)
    TextView main_CounselingPage_text;
    //我的
    @BindView(R.id.main_MinePage_rela)
    RelativeLayout main_MinePage_rela;
    @BindView(R.id.main_MinePage_image)
    ImageView main_MinePage_image;
    @BindView(R.id.main_MinePage_text)
    TextView main_MinePage_text;
    int connectCount = 1;//重新链接容云的次数只执行两次重新链接

    private HttpTools httpTools;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 909) {//检测版本号

                Object o = msg.obj;
                if (o != null && o instanceof VersionRoot) {
                    VersionRoot versionRoot = (VersionRoot) o;
                    if (versionRoot != null && "0".equals(versionRoot.getCode())) {
                        int versionC = Integer.valueOf(versionRoot.getResult().getVersion());
                        if (versionC > getVersionCode()) {//服务器版本号大于当前版本号，需要更新软件
                            Log.e("服务器版本号=", versionC + "");
                            alertDialog.show();

                        } else {//不需要更新软件

                        }
                    }
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_main);
        presenter = new MainPresenter();
        //初始化被选中的标题项实体类
        presenter.addList(
                new HomeRelativeBean(main_HomePage_rela, main_HomePage_image, main_HomePage_text),
                new HomeRelativeBean(main_MeasurePage_rela, main_MeasurePage_image, main_MeasurePage_text),
                new HomeRelativeBean(main_CounselingPage_rela, main_CounselingPage_image, main_CounselingPage_text),
                new HomeRelativeBean(main_MinePage_rela, main_MinePage_image, main_MinePage_text));
        presenter.setSelect(0);//设置被选中的标题颜色
        presenter.initFragment(getSupportFragmentManager(), R.id.main_fragLayout);//初始化fragment并显示第一个fragment
        main_MinePage_text = findViewById(R.id.main_MinePage_text);
        presenter.clearCache(this);
        presenter.ConnectRongIM(this, this);

        if (JpRegister.getInstance().isJPSHSucc(MainActivity.this) == false) {
            JpRegister.getInstance().setAlias(MainActivity.this, User.tele);
            Log.e("激光推送在MainActivity注册----", "Login激光推送注册失败");
        } else {
            Log.e("激光推送在LoginActiity注册----", "Login激光推送注册成功");
        }
        JkezAPIMain.initSDK(this, "wanyult");//血压体温测量
        JkezAPIMain.openBluetooth();//打开蓝牙



        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("检测到最新版本,请更新");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://sj.qq.com/myapp/search.htm?kw=%E5%AE%87%E5%8C%BB%E5%B9%B3%E6%9D%BF"));
                startActivity(intent);
                alertDialog.dismiss();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();

            }
        });
        alertDialog = builder.create();
        httpTools = HttpTools.getHttpToolsInstance();
        if (isNetworkConnected()) {
            httpTools.CheckVersion(handler);//检测版本号
        } else {
            Toast.makeText(MainActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.main_HomePage_rela, R.id.main_MeasurePage_rela, R.id.main_CounselingPage_rela, R.id.main_MinePage_rela})
    public void cli(View vi) {
        switch (vi.getId()) {
            case R.id.main_HomePage_rela://首页
                presenter.setSelect(0);
                presenter.ShowFragment(0);
                break;
            case R.id.main_MeasurePage_rela://测量
                presenter.setSelect(1);
                presenter.ShowFragment(1);

                break;
            case R.id.main_CounselingPage_rela://咨询
                presenter.setSelect(2);
                presenter.ShowFragment(2);
                break;
            case R.id.main_MinePage_rela://我的
                presenter.setSelect(3);
                presenter.ShowFragment(3);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    //获取容云信息或者与容云链接失败(第一次链接失败时从新链接，第二次失败时不在执行链接)
    @Override
    public void onTokenError(String message) {
        if (connectCount == 1) {
            Log.e("MainActivity", "获取容云信息／链接容云服务器第 一次 链接失败：" + message);
            presenter.ConnectRongIM(this, this);
            connectCount = 2;
        } else {
            toast.getInstance().text(this, message);
            Log.e("MainActivity", "获取容云信息／链接容云服务器第 二次 链接失败：" + message);
        }
    }

    //链接容云成功
    @Override
    public void onTokenSucc() {

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    private int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionCode = packInfo.versionCode;
        Log.e("本地软件版本号=", versionCode + "");
        return versionCode;
    }

    /**
     * 判断有没有网
     *
     * @return
     */
    public boolean isNetworkConnected() {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        JkezAPIMain.closeBluetooth();
    }
}