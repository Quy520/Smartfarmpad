package com.smartfarm.protocol;

import java.util.Vector;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.smartfarm.bean.EventHandler;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.tools.AssistThread;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.EventBus;
import com.smartfarm.view.MessageBox;
import com.smartfarm.view.MessageBox.MessageBoxButtons;

/**
 * 网络诊断
 * 
 * @author jeff
 *
 */
public class NetCheckWork implements AssistThreadWork, EventHandler {
	
	public static final String[] REASON = {
		"本地连接尚未打开，请尝试打开wifi或移动数据连接!",
		"本地连接不能连接到网络，可能因为信号不好等因素!", 
		"本地未登录服务器!",
		"服务器未响应，可能服务器延迟等原因!",
		"主手机未响应，主手机未开启或未启动程序或未联网!\n",
		"从2手机未响应，从2手机未开启或未启动程序或未联网!\n",
		"从3手机未响应，从3手机未开启或未启动程序或未联网!\n",
		"网络正常，能正常工作!\n"};
	
	public static final String[] NO_SET = {
		"暂未设置主手机!\n",
		"暂未设置从2手机!\n",
		"暂未设置从3手机!\n",
	};
	
	public static final String[] OK = {
		"本地连接已经打开;\n",
		"本地已连接上网络;\n", 
		"本地已登录;\n", 
		"本地已连接上服务器;\n",
		"本地已连接主手机!\n",
		"本地已连接从2手机!\n",
		"本地已连接从2手机!\n",};
	public static final String END = "ok!";
	
	private TextView addText;
	private TextView currText;
	private AlertDialog dialog;
	private BlockingDeque<String> tipsQueue;
	
	private Vector<Integer> receiveTest;
	
	public NetCheckWork(TextView tv, TextView curr, AlertDialog dialog) {
		addText = tv;
		currText = curr;
		this.dialog = dialog;
		tipsQueue = new LinkedBlockingDeque<String>();
		receiveTest = new Vector<Integer>();
	}
	
	@Override
	public void working() {

		EventBus.getDefault().add(this);
		
		CommonTool.delay(1000);
		if(!CommonTool.isNetworkConnected()) {
			tipsQueue.add(REASON[0]);
			AssistThread.getInstance().publishPro(this);
			tipsQueue.add(END);
			return;
		} 
		tipsQueue.add(OK[0]);
		AssistThread.getInstance().publishPro(this);

		if(!CommonTool.isConnected()) {
			tipsQueue.add(REASON[1]);
			AssistThread.getInstance().publishPro(this);
			tipsQueue.add(END);
			return;
		}
		tipsQueue.add(OK[1]);
		AssistThread.getInstance().publishPro(this);

		if(!HuanxinHelper.get().isLoggedIn()) {
			tipsQueue.add(REASON[2]);
			AssistThread.getInstance().publishPro(this);
			tipsQueue.add(END);
			return;
		}
		tipsQueue.add(OK[2]);
		AssistThread.getInstance().publishPro(this);
		
		int count = 3;
		
		receiveTest.clear();
		while(count > 0) {
			Protocol protocol= ProtocolFactory.getTestProtocolToSelf();
			protocol.send();
			
			CommonTool.delay(5000);
			
			if(receiveTest.contains(3)) 
				break;
			else 
				count--;
		}
		
		if(count <= 0) {
			tipsQueue.add(REASON[3]);
			AssistThread.getInstance().publishPro(this);
			tipsQueue.add(END);
			return;
		}
		tipsQueue.add(OK[3]);
		AssistThread.getInstance().publishPro(this);

		for(int i = 1; i <= 3; i++) {
			count = 6;
			while(count > 0) {
				Protocol protocol = ProtocolFactory.getTestRequestProtocol(i);
				
				tipsQueue.add("test-" + (7 - count));
				AssistThread.getInstance().publishPro(this);
				
				if(protocol == null) {
					count = -2;
					tipsQueue.add(NO_SET[i - 1]);
					AssistThread.getInstance().publishPro(this);
					break;
				} 

				protocol.send();
						
				CommonTool.delay(5000);
				
				if(receiveTest.contains(i - 1)) 
					break;
				else {
					count--;
				}
			}
			
			if(count > 0) {
				tipsQueue.add(OK[3 + i]);
				AssistThread.getInstance().publishPro(this);
			} else if(count == 0) {
				tipsQueue.add(REASON[3 + i]);
				AssistThread.getInstance().publishPro(this);
			}
		}

		tipsQueue.add(END);
	}

	@Override
	public void ok() {
		
		try {
			String show = tipsQueue.take();
			
			if(show.equals(END)) {
				
				dialog.cancel();
				EventBus.getDefault().remove(this);
				MessageBox.Show(addText.getContext(), addText.getText().toString(), 
						"诊断结果", MessageBoxButtons.OK);
			} else if(show.contains("test")){
				
				if(currText.getVisibility() == View.GONE)
					currText.setVisibility(View.VISIBLE);
				
				currText.setText("正在进行第" + show.split("-")[1] + "次测试！");
			} else {

				addText.append(show);
			}
		} catch(InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void onEvent(LocalEvent event) {
		
		if(event.getEventType() == LocalEvent.EVENT_TYPE_RECEIVE_TEST) {
			
			receiveTest.add(event.getEventValue());
		}
	}
}
