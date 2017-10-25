package com.technology.yuyipad.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.technology.yuyipad.DBSqlite.DBHelper;
import com.technology.yuyipad.R;
import com.technology.yuyipad.adapter.ResultCityAdapter;
import com.technology.yuyipad.bean.City;
import com.technology.yuyipad.lhdUtils.MyLetterListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class LocationActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private MapView mapView;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private ListView mAllCityListView, mSearch_city_result_listview;
    private AllCityListAdapter mAllCityAdapter;
    private MyLetterListView myLetterListView;
    private ResultCityAdapter mResultCityAdapter;
    private HashMap<String, Integer> mAlphaIndexer = new HashMap<String, Integer>();//当前汉语拼音首字母和与之对应的列表位
    private String[] mSections;//存放汉语拼音首字母
    private ArrayList<City> mAllCity_list = new ArrayList<>();//所有城市集合
    private ArrayList<City> mSearch_city_result_list = new ArrayList<>();//搜索城市结果集合
    private EditText mEditText;//输入编辑框
    private RelativeLayout mRl_Rl;
    //将全部城市按拼音排序时所需要的对象
    private Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City o1, City o2) {
            String a = o1.getPinyi().substring(0, 1);
            String b = o2.getPinyi().substring(0, 1);
            //如果前面的大那么返回1，后面的大返回-1；此位置相同，继续比较下一位，直到最后一位，如果都相同的话，就返回0；
            int flag = a.compareTo(b);
            return flag;
        }
    };


    private TextView myLocation;
    private   GeocodeSearch geocoderSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        myLocation = (TextView) findViewById(R.id.location_city);
        mapView = (MapView) findViewById(R.id.map);
        aMap = mapView.getMap();
        mapView.onCreate(savedInstanceState);

        initLocation();//定位当前城市
        checkPermission();//定位需要判断权限
        getAllCityList();//初始化定位页面的所有城市数据
        //所有城市
        mAllCityListView = (ListView) findViewById(R.id.all_city_listview);
        mAllCityAdapter = new AllCityListAdapter(this, mAllCity_list);
        mAllCityListView.setAdapter(mAllCityAdapter);
        //选择城市在地图上显示
        mAllCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectCityLocation(mAllCity_list.get(i).getName(),mAllCity_list.get(i).getName());
            }
        });

        //拼音listview
        myLetterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
        myLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());

        //搜索列表
        mSearch_city_result_listview = (ListView) findViewById(R.id.search_city_listview);//搜索城市时的城市列表ListView
        mResultCityAdapter = new ResultCityAdapter(mSearch_city_result_list, this);//搜索城市Adapter
        mSearch_city_result_listview.setAdapter(mResultCityAdapter);//设置搜索城市Adapter
        //选择搜索到的城市在地图上显示
        mSearch_city_result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectCityLocation(mSearch_city_result_list.get(i).getName(),mSearch_city_result_list.get(i).getName());
            }
        });
        mRl_Rl = (RelativeLayout) findViewById(R.id.rl_rl);
        mEditText = (EditText) findViewById(R.id.search_edit);//输入编辑框

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myLetterListView.setVisibility(View.GONE);
            }
        });
        //搜索城市
        mEditText.setOnEditorActionListener(this);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() == null || "".equals(charSequence.toString())) {
                    mAllCityListView.setVisibility(View.VISIBLE);
                    myLetterListView.setVisibility(View.VISIBLE);
                    mRl_Rl.setVisibility(View.VISIBLE);
                    mSearch_city_result_listview.setVisibility(View.GONE);

                } else {
                    mAllCityListView.setVisibility(View.GONE);
                    myLetterListView.setVisibility(View.GONE);
                    mRl_Rl.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }


    /**
     * 选择城市显示在地图上
     * @param cityName
     */
    private void selectCityLocation(String address,String cityName){
        //使用定位经纬度获取地址信息
        geocoderSearch = new GeocodeSearch(this);
        //第一个参数是地址，第二个参数是城市
        GeocodeQuery query = new GeocodeQuery(address, cityName);

        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    GeocodeQuery query1 = geocodeResult.getGeocodeQuery();
                    myLocation.setText(query1.getCity());
                    query1.getLocationName();

                    List<GeocodeAddress> list = geocodeResult.getGeocodeAddressList();

                    if (list.size() != 0) {
                        for (int k = 0; k < list.size(); k++) {
                            double lon = list.get(k).getLatLonPoint().getLongitude();
                            double lat = list.get(k).getLatLonPoint().getLatitude();
                            Log.e("经度", list.get(k).getLatLonPoint().getLongitude() + "");
                            Log.e("纬度", list.get(k).getLatLonPoint().getLatitude() + "");
                            Log.e("城市", list.get(k).getCity() + "");
                            Log.e("等级", list.get(k).getLevel() + "");
                            Log.e("区域编码", list.get(k).getAdcode() + "");
                            Log.e("建筑物", list.get(k).getBuilding() + "");
                            Log.e("区域", list.get(k).getDistrict() + "");
                            Log.e("FormatAddress", list.get(k).getFormatAddress() + "");
                            String address= list.get(k).getFormatAddress();
                            Log.e("Neighborhood", list.get(k).getNeighborhood() + "");
                            Log.e("省份", list.get(k).getProvince() + "");
                            Log.e("Township", list.get(k).getTownship() + "");
                            UiSettings uiSettings = aMap.getUiSettings();
                            uiSettings.setZoomControlsEnabled(false);
                            uiSettings.setCompassEnabled(true);

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(lat, lon), 15, 0, 0));
                            aMap.moveCamera(cameraUpdate);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(lat,lon));
                            markerOptions.title(address);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            markerOptions.draggable(true);

                            Marker marker = aMap.addMarker(markerOptions);
                            marker.showInfoWindow();// 设置默认显示一个infowinfow

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "无法查询当前城市", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.e("错误码--", "" + i);
                    myLocation.setText("未定位");
                }
            }
        });

        geocoderSearch.getFromLocationNameAsyn(query);//开始定位选择城市
    }

    //定位
    public void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
       // mLocationOption.setInterval(1000);//设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setOnceLocation(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //解析定位结果

                        myLocation.setText(aMapLocation.getDistrict());

                        Log.e("当前定位城市",aMapLocation.getCity());
                        Log.e("getDistrict",aMapLocation.getDistrict());
                        Log.e("getAddress",aMapLocation.getAddress());
                        Log.e("getAoiName",aMapLocation.getAoiName());
                        Log.e("getBuildingId",aMapLocation.getBuildingId());
                        Log.e("getCountry",aMapLocation.getCountry());
                        Log.e("getDescription",aMapLocation.getDescription());
                        Log.e("getLocationDetail",aMapLocation.getLocationDetail());
                        Log.e("getStreet",aMapLocation.getStreet());
                        if (aMapLocation.getDistrict().equals("涿州市")){
                            selectCityLocation("涿州市中医医院",aMapLocation.getDistrict());//在地图上显示城市
                        }
                    } else {
                        myLocation.setText("未定位1");

                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    //地图实现定位蓝点
    private void gaoDeMap() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.interval(100000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);

    }

    public static final int LOCATION_CODE = 123;

    public void checkPermission() {
        //sdk版本>=23时，
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            //如果定位权限没有授权
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //请求授权， 点击允许或者拒绝时会回调onRequestPermissionsResult（），
                //注意 ：如果是在fragment中申请权限，不要使用ActivityCompat.requestPermissions，
                //直接使用requestPermissions （new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_READ_PHONE）
                //否则不会调用onRequestPermissionsResult（）方法。
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);//
                return;

            } else {//如果已经授权，执行业务逻辑
                gaoDeMap();
                Toast.makeText(this, "已经授权23", Toast.LENGTH_SHORT).show();
            }

        } else { //版本小于23时，不需要判断敏感权限，执行业务逻辑
            gaoDeMap();
            Toast.makeText(this, "版本小于23", Toast.LENGTH_SHORT).show();
        }
    }

    //请求授权， 点击允许或者拒绝时会回调onRequestPermissionsResult（），
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                //点击了允许，授权成功
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getApplicationContext(), "定位授权成功", Toast.LENGTH_SHORT).show();
                    gaoDeMap();
                    //点击了拒绝，授权失败
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), "定位授权失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

            if (!"".equals(getEditTxt())) {
                mSearch_city_result_list.clear();
                getSearchResultCityList(getEditTxt());
                if (mSearch_city_result_list.size() != 0) {
                    mAllCityListView.setVisibility(View.GONE);
                    myLetterListView.setVisibility(View.GONE);
                    mRl_Rl.setVisibility(View.GONE);
                    mSearch_city_result_listview.setVisibility(View.VISIBLE);
                    mResultCityAdapter.setmResultList(mSearch_city_result_list);
                    mResultCityAdapter.notifyDataSetChanged();
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LocationActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    mAllCityListView.setVisibility(View.VISIBLE);
                    myLetterListView.setVisibility(View.VISIBLE);
                    mRl_Rl.setVisibility(View.VISIBLE);
                    mSearch_city_result_listview.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "抱歉，没有搜索到该的城市", Toast.LENGTH_SHORT).show();
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LocationActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }
            } else {
                Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
            }


            return true;
        }
        return false;
    }


    /**
     * 定位页面所有城市adapter
     */
    public class AllCityListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> list = new ArrayList<City>();//定位页面所有的城市

        public AllCityListAdapter(Context context, List<City> list) {
            this.context = context;
            this.list = list;
            this.inflater = LayoutInflater.from(this.context);
            mSections = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为" "
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1).getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    mAlphaIndexer.put(name, i);
                    mSections[i] = name;
                }
            }
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gs_search_result_listview_item, null);
                holder = new ViewHolder();
                holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(list.get(position).getName());
            String currentStr = getAlpha(list.get(position).getPinyi());
            String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getPinyi()) : " ";
            if (!previewStr.equals(currentStr)) {
                holder.alpha.setVisibility(View.VISIBLE);
                holder.alpha.setText(currentStr);
            } else {
                holder.alpha.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView alpha;// 首字母标题
        TextView name;// 城市名字
    }

    /**
     * 从city表中获取定位页面所有城市集合（包含城市名称和城市拼音）
     */
    private void getAllCityList() {
        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            db.execSQL("update city set name='阿拉善盟' where name='阿拉善'");
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                mAllCity_list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将集合中的城市排序
        Collections.sort(mAllCity_list, comparator);
    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }

    //触摸右侧拼音的监听事件
    public class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(String s) {
            //isScroll = false;
            if (mAlphaIndexer.get(s) != null) {
                int position = mAlphaIndexer.get(s);
                mAllCityListView.setSelection(position);
                // mOverlay.setText(s);
                //mOverlay.setVisibility(View.VISIBLE);
                // handler.removeCallbacks(overlayThread);
                // handler.postDelayed(overlayThread, 1000);
            }
        }
    }


    /**
     * 获取搜索时的城市，并排序
     * 参数：EditText输入的内容
     *
     * @return
     */
    private void getSearchResultCityList(String keyword) {
        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city where name like \"%" + keyword + "%\" or pinyin like \"%" + keyword + "%\"", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                mSearch_city_result_list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(mSearch_city_result_list, comparator);
    }


    //判断搜索框里是否有内容
    public String getEditTxt() {
        if (mEditText.getText().toString().trim() != null && !mEditText.getText().toString().trim().equals("")) {
            return mEditText.getText().toString().trim();
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();//销毁地图
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();//，重新绘制加载地图
    }


    @Override
    protected void onPause() {
        super.onPause();

        mapView.onPause();//暂停地图的绘制
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);////在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
    }
}
