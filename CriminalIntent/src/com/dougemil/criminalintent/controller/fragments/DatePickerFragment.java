package com.dougemil.criminalintent.controller.fragments;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.dougemil.criminalintent.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
	
	// arg KEY
	
	public static final String EXTRA_DATE = "com.dougemil.criminalintent.date";
	// arg value
	private Date mDate;
	
	// This method instantiates a new Frag with the received Date as an argument
	public static DatePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}

	@Override
	// Called by FragMgr of the hosting Activity
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// retrieve Date from Bundle
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		// Date values must be parsed to integers to work with DatePicker view
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		
		
		// Inflate dialog content view
		View v = getActivity().getLayoutInflater()
								.inflate(R.layout.dialog_date, null);
		
		// Initialize DatePicker
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		
		// Callback interface
		datePicker.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year, int month, int day) {
				// Translate args into Date using Calendar
				mDate = new GregorianCalendar(year, month, day, hour, minute).getTime();
				// Update Fragment arguments to persist selected DatePicker value
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		
		// AlertDialog.Builder class provides interface for constructing instances
		// 1st method returns an instance (param is the Context
		// 2nd method sets the content view (declared above)
		// 3/4 methods are configuration of the instance
		return new AlertDialog.Builder(getActivity())
						.setView(v)
						.setTitle(R.string.date_picker_title)
						// wire button to sendResult()
						.setPositiveButton(android.R.string.ok,
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													sendResult(Activity.RESULT_OK);
							}
						})
						.create();
		
		// positiveButton, negativeButton, neutralButton
	}
	
	// Method for calling back to the target Fragment (the original caller), invoked above
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		// load extra (date) in an intent
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE,  mDate);
		// preps target for onActivityResult(...) call, received when child dies
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

}
