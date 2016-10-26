package com.smartfarm.tools;

import com.tencent.bugly.crashreport.CrashReport;

public class ExceptionUtils {

	public static void report(Throwable e) {
		
		CrashReport.postCatchedException(e);
	}
}
