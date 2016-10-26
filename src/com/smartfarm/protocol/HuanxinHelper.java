package com.smartfarm.protocol;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.smartfarm.bean.LocalEvent;
import com.smartfarm.control.HelperThread;
import com.smartfarm.db.access.HuanxinMsgDao;
import com.smartfarm.db.bean.User;
import com.smartfarm.tools.AccountManager;
import com.smartfarm.tools.AssistThread;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.SimpleConfigManager;

/**
 * 环信辅助工具
 * 
 * @author jeff
 *
 */
public class HuanxinHelper {
    
	public static final int CONN_RES_SCUESS = 666;
	public static final int CONN_RES_REMOVEED = CONN_RES_SCUESS + 1;
	public static final int CONN_RES_CONFLTCT = CONN_RES_REMOVEED + 1;
	public static final int CONN_RES_NO_NET = CONN_RES_CONFLTCT + 1;
	public static final int CONN_RES_SERVER_ERROR = CONN_RES_NO_NET + 1;
	
	private static HuanxinHelper instance = null;
	
	private boolean alreadyNotified = false;
	private boolean relinking = false;
	
	/**
	 * 获取环信辅助工具
	 * 
	 * @return
	 */
	public static HuanxinHelper get() {
		
		if(instance == null)
			instance = new HuanxinHelper();
		
		return instance;
	}
	
	private HuanxinHelper() {

		Log.e("cxy", "THE CPU_ABI = " + android.os.Build.CPU_ABI);
		EMOptions options = new EMOptions();
		options.setAcceptInvitationAlways(false);
		EMClient.getInstance().init(AppContext.context(), options);

		EMClient.getInstance().addConnectionListener(getGlobeConnectionListener());
        registerEventListener();
        
//        notifyForRecevingEvents();
	}
    
    public GlobeConnectionListener getGlobeConnectionListener() {
    	
    	return new GlobeConnectionListener();
    }
    
    private synchronized void notifyForRecevingEvents() {
    	
    	 if(alreadyNotified)
             return;

         alreadyNotified = true;
    }
    
    public synchronized void reset() {
    	
    	alreadyNotified = false;
    }
    
    public void exit() {
    	
    	reset();
    	logout(null);
    }
    
    /**
     * 重新连接环信服务器，连接过程会显示等待对话框，请在UI线程调用
     * 
     * @param context
     */
    public void relink(Activity activity) {
    	
    	if(relinking)
    		return;
    	
    	relinking = true;
    	AssistThread.getInstance().setThreadWork(new RelinkRunable(activity));
    }
    
