package com.technology.yuyipad.Lview;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.technology.yuyipad.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wanyu on 2017/8/15.
 */
public class FormView extends View {
    float  EMPTY=-1f;
    Context con;
    boolean flagX=false,flagY=false;
    float strokeWidth=1;
    float strokeRiadio=5;
    float riadio=3;
    float textSizeLarge=30;//图表的字体大小
    float textSizeLow=20;//右上角字体的大小
    int padding=30;
    int paddingXtoY=10;//x，y周数据的距离
    int paddingXY=10;

    Paint paint;
    List<String>liX;
    List<Integer>liY;

    List<Float>listSource;
    List<Float>listOtherSource;
    List<String>listStr;
    float present=0.5f;

    int colorX=0x22f3f6;
    int colorY=0x22f3f6;
    int colorSource=0x1dbeec;
    int colorOtherSource=0x7ed66b;
    int colorBackground=0x30323a;
    int firstTextColor=0xc81dbeec;
    int otherTextColor=0xc87ed66b;
    int colorDefault=0;
    float bottomTextWidth=0f;
    int bottomTextHeight=0;
    float presentHeight=0f;
    int bottomMaxWidth=0;
    public FormView(Context context) {
        super(context);
        paint=new Paint();
        paint.setAntiAlias(true);
        this.con=context;
        initList();
        colorX=getResources().getColor(R.color.colo2);
        colorY=getResources().getColor(R.color.colo2);
        colorSource=getResources().getColor(R.color.colorSource);
        colorOtherSource=getResources().getColor(R.color.colorOtherSource);
        colorBackground=getResources().getColor(R.color.colorBackground);
        firstTextColor=getResources().getColor(R.color.firstTextColor);
        otherTextColor=getResources().getColor(R.color.otherTextColor);
        textSizeLarge=getResources().getDimension(R.dimen.textSize9);
        textSizeLow=getResources().getDimension(R.dimen.textSize9);
        listStr=new ArrayList<>();
    }
    private void initList() {
        liX=new ArrayList<>();
        liY=new ArrayList<>();
        SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.DAY_OF_MONTH,-7);
        int source=0;
        for (int i=0;i<7;i++){
            c.add(Calendar.DAY_OF_MONTH,1);
            String  data=format.format(c.getTime());
            liX.add(data);
            liY.add(source);
            source+=20;
        }
    }
    private void initListx(){
        liX=new ArrayList<>();
        SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.DAY_OF_MONTH,-7);
        int source=0;
        for (int i=0;i<7;i++){
            c.add(Calendar.DAY_OF_MONTH,1);
            String  data=format.format(c.getTime());
            liX.add(data);
            source+=20;
        }
    }
    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setAntiAlias(true);
        this.con=context;
        initList();
        colorX=getResources().getColor(R.color.colo2);
        colorY=getResources().getColor(R.color.colo2);
        colorSource=getResources().getColor(R.color.colorSource);
        colorOtherSource=getResources().getColor(R.color.colorOtherSource);
        colorBackground=getResources().getColor(R.color.colorBackground);
        firstTextColor=getResources().getColor(R.color.firstTextColor);
        otherTextColor=getResources().getColor(R.color.otherTextColor);
        textSizeLarge=getResources().getDimension(R.dimen.textSize9);
        textSizeLow=getResources().getDimension(R.dimen.textSize9);
        listStr=new ArrayList<>();
    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(colorBackground);
        int width=canvas.getWidth();
        int height=canvas.getHeight();
        if (liX!=null&& liX.size()>0){
            drawBottom(canvas,width,height);
        }
        if (liY!=null&& liY.size()>0){
            drawTop(canvas,width,height);
        }
        if (listSource!=null&&listSource.size()>0&&listSource.size()==liX.size()){
            drawLine(canvas,width,height,1);
        }
        if (listOtherSource!=null&&listOtherSource.size()>0&&listSource.size()==liX.size()){
            drawLine(canvas,width,height,2);
        }
        if (listStr!=null&&listStr.size()>0){
            drawRightText(canvas,width,height);
        }
    }
    private void drawRightText(Canvas canvas, int width, int height) {
        Rect rect=new Rect();
        paint.setTextSize(textSizeLarge);
        paint.setAntiAlias(true);
        paint.getTextBounds(listStr.get(0),0,listStr.get(0).length(),rect);
        int strWidth=rect.width();
        int strHeight=rect.height();
        int lineWidth=30;//直线的长度
        int paddingCir=5;//圆距离字体的距离
        for (int i=0;i<listStr.size();i++){
            if (i==0){
                paint.setColor(firstTextColor);
            }
            else {
                paint.setColor(otherTextColor);
            }
            if (this.colorDefault!=0){
                paint.setColor(this.colorDefault);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(listStr.get(i),width-(float)padding/2-strWidth,padding+(strHeight+paddingCir)*i+paddingCir*i,paint);
            canvas.drawLine(width-(float)padding/2-strWidth-paddingCir-riadio*2-lineWidth,padding-(float)strHeight/2+(strHeight+paddingXY)*i+2,width-(float)padding/2-strWidth-paddingCir-riadio*2,padding-(float)strHeight/2+(strHeight+paddingXY)*i+2,paint);
            paint.setStrokeWidth(strokeWidth);//宽度
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(riadio/2);
            canvas.drawCircle(width-(float)padding/2-strWidth-paddingCir-riadio,padding-(float)strHeight/2+(strHeight+paddingCir)*i+paddingCir*i+2,riadio,paint);
        }
    }
    private void drawLine(Canvas canvas,int width,int height,int type){
        List<Float>lt=new ArrayList<>();
        List<Float>listData=new ArrayList<>();
        int color=colorSource;
        switch (type){
            case 1:
                lt=listSource;
                color=colorSource;
                break;
            case 2:
                lt=listOtherSource;
                color=colorOtherSource;
                break;
        }
        if (this.colorDefault!=0){
            color=this.colorDefault;
        }
        List<MyPoint>liPoint=new ArrayList<>();
        float startX=padding+padding/2+bottomTextWidth/2;
        float startY=height-padding-bottomTextHeight-paddingXtoY;
        for (int i=0;i<lt.size();i++){
            if (lt.get(i)!=-1){
                float dat=lt.get(i)-liY.get(0);
                float presentH=dat*presentHeight;
                if (dat<0){
                    dat=present*dat;
                    if (dat<-padding){
                        dat=-padding;
                    }
                    presentH=dat;
                }
                if (lt.get(i)>liY.get(liY.size()-1)){
                    float  max=(lt.get(i)-liY.get(liY.size()-1))*present;
                    Log.e("max---","max"+max);
                    if (max>padding){
                        Log.e("max>padding","max=="+max+"--padding=="+padding);
                        max=padding;
                    }
                    dat=liY.get(liY.size()-1)-liY.get(0);
                    presentH=dat*presentHeight+max;
                }
                listData.add(lt.get(i));
                MyPoint point=new MyPoint(startX+bottomMaxWidth*i,startY-presentH-strokeRiadio);
                liPoint.add(point);
            }
        }
        if (liPoint!=null&&liPoint.size()>0){
            paint.setColor(color);
            paint.setAntiAlias(true);
            for (int i=0;i<liPoint.size();i++){
                float x=liPoint.get(i).getX();
                float y=liPoint.get(i).getY();
                if (i!=liPoint.size()-1){
                    paint.setColor(color);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setStrokeWidth(strokeWidth);
                    canvas.drawLine(x,y,liPoint.get(i+1).getX(),liPoint.get(i+1).getY(),paint);
                }
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(colorBackground);
                canvas.drawCircle(x,y,strokeRiadio,paint);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                canvas.drawCircle(x,y,strokeRiadio,paint);
                if (i==liPoint.size()-1){
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(x,y,strokeRiadio,paint);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(strokeWidth);
                    canvas.drawCircle(x,y,strokeRiadio*1.5f,paint);
                }
                paint.setTextSize(textSizeLow-2);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.LEFT);
                String text=listData.get(i)+"";
                String tt=text.substring(0,text.indexOf("."));
                String last=text.substring(text.indexOf(".")+1,text.length());
                if ("0".equals(last)){
                    text=tt;
                }
                Rect re=new Rect();
                paint.getTextBounds(text,0,text.length(),re);
                canvas.drawText(text,liPoint.get(i).getX()+strokeRiadio+re.width()/4,liPoint.get(i).getY()-riadio/3+re.height()/2,paint);
            }

        }
    }
    //绘制底部刻度
    private void drawBottom(Canvas canvas,int width,int height){
        if (liX.size()<2){
            Log.e("FormView：","error:drawBottom方法异常，您必须确保填充的X轴的list集合的size最小为2");
            return;
        }
        Rect rect=new Rect();
        paint.setTextSize(textSizeLow);
        paint.setColor(colorX);
        String text=liX.get(0);
        paint.getTextBounds(text,0,text.length(),rect);
        int wid=rect.width();
        int hei=rect.height();
        bottomTextHeight=hei;
        bottomTextWidth=wid;
        int maxWidth=(width-2*padding-padding/2)/ liX.size();
//        while (maxWidth<=wid+paddingXY){
//            textSizeLow-=2;
//            paint.setTextSize(textSizeLow);
//            paint.getTextBounds(text,0,text.length(),rect);
//            wid=rect.width();
//        }
        bottomMaxWidth=maxWidth;

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        for (int i=0;i<liX.size();i++){
            canvas.drawText(liX.get(i),padding+padding/2+maxWidth*i,height-padding,paint);
        }
    }
    //绘制顶部刻度
    private void drawTop(Canvas canvas,int width,int height){
        if (liY.get(0)>=liY.get(liY.size()-1)){
            Log.e("FormView：","error:drawTop方法error，您必须确保填充的Y轴的list集合第一个item的值小于最后一个item的值");
            return;
        }
        if (liY.size()<2){
            Log.e("FormView：","error:drawTop方法error，您必须确保填充的Y轴的list集合的size最小为2");
            return;
        }
        Rect rect=new Rect();
        String tex=liY.get(0)+"";
        paint.setTextSize(textSizeLow);
        paint.setAntiAlias(true);
        paint.getTextBounds(tex,0,tex.length(),rect);
        int topTextHeight=rect.height();//字体高度
        Log.i("topHeight==",topTextHeight+"");
        int totalHeight=height-padding-bottomTextHeight-2*padding-paddingXtoY-topTextHeight;//Y轴的数据所在的总长度（底部1个padding，顶部2个ping距离，距离x轴的字体加1个padding）
        float he=totalHeight/(liY.size()-1);//Y轴每个item的最大高度
        presentHeight=he/(liY.get(1)-liY.get(0));
        paint.setColor(colorY);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        for (int i=0;i<liY.size();i++){
            canvas.drawText(liY.get(i)+"",padding,height-padding-he*i-bottomTextHeight-paddingXtoY,paint);//(底部一个padding，底部字体高度，paddingXtoY)
//            canvas.drawLine(padding,height-padding-he*i-bottomTextHeight-strokeRiadio-paddingXtoY,width,height-padding-he*i-bottomTextHeight-strokeRiadio-paddingXtoY,paint);
        }
    }
    public void drawBottomView(List<String>lisX){
        if (lisX!=null&&lisX.size()>0){
            flagX=true;
            this.liX=lisX;
        }
        else {
            initListx();
            }
        invalidate();
    }
    //绘制Y轴数据
    public void  drawTopView(List<Integer>lisY){
        if (lisY!=null&& lisY.size()>0){
            this.liY=lisY;//Y轴数据
            flagY=true;
            invalidate();
            if (flagX!=true){
                Log.e("drawTopView:可以不更正","error:FormView必须先填充X轴的数据，才能绘制Y轴，否则X轴将使用默认的数据格式（当前日期前的7天日期），请检查您在此之前是否调用过drawBottomView（List<String>lisX）方法");
            }
        }
    }
    //绘制第一条数据()
    public void drawFirstDataView(List<Float>liSource){
        if (liSource!=null&&liSource.size()>0){
            this.listSource=liSource;
            if(flagY==true){
                if (listSource.size()==liX.size()){
                    invalidate();
                }
                else {

                    Log.e("drawDataView:需更改","error:FormView,X轴的日期个数必须与测量的集合的大小相等，否则会造成绘制的坐标与对应的日期不匹配");
                }
            }
            else {
                Log.e("drawDataView:需更改","error:FormView必须先填充X,Y轴的数据，才能绘制数据，请检查您在此之前是否调用过drawBottomView（List<String>lisX）与drawTopView（List<Integer>lisY）方法");
            }
        }
        else {
            this.listSource=new ArrayList<>();
            invalidate();
        }
    }
    //绘制第二条数据()
    public void drawOtherDataView(List<Float>liSource){
        if (liSource!=null&&liSource.size()>0){
            this.listOtherSource=liSource;
            if(flagY==true){
                if (listOtherSource.size()==liX.size()){
                    invalidate();
                }
                else {

                    Log.e("drawDataView:","error:FormView,X轴的日期个数必须与测量的集合的大小相等，否则会造成绘制的坐标与对应的日期不匹配");
                }
            }
            else {
                Log.e("drawDataView:","error:FormView必须先填充X,Y轴的数据，才能绘制数据，请检查您在此之前是否调用过drawBottomView（List<String>lisX）与drawTopView（List<Integer>lisY）方法");
            }
        }
        else {
            this.listOtherSource=new ArrayList<>();
            invalidate();
        }
    }
    //绘制右上角的说明
    public void drawRightTextView(String[]text){
        if (text==null){
            return;
        }
        listStr=new ArrayList<>();
        for (int i=0;i<text.length;i++){
            listStr.add(text[i]);
        }
    }
    public void setColor(int color){
        this.colorDefault=color;
        invalidate();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    class MyPoint{
        public MyPoint(float x,float y){
            this.x=x;
            this.y=y;
        }
        float x;

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        float y;
    }
}
