package com.technology.yuyipad.Lview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by wanyu on 2017/10/20.
 */

public class MyGirdView extends GridView{
    public MyGirdView(Context context) {
        super(context);
    }

    public MyGirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算listview或者gridview高度的公式
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        //调用时将计算好的高度传入即可；
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
