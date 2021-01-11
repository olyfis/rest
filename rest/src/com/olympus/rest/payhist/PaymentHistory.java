package com.olympus.rest.payhist;

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
	import com.olympus.rest.DButil;
	 
	@WebServlet("/payhist")
	public class PaymentHistory extends HttpServlet {
		private final Logger logger = Logger.getLogger(PaymentHistory.class.getName()); // define logger
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
			String newItem = "";
			Date date = new java.util.Date();
			//System.out.println("***^^^*** Date=" + date);
			String dateUAS = formatDate("MM/dd/yyyy");
			//headerArr = Olyutil.readInputFile(headerFile);
			//Olyutil.printStrArray(headerArr);
			
			
			for (String str : strArr) { // iterating ArrayList
				//System.out.println("**** Str=" + str);
				JsonObject obj = new JsonObject();
				String[] items = str.split(";");
				if (items[0].equals("Contract ID")) {
					continue;
				}
					/*
				 if ( i > 2) {
					 break;
				 }
				 */
				sz = items.length;
				/*
				if (sz >= 36) { 
					// System.out.println("Line=" + i + " -- SZ=" + sz );
					// System.out.println(str);
					for (int k = 0; k < 35; k++) {
						System.out.println("Line=" + i + " -- k=" + k + "/" + (sz - 1) + "-- HDR=" + headerArr.get(k) + "=" + items[k] + "--");

					}

				}
				*/
				for (int k = 0; k < 31; k++) {
					//System.out.println("Line=" + i + " -- k=" + k + "/" + (sz - 1) + "-- HDR=" + headerArr.get(k) + "=" + items[k] + "--");

				 
					
					if (k == 3  || k == 22) {
						obj.addProperty(headerArr.get(k),Olyutil.strToDouble(items[k]));
					} else if (k == 40 ) {
						if (Olyutil.isNullStr(items[k])) {
							newItem = "";
							obj.addProperty(headerArr.get(k), newItem);
						} else {
							obj.addProperty(headerArr.get(k),Olyutil.strToInt(items[k]));
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
				//System.out.println("*********************************** ");
				
				jsonArr.add(obj);
				i++;
			}
			
			
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

		String logFileName = "payhistory.log";
		String directoryName = "D:/Kettle/logfiles/payhistory";
		Handler fileHandler = OlyLog.setAppendLog(directoryName, logFileName, logger);
		String dateFmt = "";
		// System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
		String headerFile = "C:\\Java_Dev\\props\\headers\\payhistory_HDR.txt";
		String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\payhistory.sql";
		String Jsonfile = "C:\\Java_Dev\\JSON\\payhistory.json";

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
