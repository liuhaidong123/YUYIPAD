package com.technology.yuyipad.activity.FamilyUser.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.Lview.RoundImageView;
import com.technology.yuyipad.R;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.activity.FamilyUser.Bean.FamilyUserListBean;
import com.technology.yuyipad.bean.AdBean.Result;
import com.technology.yuyipad.lzhUtils.MyScrollTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanyu on 2017/10/12.
 */
//家庭用户列表的list
public class FamilyUserListAdapter extends BaseAdapter{
    List<FamilyUserListBean.ResultBean>list;
    Context con;
    public FamilyUserListAdapter(List<FamilyUserListBean.ResultBean>list,Context con){
        this.list=list;
        this.con=con;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHodler hodler;
        if (view==null){
            view= LayoutInflater.from(con).inflate(R.layout.item_familyusermanagerlist,null);
            hodler=new ViewHodler(view);
            view.setTag(hodler);
        }
        else {
            hodler= (ViewHodler) view.getTag();
        }

            hodler.familyuserList_item_userName.setFocusable(false);
            if (i==0){//第一项
                TextBean.getInstance(con).setVisiable(true,hodler.familyuserList_item_userName,hodler.familyuserList_item_userTele,hodler.familyuserList_item_image,hodler.familyuserList_item_layout,
                        hodler.familyuserList_item_textLayout,list.get(i));
                if ( hodler.familyuserList_item_userName.getEllipsize() == null) {
                    hodler.familyuserList_item_userName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                }
            }
            else {
                TextBean.getInstance(con).setVisiable(false,hodler.familyuserList_item_userName,hodler.familyuserList_item_userTele,hodler.familyuserList_item_image,hodler.familyuserList_item_layout,
                        hodler.familyuserList_item_textLayout,list.get(i));
                if ( hodler.familyuserList_item_userName.getEllipsize() != null) {
                    hodler.familyuserList_item_userName.setEllipsize(null);
                }
            }
        return view;
    }

    class ViewHodler{
        public ViewHodler(View vi){
            ButterKnife.bind(this,vi);
        }
        @BindView(R.id.familyuserList_item_layout)RelativeLayout familyuserList_item_layout;//整体的item最外层的layout(第一个的paddingtop40dp，其余的为12.5dp)
        @BindView(R.id.familyuserList_item_textLayout)RelativeLayout familyuserList_item_textLayout;//包容文字TextView的layout(第一个的marginleft=15dp，其余的为10dp)
        @BindView(R.id.familyuserList_item_image)RoundImageView familyuserList_item_image;//头像（第一个的宽高度为100dp，其余的为80dp）

        @BindView(R.id.familyuserList_item_userName)MyScrollTextView familyuserList_item_userName;//用户名
        @BindView(R.id.familyuserList_item_userTele)TextView familyuserList_item_userTele;//电话号
    }
}
