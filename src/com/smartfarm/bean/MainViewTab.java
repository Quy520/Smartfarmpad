package com.smartfarm.bean;

import com.smartfarm.view.IndexFragment;

import com.smartfarm.view.LightFragment;
import com.smartfarm.view.TempFragment;
import com.smartfarm.view.WaterFragment;
/**
 * 
 * 跳转页面tab
 * 主页面，温度控制页面，光照控制页面，湿度控制页面
 * @author Lei
 *
 */

public class MainViewTab {
	
	public static final int VIEW_FLAG_TEMP = 0;
	public static final int VIEW_FLAG_LIGHT = VIEW_FLAG_TEMP + 1;
	public static final int VIEW_FLAG_WATER = VIEW_FLAG_LIGHT + 1;
	public static final int VIEW_FLAG_INDEX = VIEW_FLAG_WATER + 1;

	public static final int COUNT = 3;
	
	public static final MainViewTab INDEX_VIEW = new MainViewTab(VIEW_FLAG_INDEX, IndexFragment.class.getName());
	public static final MainViewTab TEMP_VIEW = new MainViewTab(VIEW_FLAG_TEMP, TempFragment.class.getName());
	public static final MainViewTab LIGHT_VIEW = new MainViewTab(VIEW_FLAG_LIGHT, LightFragment.class.getName());
	public static final MainViewTab WATER_VIEW = new MainViewTab(VIEW_FLAG_WATER, WaterFragment.class.getName());
	
	public MainViewTab(int index, String name) {
		
		this.name = name;
		this.index = index;
	}

	
	private int index;
	private String name;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
