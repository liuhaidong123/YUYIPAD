package com.technology.yuyipad.Lview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.PopupSettings;

/**
 * Created by wanyu on 2017/9/27.
 */

public class NewsCircle extends View{
    boolean flag=false;
    Paint paint;
    Context con;
    public NewsCircle(Context context) {
        super(context);
        this.con=context;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
    }

    public NewsCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.con=context;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        super.onDraw(canvas);
        if (flag==false){
            canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,canvas.getWidth()>canvas.getHeight()?canvas.getHeight()/2:canvas.getWidth()/2,paint);
        }
    }

    public void drawNews(boolean flag){
        this.flag=flag;
        invalidate();
    }
}
