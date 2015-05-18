/**
 * 
 */
package com.Tyson.android.wt.data;

import java.util.Date;

/**
 *
 * 
 */
public class Exercise {
	private int id;
	private String name;
	private Date createdOn;

	/**
	 * Constructor with three parameters
	 */
	public Exercise(int id, String name, Date createdOn) {
		this.id = id;
		this.name = name;
		this.createdOn = createdOn;
	}

	/**
	 * Constructor with two parameters
	 */
	public Exercise(String name, Date createdOn) {
		this(0, name, createdOn);
	}

	/**
	 * gets and returns id
	 */
	public int getId() {
		return id;
	}

	/**
	 *  the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * gets and returns name
	 */
	public String getName() {
		return name;
	}

	/**
	 * the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}