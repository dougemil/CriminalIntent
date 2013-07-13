package com.dougemil.criminalintent.controller.activities;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.dougemil.criminalintent.R;
import com.dougemil.criminalintent.controller.fragments.CrimeFragment;
import com.dougemil.criminalintent.model.Crime;
import com.dougemil.criminalintent.model.CrimeLab;

public class CrimePagerActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// Create the View programmatically instead of w/ XML
		// Constructor uses this Activity as a context parameter
		mViewPager = new ViewPager(this);
		// Still requires an id, defined in res/values/ids
		mViewPager.setId(R.id.viewPager);
		// inflate
		setContentView(mViewPager);
		
		// Retreives list of crimes
		mCrimes = CrimeLab.get(this).getCrimes();
		
		FragmentManager fm = getSupportFragmentManager();
		// The adapter needs a FragMgr so it can add fragments to ViewPager
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mCrimes.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Crime crime = mCrimes.get(pos);
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		
		// onPageChangeListener() called when user flips a page
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) {}
			
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixes){}
			
			// changes the title to reflect the current page
			public void onPageSelected(int pos) {
				Crime crime = mCrimes.get(pos);
				if (crime.getTitle() != null) {
					setTitle(crime.getTitle());
				}
			}
		});
		
		// Set the initial pager item (it defaults to zero)
		// Set current item to the index of the selected Crime
		UUID crimeId = (UUID)getIntent()
							.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i=0; i < mCrimes.size(); i++) {
			if (mCrimes.get(i).getId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
