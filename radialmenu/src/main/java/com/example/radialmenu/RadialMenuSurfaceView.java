package com.example.radialmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Description :
 * Copyright (c) ${year}, lixiaozheng.good@gmail.com All Rights Reserved.
 * 参考java规范 https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md
 * Created by 李争 on 2018/4/22 15:39.
 */
public class RadialMenuSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private RadialMenuHelperFunctions mHelperFunctions;

    private String[] mMenuNames;

    private int[] mMenuImages;

    private int mMenuCount;

    private float mWidth = -1;//center of screen, will change to touch location

    private float mHeight = -1;

    private float mThickness;

    private float mRadius;

    private boolean mHasImage;

    private int mSelected = -1;

    private int lastE = -1;//last event, used to prevent excessive redrawing

    private Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    //这一步我们主要是定义三个成员变量以备后面绘图时使用

    private SurfaceHolder mSurfaceHolder;
    //绘图的Canvas
    private Canvas mCanvas;
    //子线程标志位
    private boolean mIsDrawing;

    private OnRadailMenuClick mCallback;




    //TODO: 自定义属性 以及其他构造方法



    public RadialMenuSurfaceView(Context context, RadialMenuRenderer renderer) {
        super(context);

        getExtraByRenderer(renderer);

        setVisibility(GONE); //通过mParentView调用才可实现，下面同理

        initSurfaceView();

        if (!mHasImage) {
            initSetPaint(renderer);
        }

    }




    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        //开启子线程
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }



    @Override
    public void run() {
        while (mIsDrawing) {
            drawView();
        }

    }


    /**
     * 处理此View 点击事件的入口在gestureHandler 通过renderer调用
     * @param event event
     * @param eat consumed
     * @return boolean
     */
    public boolean gestureHandler(MotionEvent event, boolean eat) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mWidth = event.getX();
                mHeight = event.getY();
                this.setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mHelperFunctions.distance(mWidth, mHeight, event.getX(), event.getY()) > mRadius - mThickness / 2) {
                    preEvent((int) mHelperFunctions.angle(mWidth, mHeight, event.getX(), event.getY(), mMenuCount));
                } else {
                    preEvent(-1);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mHelperFunctions.distance(mWidth, mHeight, event.getX(), event.getY()) > mRadius - mThickness / 2) {
                    this.setVisibility(View.GONE);
                    return handleEvent((int) mHelperFunctions.angle(mWidth, mHeight,event.getX(), event.getY(), mMenuCount));
                } else {
                    this.setVisibility(View.GONE);
                    return handleEvent(-1);
                }
        }
        return eat;
    }

    /**
     * 设置回调监听事件
     * @param callback 在使用此View的context设置callback
     */
    public void setOnRadialMenuClickListener(OnRadailMenuClick callback) {
        mCallback = callback;
    }

    /**
     * 通过renderer获取变量值
     * @param renderer renderer
     */
    private void getExtraByRenderer(RadialMenuRenderer renderer) {
        mHelperFunctions = new RadialMenuHelperFunctions();
        mMenuNames = renderer.getMenuNames();
        mMenuImages = renderer.getMenuImages();
        mMenuCount = mMenuImages.length;
        mThickness = renderer.getMenuThickness();
        mRadius = renderer.getRadius();
        mHasImage = renderer.isHasImage();
    }

    /**
     * 初始化SurfaceView。
     * 初始化这三个成员变量并且注册对应的回调方法
     */
    private void initSurfaceView() {
        mSurfaceHolder = getHolder();
        //注册回调方法
        mSurfaceHolder.addCallback(this);
        //设置一些参数方便后面绘图
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
    }
    /**
     * 初始化Paint
     * @param renderer renderer
     */
    private void initSetPaint(RadialMenuRenderer renderer) {
        mBgPaint.setColor(renderer.getMenuBackgroundColor());
        mBgPaint.setStrokeWidth(renderer.getMenuThickness());
        mBgPaint.setStyle(Paint.Style.STROKE);

        mSelectedPaint.setColor(renderer.getMenuSelectedColor());
        mSelectedPaint.setStrokeWidth(renderer.getMenuThickness());
        mSelectedPaint.setStyle(Paint.Style.STROKE);

        mBorderPaint.setColor(renderer.getMenuBorderColor());
        mBorderPaint.setStrokeWidth(renderer.getMenuThickness());
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mTextPaint.setColor(renderer.getMenuTextColor());
        mTextPaint.setTextSize(renderer.getMenuThickness() / 2);
    }

    /**
     * 绘制逻辑
     */
    private void drawView(){
        setLoc(mWidth, mHeight);

        try {
            //获得canvas对象
            mCanvas = mSurfaceHolder.lockCanvas();

            if (mHasImage) {
                double bridgeLength = mRadius;
                float angleDelay = 360 / mMenuCount;
                double mStartAngle = -90;
                double left, top;


                Bitmap bitmap;

                for (int i = 0; i < mMenuCount; i++) {
                    double smallRadius = ((i == mSelected) ? mRadius / 3 : mRadius / 4);
                    left = mWidth +
                            Math.round(bridgeLength
                                    * Math.cos(Math.toRadians(mStartAngle))
                                    - smallRadius / 2);
                    top = mHeight +
                            Math.round(bridgeLength
                                    * Math.sin(Math.toRadians(mStartAngle))
                                    - smallRadius / 2);

                    bitmap = BitmapFactory.decodeResource(getResources(), mMenuImages[i]);
                    RectF rectF = new RectF();
                    rectF.left = (float) left;
                    rectF.top = (float) top;
                    rectF.right = (float) (left + smallRadius * 2);
                    rectF.bottom = (float) (top + smallRadius * 2);
                    if (bitmap != null) {
                        mCanvas.drawBitmap(bitmap, null, rectF, null);
                    }
                    mStartAngle += angleDelay;
                }
            } else {
                final RectF rect = new RectF();
                rect.set(mWidth - mRadius, mHeight - mRadius, mWidth + mRadius, mHeight + mRadius);
                mBorderPaint.setStrokeWidth(mThickness);
                //draws back of radial first
                for (int counter = 0; counter < mMenuCount; counter++) {
                    mCanvas.drawArc(rect, (float) (360 / mMenuCount * counter - 90),
                            (float) (360 / mMenuCount), false,
                            (mSelected == counter ? mSelectedPaint : mBgPaint));
                }

                //draws text
                for (int counter = 0; counter < mMenuCount; counter++) {
                    Path arc = new Path();
                    arc.addArc(rect, (float) (360 / mMenuCount * counter - 90) + 10,
                            (float) (360 / mMenuCount) - 10);
                    mCanvas.drawTextOnPath(mMenuNames[counter], arc, 0,
                            -mThickness / 8, mTextPaint);
                }
                //draws separators between each option
                if (mMenuCount > 1) {
                    for (int counter = 0; counter < mMenuCount; counter++) {
                        mCanvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91),
                                2, false, mBorderPaint);
                        mCanvas.drawArc(rect, (float) (360 / mMenuCount * (counter + 1) - 91),
                                2, false, mBorderPaint);
                    }
                }

                //draws outer and inner boarders
                mBorderPaint.setStrokeWidth(2);
                rect.set(mWidth - mRadius - mThickness / 2,
                        mHeight - mRadius - mThickness / 2,
                        mWidth + mRadius + mThickness / 2,
                        mHeight + mRadius + mThickness / 2);
                for (int counter = 0; counter < mMenuCount; counter++) {
                    mCanvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91),
                            (float) (360 / mMenuCount) + 2,
                            false, mBorderPaint);
                }

                rect.set(mWidth - mRadius + mThickness / 2,
                        mHeight - mRadius + mThickness / 2,
                        mWidth + mRadius - mThickness / 2,
                        mHeight + mRadius - mThickness / 2);

                for (int counter = 0; counter < mMenuCount; counter++) {
                    mCanvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91),
                            (float) (360 / mMenuCount) + 1, false, mBorderPaint);
                }
                for (int counter = 0; counter < mMenuCount; counter++) {
                    mCanvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91),
                            (float) (360 / mMenuCount) + 1, false, mBorderPaint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }


    /**
     * prevents offscreen drawing and calcs
     * @param x x coordinate
     * @param y y coordinate
     */
    private void setLoc(float x, float y) {
        if (x < mRadius + mThickness / 2)
            x = mRadius + mThickness / 2;
        if (y < mRadius + mThickness / 2)
            y = mRadius + mThickness / 2;

        if (y > this.getHeight() - (mRadius + mThickness / 2))
            y = this.getHeight() - (mRadius + mThickness / 2);
        if (x > this.getWidth() - (mRadius + mThickness / 2))
            x = this.getWidth() - (mRadius + mThickness / 2);

        mWidth = x;
        mHeight = y;
    }

    /**
     * Handles moving gestures that haven't finished yet.
     * @param e e
     */
    private void preEvent(int e) {
        if (e == mMenuCount)
            e = 0;
        else if (lastE == e)
            return;
        lastE = e;
        if (e == -1) {
            mSelected = -1;
            return;
        }
        mSelected = e;
        if (mCallback != null) {
            // MenuView 事件
            mCallback.onRadailMenuActionDownListener(mMenuNames[e]);
        }
    }

    /**
     * Handles resulting event from onTouch up.
     * @param e 第几个
     * @return consumed
     */
    private boolean handleEvent(int e) {
        if (e == mMenuCount)
            e = 0;
        else if (e == -1) {
            mSelected = -1;
            return false;
        }
        if (mCallback != null) {
            mCallback.onRadailMenuClickedListener(mMenuNames[e]);
        }
        mSelected = -1;
        return true;
    }
}
