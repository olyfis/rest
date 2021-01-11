package com.olympus.rest.booking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
import com.olympus.rest.DButil;
//import com.olympus.rest.SalesSupportBooking;

@WebServlet("/getdocstat")
public class GetDocStatus extends HttpServlet {
	private final Logger logger = Logger.getLogger(GetDocStatus.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	//static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";		
	
	static String hdrFile = "C:\\Java_Dev\\props\\headers\\Qlik_Rest\\Qlik_Docs_Sent_Out_Hdr.txt";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\Qlik_Docs_Sent_Out_V1.sql";
	static String Jsonfile = "C:\\Java_Dev\\JSON\\GetDocStatus.json";
	
	
	
	/****************************************************************************************************************************************************/

	
	
	/****************************************************************************************************************************************************/
	/****************************************************************************************************************************************************/

	public static void displayData(ArrayList<String> strArr, ArrayList<String> hdrArr, String sep) {
		 
		
		int k = 0;
		for (String str : strArr) { // iterating ArrayList
	 			
				//System.out.println("**** Str=" + str);
				String[] items = str.split(sep);
				int sz = items.length;
			
				for (int i = 0; i < sz; i++) {
					
					System.out.println("**** SZ=" +  items.length + "-- Row="  + k + "--  i=" + i + "-- " + hdrArr.get(i)  + "=" + items[i] );
			
					//System.out.println(k + ";" + i + ";" + hdrArr.get(i)  + ";" + items[i]);
				}
				k++;
		}
		
	}
	/****************************************************************************************************************************************************/

	/****************************************************************************************************************************************************/
	/****************************************************************************************************************************************************/
	// String dateFmt = formatDate("yyyy-MM-dd");
	// String dateFmt = formatDate("yyyy-MM-dd hh:mm:ss.SSS");

	// String dateFmt = formatDate("MM/dd/yyyy");

	public static String formatDate(String format) throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return (dateFormat.format(date));
	}
	/****************************************************************************************************************************************************/
	
	/****************************************************************************************************************************************************/
	public static void writeToFile(ArrayList<String> strArr, String fileName, String sep) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    String[] strSplitArr = null;
	    //System.out.println("** Write files:" + fileName );
	    for (String str : strArr) { // iterating ArrayList
	    	strSplitArr = Olyutil.splitStr(str, sep);
	    	int i = 0;
	    	int sz = strSplitArr.length;
	    	for (String token : strSplitArr) {
	    		//writer.write(token.replaceAll("null", ""));
	    		if (i < sz -1) {
	    			writer.write(token + sep);
	    		} else {
	    			writer.write(token);
	    		}
	    		
	    		i++;
	    	}
	    	writer.newLine();
	    }
	    writer.close(); 
	   
	}
	/****************************************************************************************************************************************************/
	public JsonArray  parseData(ArrayList<String> strArr, ArrayList<String> headerArr, String sep) throws IOException  {
		int keyIdx = 0;
		JsonArray jsonArr = new JsonArray();
		//ArrayList<String> headerArr = new ArrayList<String>();
		
		String s = "";
		int i  = 0;
		int sz = 0;
		String newItem = "";
		Date date = new java.util.Date();
		//System.out.println("***^^^*** Date=" + date);
		String dateUAS = formatDate("MM/dd/yyyy");
		//headerArr = Olyutil.readInputFile(headerFile);
		//Olyutil.printStrArray(headerArr);
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		String key = "";
		//displayHashMap( hMap);
		/*
		TreeMap<Integer, SapObj> sorted = new TreeMap<>(hMap); 
		Set<java.util.Map.Entry<Integer,SapObj>> mappings = sorted.entrySet(); 
		//System.out.println("HashMap after sorting by keys in ascending order "); 
		//for(java.util.Map.Entry<Integer, SapObj> mapping : mappings){ 
			//System.out.println("***!!!*** " + mapping.getKey() + " ==> " + mapping.getValue().getCreditApp()   + "-- EC=" + mapping.getValue().getEquipCost() + "--"); 
		//}
		
		*/
		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);
			JsonObject obj = new JsonObject();
			String[] items = str.split(sep);
			
			sz = items.length;

			for (int k = 0; k < sz; k++) {
				
				if (k == 1) {
					
					if (Olyutil.isNullStr(items[k])) {
						obj.addProperty("Date",items[k]); 
					} else {
						//System.out.println("Line=" + i + " -- k=" + k + "/" + (sz - 1) + "-- Item=" + items[k] + "--");
						obj.addProperty("Date",   Olyutil.formatDate(items[k],  "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd"));
					
					}
	
				} else {
					if (Olyutil.isNullStr(items[k])) {
						newItem = "";
					} else {
						newItem = items[k];
					}
					obj.addProperty(headerArr.get(k), newItem);
				}
			}
		//System.out.println("*********************************** ID=" + items[0] + "--");
			jsonArr.add(obj);
			i++;
		}
		//System.out.println("** jArr Size=" + jsonArr.size() + "--");
		
		return(jsonArr);
		
	}
	/****************************************************************************************************************************************************/

	
	@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	JsonArray jsonArr = new JsonArray();
	HashMap<Integer, String> rtnMap = new HashMap<Integer, String>();

	PrintWriter out = response.getWriter();
	String dispatchJSP = null;

	String sep = ";";
	ArrayList<String> strArr = new ArrayList<String>();
	//Olyutil.printStrArray(strArr);
	

		String logFileName = "getDocStatus.log";
		String directoryName = "D:/Kettle/logfiles/getDocStatus";
		Handler fileHandler = OlyLog.setAppendLog(directoryName, logFileName, logger);
		String dateFmt = "";
		// System.out.println("%%%%%%%%%%%%%%%%%%%% in GetDocStatus: doGet() ");
		ArrayList<String> headerArr = new ArrayList<String>();

		headerArr = Olyutil.readInputFile(hdrFile);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();

		// Olyutil.displayJsonArray(jsonArr);
		strArr = DButil.getDbData(propFile, sqlFile, sep);
		// Olyutil.printStrArray(strArr);
		// displayData(strArr, headerArr, sep);
		jsonArr = parseData(strArr, headerArr, sep);

		Olyutil.jsonWriterResponse(jsonArr, out);
		Olyutil.jsonWriter(jsonArr, Jsonfile);
		// End Display JSON

		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		logger.info(dateFmt + ": " + "------------------ Write JSON to: " + Jsonfile);
		fileHandler.flush();
		fileHandler.close();
		// request.getRequestDispatcher(dispatchJSP).forward(request, response);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");

	} // End doGet()
} // End Class
