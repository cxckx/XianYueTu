package com.example.cxcxk.xianyuetu.view.componts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;
import com.example.cxcxk.xianyuetu.R;

/**
 * Created by cxcxk on 2016/8/1.
 */
public class PopWindowItemView extends View {

    private static Paint rectPaint;
    private static Paint textPaint;
    private String text;

    public PopWindowItemView(Context context) {
        super(context);
        init();
    }

    public PopWindowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PopWindowItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //setPadding(AndroidUtils.dip2px(getContext(),16),AndroidUtils.dip2px(getContext(),16),0,0);
        if (rectPaint == null){
            rectPaint = new Paint();
            rectPaint.setColor(getResources().getColor(R.color.colorAccent));
            rectPaint.setAntiAlias(true);
            rectPaint.setDither(true);
            rectPaint.setStyle(Paint.Style.FILL);
        }
        if(textPaint == null){
            textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setAntiAlias(true);
            textPaint.setDither(true);
            textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            textPaint.setTextSize(AndroidUtils.dip2px(getContext(),10));
        }
        setWillNotDraw(false);
    }

    public void setText(String text){
        this.text = text;
        setWillNotDraw(false);
    }

    int color;
    public void setColor(int color){
        this.color = color;
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectPaint.setColor(color);
        float textHeight = textPaint.getFontMetrics().ascent+textPaint.getFontMetrics().descent;
        canvas.drawCircle(canvas.getWidth()/2, canvas.getWidth()/2, canvas.getWidth()/2-AndroidUtils.dip2px(getContext(),10), rectPaint);
        float textWidth = textPaint.measureText(text);
        float startPosX = (canvas.getWidth()-textWidth)/2;
        canvas.drawText(text,startPosX,canvas.getWidth()+AndroidUtils.dip2px(getContext(),5),textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(), 48),MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(),96),MeasureSpec.EXACTLY));
    }
}
