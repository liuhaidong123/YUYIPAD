package com.technology.yuyipad.activity.Main;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wanyu on 2017/9/25.
 */
//首页切换的实体类
public class HomeRelativeBean {
    RelativeLayout rela;
    ImageView imageView;
    TextView textView;
    public HomeRelativeBean(RelativeLayout rel,ImageView image,TextView text){
        this.rela=rel;
        this.imageView=image;
        this.textView=text;
    }
    //设置drawable的select属性
    public void setSelect(boolean flag){
        rela.setSelected(flag);
        imageView.setSelected(flag);
        textView.setSelected(flag);
    }
}
