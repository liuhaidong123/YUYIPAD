package com.technology.yuyipad.Lview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ListView;

import com.technology.yuyipad.R;

/**
 * Created by wanyu on 2017/10/12.
 */

public class FamilyUserListView extends ListView{
    Context con;
    Paint paint;
    public FamilyUserListView(Context context) {
        super(context);
        this.con=context;
        paint=new Paint();
        paint.setColor(Color.parseColor("#dcdcdc"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(con.getResources().getDimension(R.dimen.lineWidth));
    }

    public FamilyUserListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.con=context;
        paint=new Paint();
        paint.setColor(Color.parseColor("#dcdcdc"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(con.getResources().getDimension(R.dimen.lineWidth));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.getAdapter()!=null&&this.getAdapter().getCount()>1){//
            int height=canvas.getHeight()-50;
            //  高：1.5  宽：0.5  间距：2
            float linHeight=con.getResources().getDimension(R.dimen.lineHeight);//线高度
//            float lineWidth=con.getResources().getDimension(R.dimen.lineWidth);//线宽度
            float lineSpace=con.getResources().getDimension(R.dimen.lineSpace);//间距
            float startY=lineSpace/2;//起始位置的y坐标
            float startX=con.getResources().getDimension(R.dimen.parentLeftpaddingMin)*2;
            int count=(int)(height/(linHeight+lineSpace));//矩形线的总数
            for (int i=0;i<count;i++){
                canvas.drawLine(startX,startY,startX,startY+linHeight,paint);
                startY+=linHeight+lineSpace;
            }
        }
    }
}
