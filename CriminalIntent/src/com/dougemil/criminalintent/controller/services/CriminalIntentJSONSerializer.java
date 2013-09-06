package com.dougemil.criminalintent.controller.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

import com.dougemil.criminalintent.model.Crime;

public class CriminalIntentJSONSerializer {

	private Context mContext;
	private String mFilename;
	
	public CriminalIntentJSONSerializer (Context c, String f) {
		mContext = c;
		mFilename = f;
	}
	
	public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
		
		// Build JSON array
		JSONArray array = new JSONArray();
		for (Crime c : crimes)
			array.put(c.toJSON());
		
		// Write file to disk
		Writer writer = null;
		try {
			// filename is automatically pre-pended with sandbox path, file is created
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
		
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		
		try {
			// Open and read file into a StringBuilder
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			
			// Reads file data to a String, String is composed of JSONObjects
			while ((line = reader.readLine()) != null){
				// line breaks are omitted
				jsonString.append(line);
			}
			
			// Parse the string of JSONObjects into array of JSONObjects
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			
			//Build array of crimes from JSONObjects in JSONArray
			for (int i=0; i < array.length(); i++){
				crimes.add(new Crime(array.getJSONObject(i)));
			}
			
		}catch (FileNotFoundException e) {
			// Ignoring, will fire every time no crimes are saved
		}finally{
			if (reader != null)
				reader.close();
		}
		return crimes;
	}
}
