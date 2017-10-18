package com.technology.yuyipad.activity.Medicinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.R;
import com.technology.yuyipad.bean.AdBean.Result;
import com.technology.yuyipad.code.ExitLogin;
import com.technology.yuyipad.lzhUtils.MyActivity;

import butterknife.BindView;

//我的药品
public class MyMedicalActivity extends MyActivity implements IMedical,AdapterView.OnItemClickListener{
    MyMedicalPresneter presneter;
    BeanDrugStates bean;
    MyMedicalListAdapter adapterList;//药品记录列表的适配器
    int firstVisiblePos=0;
    IFirst iFirst;
    @BindView(R.id.titleinclude_text)TextView titleinclude_text;
    @BindView(R.id.myMedical_medicalListView)ListView myMedical_medicalListView;//药品记录的listview
    @BindView(R.id.myMedical_recordListView)ListView myMedical_recordListView;//药品状态的listview
    @BindView(R.id.myMedical_emptyView)ImageView myMedical_emptyView;//空view
    @BindView(R.id.myMedical_emptyLayout)RelativeLayout myMedical_emptyLayout;//空数据的layout
    @BindView(R.id.myMedical_emptyTextView)TextView myMedical_emptyTextView;//显示状态的view
    //一共4步骤状态从1到4
    String[] str1=new String[]{"医务人员正在准备药材哦！","配药已完成，医务人员正在将药品火速送往煎药处！","熬制中药需要时间较长，请您耐心等候哦","您的中药已完成，请尽快到医院取药处取药！"};
    String[]str2=new String[]{"药材准备中","配药已完成","中药熬制中","中药熬制已完成"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChilidView(R.layout.activity_my_medical);
        titleinclude_text.setText("我的药品");
        presneter=new MyMedicalPresneter();
        myMedical_emptyTextView.setText("正在查询。。。");
        presneter.getMedicalList(this);
    }

    //获取我的药品列表成功
    @Override
    public void onGetMedicalListSuccess( BeanDrugStates bean) {
        this.bean=bean;
        myMedical_emptyLayout.setVisibility(View.GONE);
        setData();
    }
    //获取我的药品列表失败
    @Override
    public void onGetMedicalListError(String msg, String interfaceName) {
        //数据库中只有在至少有一条数据的情况下才会保存当前的字符串，所以此处不用在做任何判断或者异常捕获
        String resStr= IDbUtlis.getInstance().getOkhttpString(this,interfaceName);
        BeanDrugStates bean= gson.gson.fromJson(resStr,BeanDrugStates.class);
        if (bean!=null){
            myMedical_emptyLayout.setVisibility(View.GONE);
            this.bean=bean;
            setData();
        }
        else {
            myMedical_emptyTextView.setText(msg);
            }
    }
    //token失效
    @Override
    public void onTokenError() {
        ExitLogin.getInstance().showLogin(this);
    }
    //拿到数据后给两个listview设置适配器
    private void setData(){
        adapterList=new MyMedicalListAdapter(this,bean.getResult());
        myMedical_medicalListView.setAdapter(adapterList);
        myMedical_medicalListView.setOnItemClickListener(this);
        initBean(0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        initBean(i);
    }
    private void initBean(int selectPos){
        if (bean!=null&&bean.getResult()!=null&&bean.getResult().size()>0){
            for (int i=0;i<bean.getResult().size();i++){
                bean.getResult().get(i).setSelectMedical(false);
            }
            bean.getResult().get(selectPos).setSelectMedical(true);
            adapterList.notifyDataSetChanged();
            MedicalRecordAdapter adapter=new MedicalRecordAdapter(this,MedicalRecordAdapter.Type.getType(bean.getResult().get(selectPos).getState()));
            myMedical_recordListView.setAdapter(adapter);
            firstVisiblePos=0;
            setIFirst(adapter);
            myMedical_recordListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    int firstItemPos=myMedical_recordListView.getFirstVisiblePosition();
                    int lastItemPos=myMedical_recordListView.getLastVisiblePosition();
                       if (lastItemPos!=firstItemPos){
                               if (lastItemPos-firstItemPos==1){//两个的时候显示前一个
                                   iFirst.onVisiableFirst(firstItemPos);
                               }
                               else if (lastItemPos-firstItemPos==2){//3个的时候显示中间的
                                   iFirst.onVisiableFirst(firstItemPos+1);
                               }
                       }
                }
                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                }
            });
        }
        else {
            Log.e("MyMedicalActivity","错误：initBean方法，数据源bean不能为null");
        }
    }

    public void setIFirst(IFirst iFirst){
        this.iFirst=iFirst;
    }
}