    private void dealwithMsg(String msg) {
    	
    	Log.d(Constants.TAG, "receive huanxin msg -> " + msg);
        final Protocol protocol = ProtocolFactory.GetProtocol(msg);
        
        User user = AppContext.context().getUser();
        
        if(user == null)
        	return;
        
		String padNum = user.getPhone();
		
		if (protocol == null || !protocol.getReceiver().contains(padNum))
			return;
		
		if(protocol.getCmdType() == Constants.MOTOR_CONTROL_TYPE_SUPER_TEST) {
			
			protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
			protocol.setReceiver(protocol.getSender());
			protocol.setSender(padNum);
			protocol.send();
			return;
		}
		
		if (!SimpleConfigManager.getInstance().getReceiver()
				.contains(protocol.getSender()) && !padNum.contains(protocol.getSender()))
			return;

		if(!protocol.getPwd().equals(SimpleConfigManager.getInstance().getString("pwd")) && !protocol.getSender().equals(protocol.getReceiver())) {
			
			ProtocolFactory.getPwdErrorProtocol(protocol.getSender()).send();
			return;
		}
		
		if(protocol.getCmdType() != Constants.MOTOR_CONTROL_TYPE_TEST)
			HelperThread.getInstance().setThreadWork(new Runnable() {
				
				@Override
				public void run() {
					
					HuanxinMsgDao.add(protocol.getProtocolString());
				}
			});
		
		if (protocol.getTime() < System.currentTimeMillis() - 10 * 60 * 1000)
			return;
		
		EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(
				LocalEvent.EVENT_TYPE_RECEIVE_MSG, 0, "", protocol));
    }
    
    /**
     * 全局事件监听
     * 
     * 
     */
    protected void registerEventListener() {
    	
    	EMMessageListener messageListener = new EMMessageListener() {
			
			@Override
			public void onMessageReceived(List<EMMessage> messages) {}
			
			@Override
			public void onCmdMessageReceived(List<EMMessage> messages) {
				
			    for (EMMessage message : messages) {
			    	
                    //获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    dealwithMsg(cmdMsgBody.action());
                }
			}

			@Override
			public void onMessageReadAckReceived(List<EMMessage> messages) {}
			
			@Override
			public void onMessageDeliveryAckReceived(List<EMMessage> message) {}
			
			@Override
			public void onMessageChanged(EMMessage message, Object change) {}
		};
		
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * 注册环信帐号
     * 
     * @param account 新账号
     * @return 注册结果
     */
    public boolean register(String account) {
    	
    	try {
    		
    		EMClient.getInstance().createAccount(account, "123456");//同步方法
			
			return true;
		} catch (final HyphenateException e) {
			
			return false;
		}
    }
    
	/**
	 * 是否登录成功
	 * 
	 * @return
	 */
	public boolean isLoggedIn() {
		return EMClient.getInstance().isLoggedInBefore();
	}
    
	/**
	 * 登录
	 * 
	 * @param eMCallBack 登录回调
	 */
	public boolean login(final EMCallBack eMCallBack) {
		
		AccountManager aManager = AppContext.context().getAccountManager();
		
		if(ShowUtil.isEmpty(aManager.getPhoneNum()) || ShowUtil.isEmpty(aManager.getHuanxinPwd()))
			return false;
		Log.d("cxy","hxPwd()->"+aManager.getHuanxinPwd());
		if(!CommonTool.isNetworkConnected()) {
			
			EventBus.getDefault().noticeMsg("请检查手机网络设置!");
			return false;
		}
		
		EMClient.getInstance().login(aManager.getPhoneNum(), aManager.getHuanxinPwd(), new EMCallBack() {//回调
			@Override
			public void onSuccess() {

				if(eMCallBack != null)
					eMCallBack.onSuccess();
				
				EventBus.getDefault().runningOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager().loadAllConversations();
					}
				});
			}
		 
			@Override
			public void onProgress(int progress, String status) {
				
				if(eMCallBack != null)
					eMCallBack.onProgress(progress, status);
			}
		 
			@Override
			public void onError(int code, String message) {
				Log.d("main", "登陆聊天服务器失败！");
				if(eMCallBack != null)
					eMCallBack.onError(code, message);
			}
		});
		
		return true;
	}
	
	/**
	 * 发送消息
	 * 
	 * @param protocol
	 */
	public void sendMessage(Protocol protocol) {
		
		sendMessage(protocol, new EMCallBack() {
			
			@Override
			public void onSuccess() {
				Log.d(Constants.TAG, "send msg onSuccess");
			}
			
			@Override
			public void onProgress(int arg0, String arg1) {}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.d(Constants.TAG, "send msg onError, args : " + arg0 + " - " + arg1);
				
				if(arg0 == 201 && AppContext.context().getAccountManager().isLogined()) {

					Log.d(Constants.TAG, " to relogin user!");
					login(null);
				}
			}
		});
	}
	
	/**
	 * 发送消息
	 * 
	 * @param protocol
	 */
	public void sendMessage(Protocol protocol, EMCallBack callback) {
		
		if(!protocol.getReceiver().contains(";") && !protocol.getReceiver().contains("all")) {
			
			EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
			
			cmdMsg.setReceipt(protocol.getReceiver());
			cmdMsg.addBody(new EMCmdMessageBody(protocol.getProtocolString()));
			cmdMsg.setMessageStatusCallback(callback);
			cmdMsg.setReceipt(protocol.getReceiver());
			EMClient.getInstance().chatManager().sendMessage(cmdMsg);
		} else {
			
			List<String> receivers = SimpleConfigManager.getInstance().getReceivers();
			
			for(String receiver : receivers) {
				
				EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
				
				cmdMsg.setReceipt(protocol.getReceiver());
				cmdMsg.addBody(new EMCmdMessageBody(protocol.getProtocolString()));
				cmdMsg.setMessageStatusCallback(callback);
				cmdMsg.setReceipt(receiver);
				EMClient.getInstance().chatManager().sendMessage(cmdMsg);
			}
		}
	}
	
	/**
	 * 退出登录
	 * 
	 * @param callback
	 *            callback
	 */
	public void logout(final EMCallBack callback) {

		if(!isLoggedIn()) {
			if (callback != null) {
				callback.onSuccess();
			}
			return;
		}
		
		EMClient.getInstance().logout(true, new EMCallBack() {

			@Override
			public void onSuccess() {
			    reset();
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onProgress(int progress, String status) {
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

			@Override
			public void onError(int code, String error) {
				if (callback != null) {
					callback.onError(code, error);
				}
			}
		});
	}
	
	public void checkLink() {
		
		if(!CommonTool.isNetworkConnected() || !isLoggedIn())
			return;
		
		ProtocolFactory.getTestProtocolToSelf().send();
	}
	
    /**
     * 环信连接状态监听器
     * 
     * @author jeff
     *
     */
    class GlobeConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {

			Log.d(Constants.TAG, " connected huanxin server !");
			notifyForRecevingEvents();
			
			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_CHANGE, 1));
		}

		@Override
		public void onDisconnected(int error) {
			
			Log.e(Constants.TAG, " disconnected huanxin server ! error id -> " + error);

			int errorType = 0;
			
			if (error == EMError.USER_REMOVED) {
				
				errorType = CONN_RES_REMOVEED;
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            	
            	errorType = CONN_RES_CONFLTCT;
            } else {
            	
            	if(CommonTool.isNetworkConnected()) 
    				errorType = CONN_RES_SERVER_ERROR;
            	else
    				errorType = CONN_RES_NO_NET;
            }

			EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_CHANGE, errorType));
		}
    }
    
    class RelinkRunable implements AssistThreadWork {

    	BlockingDeque<Integer> tipsQueue;
    	ProgressDialog progressDialog = null;
    	Activity activity;
    	
    	public RelinkRunable(Activity activity) {
    		
    		this.activity = activity;
    		tipsQueue = new LinkedBlockingDeque<Integer>();
    		progressDialog = new ProgressDialog(activity);
        	progressDialog.setCancelable(false);
    		progressDialog.setMessage("正在退出当前账号...");
    		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		progressDialog.show();
    	}
    	
		@Override
		public void working() {
			
			CommonTool.delay(1000);
			
			logout(null);
			
			tipsQueue.add(1);
			AssistThread.getInstance().publishPro(RelinkRunable.this);
			
			if(!login(new EMCallBack() {

				@Override
				public void onError(final int arg0, final String arg1) {

					if(progressDialog != null) {
						
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								progressDialog.dismiss();
								ToastTool.showToast("重新连接失败，错误代码：" + arg0);
								relinking = false;
							}
						});
					}
				}

				@Override
				public void onProgress(int arg0, String arg1) {}

				@Override
				public void onSuccess() {

					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
					
					if(progressDialog != null) {
						
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								progressDialog.dismiss();
								ToastTool.showToast("已重新连接服务器");
								relinking = false;
							}
						});
					}
				}
				
			}))
				tipsQueue.add(3);
			else
				tipsQueue.add(2);
		}

		@Override
		public void ok() {
			
			if(progressDialog == null)
				return;
			
			try {
				
				int cmd = tipsQueue.take();
				
				if(cmd == 1) 
					progressDialog.setMessage("退出成功，正在尝试重新登录...");
				else if(cmd == 3) {
					progressDialog.dismiss();
					relinking = false;
				}
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}
