package com.smartfarm.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user") 
public class User {

    @DatabaseField(generatedId = true) 
	private int id;
	
    @DatabaseField(columnName = "name") 
	private String name;
	
    @DatabaseField(columnName = "sex") 
	private boolean sex;

    @DatabaseField(columnName = "addr") 
	private String addr;
    
    @DatabaseField(columnName = "qq") 
	private String qq;
    
    @DatabaseField(columnName = "birthday") 
	private String birthday;
	
    @DatabaseField(columnName = "phone") 
	private String phone;
    
    @DatabaseField(columnName = "userId") 
   	private int userId;
    
    @DatabaseField(columnName = "createTime") 
   	private String createTime;
    
    @DatabaseField(columnName = "portrait") 
   	private String portrait;
	
    @DatabaseField(columnName = "token") 
	private String token;

    @DatabaseField(columnName = "signature") 
	private String signature;

    @DatabaseField(columnName = "email") 
	private String email;
    
//    @DatabaseField(columnName = "hxPwd") 
    private String hxPwd;
    
	private String refreshToken;

	public User() {}
	
	public ApiUser getApiUser(User user){
		ApiUser _user=new ApiUser();
		_user.setAddr(addr);
		_user.setBirthday(birthday);
		_user.setEmail(email);
		_user.setName(name);
		_user.setQq(qq);
		_user.setSex(sex);
		_user.setSignature(signature);
		_user.setHxPwd(hxPwd);
		_user.setToken(token);
		_user.setRefreshToken(refreshToken);
		return _user;
	}
	
	public User(String name, String phone) {
		
		this.name = name;
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHxPwd() {
		return hxPwd;
	}

	public void setHxPwd(String hxPwd) {
		this.hxPwd = hxPwd;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
