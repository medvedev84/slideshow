package com.pos4mobile.slideshow.receivers;

import com.pos4mobile.slideshow.ImageChangeActivity;
import com.pos4mobile.slideshow.utils.StorageUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootUpReceiver extends BroadcastReceiver {
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	boolean isRunAtStartup = StorageUtils.getBooleanValue(context, StorageUtils.RUN_AT_STARTUP, false);
    	if (isRunAtStartup) {
        	Intent i = new Intent(context, ImageChangeActivity.class);  
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);      		
    	}
    }
}
