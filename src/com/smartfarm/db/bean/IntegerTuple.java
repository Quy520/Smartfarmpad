package com.smartfarm.db.bean;

public class IntegerTuple {
	public final int min;
	public final int max;
	public final boolean hasIntersection;
	
	public IntegerTuple(int min, int max) {
		
		this.max = max;
		this.min = min;
		
		if(max > min)
			hasIntersection = true;
		else
			hasIntersection = false;
	}

	@Override
	public String toString() {
		
		return "hasIntersection -> " + hasIntersection + " [max:" + max + " , min:" + min + "]";
	}
	
	
}
