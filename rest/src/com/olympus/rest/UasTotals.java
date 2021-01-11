package com.olympus.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
 
@WebServlet("/uastotals")
public class UasTotals extends HttpServlet {
	private final Logger logger = Logger.getLogger(UasTotals.class.getName()); // define logger
	/****************************************************************************************************************************************************/
	public double parseUasData(ArrayList<String> strArr )  {
		String s = "";
		double uasTotal = 0.0;
		double amount = 0.0;
		
		for (String str : strArr) { // iterating ArrayList
			 
			String[] items = str.split(",");
			if (items[6].equals("Territory")) {
				continue;
			}
			amount = Olyutil.strToDouble(items[5]);
			uasTotal += amount;	
		} // End For
		return(uasTotal);
	}
	/****************************************************************************************************************************************************/
	// String dateFmt = formatDate("yyyy-MM-dd");
			// String dateFmt = formatDate("yyyy-MM-dd hh:mm:ss.SSS");
			
			// String dateFmt = formatDate("MM/dd/yyyy");
			
			public static String formatDate(String format) throws IOException {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				Date date = new Date();
				return(dateFormat.format(date));
			}
	
	/**
	 * @throws IOException **************************************************************************************************************************************************/
	public JsonObject parseAgeData(ArrayList<String> strArr, ArrayList<String>  uasArr ) throws IOException  {
		JsonObject obj = new JsonObject();
		String s = "";
		double uasTotal = 0.0;
		Date date = new java.util.Date();
		//System.out.println("***^^^*** Date=" + date);
		String dateUAS = formatDate("M/d/yyyy");
		 
		double currTotal = 0.0;
		double past_1_30 = 0.0;
		double past_31_60 = 0.0;
		double past_61_90 = 0.0;
		double past_91_120 = 0.0;
		double past_121_150 = 0.0;
		double past_151_180 = 0.0;
		double past_181 = 0.0;
		double osBalance = 0.0;
		DecimalFormat df = new DecimalFormat("###,##0.00");
		//DecimalFormat df = new DecimalFormat("#,##0");
		
		
		 
		uasTotal = parseUasData(uasArr);
		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);
		 
			String[] items = str.split(",");
			if (items[0].equals("Contract ID")) {
				continue;
			}
			
			
			s = items[15];
			osBalance = Olyutil.strToDouble(items[5]);
			if (s.equals("Current")) {
				//System.out.println("** S=" + s + "--");
				currTotal += osBalance;
			} else if (s.equals("Past 1-30")) {
				past_1_30 += osBalance;
			} else if (s.equals("Past 31-60")) {
				past_31_60 += osBalance;
			} else if (s.equals("Past 61-90")) {
				past_61_90 += osBalance;
			} else if (s.equals("Past 91-120")) {
				past_91_120 += osBalance;
			} else if (s.equals("Past 121-150")) {
				past_121_150 += osBalance;
			} else if (s.equals("Past 151-180")) {
				past_151_180 += osBalance;
			} else if (s.equals("Past 181+")) {
				past_181 += osBalance;
			}

			
		} // End For
		/*
		System.out.println("**** uasTotal=" + df.format(uasTotal));
		System.out.println("**** currTotal=" + df.format(currTotal));
		System.out.println("**** past_1_30=" + df.format(past_1_30));
		System.out.println("**** past_31_60=" + df.format(past_31_60));
		System.out.println("**** past_61_90=" + df.format(past_61_90));
		System.out.println("**** past_91_120=" + df.format(past_91_120));
		System.out.println("**** past_121_150=" + df.format(past_121_150));
		System.out.println("**** past_151_180=" + df.format(past_151_180));
		System.out.println("**** past_181=" + df.format(past_181));
		*/
		obj.addProperty("date", dateUAS);
		obj.addProperty("uasTotal", uasTotal);
		obj.addProperty("currTotal", currTotal);
		obj.addProperty("past_1_30", past_1_30);
		obj.addProperty("past_31_60", past_31_60);
		obj.addProperty("past_61_90", past_61_90);
		obj.addProperty("past_91_120", past_91_120);
		obj.addProperty("past_121_150", past_121_150);
		obj.addProperty("past_151_180", past_151_180);
		obj.addProperty("past_181", past_181);
		
		return(obj);
		
	}
	
	/****************************************************************************************************************************************************/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 
		JsonArray jsonArr = new JsonArray();
		JsonObject objRtn = new JsonObject();
		PrintWriter out = response.getWriter();
		String dispatchJSP = null;
	
		
		String sep = null;
		ArrayList<String> ageArr = new ArrayList<String>();
		ArrayList<String> uasArr = new ArrayList<String>();
		sep = ";";

		String logFileName = "uastotals.log";
		String directoryName = "D:/Kettle/logfiles/uastotals";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		String dateFmt = "";
		//System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
		//String headerFile = "C:\\Java_Dev\\props\\headers\\uasTotalsHdr.txt";
		String Jsonfile = "C:\\Java_Dev\\JSON\\uastotals.json";
		String ageFile = "C:\\Java_Dev\\props\\uastotals\\ageDetailReport.csv";
		String uasFile = "C:\\Java_Dev\\props\\uastotals\\uasReport.csv";
 
		//ArrayList<String> headerArr = new ArrayList<String>();
		//headerArr = Olyutil.readInputFile(headerFile);
		ageArr = Olyutil.readInputFile(ageFile);
		//Olyutil.printStrArray(ageArr);
		uasArr = Olyutil.readInputFile(uasFile);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		objRtn = parseAgeData(ageArr, uasArr); 
		
		jsonArr.add(objRtn);
		//Olyutil.displayJsonArray(jsonArr);
		

	 
		//strArr = DButil.getDbData(propFile, sqlFile);
		//Olyutil.printStrArray(strArr);
		//jArr = DButil.buildJSON(strArr, headerArr);
		
		Olyutil.jsonWriterResponse(jsonArr, out);
		Olyutil.jsonWriter(jsonArr, Jsonfile);
		// End Display JSON
		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		logger.info(dateFmt + ": " + "------------------ Write JSON to: " + Jsonfile);
		fileHandler.flush();
		fileHandler.close();
		//request.getRequestDispatcher(dispatchJSP).forward(request, response);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");	
	}	
	
	
	/****************************************************************************************************************************************************/

	
	
	
	
	
	
	
	
	
	
	
	
}
