package com.smartfarm.control;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.smartfarm.control.runable.ClearDeviceRunable;
import com.smartfarm.control.runable.LightCloseRunable;
import com.smartfarm.control.runable.LightOpenRunable;
import com.smartfarm.control.runable.LightReadRunable;
import com.smartfarm.control.runable.ModifyDeviceRunable;
import com.smartfarm.control.runable.MoistureReadRunable;
import com.smartfarm.control.runable.ReadDeviceRunable;
import com.smartfarm.control.runable.SerialControlRunable;
import com.smartfarm.control.runable.TempCloseRunable;
import com.smartfarm.control.runable.TempOpenRunable;
import com.smartfarm.control.runable.TempReadRunable;
import com.smartfarm.control.runable.TempStopRunable;
import com.smartfarm.control.runable.WaterBengCloseRunable;
import com.smartfarm.control.runable.WaterBengOpenRunable;
import com.smartfarm.control.runable.WaterCloseRunable;
import com.smartfarm.control.runable.WaterOnekeyCloseRunable;
import com.smartfarm.control.runable.WaterOnekeyOpenRunable;
import com.smartfarm.control.runable.WaterOpenRunable;
import com.smartfarm.control.runable.WaterReadRunable;
import com.smartfarm.control.runable.WaterYaoCloseRunable;
import com.smartfarm.control.runable.WaterYaoOpenRunable;
import com.smartfarm.view.SimpleConfigManager;

public class SerialHelper {

	public static SerialControl seraialControl = new SerialControl();

	public static SerialControl get() {

		return seraialControl;
	}

	public static void clearRunnable() {
		seraialControl.cleanRunnable();
	}

	public static void readAll(int source) {
		TempReadRunable runable = new TempReadRunable(0, -1, source);
		runable.setIds(getAllIds());
		get().setSerialRunable(runable);
	}

	public static void readDevice() {
		ReadDeviceRunable runable = new ReadDeviceRunable();
		get().setSerialRunable(runable);
	}

	public static void modifyDeviceID(int originalID, int newID) {
		ModifyDeviceRunable runable = new ModifyDeviceRunable(originalID,newID);
		get().setSerialRunable(runable);
	}

	public static void modifyDeviceID(int number) {
		ModifyDeviceRunable runable = new ModifyDeviceRunable(0,0);
		runable.setIds(getAllDevices(number));
		get().setSerialRunable(runable);
	}

	public static void clearDeviceRunable() {
		ClearDeviceRunable runable = new ClearDeviceRunable();
		get().setSerialRunable(runable);
	}

	public static void readOthers1(int source) {
		MoistureReadRunable runable = new MoistureReadRunable(0, source);
		get().setSerialRunable(runable);
	}

