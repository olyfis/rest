package com.olympus.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class TestDateRange {
	/***************************************************************************************************************************************/

	boolean isWithinRange(Date testDate) {
	Date startDate = null;
	Date endDate = null;
	    return testDate.getTime() >= startDate.getTime() &&
	             testDate.getTime() <= endDate.getTime();
	}
	/***************************************************************************************************************************************/
	// declare calendar outside the scope of isWithinRange() so that we initialize it only once
	private static Calendar calendar = Calendar.getInstance();

	public static boolean isWithinRangeYear(Date date, Date startDate, Date endDate) {

	    calendar.setTime(startDate);
	    int startDayOfYear = calendar.get(Calendar.DAY_OF_YEAR); // first day is 1, last day is 365
	    int startYear = calendar.get(Calendar.YEAR);

	    calendar.setTime(endDate);
	    int endDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
	    int endYear = calendar.get(Calendar.YEAR);

	    calendar.setTime(date);
	    int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
	    int year = calendar.get(Calendar.YEAR);

	    return (year > startYear && year < endYear) // year is within the range
	            || (year == startYear && dayOfYear >= startDayOfYear) // year is same as start year, check day as well
	            || (year == endYear && dayOfYear < endDayOfYear); // year is same as end year, check day as well

	}
	
	/**
	 * @throws ParseException *************************************************************************************************************************************/

	public static void main(String[] args) throws ParseException {
		Date date = new Date();
		Date startDate = new Date();
		Date endDate = new Date();
		 SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 
		
		ft = new SimpleDateFormat ("yyyy-MM-dd"); 
		
		
		 date = ft.parse("2020-01-09"); 
		 startDate = ft.parse("2020-09-01"); 
		 endDate = ft.parse("2020-09-12"); 
         System.out.println("*** date=" + date); 
        if ( isWithinRangeYear(date, startDate, endDate) ) {
        	 System.out.println("*** In Range date=" + date); 
        } else {
        	 System.out.println("*** NOT In Range date=" + date); 
        }

	}
	
	
	/***************************************************************************************************************************************/

	
	
}
