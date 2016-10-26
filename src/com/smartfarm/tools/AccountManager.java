package com.smartfarm.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.db.bean.ApiUser;
import com.smartfarm.db.bean.User;
import com.smartfarm.protocol.HuanxinHelper;
import com.smartfarm.view.AppContext;

public class AccountManager {

	private String phoneNum;
	private long lastLoginTime;
	private int userId = -1;
	private boolean isLogined;
	private String huanxinPwd = "";
	private File accountIni;
	private String token="";

	private String refreshToken="";
	
	public AccountManager() {
		
		accountIni = new File(AppContext.context().getFilesDir()
				.getAbsolutePath() + File.separator + "push.ini");
    	
		if(checkFile())
			readInfo();
	}
	
	@Override
	public String toString() {
		return "<AccountManager : {phoneNum : " + phoneNum + ", huanxinPwd : " + huanxinPwd + "}>";
	}

	public synchronized String getRefreshToken() {
		return refreshToken;
	}

	public synchronized String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public synchronized void exit() {
		
		checkFile();
		
		BufferedWriter out = null;
		
		try {
			
			out = new BufferedWriter(new FileWriter(accountIni));
			
			Random rand = new Random();
			StringBuilder result = new StringBuilder();

			
			for(int i = 0; i < 10; i++) {

				String reandDouble = String.valueOf(rand.nextDouble());
				result.append(AESUtils.encrypt(DeviceIdHelper.get(), 
						Base64.encode(reandDouble.substring(2, reandDouble.length() - 1))));
				result.append("\r\n");
			}

			result.append(Base64.encode("userid=" + userId));
			result.append("\r\n");
			result.append(Base64.encode("serverUid=" + userId));
			result.append("\r\n");
			result.append(Base64.encode(AESUtils.encrypt(DeviceIdHelper.get(), String.valueOf(lastLoginTime))));
			result.append("\r\n");
			result.append(Base64.encode(AESUtils.encrypt(DeviceIdHelper.get(), "you exited the app")));
			result.append("\r\n");
			result.append(Base64.encode(AESUtils.encrypt(DeviceIdHelper.get(), phoneNum)));
			result.append("\r\n");
			result.append(Base64.encode(huanxinPwd));
			result.append("\r\n");
			result.append(token);
			result.append("\r\n");
			result.append(refreshToken);
			result.append("\r\n");
			
			for(int i = 0; i < 13; i++) {

				String reandDouble = String.valueOf(rand.nextDouble());
				result.append(AESUtils.encrypt(DeviceIdHelper.get(), 
						Base64.encode(reandDouble.substring(2, reandDouble.length() - 1))));
				result.append("\r\n");
			}
			
			out.write(result.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {}
		}
		
		isLogined = false;

		AppContext.context().setUser(null);
		AppContext.context().setApiUser(null);
		HuanxinHelper.get().exit();
		
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_CHANGE, -1));
//		NetManager.getInstence().setNetOk(false);
	}
	
	public synchronized void save(User user,ApiUser localUser, String phoneNum, String huanxinPwd, String token,String refreshToken) {
		
		checkFile();
		
		BufferedWriter out = null;
		
		try {
			
			out = new BufferedWriter(new FileWriter(accountIni));
			Log.d("cxy1","accountIni->"+accountIni);

			isLogined = true;
			lastLoginTime =  System.currentTimeMillis();
			userId = user.getId();
			
			this.phoneNum = phoneNum;
			this.huanxinPwd = huanxinPwd;
			this.token = token;
			this.refreshToken = refreshToken;
			
			Random rand = new Random();
			StringBuilder result = new StringBuilder();
			
			for(int i = 0; i < 10; i++) {

				String reandDouble = String.valueOf(rand.nextDouble());
				result.append(AESUtils.encrypt(DeviceIdHelper.get(), 
						Base64.encode(reandDouble.substring(2, reandDouble.length() - 1))));
				result.append("\r\n");
			}

			result.append(Base64.encode("userid=" + userId));
			result.append("\r\n");
			result.append(Base64.encode("serverUid=PGdfjFDSIJGfkds489462"));
			result.append("\r\n");
			result.append(Base64.encode(AESUtils.encrypt(DeviceIdHelper.get(), String.valueOf(lastLoginTime))));
			result.append("\r\n");
			result.append(Base64.encode(AESUtils.encrypt(DeviceIdHelper.get(), "now used smartfarm from shzt")));
			result.append("\r\n");
			result.append(Base64.encode(AESUtils.encrypt(DeviceIdHelper.get(), phoneNum)));
			result.append("\r\n");
			result.append(Base64.encode(huanxinPwd));
			result.append("\r\n");
			result.append(token);
			result.append("\r\n");
			result.append(refreshToken);
			result.append("\r\n");
			Log.d("cxy1",result.toString());
			
			for(int i = 0; i < 12; i++) {

				String reandDouble = String.valueOf(rand.nextDouble());
				result.append(AESUtils.encrypt(DeviceIdHelper.get(), 
						Base64.encode(reandDouble.substring(2, reandDouble.length() - 1))));
				result.append("\r\n");
			}
			
			out.write(result.toString());
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {}
		}
		
		AppContext.context().setUser(user);
		AppContext.context().setApiUser(localUser);
		
		HuanxinHelper.get().logout(null);
		HuanxinHelper.get().login(null);
	}
	
	public synchronized long getLastLoginTime() {
		
		return lastLoginTime;
	}
	
	public synchronized boolean isLogined() {
		
		return isLogined;
	}
	
	public synchronized String getHuanxinPwd() {
		
		return huanxinPwd;
	}
	
	public synchronized int getUserId() {
		
		return userId;
	}
	
	private boolean readInfo() {
		
		BufferedReader in = null;
		
		try {
			
			in = new BufferedReader(new FileReader(accountIni));
			Log.d("cxy1","accountIni->"+accountIni);
			String line;
			int lineCount = 0;
			while((line = in.readLine()) != null) {
				
				if(lineCount++ < 10)
					continue;
				if(lineCount <= 16)
					line = Base64.decode(line);
				
//				if(lineCount <= 18)
//					line = line;
				
				if(line.contains("userid") && line.contains("=")) {
					
					userId = Integer.valueOf(line.split("=")[1]);
					continue;
				} else if(line.contains("serverUid") && line.contains("=")) {
					
					continue;
				}
					
				if(userId == -1)
					continue;
				
				if(lineCount == 13) 
					lastLoginTime = Long.valueOf(AESUtils.decrypt(DeviceIdHelper.get(), line));
				else if(lineCount == 14)
					isLogined = AESUtils.decrypt(DeviceIdHelper.get(), line).contains("smartfarm");
				else if(lineCount == 15)
					phoneNum = AESUtils.decrypt(DeviceIdHelper.get(), line);
				else if(lineCount == 16)
					huanxinPwd = line;
				else if(lineCount == 17)
					token = line;
				else if(lineCount == 18)
					refreshToken = line;
			}
			
			return true;
			
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			
			try {
				if(in != null)
				in.close();
			} catch (IOException e) {}
		}
	}
	
	private boolean checkFile() {
		
		if(!accountIni.exists()) {
			
			BufferedWriter out = null;
			
			try {

				accountIni.createNewFile();
				out = new BufferedWriter(new FileWriter(accountIni));
				
				Random rand = new Random();
				StringBuilder result = new StringBuilder();
				
				for(int i = 0; i < 10; i++) {
					
					String reandDouble = String.valueOf(rand.nextDouble());
					result.append(Base64.encode(reandDouble.substring(2, reandDouble.length() - 1)));
					result.append("\r\n");
				}
				
				out.append(result.toString());
				out.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(out != null)
						out.close();
				} catch (IOException e) {}
			}
			
			return false;
		}
		
		return true;
	}
}
