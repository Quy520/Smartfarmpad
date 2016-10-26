package com.smartfarm.db.bean;

import java.util.List;

public class UploadTuple {

	public final Object bean;
	public final List<?> data;
	 
	public UploadTuple(Object bean, List<?> data) {
		
		this.bean = bean;
		this.data = data;
	}
}
