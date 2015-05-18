/**
+ * TAB VIEW
 */
package com.Tyson.android.wt.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.Tyson.android.wt.R;

/**
 * Class to add Tab Widgets
 */
public class TabWidget extends TabActivity {
	/**
	 *
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		Bundle bundle = getIntent().getExtras();
		int typeId = bundle.getInt("typeId");

		// Initialize a TabSpec for each tab and add it to the TabHost
		// Entry list tab
		intent = new Intent().setClass(this, EntryListActivity.class)
				.putExtra("typeId", typeId);
		spec = tabHost
				.newTabSpec("lists")
				.setIndicator(this.getString(R.string.tab_list),
						res.getDrawable(R.drawable.ic_tab_list))
				.setContent(intent);
		tabHost.addTab(spec);

		// Graph tab
		intent = new Intent().setClass(this, EntryGraphActivity.class)
				.putExtra("typeId", typeId);
		spec = tabHost
				.newTabSpec("date")
				.setIndicator(this.getString(R.string.tab_graph),
						res.getDrawable(R.drawable.ic_tab_graph))
				.setContent(intent);
		tabHost.addTab(spec);

		// Stats tab
		intent = new Intent().setClass(this, EntryStatsActivity.class)
				.putExtra("typeId", typeId);
		spec = tabHost
				.newTabSpec("stats")
				.setIndicator(this.getString(R.string.tab_stats),
						res.getDrawable(R.drawable.ic_tab_list))
				.setContent(intent);
		tabHost.addTab(spec);
		// Stopwatch tab
				intent = new Intent().setClass(this, EntryStopWatch.class)
						.putExtra("typeId", typeId);
				spec = tabHost
						.newTabSpec("Chrno")
						.setIndicator(this.getString(R.string.tab_meter),
								res.getDrawable(R.drawable.ic_tab_chro))
						.setContent(intent);
				tabHost.addTab(spec);
				
		// set the initial tab
		tabHost.setCurrentTab(0);
	}
}