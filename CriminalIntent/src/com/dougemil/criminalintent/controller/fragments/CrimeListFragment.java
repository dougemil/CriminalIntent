package com.dougemil.criminalintent.controller.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dougemil.criminalintent.R;
import com.dougemil.criminalintent.controller.activities.CrimePagerActivity;
import com.dougemil.criminalintent.model.Crime;
import com.dougemil.criminalintent.model.CrimeLab;

public class CrimeListFragment extends ListFragment {
	
	// Default implementation inflates a full page ListView
	// Overriding onCreateView() is not required
	
	private static final String TAG = "CrimeListFragment";
	private ArrayList<Crime> mCrimes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// Change title of host activity (displayed on action bar)
		getActivity().setTitle(R.string.crimes_title);
		// static method call to create CrimeLab singleton and populate
		mCrimes = CrimeLab.get(getActivity()).getCrimes();

				/* Deprecated in favor of custom adapater implementation below
				// 1st param gets the context required for the 2nd param
				// 2nd param is a predefined layout, TextView
				// 3rd param is the data set to be used
				ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(),
																android.R.layout.simple_list_item_1,
																mCrimes);
				*/
		
		// Custom adapter implemented below as inner class
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		
		// method inherited from ListFragment
		// sets the adapter of the ListView managed by CrimeListFragment
		setListAdapter(adapter);
		
		// default ArrayAdapter.getView() uses object.toString() to populate the TextView
		// override toString in model for more useful behavior, or implement custom adapter
	}
	
	/*
	 * Focusable widgets (Button, CheckBox, etc.)appearing in a ListItem layout
	 * must be made non-focusable
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// getListAdapter() is another ListFragment convenience method
		// get the adapter, use the position param to get an item from it
				/* Deprecated for custom adapter imp
				 * Crime c = (Crime)(getListAdapter()).getItem(position);
				 */
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
		Log.d(TAG, c.getTitle() + " was clciked");
		
		// Start CrimeActivity ***Deprecated for ViewPager Impl
		// Intent constructor requires a context,
		// 1st param getActivity() passes this classes host activity as a context
		// 2nd param is the destination
		// Intent i = new Intent(getActivity(), CrimeActivity.class);
		// pass key/value pair for Crime to be displayed
		
		// Start CrimePagerActivity
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i);
	}
	
			// Subclassing ArrayAdapter<> as an inner class
			private class CrimeAdapter extends ArrayAdapter<Crime> {
				
				//Constructor calls super class
				// 2nd param is layout, ignored with a 0 value
				public CrimeAdapter(ArrayList<Crime> crimes) {
					super(getActivity(), 0, crimes);
				}
				
				@Override
				// 2nd param allows conversion of existing views (instead of creating new views) more efficient
				public View getView(int position, View convertView, ViewGroup parent) {
					
					// check for recycled View from convertView, inflate new one of necessary
					if (convertView == null){
						convertView = getActivity()
										.getLayoutInflater()
										.inflate(R.layout.list_item_crime, null);
					}
					
					// get Crime for current position on the list
					// call Adpater's getItem(int) method
					Crime c = getItem(position);
					
					// get references and set values
					TextView titleTextView = (TextView)convertView
												.findViewById(R.id.crime_list_item_titleTextView);
					titleTextView.setText(c.getTitle());
					
					TextView dateTextView = (TextView)convertView
												.findViewById(R.id.crime_list_item_dateTextView);
					dateTextView.setText(c.getDate().toString());
					
					CheckBox solvedCheckBox = (CheckBox)convertView
												.findViewById(R.id.crime_list_item_solvedCheckBox);
					solvedCheckBox.setChecked(c.isSolved());
					
					return convertView;
					
				}
			}

			// Called when user backs from a crime to the list
			// Adapter needs to be notified so the list can be updated if it has changed
			@Override
			public void onResume() {
				
				super.onResume();
				((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
			}

}
