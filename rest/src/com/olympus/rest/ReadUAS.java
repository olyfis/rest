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
 
@WebServlet("/uasdata")
public class ReadUAS extends HttpServlet {
	private final Logger logger = Logger.getLogger(ReadUAS.class.getName()); // define logger
	/****************************************************************************************************************************************************/
	/****************************************************************************************************************************************************/
	// String dateFmt = formatDate("yyyy-MM-dd");
			// String dateFmt = formatDate("yyyy-MM-dd hh:mm:ss.SSS");
			
			// String dateFmt = formatDate("MM/dd/yyyy");
			
			public static String formatDate(String format) throws IOException {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				Date date = new Date();
				return(dateFormat.format(date));
			}

	/*****************************************************************************************************************************************************/
	public JsonArray  parseData(ArrayList<String> strArr) throws IOException  {
		
		JsonArray jsonArr = new JsonArray();
		ArrayList<String> headerArr = new ArrayList<String>();
		String headerFile = "C:\\Java_Dev\\props\\headers\\uas_rest_hdr.txt";
		String s = "";
		int i  = 0;
		int sz = 0;
		Date date = new java.util.Date();
		//System.out.println("***^^^*** Date=" + date);
		String dateUAS = formatDate("MM/dd/yyyy");
		headerArr = Olyutil.readInputFile(headerFile);
		//Olyutil.printStrArray(headerArr);
		
		
		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);
			JsonObject obj = new JsonObject();
			String[] items = str.split(",");
			if (items[0].equals("Unapplied Trans #")) {
				continue;
			}

			 
			sz = items.length;
			for (int k = 0; k < sz; k++) {
				//System.out.println("k=" + k + "/" + sz + "-- " + items[k] );
				
				//
				
				//System.out.println("k=" + k + "/" + sz + "-- " + items[k]  + "--HDR=" + headerArr.get(k));
				if (k == 5) {
					obj.addProperty(headerArr.get(k),Olyutil.strToDouble(items[k]));
				} else {
					obj.addProperty(headerArr.get(k), items[k]);
				}
				
				
				
			}
			//System.out.println("*********************************** ");
			jsonArr.add(obj);
		}
		
		
		return(jsonArr);
		
	}
	/****************************************************************************************************************************************************/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 
		JsonArray jsonArr = new JsonArray();
		//JsonObject objRtn = new JsonObject();
		PrintWriter out = response.getWriter();
		String dispatchJSP = null;
	
		String sep = null;
		//ArrayList<String> ageArr = new ArrayList<String>();
		ArrayList<String> uasArr = new ArrayList<String>();
		sep = ";";

		String logFileName = "uasdata.log";
		String directoryName = "D:/Kettle/logfiles/uasdata";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		String dateFmt = "";
		//System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
		//String headerFile = "C:\\Java_Dev\\props\\headers\\uasTotalsHdr.txt";
		String Jsonfile = "C:\\Java_Dev\\JSON\\uasdata.json";
		//String ageFile = "C:\\Java_Dev\\props\\uastotals\\ageDetailReport.csv";
		String uasFile = "C:\\Java_Dev\\props\\uastotals\\uasReport.csv";
 
		//ArrayList<String> headerArr = new ArrayList<String>();
		//headerArr = Olyutil.readInputFile(headerFile);
		//ageArr = Olyutil.readInputFile(ageFile);
		//
		uasArr = Olyutil.readInputFile(uasFile);
		//Olyutil.printStrArray(uasArr);
		jsonArr = parseData(uasArr);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
	 
	 
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
