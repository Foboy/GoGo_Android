package com.foboy.gogo_android.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.foboy.gogo_android.R;
import com.foboy.gogo_android.common.HttpUtils;
import com.foboy.gogo_android.widget.ProgressWheel;

public class LoadingMaskView {
	private Dialog dialog;
	private ProgressBar progress;
	private boolean running = false;
	
	public LoadingMaskView(Activity mActivity) {
		dialog = new Dialog(mActivity, R.style.mask_dialog);

		LinearLayout popView = (LinearLayout) LayoutInflater.
				from(mActivity).inflate(R.layout.frame_loading_mask, null);
		progress = (ProgressBar) popView.findViewById(R.id.dialogProgress);
		try
		{
			progress.wait();
		}
		catch(Exception e){
			
		}
		dialog.setContentView(popView, 
				new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 200);
		
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				HttpUtils.cancelRequests();
			}
		});
	}
	
	public void show() {
		this.running = true;
		dialog.show();
	}
	
	public void hide() {
		this.running = false;
		dialog.dismiss();
	}
	
	public boolean isRunning()
	{
		return this.running;
	}
	
	
}
