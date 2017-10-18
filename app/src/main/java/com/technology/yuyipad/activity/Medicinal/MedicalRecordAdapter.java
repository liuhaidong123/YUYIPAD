package com.technology.yuyipad.activity.Medicinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.technology.yuyipad.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanyu on 2017/10/17.
 */
//药品状态适配器
public class MedicalRecordAdapter extends BaseAdapter implements IFirst{
    //一共4步骤状态从1到4
    String[] str1=new String[]{"医务人员正在准备药材哦！","配药已完成，医务人员正在将药品火速送往煎药处！","熬制中药需要时间较长，请您耐心等候哦","您的中药已完成，请尽快到医院取药处取药！"};
    String[]str2=new String[]{"药材准备中","配药已完成","中药熬制中","中药熬制已完成"};
    int[]image1=new int[]{R.mipmap.s1,R.mipmap.s2,R.mipmap.s3,R.mipmap.s4};
    int[]image2=new int[]{R.mipmap.picture1,R.mipmap.picture2,R.mipmap.picture3,R.mipmap.picture4};
    Context con;
    Type tp;
    int selectPos=0;
    public MedicalRecordAdapter(Context con,Type tp){
        this.con=con;
        this.tp=tp;
    }
    @Override
    public int getCount() {
        int count=1;
        switch (tp){
            case ONE:
                count=1;
                break;
            case TWO:
                count=2;
                break;
            case THREE:
                count=3;
                break;
            case FOUR:
                count=4;
                break;
        }
        return count;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler hodler;
        if (view==null){
            view= LayoutInflater.from(con).inflate(R.layout.item_medicalrecord_list,null);
            hodler=new ViewHodler(view);
            view.setTag(hodler);
        }
        else {
            hodler= (ViewHodler) view.getTag();
        }
        init(i,hodler.medicalRecordList_stateImage,hodler.medicalRecordList_stateLine,hodler.medicalRecordList_statePicture,hodler.medicalRecordList_stateInfo,hodler.medicalRecordList_stateText);
        if (hodler.fouceView.isFocusable()){
            hodler.medicalRecordList_stateText.setText(str2[i]);
        }
        else {
            hodler.medicalRecordList_stateText.setText("");
            }
        return view;
    }
    @Override
    public void onVisiableFirst(int pos) {
        this.selectPos=pos;
        notifyDataSetChanged();
    }
    class  ViewHodler{
        public ViewHodler(View vi){
            ButterKnife.bind(this,vi);
        }
        @BindView(R.id.medicalRecordList_stateImage)ImageView medicalRecordList_stateImage;//当前的步骤的image
        @BindView(R.id.medicalRecordList_stateLine)View medicalRecordList_stateLine;//步骤imaeg下边的线
        @BindView(R.id.medicalRecordList_statePicture)ImageView medicalRecordList_statePicture;//系统提供的药品状态图片
        @BindView(R.id.medicalRecordList_stateInfo)TextView medicalRecordList_stateInfo;//说明文字
        @BindView(R.id.medicalRecordList_stateText)TextView medicalRecordList_stateText;//说明文字上方的
        @BindView(R.id.fouceView)View fouceView;
    }
    private void init(int pos,ImageView medicalRecordList_stateImage,View medicalRecordList_stateLine,ImageView medicalRecordList_statePicture,TextView medicalRecordList_stateInfo,TextView medicalRecordList_stateText){
        int max=Type.getTypeInt(tp)-1;
        medicalRecordList_stateImage.setImageResource(image1[pos]);
        medicalRecordList_statePicture.setImageResource(image2[pos]);
        medicalRecordList_stateText.setText(str2[pos]);
        medicalRecordList_stateInfo.setText(str1[pos]);
        if (pos!=max){
            medicalRecordList_stateLine.setVisibility(View.VISIBLE);
            }
            else {
            medicalRecordList_stateLine.setVisibility(View.GONE);
            }
        if (pos==selectPos){
            medicalRecordList_stateText.setText(str2[pos]);
        }else {
            medicalRecordList_stateText.setText("");
        }
    }
    public enum Type{
        ONE,TWO,THREE,FOUR;//1，2，3，4步骤
        public static Type getType(int count){
            Type tps=ONE;
            switch (count){
                case 1:
                    tps=ONE;
                    break;
                case 2:
                    tps=TWO;
                    break;
                case 3:
                    tps=THREE;
                    break;
                case 4:
                    tps=FOUR;
                    break;
            }
            return tps;
        }
        public static int getTypeInt(Type tp){
            int ps=1;
            switch (tp){
                case ONE:
                    ps=1;
                    break;
                case TWO:
                    ps=2;
                    break;
                case THREE:
                    ps=3;
                    break;
                case FOUR:
                    ps=4;
                    break;
                        }
            return ps;
        }
    }
}

