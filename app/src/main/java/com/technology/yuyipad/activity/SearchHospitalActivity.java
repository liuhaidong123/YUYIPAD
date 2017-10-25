package com.technology.yuyipad.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.yuyipad.DBSqlite.ResultHospitalDB;
import com.technology.yuyipad.R;
import com.technology.yuyipad.adapter.DrugHospitalResult;
import com.technology.yuyipad.adapter.SearchHistoryListViewAdapter;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.lhdUtils.InformationListView;

import java.util.ArrayList;
import java.util.List;

public class SearchHospitalActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private SearchHistoryListViewAdapter mAdapter;
    private List<com.technology.yuyipad.bean.SearchHospital.Result> mSearchHospitalList = new ArrayList<>();
    private List<String> resultHospitalList = new ArrayList<>();
    private HttpTools mHttptools;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 33) {//医院
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.SearchHospital.Root) {
                    com.technology.yuyipad.bean.SearchHospital.Root root = (com.technology.yuyipad.bean.SearchHospital.Root) o;
                    if (root.getCode().equals("0")) {
                        //MyDialog.stopDia();
                        mSearchHospitalList = root.getResult();
                        if (mSearchHospitalList.size() == 0) {
                            mPrompt_Tv.setVisibility(View.VISIBLE);
                            mHospitalScrollView.setVisibility(View.GONE);
                            mListView.setVisibility(View.GONE);
                        } else {
                            mPrompt_Tv.setVisibility(View.GONE);
                            mAdapter = new SearchHistoryListViewAdapter(SearchHospitalActivity.this, mSearchHospitalList);
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            //显示搜索到的药品布局
                            //mDrugScrollView.setVisibility(View.GONE);
                            mHospitalScrollView.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        }

                    }
                }
            } else if (msg.what == 222) {//json解析错误
                //MyDialog.stopDia();
            } else if (msg.what == 223) {//请求数据错误
                //MyDialog.stopDia();
                //ToastUtils.myToast(SearchActivity.this, "请求数据失败");
            }

        }
    };

    private TextView mCancel;
    private EditText mEdit;
    //医院数据库
    private ResultHospitalDB mResultHospitalDB;
    private SQLiteDatabase mSqliteHospitalDB;

    //显示曾经搜索过得医院
    private ScrollView mHospitalScrollView;
    private InformationListView mHospitalListView;
    private TextView mClearHospitalResult;
    private DrugHospitalResult mAdapterDrugHospitalResult;

    private TextView mPrompt_Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hospital);
        initView();
    }

    public void initView() {

        //医院数据库
        mResultHospitalDB = new ResultHospitalDB(this, "hospital_db", null, 1);
        mSqliteHospitalDB = mResultHospitalDB.getWritableDatabase();

        //显示医院曾经的搜索历史
        mHospitalScrollView = (ScrollView) findViewById(R.id.hospital_scroll);
        mHospitalListView = (InformationListView) findViewById(R.id.hospital_listview);
        mClearHospitalResult = (TextView) findViewById(R.id.clear_hospital_result);
        mClearHospitalResult.setOnClickListener(this);

        //搜索结果提示
        mPrompt_Tv = (TextView) findViewById(R.id.prompt_result);

        mHttptools = HttpTools.getHttpToolsInstance();

        //搜索的历史数据
        mListView = (ListView) findViewById(R.id.search_history_listview);

        //取消按钮
        mCancel = (TextView) findViewById(R.id.cancel_tv);
        mCancel.setOnClickListener(this);
        //输入框
        mEdit = (EditText) findViewById(R.id.edit_box);


        //查看hospital表中所有数据
        Cursor cursor = mSqliteHospitalDB.rawQuery("select * from hospital order by _id desc", null);
        while (cursor.moveToNext()) {
            String hospitalname = cursor.getString(cursor.getColumnIndex("hospitalname"));
            Log.e("医院名：", "------------" + hospitalname);
            resultHospitalList.add(hospitalname);
        }
        cursor.close();
        //显示曾经搜索过的医院
        mAdapterDrugHospitalResult = new DrugHospitalResult(SearchHospitalActivity.this, resultHospitalList);
        mHospitalListView.setAdapter(mAdapterDrugHospitalResult);

        mHospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHttptools.getSearchHospitalData(mHandler, resultHospitalList.get(position));
                // MyDialog.showDialog(SearchActivity.this);
            }
        });
        //显示曾经搜索过的医院布局
        mPrompt_Tv.setVisibility(View.GONE);
        mHospitalScrollView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        //清除历史按钮
        if (resultHospitalList.size() > 0) {
            mClearHospitalResult.setVisibility(View.VISIBLE);
        } else {
            mClearHospitalResult.setVisibility(View.GONE);
        }


        //点击搜索按钮，回调这个方法(注意：如果不处理的话，此方法会被调用2次，搜索按下和松开都会被调用)
        mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //搜索医院接口
                        if (!getEditTxt().equals("")) {
                            if (event.getAction() == KeyEvent.ACTION_UP) {
                                //MyDialog.showDialog(SearchHospitalActivity.this);
                                mHttptools.getSearchHospitalData(mHandler, getEditTxt());
                                ContentValues valuesHospital = new ContentValues();
                                valuesHospital.put("hospitalname", getEditTxt());
                                mSqliteHospitalDB.insert("hospital", null, valuesHospital);
                                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        } else {
                            Toast.makeText(SearchHospitalActivity.this, "请输入查询的医院名称", Toast.LENGTH_SHORT).show();
                        }

                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mCancel.getId()) {
            //隐藏软键盘
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            finish();
        }  else if (id == mClearHospitalResult.getId()) {//清空曾经搜索过医院的表
            mSqliteHospitalDB.execSQL("delete from hospital");
            resultHospitalList.clear();
            mAdapterDrugHospitalResult.notifyDataSetChanged();
            mClearHospitalResult.setVisibility(View.GONE);
        }
    }



    //判断搜索框里是否有内容
    public String getEditTxt() {
        if (mEdit.getText().toString().trim() != null && !mEdit.getText().toString().trim().equals("")) {
            return mEdit.getText().toString().trim();
        }
        return "";
    }
}
