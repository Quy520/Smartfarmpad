package com.smartfarm.control;

import com.smartfarm.bean.LocalEvent;
/**
 * 
 * @author QSD
 *Msg消息的控制
 */
public interface MsgRespondDelay {
	
	public void receiveSerialMsg(String receiveMsg, byte[] data);
	
	public void pushMsgEvent(LocalEvent event);
	
	public boolean init();
	
	public boolean write(byte[] data, int len);
	
	public void closeDriver();

}
