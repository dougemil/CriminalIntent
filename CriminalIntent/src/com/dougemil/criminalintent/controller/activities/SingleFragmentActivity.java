package com.dougemil.criminalintent.controller.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.dougemil.criminalintent.R;

public abstract class SingleFragmentActivity extends FragmentActivity {
	
	// subclasses implement this method to return appropriate Fragment
	protected abstract Fragment createFragment();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		
		// "getFragmentManager" (for > API 11)
		FragmentManager fm = getSupportFragmentManager();
		
		// references layout that was created as a conatiner (activity_fragment)
		// ask fm for a Fragment by referencing it's conatiner
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		// check to see if Fragment was already in fms list of Frags
		if (fragment == null){
			// call protected method to create new frag
			fragment = createFragment();
			// create and commit a Fragment transaction
			fm.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}

}
