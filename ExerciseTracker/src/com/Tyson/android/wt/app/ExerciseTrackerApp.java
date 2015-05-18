/**
 * 
 */
package com.Tyson.android.wt.app;

import android.app.Application;

/**
 * Application object for the app
 */
public class ExerciseTrackerApp extends Application {

	// Entry status
	private DialogStatus currrentDialogStatus = DialogStatus.DEFAULT;
	private GroupBy currentGroupBy = GroupBy.NONE;
	private SortBy currentSortBy = SortBy.DATE_ASC;

	// Exercise status
	private DialogStatus currentExerciseDialogStatus = DialogStatus.DEFAULT;

	/**
	 * Current dialog status
	 */
	public enum DialogStatus {
		DEFAULT, ADD, EDIT;
	}

	/**
	 * Current group by status
	 */
	public enum GroupBy {
		NONE, DATE, MAX;
	}

	/**
	 * Current Sort by status
	 */
	public enum SortBy {
		DATE_ASC, DATE_DESC, VALUE_ASC, VALUE_DESC;
	}

	/**
	 * Default Contstructor
	 */
	public ExerciseTrackerApp() {
		super();
	}

	/**
	 *the currrentDialogStatus to set
	 */
	public void setCurrrentDialogStatus(DialogStatus currrentDialogStatus) {
		this.currrentDialogStatus = currrentDialogStatus;
	}

	/**
	 * gets and returns the currrentDialogStatus
	 */
	public DialogStatus getCurrrentDialogStatus() {
		return currrentDialogStatus;
	}

	/**
	 *the currentGroupBy to set
	 */
	public void setCurrentGroupBy(GroupBy currentGroupBy) {
		this.currentGroupBy = currentGroupBy;
	}

	/**
	 * the currentGroupBy
	 */
	public GroupBy getCurrentGroupBy() {
		return currentGroupBy;
	}

	/**
	 *the currentSortBy
	 */
	public SortBy getCurrentSortBy() {
		return currentSortBy;
	}

	/**
	 *the currentSortBy to set
	 */
	public void setCurrentSortBy(SortBy currentSortBy) {
		this.currentSortBy = currentSortBy;
	}

	/**
	 *the currentExerciseDialogStatus to set
	 */
	public void setCurrentExerciseDialogStatus(
			DialogStatus currentExerciseDialogStatus) {
		this.currentExerciseDialogStatus = currentExerciseDialogStatus;
	}

	/**
	 *
	 */
	public DialogStatus getCurrentExerciseDialogStatus() {
		return currentExerciseDialogStatus;
	}

}
