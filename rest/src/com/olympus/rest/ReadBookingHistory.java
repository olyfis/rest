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
	 
	@WebServlet("/pbhist")
	public class ReadBookingHistory extends HttpServlet {
		private final Logger logger = Logger.getLogger(ReadBookingHistory.class.getName()); // define logger
		private final String label = "pbhist";
		
		/****************************************************************************************************************************************************/
		/****************************************************************************************************************************************************/
		public double parseUasData(ArrayList<String> strArr )  {
			String s = "";
			double uasTotal = 0.0;
			double amount = 0.0;
			
			for (String str : strArr) { // iterating ArrayList
				 
				String[] items = str.split(",");
				if (items[0].equals("Date")) {
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
		/****************************************************************************************************************************************************/
		public JsonArray parseData(ArrayList<String> strArr) throws IOException  {
			JsonArray jsonArr = new JsonArray();
			
			 
			Date date = new java.util.Date();
			//System.out.println("***^^^*** Date=" + date);
			String dateUAS = formatDate("M/d/yyyy");
			DecimalFormat df = new DecimalFormat("###,##0.00");
			//DecimalFormat df = new DecimalFormat("#,##0");
	 
			for (String str : strArr) { // iterating ArrayList
				JsonObject obj = new JsonObject();
				//System.out.println("**** Str=" + str);
			 
				String[] items = str.split(",");
				if (items[0].equals("Date")) {
					continue;
				} else {
					obj.addProperty("date", items[0]);
				}	
				obj.addProperty("pending_booking", Olyutil.strToInt(items[1]));
				obj.addProperty("dollars_pending", Olyutil.strToDouble(items[2]));
				
				jsonArr.add(obj);	
			} // End For
			
			return(jsonArr);
			
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
			ArrayList<String> dataArr = new ArrayList<String>();
			//ArrayList<String> uasArr = new ArrayList<String>();
			sep = ",";

			String logFileName = label + ".log";
			String directoryName = "D:/Kettle/logfiles/" + label;
			Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
			String dateFmt = "";
			//System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
			//String headerFile = "C:\\Java_Dev\\props\\headers\\uasTotalsHdr.txt";
			String Jsonfile = "C:\\Java_Dev\\JSON\\" + label + ".json";
			String weekTotalFile = "C:\\Java_Dev\\props\\uastotals\\Pending_Booking_History.csv";
			 
			//ArrayList<String> headerArr = new ArrayList<String>();
			//headerArr = Olyutil.readInputFile(headerFile);
			dataArr = Olyutil.readInputFile(weekTotalFile);
			//Olyutil.printStrArray(dataArr);
			 
			Date date = Olyutil.getCurrentDate();
			String dateStamp = date.toString();
			 
			jsonArr = parseData(dataArr); 
			
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
