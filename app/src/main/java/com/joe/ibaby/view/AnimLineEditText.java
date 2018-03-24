package com.joe.ibaby.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.joe.ibaby.R;
import com.joe.ibaby.helper.ResourceUtil;

/**
 * Created by QiaoJF on 2016/10/24.
 */

public class AnimLineEditText extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener {

    private Paint normalPaint;
    private Paint pressPaint;

    private final int NORMAL_PAINT_WIDTH = 2;
    private final int PRESS_PAINT_WIDTH = 3;
    /**
     * 字体到划线之间的距离
     */
    private final int LINE2BOTTOM_WIDTH = 6;
    private final int ANIM_DURATION = 800;

    private int normalColor = ResourceUtil.INSTANCE.getColor(R.color.txt_normal);
    private int pressColor = ResourceUtil.INSTANCE.getColor(R.color.colorPrimary);
    /**
     * 画布起始坐标
     */
    private int startX;
    private int startY;
    /**
     * 记录划线长度
     */
    private float changeWidth;

    /**
     * 实际划线长度
     */
    private int lengthX;
    private int lengthY;

    public AnimLineEditText(Context context) {
        super(context);
        init();
    }

    public AnimLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setBackgroundDrawable(null);
        //初始化默认
        normalPaint = new Paint();
        normalPaint.setStyle(Paint.Style.STROKE);
        normalPaint.setStrokeWidth(NORMAL_PAINT_WIDTH);
        normalPaint.setColor(normalColor);

        pressPaint = new Paint();
        pressPaint.setStyle(Paint.Style.STROKE);
        pressPaint.setStrokeWidth(PRESS_PAINT_WIDTH);
        pressPaint.setStrokeCap(Paint.Cap.SQUARE);

        pressPaint.setColor(pressColor);

        setOnFocusChangeListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int padL = this.getPaddingLeft();//获取框内左边留白
        int padR = this.getPaddingRight();//获取框内右边留白
        int padT = this.getPaddingTop();//获取框内顶部留白
        int lines = this.getLineCount();//获取行数
        float size = this.getTextSize();//获取字体大小
        float baseTop = padT + size/2.5f ;//从上向下第一条线的位置
        float gap = this.getLineHeight();//获取行宽

        lengthX = getWidth() - getPaddingRight();
        lengthY = getHeight() - PRESS_PAINT_WIDTH - LINE2BOTTOM_WIDTH;

        canvas.drawLine(padL,lengthY,lengthX,lengthY,normalPaint);
        canvas.drawLine(padL,lengthY,padL+changeWidth,lengthY,pressPaint);

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    public void setFocusChange(){
        changeWidth = 0;
        final ValueAnimator animator = ValueAnimator.ofFloat(1f,100f);
        animator.setDuration(ANIM_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animator.getAnimatedValue();
                changeWidth = (lengthX / 100f) * (value);
                invalidate();
                //如果快速切换 结束动画
                if (!isFocused()) {
                    animation.cancel();
                    setNoFocusChange();
                }
            }
        });
        animator.start();
    }

    public void setNoFocusChange(){
        changeWidth = 0;
        invalidate();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            setFocusChange();
        }else{
            setNoFocusChange();
        }
    }

}
