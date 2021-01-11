package com.olympus.rest;

import java.util.HashMap;

public class BookingHist {
	private String  bookedBy;
	private HashMap<String, Integer> bookMap = new HashMap<String, Integer>();
	
	/********************************************************************************************************************************************/
	public String getBookedBy() {
		return bookedBy;
	}
	public void setBookedBy(String bookedBy) {
		this.bookedBy = bookedBy;
	}
	public HashMap<String, Integer> gethMap() {
		return bookMap;
	}
	public void sethMap(HashMap<String, Integer> bookMap) {
		this.bookMap = bookMap;
	}
	
	
	
	
}
