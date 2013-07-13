package com.dougemil.criminalintent.controller.fragments;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import com.dougemil.criminalintent.R;

public class TimePickerFragment extends DialogFragment {
	
	// arg Key
	public static final String EXTRA_TIME = "com.dougemil.criminalintent.time";
	// arg value
	private Date mTime;
	
	public static TimePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);
		
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mTime = (Date)getArguments().getSerializable(EXTRA_TIME);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mTime);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
	
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		
		TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
		
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		
		// Callback interface
		// this corresponds the the DatePicker init method
		timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// This constructor sets the time and keeps the date the same
				mTime = new GregorianCalendar(year, month, day, hourOfDay, minute).getTime();
				getArguments().putSerializable(EXTRA_TIME, mTime);
			}
			
		});
		return new AlertDialog.Builder(getActivity())
								.setView(v)
								.setPositiveButton(android.R.string.ok, 
												new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int which) {
														sendResult(Activity.RESULT_OK);
													}
																		
												})
								.create();
	
	} 
	private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, mTime);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }	
}
		

		

	

