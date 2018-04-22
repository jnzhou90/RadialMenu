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

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

/** 
 * @author Arindam Nath (strider2023@gmail.com)
 */
public class RadialMenuRenderer {


    private String[] mMenuNames;

    private int[] mMenuImages;

	private boolean alt = false;
	
	private float mThickness = 30;
	
	private float mRadius = 60;
	
	private int mMenuBackgroundColor = 0x80444444;
	
	private int mMenuSelectedColor = 0x8033b5e5;
	
	private int mMenuTextColor = Color.WHITE;
	
	private int mMenuBorderColor = 0xff777777;
	
	private View mParentView;

	private boolean hasImage;

	/**
	 * @param
	 * @param alt
	 * @param mThick
	 * @param mRadius
	 */
	public RadialMenuRenderer(View parentView, boolean alt, float mThick, float mRadius, boolean hasImage) {
		this.mParentView = parentView;
		this.alt = alt;
		this.mThickness = mThick;
		this.mRadius = mRadius;
		this.hasImage = hasImage;
	}

    /**
     * 设置图标 和 相应的名字
     * @param mMenuNames 名字数组
     * @param mMenuImages 图标数组
     */
	public void setRadialMenuContent(String[] mMenuNames, int[] mMenuImages) {
	    this.mMenuNames = mMenuNames;
	    this.mMenuImages = mMenuImages;
	}
	
	public RadialMenuView renderView() {
		final RadialMenuView menu = new RadialMenuView(mParentView.getContext(), this);
		mParentView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return menu.gestureHandler(event, true);
			}
		});
		return menu;
	}

	public RadialMenuSurfaceView renderSurfaceView() {
		final RadialMenuSurfaceView menu = new RadialMenuSurfaceView(mParentView.getContext(), this);
		mParentView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return menu.gestureHandler(event, true);
			}
		});
		return menu;
	}


    public String[] getMenuNames() {
        return mMenuNames;
    }

    public int[] getMenuImages() {
        return mMenuImages;
    }

    /**
	 * @return the alt
	 */
	public boolean isAlt() {
		return alt;
	}

	/**
	 * @return the mThick
	 */
	public float getMenuThickness() {
		return mThickness;
	}

	/**
	 * @return the mRadius
	 */
	public float getRadius() {
		return mRadius;
	}

	/**
	 * @return the mMenuBackgroundColor
	 */
	public int getMenuBackgroundColor() {
		return mMenuBackgroundColor;
	}

	/**
	 * @param mMenuBackgroundColor the mMenuBackgroundColor to set
	 */
	public void setMenuBackgroundColor(int mMenuBackgroundColor) {
		this.mMenuBackgroundColor = mMenuBackgroundColor;
	}

	/**
	 * @return the mMenuSelectedColor
	 */
	public int getMenuSelectedColor() {
		return mMenuSelectedColor;
	}

	/**
	 * @param mMenuSelectedColor the mMenuSelectedColor to set
	 */
	public void setMenuSelectedColor(int mMenuSelectedColor) {
		this.mMenuSelectedColor = mMenuSelectedColor;
	}

	/**
	 * @return the mMenuTextColor
	 */
	public int getMenuTextColor() {
		return mMenuTextColor;
	}

	/**
	 * @param mMenuTextColor the mMenuTextColor to set
	 */
	public void setMenuTextColor(int mMenuTextColor) {
		this.mMenuTextColor = mMenuTextColor;
	}

	/**
	 * @return the mMenuBorderColor
	 */
	public int getMenuBorderColor() {
		return mMenuBorderColor;
	}

	/**
	 * @param mMenuBorderColor the mMenuBorderColor to set
	 */
	public void setMenuBorderColor(int mMenuBorderColor) {
		this.mMenuBorderColor = mMenuBorderColor;
	}

    public boolean isHasImage() {
        return hasImage;
    }
}
