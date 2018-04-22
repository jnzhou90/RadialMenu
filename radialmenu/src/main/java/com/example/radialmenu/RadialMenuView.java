/*
 * Copyright (C) 2012 
 * Arindam Nath (strider2023@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.radialmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * 
 *
 */
public class RadialMenuView extends View {

    private String[] mMenuNames;

    private int[] mMenuImages;
    
    private int mMenuCount;
    
    boolean alt;
	
	float mWidth = -1;//center of screen, will change to touch location
	
	float mHeight = -1;
	
	float mThickness;
	
	float mRadius;
	
	int selected = -1;
	
	int lastE = -1;//last event, used to prevent excessive redrawing
	
	float[] endTouch;

	private Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private RadialMenuHelperFunctions mHelperFunctions;

	// 为了避免每个 item 的点击事件的重复编写，集成为一个
    private OnRadailMenuClick mCallback;

    Context mContext;  //此变量考虑是不是使用

    private boolean hasImage;



    /**
	 * 加载Radial Menu 借助RadialMenuRenderer
	 * @param context context
	 * @param renderer renderer
	 */
	public RadialMenuView(Context context, RadialMenuRenderer renderer) {
		super(context);
		mContext = context;
		mHelperFunctions = new RadialMenuHelperFunctions();
        mMenuNames = renderer.getMenuNames();
        mMenuImages = renderer.getMenuImages();
        mMenuCount = mMenuImages.length;
		alt = renderer.isAlt();
		mThickness = renderer.getMenuThickness();
		mRadius = renderer.getRadius();
		hasImage = renderer.isHasImage();
		setVisibility(GONE); //通过mParentView调用才可实现，下面同理
		if (!hasImage) {
            initSetPaint(renderer);
        }
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
     * prevents offscreen drawing and calcs
     * @param x x coordinate
     * @param y y coordinate
     */
	public void setLoc(float x, float y) {
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


    @Override
	public void onDraw(Canvas canvas) {
		//Fixes drawing off screen
		setLoc(mWidth, mHeight);
        
       if (hasImage) {
           double bridgeLength = mRadius;
           float angleDelay = 360 / mMenuCount;
           double mStartAngle = -90;
           double left, top;


           Bitmap bitmap;

           for (int i = 0; i < mMenuCount; i++) {
               double smallRadius = ((i == selected) ? mRadius / 3 : mRadius / 4);
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
                   canvas.drawBitmap(bitmap, null, rectF, null);
               }

               mStartAngle += angleDelay;



           }
       } else {
           final RectF rect = new RectF();
           rect.set(mWidth - mRadius, mHeight - mRadius, mWidth + mRadius, mHeight + mRadius);


           mBorderPaint.setStrokeWidth(mThickness);
           //draws back of radial first
           for (int counter = 0; counter < mMenuCount; counter++) {
               if (alt)
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 90 - 360 / mMenuCount / 2), (float) (360 / mMenuCount), false, (selected == counter ? mSelectedPaint : mBgPaint));
               else
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 90), (float) (360 / mMenuCount), false, (selected == counter ? mSelectedPaint : mBgPaint));
           }

           //draws text
           for (int counter = 0; counter < mMenuCount; counter++) {
               Path arc = new Path();
               if (alt) {
                   arc.addArc(rect, (float) (360 / mMenuCount * counter - 90 - 360 / mMenuCount / 2) + 10, (float) (360 / mMenuCount) - 10);
                   canvas.drawTextOnPath(mMenuNames[counter], arc, 0, +mThickness / 8, mTextPaint);
               } else {
                   arc.addArc(rect, (float) (360 / mMenuCount * counter - 90) + 10, (float) (360 / mMenuCount) - 10);
                   canvas.drawTextOnPath(mMenuNames[counter], arc, 0, -mThickness / 8, mTextPaint);
               }
           }

           //draws separators between each option
           if (mMenuCount > 1)
               for (int counter = 0; counter < mMenuCount; counter++) {
                   if (alt) {
                       canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91 - 360 / mMenuCount / 2), 2, false, mBorderPaint);
                       canvas.drawArc(rect, (float) (360 / mMenuCount * (counter + 1) - 91 - 360 / mMenuCount / 2), 2, false, mBorderPaint);
                   } else {
                       canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91), 2, false, mBorderPaint);
                       canvas.drawArc(rect, (float) (360 / mMenuCount * (counter + 1) - 91), 2, false, mBorderPaint);
                   }
               }

           //draws outer and inner boarders
           mBorderPaint.setStrokeWidth(2);
           rect.set(mWidth - mRadius - mThickness / 2, mHeight - mRadius - mThickness / 2, mWidth + mRadius + mThickness / 2, mHeight + mRadius + mThickness / 2);

           for (int counter = 0; counter < mMenuCount; counter++) {
               if (alt) {
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91 - 360 / mMenuCount / 2), (float) (360 / mMenuCount) + 2, false, mBorderPaint);
               } else {
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91), (float) (360 / mMenuCount) + 2, false, mBorderPaint);
               }
           }

           rect.set(mWidth - mRadius + mThickness / 2, mHeight - mRadius + mThickness / 2, mWidth + mRadius - mThickness / 2, mHeight + mRadius - mThickness / 2);

           for (int counter = 0; counter < mMenuCount; counter++) {
               if (alt) {
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91 - 360 / mMenuCount / 2), (float) (360 / mMenuCount) + 1, false, mBorderPaint);
               } else {
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91), (float) (360 / mMenuCount) + 1, false, mBorderPaint);
               }
           }

           for (int counter = 0; counter < mMenuCount; counter++) {
               if (alt) {
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91 - 360 / mMenuCount / 2), (float) (360 / mMenuCount) + 1, false, mBorderPaint);
               } else {
                   canvas.drawArc(rect, (float) (360 / mMenuCount * counter - 91), (float) (360 / mMenuCount) + 1, false, mBorderPaint);
               }
           }
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
			selected = -1;
			return false;
		}

        if (mCallback != null) {
            mCallback.onRadailMenuClickedListener(mMenuNames[e]);

        }
		selected = -1;
		invalidate();
		return true;
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
			selected = -1;
			invalidate();
			return;
		}
		selected = e;
        if (mCallback != null) {
            // MenuView 事件
            mCallback.onRadailMenuActionDownListener(mMenuNames[e]);
        }
		invalidate();
	}

    /**
     * 处理此View 点击事件的入口在gestureHandler 通过renderer调用
     * @param event event
     * @param eat consumed
     * @return boolean
     */
	public boolean gestureHandler(MotionEvent event, boolean eat) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			endTouch = new float[] { event.getX(), event.getY() };
			if (mHelperFunctions.distance(mWidth, mHeight, endTouch[0], endTouch[1]) > mRadius - mThickness / 2) {
				this.setVisibility(View.GONE);
				return handleEvent((int) mHelperFunctions.angle(mWidth, mHeight, endTouch[0], endTouch[1], alt, mMenuCount));
			} else {
				this.setVisibility(View.GONE);
				return handleEvent(-1);
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mWidth = event.getX();
			mHeight = event.getY();
			this.setVisibility(View.VISIBLE);
			invalidate();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {//drag
			endTouch = new float[] { event.getX(), event.getY() };
			if (mHelperFunctions.distance(mWidth, mHeight, endTouch[0], endTouch[1]) > mRadius - mThickness / 2) {
				preEvent((int) mHelperFunctions.angle(mWidth, mHeight, endTouch[0], endTouch[1], alt, mMenuCount));
			} else {
				preEvent(-1);
			}
		}
		//Eats touch if needed, fixes scrollable elements from interfering
		return eat;
	}

    public void setOnRadialMenuClickListener(OnRadailMenuClick callback) {
        mCallback = callback;
    }
}
