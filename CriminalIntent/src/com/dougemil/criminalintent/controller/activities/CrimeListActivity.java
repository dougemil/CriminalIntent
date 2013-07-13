package com.dougemil.criminalintent.controller.activities;

import com.dougemil.criminalintent.controller.fragments.CrimeListFragment;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		// Overrides method from superclass to return appropriate frag
		return new CrimeListFragment();
	}

}
