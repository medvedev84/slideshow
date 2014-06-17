package com.pos4mobile.slideshow.receivers;

import com.pos4mobile.slideshow.ImageChangeActivity;
import com.pos4mobile.slideshow.utils.StorageUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
        boolean isStartTimeEnabled = StorageUtils.getBooleanValue(context, StorageUtils.ENABLE_START_TIME, false);
        if (isStartTimeEnabled) {
            Intent i = new Intent(context, ImageChangeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
	}
}
