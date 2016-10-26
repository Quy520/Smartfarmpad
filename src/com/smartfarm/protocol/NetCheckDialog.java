package com.smartfarm.protocol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smartfarm.tools.AssistThread;
import com.smartfarm.view.R;

public class NetCheckDialog {
	@SuppressLint("InflateParams")
	public static void Show(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.net_diagnosis_dialog, null);
		
		TextView progressText = (TextView) view.findViewById(R.id.diagnosis_process_text);
		TextView curr = (TextView) view.findViewById(R.id.diagnosis_curr);
		
		AlertDialog dialog = new Builder(context)
			.setView(view)
			.setCancelable(false)
			.setTitle(R.string.net_diagnosis)
			.create();

		dialog.show();
		
		AssistThread.getInstance().setThreadWork(new NetCheckWork(progressText, curr, dialog));
	}
}
