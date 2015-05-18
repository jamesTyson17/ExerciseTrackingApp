/**
 * Activity to display entries in list 
 */
package com.Tyson.android.wt.activities;

import static com.Tyson.android.wt.data.DataBaseConstants.ENTRY_DATE;
import static com.Tyson.android.wt.data.DataBaseConstants.ENTRY_VALUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;

import com.Tyson.android.wt.app.ExerciseTrackerApp;
import com.Tyson.android.wt.app.ExerciseTrackerApp.DialogStatus;
import com.Tyson.android.wt.app.ExerciseTrackerApp.GroupBy;
import com.Tyson.android.wt.app.ExerciseTrackerApp.SortBy;
import com.Tyson.android.wt.data.DataBaseUtil;
import com.Tyson.android.wt.data.Entry;
import com.Tyson.android.wt.data.Exercise;
import com.Tyson.android.wt.R;

/**
 * 
 * 
 */
public class EntryListActivity extends ListActivity {

	private static final String TAG = "ShowEntries";

	private List<Entry> mEntries;
	private Exercise mType;
	private Button entryDate;
	private Button entryTime;
	private View mAddEntryDialogLayout;
	private Context mContext;
	private ArrayAdapter<Entry> mArrayAdapter;

	// package scope, since it is accessed in inner classes
	ExerciseTrackerApp mApp;
	LayoutInflater mInflater;

	int mYear;
	int mMonth;
	int mDay;
	int mHour;
	int mMinute;
	int mSecond;

	private static final int DIALOG_ADD_ENTRY = 0;
	private static final int DIALOG_DATE_PICKER = 2;
	private static final int DIALOG_TIME_PICKER = 3;

