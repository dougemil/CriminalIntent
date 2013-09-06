package com.dougemil.criminalintent.model;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
	
public class Crime {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	
	private UUID mId;
	private String mTitle;	
	private Date mDate;
	private boolean mSolved;
	
	public Crime(){
		// Generates unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	// Constructor that accepts serializable format and populates mVariables
	public Crime(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ID));
		mTitle = json.getString(JSON_TITLE);
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
	}
	
	// Convert Crime data into serializabale format
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		return json;
	}
	
	// Override for use with ArrayAdapter View generation
	@Override
	public String toString() {
		return mTitle;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

}
