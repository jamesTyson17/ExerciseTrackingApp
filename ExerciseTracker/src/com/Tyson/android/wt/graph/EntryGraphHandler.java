/**
 * Graph Handler
 */
package com.Tyson.android.wt.graph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.Tyson.android.wt.app.ExerciseTrackerApp;
import com.Tyson.android.wt.data.DataBaseUtil;
import com.Tyson.android.wt.data.Exercise;

/**
 * 
 * 
 */
public class EntryGraphHandler {
	private static final String TAG = "EntryGraphHandler";
	private WebView mAppView;
	private Exercise mType;
	private Context mContext;
	private ExerciseTrackerApp mApp;

	/**
	 * Constructor
	 */
	public EntryGraphHandler(WebView appView, Exercise type, Context context,
			ExerciseTrackerApp app) {
		this.mAppView = appView;
		this.mType = type;
		this.mContext = context;
		this.mApp = app;
	}

	/**
	 * Set the title of the graph
	 */
	public String getGraphTitle() {
		return mType.getName();
	}

	/**
	 * Load the default graph
	 */
	public void loadGraph() {
		JSONArray data = null;

		switch (mApp.getCurrentGroupBy()) {
		case DATE:

			data = DataBaseUtil.fetchEntriesByDateInJSON(mContext, mType.getId());
			break;

		case MAX:

			data = DataBaseUtil.fetchEntriesByMaxInJSON(mContext, mType.getId());
			break;

		case NONE:
			data = DataBaseUtil.fetchEntriesInJSON(mContext, mType.getId());
			break;
		}
		loadGraph(data);
	}

	/**
	 * Load Graph data
	 */
	private void loadGraph(JSONArray data) {
		JSONArray arr = new JSONArray();

		JSONObject result = new JSONObject();
		try {
			result.put("data", data);// will ultimately look like: {"data":
										// p[x1,y1],[x2,y2],[x3,y3],[]....]},
			result.put("lines", getLineOptionsJSON()); // { "lines": { "show" :
														// true }},
			result.put("points", getPointOptionsJSON()); // { "points": { "show"
															// : true }}
		} catch (JSONException e) {
			Log.d(TAG, "Got an exception while trying to parse JSON");
			e.printStackTrace();
		}
		arr.put(result);

		// return arr.toString(); //This _WILL_ return the data in a good
		// looking JSON string, but if you pass it straight into the Flot Plot
		// method, it will not work!
		mAppView.loadUrl("javascript:GotGraph(" + arr.toString() + ")"); 
																		
																			
	}

	/**
	 * Get Points action
	 */
	private JSONArray getPointOptionsJSON() {
		JSONArray pointOption = new JSONArray();
		pointOption.put("show");
		pointOption.put(true);

		return pointOption;
	}

	/**
	 * Get Lines option
	 */
	private JSONArray getLineOptionsJSON() {
		JSONArray lineOption = new JSONArray();
		lineOption.put("show");
		lineOption.put(true);

		return lineOption;
	}
}