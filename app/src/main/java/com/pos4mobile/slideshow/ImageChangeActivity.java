package com.pos4mobile.slideshow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pos4mobile.slideshow.utils.StorageUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ImageChangeActivity extends Activity {
	    
	private long touch1 = 0;
	private long touch2 = 0;
	private long touch3 = 0;	
	
	private Bitmap imageBitmap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final boolean isFullScreen = StorageUtils.getBooleanValue(ImageChangeActivity.this, StorageUtils.FULL_SCREEN_MODE, true);
        if (isFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_screen_change);

        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
            // doesn't matter what happened - just hide navigation bar again
            decorView.setSystemUiVisibility(8);
            }
        });
        init();
    }

    @Override
    public void onResume(){
    	super.onResume();

        final boolean isFullScreen = StorageUtils.getBooleanValue(ImageChangeActivity.this, StorageUtils.FULL_SCREEN_MODE, true);
        final View decorView = getWindow().getDecorView();         
        if (isFullScreen) {        
        	// Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;        	        	
         	decorView.setSystemUiVisibility(uiOptions);
        }     
        init();
    }
    
    private void init(){
        //To keep the screen on
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
                
        String targetPath = StorageUtils.getStringValue(ImageChangeActivity.this, StorageUtils.SOURCE_PATH, StorageUtils.getRootPath());                
        File targetDirectory = new File(targetPath);
        File[] files1 = targetDirectory.listFiles();
        if (files1 == null)
            files1 = new File[] {};

        String targetPath2 = StorageUtils.getStringValue(ImageChangeActivity.this, StorageUtils.SOURCE_PATH2, StorageUtils.getRootPath());
        File targetDirectory2 = new File(targetPath2);
        File[] files2 = targetDirectory2.listFiles();
        if (files2 == null)
            files2 = new File[] {};

        String targetPath3 = StorageUtils.getStringValue(ImageChangeActivity.this, StorageUtils.SOURCE_PATH3, StorageUtils.getRootPath());
        File targetDirectory3 = new File(targetPath3);
        File[] files3 = targetDirectory3.listFiles();
        if (files3 == null)
            files3 = new File[] {};

        File[] files = concatenate(concatenate(files1, files2), files3);

        if (files == null || files.length == 0)
         	showNoImagesDialog(targetPath);
        else {             
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

        	ImageView iv = ((ImageView) findViewById(R.id.myImageView));             
            final ChangeShowRunner runner = new ChangeShowRunner(StorageUtils.getIntValue(ImageChangeActivity.this, StorageUtils.DELAY, StorageUtils.MIN_DELAY), files, iv, this, size.x, size.y);
            runner.start();                                            
            
            boolean isUnblockOnTripleTap = StorageUtils.getBooleanValue(ImageChangeActivity.this, StorageUtils.UNBLOCK_TRIPLE_TAP, true);           
            if (isUnblockOnTripleTap) {
            	iv.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN && isTripleTap()) {							
							runner.stop();
							return true;
						}
						return false;
					}
                    		});
            }  
        }    	
    }
    
    private void showNoImagesDialog(String sourcePath) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.warning_no_files_title);
		builder.setMessage(String.format(getResources().getString(R.string.warning_no_files_body), sourcePath));
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent(ImageChangeActivity.this, SettingsActivity.class);				
				startActivity(intent);								
			}
		});
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();		
			}
		});			
		AlertDialog dialog = builder.create();
		dialog.show();    	
    }
    
    private boolean isTripleTap(){
    	boolean result = false;
		
    	long diff = 0;							
		
		if (touch1 == 0) {
			touch1 = (new Date()).getTime();								
		} else {
			if (touch2 == 0) {
				touch2 = (new Date()).getTime();
				diff = touch2 - touch1;
				if (diff > 1000) {
					touch1 = 0;
					touch2 = 0;										
				} 
			} else {				
				if (touch3 == 0) {
					touch3 = (new Date()).getTime();
					diff = touch3 - touch2;
					
					if (diff > 1000) { 
						touch1 = 0;
						touch2 = 0;
						touch3 = 0;
					} else {
						result = true;	
					}
				}					
			}																
		}		
		return result;
    }
        
    public boolean onTouchEvent(MotionEvent event){
    	return true;
    }
    
    @Override
    public void onBackPressed() {
    	boolean isDisableScreen = StorageUtils.isScreenDisabled(ImageChangeActivity.this);
        if (!isDisableScreen)
        	super.onBackPressed();    
    }    
    
    @Override
    public void onStop(){        
    	boolean isDisableScreen = StorageUtils.isScreenDisabled(ImageChangeActivity.this);
        if (isDisableScreen) {
        	restartSlideShow();
        } else {        	
        	super.onStop();    
        }        
    }   
    
    private void restartSlideShow(){
    	android.os.Process.killProcess(android.os.Process.myPid());
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);	    	
    }
    
    private class ChangeShowRunner {        
    	private boolean isStopped = false;
    	private int delay;
    	private int step = 0;
    	private File[] files;
    	private ImageView iv;
        private final Handler handler = new Handler();
        private Activity context;
        private int width;
        private int height;
        
    	public ChangeShowRunner(int delay, File[] files, ImageView iv, Activity context, int width, int height){
    		this.delay = delay;
    		this.files = files;
    		this.iv = iv;
    		this.context = context;
    		this.width = width;
    		this.height = height;
    	}
    	
    	public void start(){
            Runnable imageChanger = new Runnable() {
                @Override
                public void run() {
                	if (isStopped)
                		return;
                	
                	if (step == files.length)
                		step = 0;
                	

                	System.gc();
    	            if(imageBitmap != null) {
    	                 imageBitmap.recycle();
    	                 imageBitmap = null;
    	            }
    	            BitmapFactory.Options options = new BitmapFactory.Options();
    	            options.inSampleSize = 3;
    	            imageBitmap = BitmapFactory.decodeFile(files[step].getAbsolutePath(), options);

                    if (imageBitmap != null) {
        	            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height+50, true);
        	            iv.setImageBitmap(scaledBitmap);
                    } else {        	                
                    	iv.setImageResource(R.raw.noimage);
                    }                                                 
                    
                    step++;
                    
                    handler.postDelayed(this, delay * 1000);                        
                }
                
            }; 
            handler.post(imageChanger);  
                        
            Runnable stopCheker = new Runnable() {
                @Override
                public void run() {                    
                    if (isStopped)
                    	return;
                                        
                    if (isEndTime()) {
                    	stop();
                    }
                    
                    handler.postDelayed(this, 1000);                    
                }
                
            }; 
            handler.post(stopCheker);                         
                        
    	}
    	
    	public void stop(){
    		isStopped = true;
    		context.finish();
    	}
    	
    	private boolean isEndTime(){
            boolean isEndTimeEnabled = StorageUtils.getBooleanValue(context, StorageUtils.ENABLE_END_TIME, false);
    		int[] endTime = StorageUtils.getTimeValue(context, StorageUtils.END_TIME);     	
    		Calendar now = Calendar.getInstance(TimeZone.getDefault());
    		return isEndTimeEnabled && now.get(Calendar.HOUR_OF_DAY) == endTime[0] && now.get(Calendar.MINUTE) == endTime[1];
    	}
    }

    public <T> T[] concatenate (T[] A, T[] B) {
        int aLen = A.length;
        int bLen = B.length;

        @SuppressWarnings("unchecked")
        T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
}
