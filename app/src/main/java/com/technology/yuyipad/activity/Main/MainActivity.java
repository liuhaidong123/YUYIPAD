package com.technology.yuyipad.activity.Main;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.MyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends MyActivity {
    MainPresenter presenter;
    //首页
    @BindView(R.id.main_HomePage_rela) RelativeLayout main_HomePage_rela;
    @BindView(R.id.main_HomePage_image)ImageView main_HomePage_image;
    @BindView(R.id.main_HomePage_text)TextView main_HomePage_text;
    //测量
    @BindView(R.id.main_MeasurePage_rela) RelativeLayout main_MeasurePage_rela;
    @BindView(R.id.main_MeasurePage_image)ImageView main_MeasurePage_image;
    @BindView(R.id.main_MeasurePage_text)TextView main_MeasurePage_text;
    //咨询
    @BindView(R.id.main_CounselingPage_rela) RelativeLayout main_CounselingPage_rela;
    @BindView(R.id.main_CounselingPage_image)ImageView main_CounselingPage_image;
    @BindView(R.id.main_CounselingPage_text)TextView main_CounselingPage_text;
    //我的
    @BindView(R.id.main_MinePage_rela) RelativeLayout main_MinePage_rela;
    @BindView(R.id.main_MinePage_image)ImageView main_MinePage_image;
    @BindView(R.id.main_MinePage_text)TextView main_MinePage_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder= ButterKnife.bind(this);
        presenter=new MainPresenter();
        presenter.addList(
                new HomeRelativeBean(main_HomePage_rela,main_HomePage_image,main_HomePage_text),
                new HomeRelativeBean(main_MeasurePage_rela,main_MeasurePage_image,main_MeasurePage_text),
                new HomeRelativeBean(main_CounselingPage_rela,main_CounselingPage_image,main_CounselingPage_text),
                new HomeRelativeBean(main_MinePage_rela,main_MinePage_image,main_MinePage_text));
        presenter.setSelect(0);
    }

    @OnClick({R.id.main_HomePage_rela, R.id.main_MeasurePage_rela, R.id.main_CounselingPage_rela, R.id.main_MinePage_rela})
    public void cli(View vi){
        switch (vi.getId()){
            case R.id.main_HomePage_rela://首页
                presenter.setSelect(0);
                break;
            case R.id.main_MeasurePage_rela://测量
                presenter.setSelect(1);
                break;
            case R.id.main_CounselingPage_rela://咨询
                presenter.setSelect(2);
                break;
            case R.id.main_MinePage_rela://我的
                presenter.setSelect(3);
                break;
        }

    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        return;
    }
}