	public static void readOthers1(int source, OnTaskFinishListener listener) {
		MoistureReadRunable runable = new MoistureReadRunable(0, source);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void readLight(int source) {

		LightReadRunable runable = new LightReadRunable(source);
		get().setSerialRunable(runable);
		/*
		 * MoistureReadRunable runable=new MoistureReadRunable(0, source);
		 * get().setSerialRunable(runable);
		 */
	}

	public static void readLight(int source, OnTaskFinishListener listener) {

		LightReadRunable runable = new LightReadRunable(source);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void readWater(int source) {
		Log.d("zqq", "读取湿度");
		WaterReadRunable runable = new WaterReadRunable(source, 0);
		get().setSerialRunable(runable);
	}

	public static void readWater(int source, OnTaskFinishListener listener) {
		Log.d("zqq", "读取湿度");
		WaterReadRunable runable = new WaterReadRunable(source, 0);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void openLightAll(int source) {

		LightOpenRunable runable = new LightOpenRunable(0, source);
		// runable.setIds(getAllLights());
		get().setSerialRunable(runable);
	}

	public static void openLight(int id, int source) {

		LightOpenRunable runable = new LightOpenRunable(id, source);
		get().setSerialRunable(runable);
	}

	public static void openLight(int source, List<Integer> windowIds,
			OnTaskFinishListener listener) {

		LightOpenRunable runable = new LightOpenRunable(0, source);
		runable.setIds(windowIds);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void closeLightAll(int source) {

		LightCloseRunable runable = new LightCloseRunable(0, source);
		// runable.setIds(getAllLights());
		get().setSerialRunable(runable);
	}

	public static void closeLight(int id, int source) {

		LightCloseRunable runable = new LightCloseRunable(id, source);
		get().setSerialRunable(runable);
	}

	public static void closeLight(int source, List<Integer> windowIds,
			OnTaskFinishListener listener) {

		LightCloseRunable runable = new LightCloseRunable(0, source);
		runable.setIds(windowIds);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	// 暂时没有要运行的内容
	public static void openKeyWater(int source) {
		WaterOnekeyOpenRunable runable = new WaterOnekeyOpenRunable(source);
		get().setSerialRunable(runable);
	}

	public static void openKeyWater(int source, OnTaskFinishListener listener) {
		WaterOnekeyOpenRunable runable = new WaterOnekeyOpenRunable(source);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void closeKeyWater(int source) {
		WaterOnekeyCloseRunable runable = new WaterOnekeyCloseRunable(source);
		get().setSerialRunable(runable);
	}

	public static void closeKeyWater(int source, OnTaskFinishListener listener) {
		WaterOnekeyCloseRunable runable = new WaterOnekeyCloseRunable(source);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void openWater(int id, int source, boolean isAuto) {

		WaterOpenRunable runable = new WaterOpenRunable(id, source, isAuto);
		get().setSerialRunable(runable);
	}

	public static void openWater(int id, int source,
			OnTaskFinishListener listener, boolean isAuto) {

		WaterOpenRunable runable = new WaterOpenRunable(id, source, isAuto);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void openWater(int source, List<Integer> windowIds,
			OnTaskFinishListener listener, boolean isAuto) {

		WaterOpenRunable runable = new WaterOpenRunable(0, source, isAuto);
		runable.setIds(windowIds);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void closeWater(int id, int source, boolean isAuto) {

		WaterCloseRunable runable = new WaterCloseRunable(id, source, isAuto);
		get().setSerialRunable(runable);
	}

	public static void openYao(int source) {

		WaterYaoOpenRunable runable = new WaterYaoOpenRunable(source);
		get().setSerialRunable(runable);
	}

	public static void closeYao(int source) {

		WaterYaoCloseRunable runable = new WaterYaoCloseRunable(source);
		get().setSerialRunable(runable);
	}

	public static void openBeng(int source, boolean isAuto) {

		WaterBengOpenRunable runable = new WaterBengOpenRunable(source, isAuto);
		get().setSerialRunable(runable);
	}

	public static void closeBeng(int source, boolean isAuto) {

		WaterBengCloseRunable runable = new WaterBengCloseRunable(source,
				isAuto);
		get().setSerialRunable(runable);
	}

	public static void closeBeng(int source, OnTaskFinishListener listener,
			boolean isAuto) {

		WaterBengCloseRunable runable = new WaterBengCloseRunable(source,
				isAuto);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void closeYao(int source, OnTaskFinishListener listener) {

		WaterYaoCloseRunable runable = new WaterYaoCloseRunable(source);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void openBeng(int source, OnTaskFinishListener listener,
			boolean isAuto) {

		WaterBengOpenRunable runable = new WaterBengOpenRunable(source, isAuto);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void openYao(int source, OnTaskFinishListener listener) {

		WaterYaoOpenRunable runable = new WaterYaoOpenRunable(source);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void closeWater(int id, int source,
			OnTaskFinishListener listener, boolean isAuto) {

		WaterCloseRunable runable = new WaterCloseRunable(id, source, isAuto);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void closeWater(int source, List<Integer> windowIds,
			OnTaskFinishListener listener, boolean isAuto) {

		WaterCloseRunable runable = new WaterCloseRunable(0, source, isAuto);
		runable.setIds(windowIds);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);

	}

	public static void readAll(int source, OnTaskFinishListener listener) {
		Log.d("zqq", "读取温度");
		TempReadRunable runable = new TempReadRunable(0, -1, source);
		runable.setIds(getAllIds());
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static List<Integer> getAllIds() {

		List<Integer> res = new ArrayList<Integer>();

		int count = SimpleConfigManager.getInstance().getConfig().getWindowCount();
		for (int i = 0; i < count; i++)
			res.add(i);

		return res;
	}

	private static List<Integer> getAllDevices(int number) {

		List<Integer> res = new ArrayList<Integer>();
		for (int i = 1; i <= number; i++)
			res.add(i);

		return res;
	}

	public static List<Integer> getAllLights() {

		List<Integer> res = new ArrayList<Integer>();

		int count = SimpleConfigManager.getInstance().getLightModel()
				.getCount();
		for (int i = 0; i < count; i++)
			res.add(i);

		return res;
	}

	public static void closeAll(int source) {

		TempCloseRunable runable = new TempCloseRunable(0, false, source);
		runable.setIds(getAllIds());
		get().setSerialRunable(runable);
	}

	public static void openAll(int source) {

		TempOpenRunable runable = new TempOpenRunable(0, source);
		runable.setIds(getAllIds());
		get().setSerialRunable(runable);
	}

	public static void openByStalls(int source, int windowId, int stalls) {

		TempOpenRunable runable = new TempOpenRunable(windowId, source);
		runable.setStalls(stalls);
		get().setSerialRunable(runable);
	}

	public static void closeByStalls(int source, int windowId, int stalls) {

		TempCloseRunable runable = new TempCloseRunable(windowId, false, source);
		runable.setStalls(stalls);
		get().setSerialRunable(runable);
	}

	public static void losePowerStopAll() {

		TempStopRunable runable = new TempStopRunable(0,SerialControlRunable.SOURCE_AUTO);
		runable.setIds(getAllIds());
		runable.setClearWorkState(true);
		get().setSerialRunable(runable);
	}

	public static void openAll(int source, OnTaskFinishListener listener) {

		TempOpenRunable runable = new TempOpenRunable(0, source);
		runable.setIds(getAllIds());
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void rainCloseAll(int source) {

		TempCloseRunable runable = new TempCloseRunable(0, true, source);
		runable.setIds(getAllIds());
		get().setSerialRunable(runable);
	}

	public static void rainCloseAll(int source, OnTaskFinishListener listener) {

		TempCloseRunable runable = new TempCloseRunable(0, true, source);
		runable.setIds(getAllIds());
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void openAllMax(int source) {

		TempOpenRunable runable = new TempOpenRunable(0, 100, true, source);
		runable.setIds(getAllIds());
		get().setSerialRunable(runable);
	}

	public static void calibrateState() {

		TempCloseRunable runable = new TempCloseRunable(0, true,
				SerialControlRunable.SOURCE_PAD_BUTTON);
		runable.setIds(getAllIds());
		runable.runCalibrate();
		get().setSerialRunable(runable);
	}

	public static void calibrateState(int windowsId) {
		TempCloseRunable runable = new TempCloseRunable(windowsId, true,
				SerialControlRunable.SOURCE_PAD_BUTTON);
		get().setSerialRunable(runable);
	}

	public static void stopAll(int source) {

		TempStopRunable runable = new TempStopRunable(0, source);
		runable.setIds(getAllIds());
		get().setSerialRunable(runable);
	}
	//放的操作
	public static void exeOpen(int source, int windowId) {

		TempOpenRunable runable = new TempOpenRunable(windowId, source);
		get().setSerialRunable(runable);
	}

	public static void exeOpenMore(int source, List<Integer> windowIds,
			OnTaskFinishListener listener) {

		TempOpenRunable runable = new TempOpenRunable(0, source);
		runable.setIds(windowIds);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void exeClose(int source, int windowId) {

		TempCloseRunable runable = new TempCloseRunable(windowId, false, source);
		get().setSerialRunable(runable);
	}

	public static void exeCloseMore(int source, List<Integer> windowIds,
			OnTaskFinishListener listener) {

		TempCloseRunable runable = new TempCloseRunable(0, false, source);
		runable.setIds(windowIds);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void ExeStopMore(int source, List<Integer> windowIds,
			OnTaskFinishListener listener) {

		TempStopRunable runable = new TempStopRunable(0, source);
		runable.setIds(windowIds);
		runable.setOnTaskFinishListener(listener);
		get().setSerialRunable(runable);
	}

	public static void exeStop(int source, int windowId) {

		TempStopRunable runable = new TempStopRunable(windowId, source);
		get().setSerialRunable(runable);
	}

	public static void ReadTemp(int source, int windowId, int needSave,
			float last) {

		TempReadRunable runable = new TempReadRunable(windowId, needSave,
				source);
		get().setSerialRunable(runable);
	}

	public interface OnTaskFinishListener {
		public void onTaskFinish(String res);
	}

	public static String getExeResString(int errorCode) {

		String res = "";

		if (errorCode < 0) {

			if (errorCode == SerialControlRunable.DRIVER_ERROR)
				res = "驱动异常!\n";
			else if (errorCode == SerialControlRunable.MOTOR_IS_WORKING)
				res = "该电机正在运转!\n";
			else if (errorCode == SerialControlRunable.LINK_ERROR)
				res = "该链路不连通!\n";

			return "原因:" + res;
		}

		return res;
	}
}
