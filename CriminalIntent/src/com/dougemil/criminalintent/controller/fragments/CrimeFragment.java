package com.dougemil.criminalintent.controller.fragments;



import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.dougemil.criminalintent.R;
import com.dougemil.criminalintent.model.Crime;
import com.dougemil.criminalintent.model.CrimeLab;

public class CrimeFragment extends Fragment {
	// key for intent extra
	public static final String EXTRA_CRIME_ID = "com.dougemil.criminalintent.crime_id";
	
	// constant Tag for DatePickerFragment
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	// Constant requestCode for setTargetFragment(...)
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mSolvedCheckBox;
	
	// this class bundles a new Fragment w/ arguments instead of using the constructor
		public static CrimeFragment newInstance(UUID crimeId) {
			Bundle args = new Bundle();
			args.putSerializable(EXTRA_CRIME_ID, crimeId);
			
			CrimeFragment fragment = new CrimeFragment();
			// attach bundle to fragment
			fragment.setArguments(args);
			
			return fragment;
		}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
//		// Directly accessing the intent to retireve an extra
//		UUID crimeId = (UUID)getActivity()
//							.getIntent()
//							.getSerializableExtra(EXTRA_CRIME_ID);
		
		// Access the bundle to retrieve an argument (an "extra")
		UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		
		// use the id to fetch a crime from the CrimeLab singleton
		mCrime = CrimeLab.get(getActivity())
							.getCrime(crimeId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		
		// Explicitly inflates the Fragment view,
		// 3rd param tells inflater whether to add View to parent
		// false if View will be added by something else
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		
		// View must be explicitly referenced in the method call
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		
		// Event Listener and Handler
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(
					CharSequence c, int start, int before, int count){
				mCrime.setTitle(c.toString());
			}
			
			public void beforeTextChanged(
					CharSequence c, int start, int count, int after){
				
			}
			
			public void afterTextChanged(Editable c) {
				
			}
		});
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		updateDate();
		
//		// button not yet active
//		mDateButton.setEnabled(false);
		
		// Dialogs are added via the FragMgr
		mDateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				FragmentManager fm = getActivity()
										.getSupportFragmentManager();
				// Populates Bundle of dialog Frag with the Crime's date
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				// target set so DatePicker can return data
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mTimeButton = (Button)v.findViewById(R.id.crime_time);
		updateTime();
		
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				FragmentManager fm = getActivity()
										.getSupportFragmentManager();
				// Populates Bundle of dialog Frag with the Crime's date
				TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
				dialog.show(fm, DIALOG_TIME);
			}
		});
		
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		// event listener/action handler
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				// Set the crime's solved property
				mCrime.setSolved(isChecked);
			}
		});
		
		return v;
	}
	
	// This method is called by ActivityManager when a dialog (or other child) closes
	@Override
	// passes parameters from the closed child
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		// get and set the date selected by DatePickerFragment
		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();

		}
			
		if (requestCode == REQUEST_TIME){
			Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(time);
			updateTime();
		}
		
	}
	
	// Helper method to avoid duplicate code (encapsulation)
	private void updateDate() {
		// get text for date button from crime object, format and set
		String dateFormatString = "EEEE, MMMM dd, yyyy";
		Date crimeDate = mCrime.getDate();
		String dateString = (String) DateFormat.format(dateFormatString, crimeDate);
		
		mDateButton.setText(dateString);
	}
	
	public void updateTime(){
		String timeFormatString = "kk:mm";
		Date crimeDate = mCrime.getDate();
		String timeString = (String)DateFormat.format(timeFormatString, crimeDate);
		mTimeButton.setText(timeString);
	}
	
	

}
