/**
 * 
 */
package com.Tyson.android.wt.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * holds the date and value of exercise
 */
public class EntryDateActivity extends ListActivity {

	static class ViewHolder {
		TextView date;
		TextView value;
	}

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
