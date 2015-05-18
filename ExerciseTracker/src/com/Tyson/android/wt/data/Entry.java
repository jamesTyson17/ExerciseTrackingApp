/**
 * 
 */
package com.Tyson.android.wt.data;

import java.util.Date;

/**
 * 
 * 
 */
public class Entry {
	private int id;
	private int typeId;
	private Date date;
	private int daySeq;
	private String value;

	/**
	 * Constructor with id
	 */
	public Entry(int id, int typeId, Date date, int daySeq, String value) {
		this.id = id;
		this.typeId = typeId;
		this.date = date;
		this.daySeq = daySeq;
		this.value = value;
	}

	/**
	 * Constructor without id
	 */
	public Entry(int typeId, Date date, int daySeq, String value) {
		this(0, typeId, date, daySeq, value);
	}

	/**
	 *
	 */
	public int getId() {
		return id;
	}

	/**
	 * to set the id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * 
	 */
	public int getDaySeq() {
		return daySeq;
	}

	/**
	 *the daySeq to set
	 */
	public void setDaySeq(int daySeq) {
		this.daySeq = daySeq;
	}

	/**
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 *the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}