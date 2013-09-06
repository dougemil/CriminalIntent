package com.dougemil.criminalintent.model;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.dougemil.criminalintent.controller.services.CriminalIntentJSONSerializer;

/*
 * Singleton pattern
 * Endures for life of the app
 * 
 * mCrimes holds the list of crimes for the whole app
 * saveCrimes() uses service to serialize mCrimes
 */

public class CrimeLab {
	
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mSerializer;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	
	// private constructor
	private CrimeLab(Context appContext){
		mAppContext = appContext;
		mSerializer = new CriminalIntentJSONSerializer(mAppContext,FILENAME);
		// attempt to load crimes, create new list if file not found
		try{
			mCrimes = mSerializer.loadCrimes();
		}catch (Exception e){
			mCrimes = new ArrayList<Crime>();
			Log.e(TAG, "Error loading crimes: ", e);
		}
	}
	
	// public method calls constructor to populate static variable
	public static CrimeLab get(Context c){
		if (sCrimeLab == null){
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	public void addCrime(Crime c) {
		mCrimes.add(c);
	}
	
	public void deleteCrime(Crime c){
		mCrimes.remove(c);
		Log.d(TAG,"**Crime Deleted");
	}
	
	public boolean saveCrimes(){
		// Logs status instead of displaying to user
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "Crimes saved to file");
			return true;
		}catch (Exception e){
			Log.e(TAG, "Error saving crimes: ", e);
			return false;
		}
	}
	
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	public Crime getCrime(UUID id) {
		for (Crime c : mCrimes) {
			if (c.getId().equals(id))
				return c;
		}
		return null;
	}
}
