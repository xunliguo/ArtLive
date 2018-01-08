package com.example.rh.artlive.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.rh.artlive.R;


/**
 * 带百分比的ProgressView
 *
 */
public class PercentProgressView extends View {

    private Paint mPaint;
    // 当前进度颜色
    private int mProgressColor;
    // 总进度颜色
    private int mTotalColor;
    // view的宽度
    private int mWidth;
    // view的高度
    private int mHeight;
    // 当前进度
    private float mProgress;
    // 总进度
    private float mTotal;
    // 字体大小
    private float mTextSize;
    // 进度文字偏移
    private float mTextOffset;
    // 进度条高度
    private float mProgressHeight;
    // 真实宽度
    private int realWidth;
    // 文字默认宽度
    private static final float TEXT_SIZE = 20;
    // 文字偏移
    private static final float TEXT_OFFSET = 10;
    // 默认进度条高度
    private static final float DEFAULT_PROGRESS_HEIGHT = 4;

    public PercentProgressView(Context context) {
        this(context, null);
    }

    public PercentProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PercentProgressView);
        // 当前进度颜色
        mProgressColor = a.getColor(R.styleable.PercentProgressView_progress_color, Color.RED);
        // 总进度颜色
        mTotalColor = a.getColor(R.styleable.PercentProgressView_total_color, Color.GRAY);
        // 当前的进度
        mProgress = a.getFloat(R.styleable.PercentProgressView_progress, 0f);
        // 总量
        mTotal = a.getFloat(R.styleable.PercentProgressView_total, 100f);
        // 字体大小
        float defaultTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE, getResources()
                        .getDisplayMetrics());
        mTextSize = a.getDimension(R.styleable.PercentProgressView_text_size, defaultTextSize);
        // 进度条高度
        float defaultProgressHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PROGRESS_HEIGHT, getResources()
                        .getDisplayMetrics());
        mProgressHeight = a.getDimension(R.styleable.PercentProgressView_progress_height, defaultProgressHeight);
        // 进度文字偏移
        float defaultTextOffset = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, TEXT_OFFSET, getResources()
                        .getDisplayMetrics());
        mTextOffset = a.getDimension(R.styleable.PercentProgressView_text_offset, defaultTextOffset);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(mProgressHeight);
        mPaint.setColor(mProgressColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = measureHeight(mode, size);
        setMeasuredDimension(mWidth, mHeight);
        realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    //测量高度
    private int measureHeight(int mode, int size) {
        int result;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int h = (int) (getPaddingBottom() + getPaddingTop() +
                    Math.max(mProgressHeight, Math.abs(mPaint.descent() - mPaint.ascent())));
            result = h;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(h, size);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        // 设置画笔颜色为已完成的颜色
        mPaint.setColor(mProgressColor);
        float p = getPercent();
        // 得到百分比
        String s = (int) (p * 100) + "%";
        // 测量文字的宽度
        float textWidth = mPaint.measureText(s);
        
        canvas.drawRect(getPaddingLeft(), (mHeight - mProgressHeight) / 2,
                getPaddingLeft() + (realWidth - textWidth - mTextOffset) * p, (mHeight + mProgressHeight) / 2, mPaint);
				// 测量文字的高度
        float textHeight = Math.abs((mPaint.descent() + mPaint.ascent()) / 2);
				
        canvas.drawText(s, getPaddingLeft() + (realWidth - textWidth - mTextOffset) * p + mTextOffset / 2,
                textHeight + mHeight / 2, mPaint);
				// 设置画笔颜色为未完成的颜色
        mPaint.setColor(mTotalColor);

        canvas.drawRect(getPaddingLeft() + (realWidth - textWidth - mTextOffset) * p + mTextOffset + textWidth, (mHeight - mProgressHeight) / 2,
                mWidth - getPaddingRight(), (mHeight + mProgressHeight) / 2, mPaint);
        canvas.restore();
    }

    //得到当前进度
    public float getPercent() {
        return mProgress / mTotal > 1 ? 1 : mProgress / mTotal;
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
    }

    public float getProgress() {
        return mProgress;
    }

    public float getTotal() {
        return mTotal;
    }

    public void setTotal(float mTotal) {
        this.mTotal = mTotal;
    }
}
