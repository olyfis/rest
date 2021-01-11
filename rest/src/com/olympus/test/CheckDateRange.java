package com.olympus.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckDateRange {

	
	// invoke: checkBetween("2020-10-04", "2020-10-01" , "2020-10-09")
	public static boolean checkBetween(String dateToCheck, String startDate, String endDate) {
		boolean res = false;
		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd"); // 2020-10-09
		SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd"); // 2020-10-09
		try {
			Date requestDate = fmt2.parse(dateToCheck);
			Date fromDate = fmt1.parse(startDate);
			Date toDate = fmt1.parse(endDate);
			res = requestDate.compareTo(fromDate) >= 0 && requestDate.compareTo(toDate) <= 0;
		} catch (ParseException pex) {
			pex.printStackTrace();
		}
		return res;
	}
	/***************************************************************************************************************************************************/

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String fromDate = "2020-10-01";
		String toDate = "2020-10-08";

		// String requestDate = "17/02/2018";
		String requestDate = "2020-10-05";

		System.out.println(checkBetween(requestDate, fromDate, toDate));
	}

	/***************************************************************************************************************************************************/
}