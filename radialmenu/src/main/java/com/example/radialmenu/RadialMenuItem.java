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

/**
 * @author Arindam Nath (strider2023@gmail.com)
 */
public class RadialMenuItem {

	private String mMenuID;
	private String mMenuName;
	private int drawableInt;
	private OnRadailMenuClick mCallback;
	
	/**
	 * @param mMenuID
	 * @param mMenuName
	 */
	public RadialMenuItem(String mMenuID, String mMenuName) {
		this.mMenuID = mMenuID;
		this.mMenuName = mMenuName;
	}

	public RadialMenuItem(String menuID, String menuName, int drawableInt) {
		mMenuID = menuID;
		mMenuName = menuName;
		this.drawableInt = drawableInt;
	}

	/**
	 * @return the mMenuID
	 */
	public String getMenuID() {
		return mMenuID;
	}
	
	/**
	 * @return the mMenuName
	 */
	public String getMenuName() {
		return mMenuName;
	}


	public int getDrawableInt() {
		return drawableInt;
	}

	public void setDrawableInt(int drawableInt) {
		this.drawableInt = drawableInt;
	}

	/**
	 * 
	 * @param onRadailMenuClick
	 */
	public void setOnRadialMenuClickListener(OnRadailMenuClick onRadailMenuClick) {
		this.mCallback = onRadailMenuClick;
	}
	
	/**
	 * 
	 * @return
	 */
	public OnRadailMenuClick getOnRadailMenuClick() {
		return mCallback;
	}
}
