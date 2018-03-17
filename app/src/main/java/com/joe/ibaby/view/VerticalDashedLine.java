package com.joe.ibaby.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.joe.ibaby.R;

/**
 *
 * 竖向的虚线
 * 
 */
public class VerticalDashedLine extends View {

	private DashPathEffect effects;
	private Paint paint;
	private Path path;
	private int mLineColor;
	private float mLineWidth;

	public VerticalDashedLine(Context context) {
		super(context);
	}

	public VerticalDashedLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint(context,attrs);
	}

	public VerticalDashedLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPaint(context,attrs);
	}

	private void initPaint(Context context, AttributeSet attrs){

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalDashedLine);
		if(typedArray != null){
			mLineColor = typedArray.getColor(R.styleable.VerticalDashedLine_lineColor,
					context.getResources().getColor(R.color.dash_line));
			mLineWidth = typedArray.getDimension(R.styleable.VerticalDashedLine_lineWidth,
					context.getResources().getDimension(R.dimen.vertical_dash_line));
			typedArray.recycle();
		}

		effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paint = new Paint();
		path = new Path();

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(mLineColor);
		paint.setStrokeWidth(mLineWidth);

		paint.setPathEffect(effects);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		path.moveTo(0, 0);
		path.lineTo(0, getHeight());
		canvas.drawPath(path, paint);
	}

}