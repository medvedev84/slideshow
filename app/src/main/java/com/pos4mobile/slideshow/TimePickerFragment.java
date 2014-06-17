package com.pos4mobile.slideshow;

import java.util.Calendar;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		
	private TimePickerDialog.OnTimeSetListener listener;
			
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
	
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {		 
		 listener.onTimeSet(view, hourOfDay, minute);
	}

	public TimePickerDialog.OnTimeSetListener getListener() {
		return listener;
	}

	public void setListener(TimePickerDialog.OnTimeSetListener listener) {
		this.listener = listener;
	}
	
	
}
