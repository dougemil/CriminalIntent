package com.dougemil.criminalintent.controller.fragments;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
	private boolean mSubtitleVisible;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// Advise FragmentManager of Options Menu
		setHasOptionsMenu(true);
		
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
		
		// Handle rotation
		setRetainInstance(true);
		mSubtitleVisible = false;
	}
	
	
	@TargetApi(11)
	@Override
	// Managing rotation
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(mSubtitleVisible){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		// Registering the view of a floating context menu
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			// Use floating context menu
			registerForContextMenu(listView);
			
		}else{
			// Use contextual action bar
			// allow multiple selections (choices)
			
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			
			// **Creating the listener
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked){
					// Required method, not used
				}
				
				// ActionMode.Callback methods
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// inlfater retrieved from ActionMode, not Activity
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				public boolean onPrepareActionMode(ActionMode mode, Menu menu){
					// Required method, not used
					return false;
				}
				public boolean onActionItemClicked(ActionMode mode, MenuItem item){
					switch (item.getItemId()){
						case R.id.menu_item_delte_crime:
							CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
							CrimeLab crimeLab = CrimeLab.get(getActivity());
							for(int i = adapter.getCount() - 1; i >= 0; i--){
								if (getListView().isItemChecked(i)){
									crimeLab.deleteCrime(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default:
							return false;
								}
							}
				public void onDestroyActionMode(ActionMode mode){
					// Required method, not used
					}
				
			});
		}
		return v;
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

			@Override
			// Called by FragmentManager, FragMgr must be adivsed to handle
			public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
				
				super.onCreateOptionsMenu(menu, inflater);
				// Inflate and populate menu
				inflater.inflate(R.menu.fragment_crime_list, menu);
				
				// Manage rotation
				MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
				if (mSubtitleVisible && showSubtitle != null) {
					showSubtitle.setTitle(R.string.hide_subtitle);
				}
			}

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				// check ID to determine nature of MenuItem
				switch (item.getItemId()){
				case R.id.menu_item_new_crime:
					Crime crime = new Crime();
					CrimeLab.get(getActivity()).addCrime(crime);
					Intent i = new Intent(getActivity(), CrimePagerActivity.class);
					i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
					startActivityForResult(i,0);
					return true;
				case R.id.menu_item_show_subtitle:
					if(getActivity().getActionBar().getSubtitle() == null){
						getActivity().getActionBar().setSubtitle(R.string.subtitle);
						mSubtitleVisible = true;
						item.setTitle(R.string.hide_subtitle);
					}else{
						getActivity().getActionBar().setSubtitle(null);
						mSubtitleVisible = false;
						item.setTitle(R.string.show_subtitle);
					}
					return true;
				default:
					return super.onOptionsItemSelected(item);
				}
			}


			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// inflate menu, inflater must be retrieved from parent activity
				// there is only one context menu resource defined so no need to check id of received view
				// view must be registered in onCreateView(...)
				getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
			}


			@Override
			public boolean onContextItemSelected(MenuItem item) {
				
				// Use MenuInfo and adapter to determine which Crime was long-pressed
				AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
				int position = info.position;
				CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
				Crime crime = adapter.getItem(position);
				
				// delete Crime
				switch (item.getItemId()) {
					case R.id.menu_item_delte_crime:
						CrimeLab.get(getActivity()).deleteCrime(crime);
						adapter.notifyDataSetChanged();
						return true;
				}		
				return super.onContextItemSelected(item);
			}


			@Override
			// Save crimes to disk, crimes are stored in a member variable of CrimeLab singleton
			public void onPause() {
				super.onPause();
				CrimeLab.get(getActivity()).saveCrimes();
			}

}
