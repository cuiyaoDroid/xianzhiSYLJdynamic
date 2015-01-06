package com.xianzhi.tool.view;


import com.xianzhisylj.dynamiclcz.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author naiyu(http://snailws.com)
 * @version 1.0
 */
public class TasksCompletedView extends View {
	private Paint mCirclePaint;
	private Paint mRingPaint;
	private Paint mTextPaint;
	private int mCircleColor;
	private int mRingColor;
	private float mRadius;
	private float mRingRadius;
	private float mStrokeWidth;
	private int mXCenter;
	private int mYCenter;
	private float mTxtWidth;
	private float mTxtHeight;
	private int mTotalProgress = 100;
	private int mProgress;

	public TasksCompletedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TasksCompletedView, 0, 0);
		mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
		mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
		mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
		mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);
		
		mRingRadius = mRadius + mStrokeWidth / 2;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Paint.Style.STROKE);
		mCirclePaint.setStrokeWidth(mStrokeWidth);
		
		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setARGB(255, 46, 121, 195);
		mTextPaint.setTextSize(mRadius / 2);
		
		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {

		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
//		canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
		if (mProgress > 0 ) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
			canvas.drawArc(oval, -90, (-(float)mProgress / mTotalProgress) * 360, false, mRingPaint); 
			canvas.drawArc(oval, -90, (1-(float)mProgress / mTotalProgress) * 360, false, mCirclePaint); 
//			canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);
			String txt = mProgress+"";
			String txt2="/";
			String txt3=mTotalProgress+"";
			float length1=mTextPaint.measureText(txt, 0, txt.length());
			float length2=mTextPaint.measureText(txt2, 0, txt2.length());
			float length3=mTextPaint.measureText(txt3, 0, txt3.length());
			mTxtWidth =length1+length2+length3;
			mTextPaint.setARGB(255, 46, 121, 195);
			canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
			mTextPaint.setARGB(255, 0, 0, 0);
			canvas.drawText(txt2, mXCenter - mTxtWidth / 2+length1, mYCenter + mTxtHeight / 4, mTextPaint);
			mTextPaint.setARGB(255, 255, 144, 0);
			canvas.drawText(txt3, mXCenter - mTxtWidth / 2+length1+length2, mYCenter + mTxtHeight / 4, mTextPaint);
		}
	}
	public void setTotalProgress(int mTotalProgress){
		this.mTotalProgress = mTotalProgress;
	}
	public void setProgress(int progress) {
		mProgress = progress;
//		invalidate();
		postInvalidate();
	}

}
