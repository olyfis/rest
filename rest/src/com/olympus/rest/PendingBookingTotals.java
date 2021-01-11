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
	 
	@WebServlet("/pendbook")
	public class PendingBookingTotals extends HttpServlet {
		private final Logger logger = Logger.getLogger(PendingBookingTotals.class.getName()); // define logger
		static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";		
		
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
		public JsonArray  parseData(ArrayList<String> strArr, ArrayList<String> headerArr) throws IOException  {
			
			JsonArray jsonArr = new JsonArray();
			//ArrayList<String> headerArr = new ArrayList<String>();
			//String headerFile = "C:\\Java_Dev\\props\\headers\\age_rest_hdr.txt";
			String s = "";
			int i  = 0;
			int sz = 0;
			int pendBookingCount = 0;
			double sumEquipCosts = 0.00;
			String newItem = "";
			Date date = new java.util.Date();
			//System.out.println("***^^^*** Date=" + date);
			String dateUAS = formatDate("MM/dd/yyyy");
			//headerArr = Olyutil.readInputFile(headerFile);
			//Olyutil.printStrArray(headerArr);
			
			JsonObject obj = new JsonObject();
			int j = 0;
			for (String str : strArr) { // iterating ArrayList
				//System.out.println("**** Str=" + str);
				
				String[] items = str.split(";");
				sz = items.length;
				if (sz > 0) {
					if (items[0].equals("Contract ID")) {
						continue;
					}
					
					if (items[20].equals("Y")) {
						System.out.println("**** Str=" + str);
						j++;
						sumEquipCosts += Olyutil.strToDouble(items[3]);
						pendBookingCount += 1;
						//System.out.println("**** Count=" + pendBookingCount   + "-- EC=" + items[3] + "-- PB=" + items[20] + "--");
						
					}
				}
				//System.out.println("*********************************** ");
				
				i++;
			}
			System.out.println("**** Booked:" + j);
			obj.addProperty("date", formatDate("M/d/yyyy"));
			
		
			obj.addProperty("sumEquipCosts", sumEquipCosts);
			obj.addProperty("pendBookingCount", pendBookingCount);
			jsonArr.add(obj);
			
			return(jsonArr);
			
		}
		/****************************************************************************************************************************************************/
		@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JsonArray jsonArr = new JsonArray();

		PrintWriter out = response.getWriter();
		String dispatchJSP = null;

		String sep = null;
		ArrayList<String> strArr = new ArrayList<String>();

		sep = ";";

		String logFileName = "pendingBookingTotals.log";
		String directoryName = "D:/Kettle/logfiles/pendingBookingTotals";
		Handler fileHandler = OlyLog.setAppendLog(directoryName, logFileName, logger);
		String dateFmt = "";
		// System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
		String headerFile = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR.txt";
		String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V1.sql";
		String Jsonfile = "C:\\Java_Dev\\JSON\\pendingBookingTotals.json";

		// String ageFile = "C:\\Java_Dev\\props\\uastotals\\ageDetailReport.csv";

		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFile);
		//Olyutil.printStrArray(headerArr);
		// ageArr = Olyutil.readInputFile(ageFile);

		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();

		// Olyutil.displayJsonArray(jsonArr);

		strArr = DButil.getDbData(propFile, sqlFile, sep);
		// Olyutil.printStrArray(strArr);

		 jsonArr = parseData(strArr, headerArr);
		// jArr = DButil.buildJSON(strArr, headerArr);

		  Olyutil.jsonWriterResponse(jsonArr, out);
		  Olyutil.jsonWriter(jsonArr, Jsonfile);
		// End Display JSON

		dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		logger.info(dateFmt + ": " + "------------------ Write JSON to: " + Jsonfile);
		fileHandler.flush();
		fileHandler.close();
		// request.getRequestDispatcher(dispatchJSP).forward(request, response);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");
	}
		
		
		/****************************************************************************************************************************************************/

		
		
		
	}
