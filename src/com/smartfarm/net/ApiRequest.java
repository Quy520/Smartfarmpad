package com.smartfarm.net;

import java.util.ArrayList;
import com.smartfarm.bean.ResponseModel;

public class ApiRequest {
	private ApiRequest(){}
	private static ApiRequest aRequest=new ApiRequest();
	
	public static ApiRequest getApiRequest(){
        return aRequest;
    }
	
	private ArrayList<ResponseModel> list = new ArrayList<ResponseModel>();

	public ArrayList<ResponseModel> getList() {
		return list;
	}

	public void setList(ArrayList<ResponseModel> list) {
		this.list = list;
	}
	
	
	
}