	/**
	 * View Holder class
	 */
	static class ViewHolder {
		TextView date;
		TextView daySeq;
		TextView value;
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
			resetMaxDaySeq();
		}
	};

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};

	/**
	 * Group by Array Adpater
	 */
	private class GroupByAdapter extends ArrayAdapter<Entry> {
		/**
		 */
		public GroupByAdapter(Context context, int textViewResourceId,
				List<Entry> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.entry_list_item,
						parent, false);

				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.daySeq = (TextView) convertView
						.findViewById(R.id.day_seq);
				holder.value = (TextView) convertView.findViewById(R.id.value);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Entry entry = (Entry) mEntries.get(position);

			if (mApp.getCurrentGroupBy() == GroupBy.NONE) {
				holder.date.setText(DataBaseUtil.dateToString(entry.getDate(),
						"yyyy-MM-dd HH:mm:ss"));
			} else {
				holder.date.setText(DataBaseUtil.dateToString(entry.getDate(),
						"yyyy-MM-dd"));
			}

			holder.daySeq.setText("" + entry.getDaySeq());
			holder.value.setText(entry.getValue());

			return convertView;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_list);

		mApp = (ExerciseTrackerApp) getApplication();
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = this;

		Bundle bundle = getIntent().getExtras();
		Log.d(TAG, "Got type id: " + bundle.getInt("typeId"));
		mType = DataBaseUtil.fetchExercise(mContext, bundle.getInt("typeId"));

		// dialog box layout
		mAddEntryDialogLayout = mInflater.inflate(R.layout.add_entry_dialog,
				(ViewGroup) findViewById(R.id.layout_root));

		inializeTimeControls();

		// Add entry button
		Button addEntryButton = (Button) findViewById(R.id.add_entry);
		addEntryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mApp.setCurrrentDialogStatus(DialogStatus.ADD);
				showDialog(DIALOG_ADD_ENTRY);
			}
		});

		// sorting entires
		Spinner sortBy = (Spinner) findViewById(R.id.sort_by);
		sortBy.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				SortBy currentSortBy = mApp.getCurrentSortBy();

				switch (position) {
				case 0:
					// sort by date - DESC
					if (currentSortBy != SortBy.DATE_DESC) {
						mApp.setCurrentSortBy(SortBy.DATE_DESC);
						resetListView();
					}
					break;

				case 1:
					// sort by date - ASC
					if (currentSortBy != SortBy.DATE_ASC) {
						mApp.setCurrentSortBy(SortBy.DATE_ASC);
						resetListView();
					}
					break;

				case 2:
					// sort by value - DESC
					if (currentSortBy != SortBy.VALUE_DESC) {
						mApp.setCurrentSortBy(SortBy.VALUE_DESC);
						resetListView();
					}
					break;

				case 3:
					// sort by value - ASC
					if (currentSortBy != SortBy.VALUE_ASC) {
						mApp.setCurrentSortBy(SortBy.VALUE_ASC);
						resetListView();
					}
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Callback method to be invoked when the selection disappears
				// from this view. The selection can disappear for instance when
				// touch is activated or when the adapter becomes empty.
				// We don't need to do anything for this
			}
		});

		// register for context menu
		registerForContextMenu(getListView());
	}

	/**
	 * 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		if (mApp.getCurrentGroupBy() == GroupBy.NONE) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.entry_context_menu, menu);
		}

	}

	/**
	 *
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.edit_entry:
			editEntry((int) info.id);
			return true;
		case R.id.delete_entry:
			deleteEntry((int) info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * Edit Entry when context menu is clicked
	 */
	private void editEntry(int position) {
		entryTime.setTag(mEntries.get(position));

		mApp.setCurrrentDialogStatus(DialogStatus.EDIT);

		showDialog(DIALOG_ADD_ENTRY);
	}

	/**
	 * Delete Entry when context menu is clicked
	 */
	private void deleteEntry(int position) {
		Entry entry = mEntries.get(position);
		DataBaseUtil.deleteEntry(mContext, entry.getId());

		Toast.makeText(mContext,
				mContext.getResources().getString(R.string.entry_deleted),
				Toast.LENGTH_SHORT).show();

		mArrayAdapter.remove(entry);
		mArrayAdapter.notifyDataSetChanged();
	}

	/**
	 * Create options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.entries_menu, menu);
		return true;
	}

	/**
	 * When a Option menu item is selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.group_entry_by_date:

			if (mApp.getCurrentGroupBy() != GroupBy.DATE) {
				mApp.setCurrentGroupBy(GroupBy.DATE);
				resetListView();
			}

			break;

		case R.id.group_entry_by_max:

			if (mApp.getCurrentGroupBy() != GroupBy.MAX) {
				mApp.setCurrentGroupBy(GroupBy.MAX);
				resetListView();
			}

			break;

		case R.id.group_entry_by_none:

			if (mApp.getCurrentGroupBy() != GroupBy.NONE) {
				mApp.setCurrentGroupBy(GroupBy.NONE);
				resetListView();
			}

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * When the back button is pressed
	 */
	@Override
	public void onBackPressed() {
		mApp.setCurrentGroupBy(GroupBy.NONE);
		super.onBackPressed();
	}

	/**
	 * Create Dialog for the first time
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder;

		switch (id) {

		case DIALOG_ADD_ENTRY:
			// build the dialog
			builder = new AlertDialog.Builder(mContext);
			builder.setView(mAddEntryDialogLayout);
			builder.setMessage("")
					.setCancelable(false)
					.setPositiveButton(
							this.getString(R.string.add, mType.getName()), null)
					.setNegativeButton(this.getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			dialog = builder.create();

			break;

		case DIALOG_DATE_PICKER:
			// Date picker
			dialog = new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth, mDay);
			break;

		case DIALOG_TIME_PICKER:
			// Time Picker
			dialog = new TimePickerDialog(this, mTimeSetListener, mHour,
					mMinute, false);
			break;

		default:
			dialog = null;
			break;
		}

		return dialog;
	}

	/*
	 * 
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		EditText entryValue = (EditText) mAddEntryDialogLayout
				.findViewById(R.id.entry_value);
		AlertDialog alertDialog = (AlertDialog) dialog;
		Button positiveButton = null;

		switch (id) {
		case DIALOG_ADD_ENTRY:

			switch (mApp.getCurrrentDialogStatus()) {
			case ADD:

				alertDialog.setMessage(this.getString(R.string.add,
						mType.getName()));
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
						this.getString(R.string.add, mType.getName()),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Insert the new data into db
								EditText entryValue = (EditText) mAddEntryDialogLayout
										.findViewById(R.id.entry_value);
								int maxDaySeq = Integer
										.parseInt((String) entryDate.getTag());
								Entry entry = new Entry(mType.getId(),
										getEntryDate(), maxDaySeq + 1,
										entryValue.getText().toString());

								DataBaseUtil.insertEntry(mContext, entry);
								Toast.makeText(
										mContext,
										mContext.getResources().getString(
												R.string.entry_saved),
										Toast.LENGTH_SHORT).show();

								if (mApp.getCurrentGroupBy() != GroupBy.NONE) {
									resetListView();
								} else {
									mArrayAdapter.add(entry);
									mArrayAdapter.notifyDataSetChanged();
								}
							}
						});

				positiveButton = alertDialog
						.getButton(DialogInterface.BUTTON_POSITIVE);
				positiveButton.setText(this.getString(R.string.add,
						mType.getName()));
				positiveButton.invalidate();

				setDateAndtime();
				updateDisplay();
				resetMaxDaySeq();

				entryValue.setText("");

				break;

			case EDIT:

				alertDialog.setMessage(this.getString(R.string.edit,
						mType.getName()));

				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
						this.getString(R.string.edit_button),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Update the data into db
								EditText entryValue = (EditText) mAddEntryDialogLayout
										.findViewById(R.id.entry_value);

								Entry entryToEdit = (Entry) entryTime.getTag();
								entryToEdit.setDate(getEntryDate());
								entryToEdit.setValue(entryValue.getText()
										.toString());

								DataBaseUtil.updateEntry(mContext, entryToEdit);
								Toast.makeText(
										mContext,
										mContext.getResources().getString(
												R.string.entry_edited),
										Toast.LENGTH_SHORT).show();
								mArrayAdapter.notifyDataSetChanged();
							}

						});

				positiveButton = alertDialog
						.getButton(DialogInterface.BUTTON_POSITIVE);
				positiveButton.setText(this.getString(R.string.edit_button));
				positiveButton.invalidate();

				Entry entryToEdit = (Entry) entryTime.getTag();
				entryDate.setTag(Integer.toString(entryToEdit.getDaySeq()));

				entryValue.setText(entryToEdit.getValue());

				setDateAndtime(entryToEdit.getDate());
				updateDisplay();
				break;

			case DEFAULT:

				break;
			}

			mApp.setCurrrentDialogStatus(DialogStatus.DEFAULT);
			break;

		case DIALOG_DATE_PICKER:
			// Date picker
			DatePickerDialog datePicker = (DatePickerDialog) dialog;
			datePicker.updateDate(mYear, mMonth, mDay);

			break;

		case DIALOG_TIME_PICKER:
			// Time Picker
			TimePickerDialog timePicker = (TimePickerDialog) dialog;
			timePicker.updateTime(mHour, mMinute);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		resetListView();
		setDateAndtime();
	}

	/**
	 * Update the Date and Time buttons
	 */
	private void updateDisplay() {
		entryDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));

		entryTime.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}

	/**
	 * Reset max day seq
	 */
	private void resetMaxDaySeq() {
		// set the max day seq
		int maxDaySeq = DataBaseUtil.getMaxDaySeq(mContext, mYear + "-"
				+ pad(mMonth + 1) + "-" + pad(mDay), mType.getId());
		entryDate.setTag(Integer.toString(maxDaySeq));
	}

	/**
	 * InializeTimeControls
	 */
	private void inializeTimeControls() {
		// set Date
		entryDate = (Button) mAddEntryDialogLayout
				.findViewById(R.id.entry_date);
		entryDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_DATE_PICKER);
			}
		});

		// set time
		entryTime = (Button) mAddEntryDialogLayout
				.findViewById(R.id.entry_time);
		entryTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_TIME_PICKER);
			}
		});
	}

	/**
	 * Pad the time display
	 */
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	/**
	 * Get the entry date from other values
	 */
	private Date getEntryDate() {
		SimpleDateFormat iso8601Format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = iso8601Format.parse(mYear + "-" + (mMonth + 1) + "-" + mDay
					+ " " + mHour + ":" + mMinute + ":" + mSecond);
		} catch (ParseException e) {
			Log.e(TAG, "Parsing ISO8601 datetime failed", e);
		}
		return date;
	}

	/**
	 * Set the current date and time
	 */
	private void setDateAndtime() {
		// get the current date and time
		final Calendar c = Calendar.getInstance();
		setDateAndTimeHelper(c);
	}

	/**
	 * Set the passed date
	 */
	private void setDateAndtime(Date date) {
		// get the date and time
		final Calendar c = Calendar.getInstance();
		c.setTime(date);

		setDateAndTimeHelper(c);
	}

	/**
	 * Initalize all member variables
	 */
	private void setDateAndTimeHelper(final Calendar c) {
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);
	}

	/**
	 * Reset Listview based on current sort by and group by
	 */
	private void resetListView() {
		Log.d(this.getClass().getSimpleName(), "List View reset");

		switch (mApp.getCurrentGroupBy()) {
		case DATE:
			mEntries = DataBaseUtil.fetchEntriesByDate(mContext, mType.getId(),
					getSortBy());
			break;

		case MAX:
			mEntries = DataBaseUtil.fetchEntriesByMax(mContext, mType.getId(),
					getSortBy());
			break;

		case NONE:

			mEntries = DataBaseUtil
					.fetchEntries(mContext, mType.getId(), getSortBy());
			break;
		}

		mArrayAdapter = new GroupByAdapter(this, R.layout.entry_list_item,
				mEntries);

		setListAdapter(mArrayAdapter);
		mArrayAdapter.notifyDataSetInvalidated();
	}

	/**
	 * Get the order by string based on current sort by
	 */
	private String getSortBy() {
		String orderBy = "";
		switch (mApp.getCurrentSortBy()) {
		case DATE_ASC:
			orderBy = ENTRY_DATE;
			break;

		case DATE_DESC:
			orderBy = ENTRY_DATE + " DESC";
			break;

		case VALUE_ASC:
			orderBy = ENTRY_VALUE;
			break;

		case VALUE_DESC:
			orderBy = ENTRY_VALUE + " DESC";
			break;
		}
		return orderBy;
	}
}