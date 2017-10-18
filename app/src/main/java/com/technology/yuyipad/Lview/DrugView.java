package com.technology.yuyipad.Lview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wanyu on 2017/10/17.
 */

public class DrugView extends View{
    Context con;
    Paint paint;
    int radio;//圆环的宽度
    int color;//
    boolean drawBottomLine=true;//是否绘制底部的线,默认绘制
    public DrugView(Context context) {
        super(context);
        this.con=context;
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    public DrugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.con=context;
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height=canvas.getHeight();
        int width=canvas.getWidth();
    }

    public void drawBottomLine(boolean flag){
        this.drawBottomLine=flag;
        invalidate();
    }
}
