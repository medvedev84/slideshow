package com.pos4mobile.slideshow;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.pos4mobile.slideshow.receivers.OnAlarmReceiver;
import com.pos4mobile.slideshow.utils.FileDialog;
import com.pos4mobile.slideshow.utils.StorageUtils;

import java.io.File;
import java.util.Calendar;

public class SettingsActivity extends FragmentActivity {
	  		
	private CheckBox checkBoxStarup;
	private CheckBox checkBoxCharge;
	private CheckBox checkBoxFullScreen;
	//private CheckBox checkBoxDisableScreen;
	private CheckBox checkBoxTripleTap;

	private Button buttonSourceSelect;
	private TextView textViewSourcePath;	
	
	private TextView textViewDelaySeconds;
	private TextView textViewDelayMinutes;
	private TextView textViewDelayHours;
	
	private SeekBar seekBarDelaySeconds;
	private SeekBar seekBarDelayMinutes;	
	private SeekBar seekBarDelayHours;

    private CheckBox checkBoxStartDate;
    private CheckBox checkBoxEndDate;
	private TextView textViewStartTime;
	private TextView textViewEndTime;	
	private Button buttonPickStartTime;	
	private Button buttonPickEndTime;
    private Button buttonRestoreDefaults;
    private Button buttonStart;

    private Button buttonAddMore;
    private Button buttonRemoveLast;

    private TableRow tableRowSelect2;
    private TextView textViewSourcePath2;
    private Button buttonSourceSelect2;

