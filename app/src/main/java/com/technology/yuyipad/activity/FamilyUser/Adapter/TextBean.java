package com.technology.yuyipad.activity.FamilyUser.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.Lview.RoundImageView;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.FamilyUser.Bean.FamilyUserListBean;
import com.technology.yuyipad.lzhUtils.MyScrollTextView;

import static android.R.attr.textSize;

/**
 * Created by wanyu on 2017/10/12.
 */
//设置字体，字体大小的bean以及部分view显示隐藏的bean
public class TextBean {
//    TextView userName,userTele;//用户名,用户电话号
//    RoundImageView userImage;
//    RelativeLayout userParentLayout,userChildLayout;//最外层的layout与textview的layout
    int pLeftPaddingMax,pLeftPaddingMin,pTopPaddingMax,pTopPaddingMin,pBottomPadding,pRightPadding,cLeftPaddingMax,cLeftPaddingMin;
    float textSizeMax,textSizeMin,textSizeNormal;//字体大小
    int textColorAdd,textColor;//添加按钮的字体颜色，非添加按钮的字体颜色
    int imageMax,imageMin;//头像
    static TextBean bean;
    Context con;
    private TextBean(Context con){
        //初始化字体大小
        textSizeMax=con.getResources().getDimension(R.dimen.textSize12);
        textSizeMin=con.getResources().getDimension(R.dimen.textSize10);
        //初始化外层的padding值
        pLeftPaddingMin= (int) con.getResources().getDimension(R.dimen.parentLeftpaddingMin);
        pLeftPaddingMax= (int) con.getResources().getDimension(R.dimen.parentLeftpaddingMax);
        pTopPaddingMax= (int) con.getResources().getDimension(R.dimen.parentToppaddingMax);
        pTopPaddingMin= (int) con.getResources().getDimension(R.dimen.parentToppaddingMin);
        pRightPadding= (int) con.getResources().getDimension(R.dimen.parentRightpadding);
        pBottomPadding= (int) con.getResources().getDimension(R.dimen.parentBottompadding);
        //初始化内层用到的padding值
        cLeftPaddingMax= (int) con.getResources().getDimension(R.dimen.childLeftPaddingMax);
        cLeftPaddingMin= (int) con.getResources().getDimension(R.dimen.childLeftPaddingMin);
        //初始化头像的大小
        imageMax= (int) con.getResources().getDimension(R.dimen.imageMax);
        imageMin= (int) con.getResources().getDimension(R.dimen.imageMin);
        //字体颜色
        textColorAdd=con.getResources().getColor(R.color.name_color);
        textColor=con.getResources().getColor(R.color.color_333333);
        textSizeNormal=con.getResources().getDimension(R.dimen.textSize15);
        this.con=con;
    }
    public static TextBean getInstance(Context con){
        if (bean==null){
            bean=new TextBean(con);
        }
        return bean;
    }
    public void setVisiable(boolean isFirstItem,  TextView userName,  TextView userTele,ImageView userImage,RelativeLayout parentLayout,RelativeLayout ChildLayout,FamilyUserListBean.ResultBean result){
        boolean isAdd=result.isAdd();//是否为添加按钮
        if (isFirstItem){//第一个字体大小18sp
                if (isAdd==true){//设置字体颜色与大小
                    userTele.setVisibility(View.GONE);//隐藏电话号
                    userName.setText("添加");
                    userName.setTextSize(textSizeNormal);
                    userName.setTextColor(textColorAdd);
                }
                else{//非添加按钮
                    //显示年龄
                    userName.setTextColor(textColor);
                    userName.setTextSize(textSizeMax);
                    userName.setText(result.getTrueName()+"  "+"("+result.getNickName()+")"+"  "+result.getAge()+"岁");
                    //显示电话号
                    userTele.setVisibility(View.VISIBLE);
                    userTele.setText(result.getTelephone()==0?"保密":result.getTelephone()+"");
                    userTele.setTextSize(textSizeMax);
                }
                //外层顶部的padding为40dp,左边距离为50dp
                parentLayout.setPadding(pLeftPaddingMin,pTopPaddingMax,pRightPadding,pBottomPadding);
                //内层的layoutpadding为15dp
                ChildLayout.setPadding(cLeftPaddingMin,0,0,0);
                //头像大小为100dp
                ViewGroup.LayoutParams params= userImage.getLayoutParams();
                params.width=imageMax;
                params.height=imageMax;
                userImage.setLayoutParams(params);

            }
            else {
                if (isAdd==true){//添加按钮
                    userTele.setVisibility(View.GONE);
                    userName.setText("添加");
                    userName.setTextSize(textSizeNormal);
                    userName.setTextColor(textColorAdd);
                }
                else {
                    userTele.setVisibility(View.GONE);
                    userName.setTextSize(textSizeMin);
                    userName.setTextColor(textColor);
                    userTele.setTextSize(textSizeMin);
                    //隐藏年龄
                    userName.setText(result.getTrueName()+"  "+"("+result.getNickName()+")");
                }
                //隐藏电话号
                userTele.setVisibility(View.GONE);
                //外层顶部的padding为12.5dp,左边距离为60dp
                parentLayout.setPadding(pLeftPaddingMax,pTopPaddingMin,pRightPadding,pBottomPadding);
                //内层的layoutpadding为15dp
                ChildLayout.setPadding(cLeftPaddingMin,0,0,0);
                //头像大小为80dp
                ViewGroup.LayoutParams params= userImage.getLayoutParams();
                params.width=imageMin;
                params.height=imageMin;
                userImage.setLayoutParams(params);
            }
        }
}
