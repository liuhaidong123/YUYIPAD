package com.technology.yuyipad.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technology.yuyipad.R;
import com.technology.yuyipad.httptools.HttpTools;
import com.technology.yuyipad.httptools.UrlTools;

public class AdDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView title, grade, content;
    private HttpTools httpTools;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 43) {//
                Object o = msg.obj;
                if (o != null && o instanceof com.technology.yuyipad.bean.NewInforAdDetails.Root) {
                    com.technology.yuyipad.bean.NewInforAdDetails.Root information = (com.technology.yuyipad.bean.NewInforAdDetails.Root) o;

                    if (information.getCode().equals("0")) {

                        Picasso.with(getApplicationContext()).load(UrlTools.BASE +information.getResult().getPicture()).error(R.mipmap.errorpicture).into(imageView);
                        title.setText(information.getResult().getTitle());
                        grade.setText(information.getResult().getSmallTitle());
                        content.setText(information.getResult().getContent());
                    } else {
                        Toast.makeText(AdDetailsActivity.this, "获取详情错误", Toast.LENGTH_SHORT).show();
                    }


                }
            } else if (msg.what == 204) {//
                Toast.makeText(AdDetailsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        httpTools = HttpTools.getHttpToolsInstance();
        httpTools.getNewInformationAdDetails(handler, getIntent().getLongExtra("id", -1));
        imageView = (ImageView) findViewById(R.id.infor_img);
        title = (TextView) findViewById(R.id.infor_title);
        grade = (TextView) findViewById(R.id.infor_grade);
        content = (TextView) findViewById(R.id.infor_content);
    }
}