    private TableRow tableRowSelect3;
    private TextView textViewSourcePath3;
    private Button buttonSourceSelect3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        final String targetPath = StorageUtils.getStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH, StorageUtils.getRootPath());
        textViewSourcePath = (TextView) findViewById(R.id.textViewSourcePath);
        textViewSourcePath.setText(targetPath);
        
        buttonSourceSelect = (Button) findViewById(R.id.buttonSourceSelect);
        buttonSourceSelect.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				initFileDialog(targetPath, textViewSourcePath, StorageUtils.SOURCE_PATH).showDialog();
			}
		});

        tableRowSelect2 = (TableRow) findViewById(R.id.tableRowSelect2);
        final String targetPath2 = StorageUtils.getStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH2, "");
        textViewSourcePath2 = (TextView) findViewById(R.id.textViewSourcePath2);
        textViewSourcePath2.setText(targetPath2);
        buttonSourceSelect2 = (Button) findViewById(R.id.buttonSourceSelect2);
        buttonSourceSelect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFileDialog(targetPath2, textViewSourcePath2, StorageUtils.SOURCE_PATH2).showDialog();
            }
        });
        tableRowSelect2.setVisibility( targetPath2 != "" ? View.VISIBLE: View.GONE);

        tableRowSelect3 = (TableRow) findViewById(R.id.tableRowSelect3);
        final String targetPath3 = StorageUtils.getStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH3, "");
        textViewSourcePath3 = (TextView) findViewById(R.id.textViewSourcePath3);
        textViewSourcePath3.setText(targetPath3);
        buttonSourceSelect3 = (Button) findViewById(R.id.buttonSourceSelect3);
        buttonSourceSelect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFileDialog(targetPath3, textViewSourcePath3, StorageUtils.SOURCE_PATH3).showDialog();
            }
        });
        tableRowSelect3.setVisibility( targetPath3 != "" ? View.VISIBLE: View.GONE);

        buttonAddMore = (Button)findViewById(R.id.buttonAddMore);
        buttonRemoveLast = (Button)findViewById(R.id.buttonRemoveLast);

        buttonAddMore.setEnabled(tableRowSelect3.getVisibility() == View.GONE);
        buttonAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableRowSelect2.getVisibility() == View.GONE){
                    tableRowSelect2.setVisibility(View.VISIBLE);
                    textViewSourcePath2.setText( StorageUtils.getRootPath());
                    StorageUtils.saveStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH2, StorageUtils.getRootPath());
                    buttonRemoveLast.setEnabled(true);
                } else if (tableRowSelect3.getVisibility() == View.GONE){
                    tableRowSelect3.setVisibility(View.VISIBLE);
                    textViewSourcePath3.setText( StorageUtils.getRootPath());
                    StorageUtils.saveStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH3, StorageUtils.getRootPath());
                    buttonAddMore.setEnabled(false);
                }
            }
        });

        buttonRemoveLast.setEnabled(tableRowSelect2.getVisibility() == View.VISIBLE);
        buttonRemoveLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableRowSelect3.getVisibility() == View.VISIBLE){
                    StorageUtils.saveStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH3, "");
                    tableRowSelect3.setVisibility(View.GONE);
                    buttonAddMore.setEnabled(true);
                } else if (tableRowSelect2.getVisibility() == View.VISIBLE){
                    StorageUtils.saveStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH2, "");
                    tableRowSelect2.setVisibility(View.GONE);
                    buttonRemoveLast.setEnabled(false);
                }
            }
        });

        initDelaySettings();
        initCheckBoxes();
        initButtonsStartEndShow();
        initButtonRestoreDefaults();

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ImageChangeActivity.class));
            }
        });
    }

    private void initDelaySettings(){
        textViewDelaySeconds = (TextView) findViewById(R.id.textViewDelaySeconds);
        textViewDelayMinutes = (TextView) findViewById(R.id.textViewDelayMinutes);
        textViewDelayHours = (TextView) findViewById(R.id.textViewDelayHours);

        seekBarDelaySeconds = (SeekBar) findViewById(R.id.seekBarDelaySeconds);
        seekBarDelaySeconds.setMax(StorageUtils.MAX_DELAY_SECONDS);

        seekBarDelayMinutes = (SeekBar) findViewById(R.id.seekBarDelayMinutes);
        seekBarDelayMinutes.setMax(StorageUtils.MAX_DELAY_MINUTES);

        seekBarDelayHours = (SeekBar) findViewById(R.id.seekBarDelayHours);
        seekBarDelayHours.setMax(StorageUtils.MAX_DELAY_HOURS);

        int delay = StorageUtils.getIntValue(SettingsActivity.this, StorageUtils.DELAY, StorageUtils.PREDEFINED_DELAY);
        initDelaySeekBars(delay);

        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar bar) {
                int seconds = Math.min(seekBarDelaySeconds.getProgress(), 59);
                int minutes = Math.min(seekBarDelayMinutes.getProgress(), 59);
                int hours = Math.min(seekBarDelayHours.getProgress(), 23);

                int total = seconds + minutes * 60 + hours * 60 * 60;
                initDelaySeekBars(Math.max(total, 1));
                StorageUtils.saveIntValue(SettingsActivity.this, StorageUtils.DELAY, total);
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {}

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {}
        };

        seekBarDelaySeconds.setOnSeekBarChangeListener(seekBarListener);
        seekBarDelayMinutes.setOnSeekBarChangeListener(seekBarListener);
        seekBarDelayHours.setOnSeekBarChangeListener(seekBarListener);
    }

    private void initCheckBoxes(){
        checkBoxStarup = (CheckBox) findViewById(R.id.checkBoxStarup);
        checkBoxStarup.setChecked(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.RUN_AT_STARTUP, true));
        checkBoxStarup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton chackbox, boolean value) {
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.RUN_AT_STARTUP, value);
            }
        });
        checkBoxCharge = (CheckBox) findViewById(R.id.checkBoxCharge);
        checkBoxCharge.setChecked(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.RUN_ON_CHARGE, true));
        checkBoxCharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton chackbox, boolean value) {
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.RUN_ON_CHARGE, value);
            }
        });
        checkBoxTripleTap = (CheckBox) findViewById(R.id.checkBoxTripleTap);
        checkBoxTripleTap.setChecked(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.UNBLOCK_TRIPLE_TAP, true));
        checkBoxTripleTap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton chackbox, boolean value) {
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.UNBLOCK_TRIPLE_TAP, value);
            }
        });

        //checkBoxDisableScreen = (CheckBox) findViewById(R.id.checkBoxDisableScreen);
        checkBoxFullScreen = (CheckBox) findViewById(R.id.checkBoxFullScreen);

        /*
        checkBoxDisableScreen.setChecked(StorageUtils.isScreenDisabled(SettingsActivity.this));
        checkBoxDisableScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton chackbox, boolean value) {
				if (value) {
					// need to set up SlideShow as default launcher
					showSelectSlideShowLauncherDialog();
				} else {
					// need to get back to default launcher
					String packageName = getPackageName();
					getPackageManager().clearPackagePreferredActivities(packageName);
					showRevertSlideShowLauncherDialog();
				}
                // force to enable interrupting by triple tap
                checkTripleTapCheckbox();
			}
		});
        */

        checkBoxFullScreen.setChecked(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.FULL_SCREEN_MODE, true));
        checkBoxFullScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton chackbox, boolean value) {
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.FULL_SCREEN_MODE, value);

                // force to enable interrupting by triple tap
                checkTripleTapCheckbox();
            }
        });
        checkTripleTapCheckbox();
    }

    private void initButtonsStartEndShow(){
        int[] startTime = StorageUtils.getTimeValue(SettingsActivity.this, StorageUtils.START_TIME);
        textViewStartTime = (TextView) findViewById(R.id.textViewStartTime);
        setTimeToTextView(textViewStartTime, startTime[0], startTime[1]);
        buttonPickStartTime = (Button) findViewById(R.id.buttonPickStartTime);
        buttonPickStartTime.setEnabled(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_START_TIME, false));
        buttonPickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setListener(new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        StorageUtils.saveTimeValue(SettingsActivity.this, StorageUtils.START_TIME, hourOfDay, minute);
                        setTimeToTextView(textViewStartTime, hourOfDay, minute);
                        setAlarm(SettingsActivity.this, hourOfDay, minute);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "timepicker");
            }
        });

        int[] endTime = StorageUtils.getTimeValue(SettingsActivity.this, StorageUtils.END_TIME);
        textViewEndTime = (TextView) findViewById(R.id.textViewEndTime);
        setTimeToTextView(textViewEndTime, endTime[0], endTime[1]);
        buttonPickEndTime = (Button) findViewById(R.id.buttonPickEndTime);
        buttonPickEndTime.setEnabled(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_END_TIME, false));
        buttonPickEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setListener(new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        StorageUtils.saveTimeValue(SettingsActivity.this, StorageUtils.END_TIME, hourOfDay, minute);
                        setTimeToTextView(textViewEndTime, hourOfDay, minute);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "timepicker");
            }
        });


        checkBoxStartDate = (CheckBox) findViewById(R.id.checkBoxStartDate);
        checkBoxStartDate.setChecked(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_START_TIME, false));
        checkBoxStartDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton chackbox, boolean value) {
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_START_TIME, value);
                buttonPickStartTime.setEnabled(value);
            }
        });

        checkBoxEndDate = (CheckBox) findViewById(R.id.checkBoxEndDate);
        checkBoxEndDate.setChecked(StorageUtils.getBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_END_TIME, false));
        checkBoxEndDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton chackbox, boolean value) {
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_END_TIME, value);
                buttonPickEndTime.setEnabled(value);
            }
        });
    }

    private void initButtonRestoreDefaults(){
        buttonRestoreDefaults = (Button) findViewById(R.id.buttonRestoreDefaults);
        buttonRestoreDefaults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String targetPath = StorageUtils.getRootPath();
                StorageUtils.saveStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH, targetPath);
                textViewSourcePath.setText(targetPath);

                tableRowSelect2.setVisibility(View.GONE);
                StorageUtils.saveStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH2, "");
                buttonRemoveLast.setEnabled(false);

                tableRowSelect3.setVisibility(View.GONE);
                StorageUtils.saveStringValue(SettingsActivity.this, StorageUtils.SOURCE_PATH3, "");
                buttonAddMore.setEnabled(true);

                initDelaySeekBars(StorageUtils.PREDEFINED_DELAY);
                StorageUtils.saveIntValue(SettingsActivity.this, StorageUtils.DELAY, StorageUtils.PREDEFINED_DELAY);

                checkBoxStarup.setChecked(true);
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.RUN_AT_STARTUP, true);

                checkBoxCharge.setChecked(true);
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.RUN_ON_CHARGE, true);

                checkBoxFullScreen.setChecked(true);
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.FULL_SCREEN_MODE, true);

                checkBoxTripleTap.setChecked(true);
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.UNBLOCK_TRIPLE_TAP, true);

                checkBoxStartDate.setChecked(false);
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_START_TIME, false);

                setTimeToTextView(textViewStartTime, 0, 0);
                StorageUtils.saveTimeValue(SettingsActivity.this, StorageUtils.START_TIME, 0, 0);

                checkBoxEndDate.setChecked(false);
                StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.ENABLE_END_TIME, false);

                setTimeToTextView(textViewEndTime, 0, 0);
                StorageUtils.saveTimeValue(SettingsActivity.this, StorageUtils.END_TIME, 0, 0);
            }
        });
    }



    private void checkTripleTapCheckbox(){
        //boolean stopByTripleTap = checkBoxDisableScreen.isChecked() || checkBoxFullScreen.isChecked();
        boolean stopByTripleTap = checkBoxFullScreen.isChecked();
        checkBoxTripleTap.setChecked(stopByTripleTap);
        checkBoxTripleTap.setEnabled(!stopByTripleTap);
        StorageUtils.saveBooleanValue(SettingsActivity.this, StorageUtils.UNBLOCK_TRIPLE_TAP, stopByTripleTap);
    }
    
    private void initDelaySeekBars(int total){
    	int hours = total / 3600;
    	int minutes = ( total % 3600 ) / 60;
    	int seconds = total % 60;

    	seekBarDelaySeconds.setProgress(seconds);
    	textViewDelaySeconds.setText(String.valueOf(seconds));
		seekBarDelayMinutes.setProgress(minutes);
		textViewDelayMinutes.setText(String.valueOf(minutes));
		seekBarDelayHours.setProgress(hours);
		textViewDelayHours.setText(String.valueOf(hours));
    }
    
    private void setTimeToTextView(TextView textView, int hourOfDay, int minute){    	  
    	String hourString = hourOfDay < 10 ? "0" + String.valueOf(hourOfDay): String.valueOf(hourOfDay);
    	String minuteString = minute < 10 ? "0" + String.valueOf(minute): String.valueOf(minute);    	
    	textView.setText(String.format("%s:%s", hourString, minuteString));	
    }
    
    private FileDialog initFileDialog(String path, final TextView textView, final String value){
        FileDialog dialog = new FileDialog(SettingsActivity.this, new File(path));
    	dialog.setSelectDirectoryOption(true);
    	dialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
			@Override
			public void directorySelected(File directory) {
				String path = directory.getAbsolutePath();
                textView.setText(path);
				StorageUtils.saveStringValue(SettingsActivity.this, value, path);
			}
		});
    	return dialog;
    }
    
    private void setAlarm(Context context, int hour, int minute){        
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.HOUR_OF_DAY, hour);
    	calendar.set(Calendar.MINUTE, minute);
    	calendar.set(Calendar.SECOND, 0);
    	
    	AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, OnAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        mgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);                   
    }
    
    private void showSelectSlideShowLauncherDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.warning_no_set_slideshow_title);
		builder.setMessage(R.string.warning_no_set_slideshow_body);
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent showSettings = new Intent();
				showSettings.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uriAppSettings = Uri.fromParts("package", StorageUtils.getCurrentLauncherPackageName(getPackageManager()), null);
				showSettings.setData(uriAppSettings);
				startActivity(showSettings);		
			}
		});		    			    		
		AlertDialog dialog = builder.create();
		dialog.show();    	
    }   

    private void showRevertSlideShowLauncherDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.warning_no_set_default_title);
		builder.setMessage(R.string.warning_no_set_default_body);
		builder.setPositiveButton(R.string.dialog_ok, null);		    			    					
		AlertDialog dialog = builder.create();
		dialog.show();    	
    } 
         
    
}
