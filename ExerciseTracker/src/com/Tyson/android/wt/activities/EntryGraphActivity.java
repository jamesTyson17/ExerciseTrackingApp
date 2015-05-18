/**
 * Graph Activity
 */
package com.Tyson.android.wt.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.Tyson.android.wt.app.ExerciseTrackerApp;
import com.Tyson.android.wt.app.ExerciseTrackerApp.GroupBy;
import com.Tyson.android.wt.data.DataBaseUtil;
import com.Tyson.android.wt.graph.EntryGraphHandler;
import com.Tyson.android.wt.R;

/**
 *
 */
public class EntryGraphActivity extends Activity {
	private static final String TAG = "GraphActivity";

	private ExerciseTrackerApp mApp;
	private EntryGraphHandler mEntryGraphHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_graph);
		WebView wv = (WebView) findViewById(R.id.wv1);

		Bundle bundle = getIntent().getExtras();
		Log.d(TAG, "Got type id: " + bundle.getInt("typeId"));

		mApp = (ExerciseTrackerApp) getApplication();
		mEntryGraphHandler = new EntryGraphHandler(wv, DataBaseUtil.fetchExercise(
				this, bundle.getInt("typeId")), this, mApp);

		wv.getSettings().setJavaScriptEnabled(true);
		wv.addJavascriptInterface(mEntryGraphHandler, "testhandler");
		wv.loadUrl("file:///android_asset/flot/entry_graph.html");

		Log.d(TAG, "Web view initialized");
	}

	/**
	 * When the activity is resumed
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mEntryGraphHandler.loadGraph();
	}

	/**
	 * When the back button is pressed
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public void onBackPressed() {
		mApp.setCurrentGroupBy(GroupBy.NONE);
		super.onBackPressed();
	}

	/**
	 * Create options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.entries_graph_menu, menu);
		return true;
	}

	/**
	 * When a Option menu item is selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.group_entry_by_date:

			mApp.setCurrentGroupBy(GroupBy.DATE);
			break;

		case R.id.group_entry_by_max:

			mApp.setCurrentGroupBy(GroupBy.MAX);
			break;

		case R.id.group_entry_by_none:

			mApp.setCurrentGroupBy(GroupBy.NONE);
			break;
		default:
			break;
		}

		mEntryGraphHandler.loadGraph();
		return super.onOptionsItemSelected(item);
	}

}