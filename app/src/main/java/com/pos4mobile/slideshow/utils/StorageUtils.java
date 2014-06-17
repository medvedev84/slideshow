package com.pos4mobile.slideshow.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;

public class StorageUtils {
    public static final String PREDEFINED_FOLDER = "/pos4mobile/slideshow";
    public static final int PREDEFINED_DELAY = 30;

	public static final String SETTINGS_FILE_NAME = "SlideShowSettings";
	
	public static final String FULL_SCREEN_MODE = "FullScreenMode";
	public static final String DISABLE_SCREEN = "DisableScreen";
	public static final String UNBLOCK_TRIPLE_TAP = "UnblockTripleTap";
	public static final String RUN_AT_STARTUP = "RunAtStartup";
	public static final String RUN_ON_CHARGE = "RunOnCharge";
	public static final String SOURCE_PATH = "SourcePath";
    public static final String SOURCE_PATH2 = "SourcePath2";
    public static final String SOURCE_PATH3 = "SourcePath3";
	public static final String DELAY = "Delay";
    public static final String ENABLE_START_TIME = "EnableStartTime";
    public static final String START_TIME = "StartTime";
	public static final String START_TIME_HOUR = "StartTimeHour";
	public static final String START_TIME_MINUTE = "StartTimeMinute";
    public static final String ENABLE_END_TIME = "EnableEndTime";
	public static final String END_TIME = "EndTime";
	public static final String END_TIME_HOUR = "EndTimeHour";
	public static final String END_TIME_MINUTE = "EndTimeMinute";
	
	public static final int MIN_DELAY = 1;
	public static final int MAX_DELAY_SECONDS = 60;
	public static final int MAX_DELAY_MINUTES = 60;
	public static final int MAX_DELAY_HOURS = 24;
	   	
    public static void saveBooleanValue(Context context, String type, boolean value){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putBoolean(type, value);
	      editor.commit();
    }

    public static boolean getBooleanValue(Context context, String type, boolean value){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      return settings.getBoolean(type, value);	      
    }  

    public static void saveStringValue(Context context, String type, String value){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString(type, value);
	      editor.commit();
    }
    
    public static String getStringValue(Activity context, String type, String value){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      return settings.getString(type, value);	      
    }
    
    public static void saveIntValue(Context context, String type, int value){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putInt(type, value);
	      editor.commit();
    }
  
    public static int getIntValue(Context context, String type, int value){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      return settings.getInt(type, value);	      
    }
  
    public static void saveTimeValue(Context context, String type, int hour, int minute){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      if (type == START_TIME) {
	    	  editor.putInt(START_TIME_HOUR, hour);
	    	  editor.putInt(START_TIME_MINUTE, minute);
	      } else if (type == END_TIME)  {
	    	  editor.putInt(END_TIME_HOUR, hour);
	    	  editor.putInt(END_TIME_MINUTE, minute);	    	  
	      }	      
	      editor.commit();
    }
    
    public static int[] getTimeValue(Context context, String type){
	      SharedPreferences settings = context.getSharedPreferences(SETTINGS_FILE_NAME, Activity.MODE_PRIVATE);
	      int hour = 0;
	      int minute = 0;
	      if (type == START_TIME) {
	    	  hour = settings.getInt(START_TIME_HOUR, 0);
	    	  minute = settings.getInt(START_TIME_MINUTE, 0);	    	  
	      } else if (type == END_TIME)  {
	    	  hour = settings.getInt(END_TIME_HOUR, 0);
	    	  minute = settings.getInt(END_TIME_MINUTE, 0);   	  
	      }	  	     
	      return new int[] {hour, minute};	      
    }
    
    public static String getRootPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + StorageUtils.PREDEFINED_FOLDER;
    }
    
    public static String getCurrentLauncherPackageName(PackageManager manager){
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	ResolveInfo resolveInfo = manager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
    	return resolveInfo.activityInfo.packageName;    	    	
    }     
    
    public static boolean isScreenDisabled(Activity context){
    	String slideShowPackageName = context.getPackageName();
    	String currentPackageName = getCurrentLauncherPackageName(context.getPackageManager());
    	return slideShowPackageName.equalsIgnoreCase(currentPackageName);    	
    } 
}
