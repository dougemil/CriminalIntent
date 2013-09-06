package com.dougemil.criminalintent.model;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

/*
 * Singleton pattern
 * Endures for life of the app
 * 
 */

public class CrimeLab {
	
	private static CrimeLab sCrimeLab;
	@SuppressWarnings("unused")
	private Context mAppContext;
	private ArrayList<Crime> mCrimes;
	
	
	// private constructor
	private CrimeLab(Context appContext){
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
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
