package com.olympus.rest;


	import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
	import java.io.PrintWriter;
import java.security.KeyStore.Entry;
import java.text.DecimalFormat;
	import java.text.SimpleDateFormat;
	import java.util.ArrayList;
	import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
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
	 
	@WebServlet("/bookingtotals2")
	public class GetBookingTotals extends HttpServlet {
		private final Logger logger = Logger.getLogger(GetBookingTotals.class.getName()); // define logger
		static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";	
		
		static String hdrFile = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR_V2.txt";
		
		 
		
		static String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V3.sql";
		 
		static String Jsonfile = "C:\\Java_Dev\\JSON\\getbookingdata.json";

		
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
		public JsonArray  parseData(ArrayList<String> strArr, ArrayList<String> headerArr, HashMap<Integer, SapObj> hMap) throws IOException  {
			int keyIdx = 0;
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
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			String key = "";
			//displayHashMap( hMap);
			
			 
			//System.out.println("HashMap after sorting by keys in ascending order "); 
			//for(java.util.Map.Entry<Integer, SapObj> mapping : mappings){ 
				//System.out.println("***!!!*** " + mapping.getKey() + " ==> " + mapping.getValue().getCreditApp()   + "-- EC=" + mapping.getValue().getEquipCost() + "--"); 
			//}
			
			
			for (String str : strArr) { // iterating ArrayList
				//System.out.println("**** Str=" + str);
				JsonObject obj = new JsonObject();
				String[] items = str.split("~");
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
				System.out.println("**** EC=" + items[3] + "--");
				System.out.println("**** Push=" + items[12] + "--");
				System.out.println("**** Date=" + items[27] + "--");
				System.out.println("**** Booked=" + items[28] + "--");
				*/
				if (! Olyutil.isNullStr(items[27])) {
					String dateBooked = Olyutil.formatDate(items[27],  "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd");
					if (! Olyutil.isNullStr(items[28])) {
						if (items[28].equals("Tricia Durham")) {
							System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked + "--");
						} else if (items[28].equals("Robert Patten")) {
							System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked + "--");
						} else if (items[28].equals("Mumtaz Manji")) {
							System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked + "--");
						}
					}
					
				}
				/*	 
				for (int k = 0; k < sz; k++) {
					
					
					
					
					if (k == 0) {
						//System.out.println("Line=" + i + " -- k=" + k + "/" + (sz - 1) + "-- HDR=" + headerArr.get(k) + "=" + items[k] + "--");

					}
					if (k == 3  || k == 22) {
						obj.addProperty(headerArr.get(k),Olyutil.strToDouble(items[k]));
					} else if (k == 40 ) {
						if (Olyutil.isNullStr(items[k])) {
							newItem = "";
							obj.addProperty(headerArr.get(k), newItem);
						} else {
							obj.addProperty(headerArr.get(k),Olyutil.strToInt(items[k]));
						}
						
					} else if ((k >= 10  && k <= 14) || (k == 26) || (k == 27) ) {
						
						if (Olyutil.isNullStr(items[k])) {
							obj.addProperty(headerArr.get(k),items[k]); 
						} else {
							//System.out.println("Line=" + i + " -- k=" + k + "/" + (sz - 1) + "-- Item=" + items[k] + "--");
							obj.addProperty(headerArr.get(k),   Olyutil.formatDate(items[k],  "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd"));
						
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
			*/
			
			
			//System.out.println("*********************************** ID=" + items[0] + "--");
				
				keyIdx = Olyutil.strToInt(items[0]);
				
				
				
				
				
				jsonArr.add(obj);
				i++;
			}
			//System.out.println("** jArr Size=" + jsonArr.size() + "--");
			
			return(jsonArr);
			
		}
		
		/****************************************************************************************************************************************************/
		
		
		/****************************************************************************************************************************************************/
	
		public static boolean displayHashMap2(HashMap<Integer, SapObj> hashMap) {
			boolean status = true;
		/*	
			Set<String> keys = hashMap.keySet();  //get all keys
			for(String key: keys) {
	
			  System.out.println("**----** Key=" + key + "-- Value=" + hashMap.get(key).getCreditApp() + "--");
			  System.out.println("**----** Key=" + key + "-- Value=" + hashMap.get(key).getEquipCost() + "--");
			  
			}
		*/	
			TreeMap<Integer, SapObj> sorted = new TreeMap<>(hashMap); 
			Set<java.util.Map.Entry<Integer,SapObj>> mappings = sorted.entrySet(); 
			System.out.println("HashMap after sorting by keys in ascending order "); 
			for(java.util.Map.Entry<Integer, SapObj> mapping : mappings){ 
				System.out.println("***!*** " + mapping.getKey() + " ==> " + mapping.getValue().getCreditApp()   + "-- EC=" + mapping.getValue().getEquipCost() + "--"); 
				}
		
			
			return(status);
			
		}
		/***********************************************************************************************************************************/
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
		
		public static void loadObjData(HashMap<String, BookingHist> hashMap, String bKey, String dateBooked  ) {

		HashMap<String, Integer> bkMap = null;
		BookingHist bkHist = new BookingHist();

		// System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked +  "--");
	 
		if (hashMap.get(bKey) == null) {
			// System.out.println("** Map key not set.");
			bkHist.setBookedBy(bKey);
			bkHist.gethMap().put(dateBooked, 1);
			// hashMap.get(bKey).sethMap(bkHist);
			hashMap.put(bKey, bkHist);

		} else {
			// System.out.println("** Key Found");
			bkMap = hashMap.get(bKey).gethMap();
			// bkHist.setBookedBy(bKey);
			if (bkMap.get(dateBooked) == null) {
				// System.out.println("** Date NOT in hash.");
				bkHist.gethMap().put(dateBooked, 1);
			} else {
				int count = bkMap.get(dateBooked);
				bkMap.put(dateBooked, count + 1);
				// System.out.println("**** Count=" + count + "-- Date=" + dateBooked + "--");
			}
		}
		 
		
	}
		
		
		/****************************************************************************************************************************************************/
		public HashMap<String, BookingHist>  getBookingData(ArrayList<String> strArr) throws IOException  {
			HashMap<String, Integer> bkMapT = null;
			HashMap<String, Integer> bkMapR = null;
			HashMap<String, Integer> bkMapM = null;
			
			BookingHist bkHistT = new BookingHist();
			BookingHist bkHistR = new BookingHist();
			BookingHist bkHistM = new BookingHist();
			
			HashMap<String, BookingHist> hashMap = new HashMap<String, BookingHist>();
			
			String bKey = "";
			String dKey = "";
			for (String str : strArr) { // iterating ArrayList
				//System.out.println("**** Str=" + str);
				//JsonObject obj = new JsonObject();
				String[] items = str.split("~");
				
				if (! Olyutil.isNullStr(items[27])) {
					String dateBooked = Olyutil.formatDate(items[27],  "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd");
					
					if (dateBooked.equals("2020-09-18")) {		
						System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked + "--");
						 
					}
					
					if (! Olyutil.isNullStr(items[28])) {
						if (items[28].equals("Tricia Durham")) {
							bKey = "trish";
							//loadObjData(hashMap,bKey,dateBooked);
							//System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked + "--");
						 
							if (hashMap.get(bKey) == null) {
								//System.out.println("** Map key not set.");
								bkHistT.setBookedBy(bKey);
								bkHistT.gethMap().put(dateBooked, 1);
								//hashMap.get(bKey).sethMap(bkHistT);
								hashMap.put(bKey, bkHistT);
					
							} else {
								//System.out.println("** Key Found");
								bkMapT = hashMap.get(bKey).gethMap();
								//bkHist.setBookedBy(bKey);
								if (bkMapT.get(dateBooked) == null) {
									//System.out.println("** Date NOT in hash.");
									bkHistT.gethMap().put(dateBooked, 1);
								} else {
									int tcount = bkMapT.get(dateBooked);
									bkMapT.put(dateBooked, tcount + 1);	
									//System.out.println("**** Count=" + count + "-- Date=" + dateBooked + "--");
								} 
							}
							 
						} else if (items[28].equals("Robert Patten")) {
							bKey = "rob";
						 
							if (hashMap.get(bKey) == null) {
								//System.out.println("** Map key not set -- " + bKey);
								bkHistR.setBookedBy(bKey);
								bkHistR.gethMap().put(dateBooked, 1);
								//hashMap.get(bKey).sethMap(bkHistR);
								hashMap.put(bKey, bkHistR);
					
							} else {
								//System.out.println("** Key Found");
								bkMapR = hashMap.get(bKey).gethMap();
								//bkHist.setBookedBy(bKey);
								if (bkMapR.get(dateBooked) == null) {
									//System.out.println("** Date NOT in hash.");
									bkHistR.gethMap().put(dateBooked, 1);
								} else {
									int rcount = bkMapR.get(dateBooked);
									bkMapR.put(dateBooked, rcount + 1);	
									//System.out.println("**** Count=" + count + "-- Date=" + dateBooked + "--");
								} 
							}
							
							 
							
							
							//System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked + "--");
						} else if (items[28].equals("Mumtaz Manji")) {
							bKey = "mumtaz";
							//System.out.println("**** Booked=" + items[28] + "-- Date=" + dateBooked + "--");
							if (hashMap.get(bKey) == null) {
								//System.out.println("** Map key not set -- " + bKey);
								bkHistM.setBookedBy(bKey);
								bkHistM.gethMap().put(dateBooked, 1);
								 
								hashMap.put(bKey, bkHistM);
					
							} else {
								//System.out.println("** Key Found");
								bkMapM = hashMap.get(bKey).gethMap();
								//bkHist.setBookedBy(bKey);
								if (bkMapM.get(dateBooked) == null) {
									//System.out.println("** Date NOT in hash.");
									bkHistM.gethMap().put(dateBooked, 1);
								} else {
									int mcount = bkMapM.get(dateBooked);
									bkMapM.put(dateBooked, mcount + 1);	
									//System.out.println("**** Count=" + count + "-- Date=" + dateBooked + "--");
								} 
							}
						}
					}
					
				} // End If
				
				bKey = "";
				dKey = "";
				
			} // End For
			
			return(hashMap);
		}

		/****************************************************************************************************************************************************/
		public static void displayHashMap(HashMap<String, Integer> hashMap, String booker) {
			
			TreeMap<String, Integer> sorted = new TreeMap<>(hashMap); 
			Set<java.util.Map.Entry<String, Integer>> mappings = sorted.entrySet(); 
			for(java.util.Map.Entry<String, Integer> mapping : mappings){ 
				System.out.println("***!*** Booker=" +  booker + "-- Date   " + mapping.getKey() + " ==> " + mapping.getValue() + "--"); 
				
			}
			
		}
		/****************************************************************************************************************************************************/
		public static JsonObject createJsonObj(HashMap<String, Integer> hashMap, String booker) {

			JsonObject obj = new JsonObject();
			
			obj.addProperty("User_Booked", booker);
			TreeMap<String, Integer> sorted = new TreeMap<>(hashMap); 
			Set<java.util.Map.Entry<String, Integer>> mappings = sorted.entrySet(); 
			for(java.util.Map.Entry<String, Integer> mapping : mappings){ 
				//System.out.println("***!*** Booker=" +  booker + "-- Date   " + mapping.getKey() + " ==> " + mapping.getValue() + "--"); 
				obj.addProperty("Date_Booked", mapping.getKey());
				obj.addProperty("Number_Booked", mapping.getValue());
			}
			
		
			return(obj);
		}
		
				
		/****************************************************************************************************************************************************/

		@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			HashMap<String, BookingHist> hMap = new HashMap<String, BookingHist>();
			HashMap<String, Integer> bookMapTrish = new HashMap<String, Integer>();
			HashMap<String, Integer> bookMapRob = new HashMap<String, Integer>();
			HashMap<String, Integer> bookMapMum = new HashMap<String, Integer>();
			
			
		JsonArray jsonArr = new JsonArray();
		

		HashMap<Integer, SapObj> rtnMap = new HashMap<Integer, SapObj>();

		PrintWriter out = response.getWriter();
		String dispatchJSP = null;

		String sep = null;
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> strArr2 = new ArrayList<String>();
		sep = "~";

		String logFileName = "bookingdata.log";
		String directoryName = "D:/Kettle/logfiles/bookingdata";
		Handler fileHandler = OlyLog.setAppendLog(directoryName, logFileName, logger);
		String dateFmt = "";
		// System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
		
		/*
		 String outputFileName1 = "C:\\Java_Dev\\data\\ss.txt";
		 String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V1.sql";
		*/
		
		String outputFileName1 = "C:\\Java_Dev\\dataLog\\bookings.txt";
		 
		
		// String headerFile = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR_V2.txt";
		 //String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V2.sql";
		
		
		 

		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(hdrFile);
		 
		//Olyutil.printStrArray(headerArr);
		// ageArr = Olyutil.readInputFile(ageFile);

		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();

		// Olyutil.displayJsonArray(jsonArr);
		strArr = DButil.getDbData(propFile, sqlFile, sep);
		hMap = getBookingData(strArr);
		
		
		
		
		BookingHist bkHist = new BookingHist();
		bkHist = hMap.get("trish");	
		bookMapTrish = bkHist.gethMap();
		//displayHashMap(bookMapTrish, "trish");
		JsonObject trishObj = new JsonObject();
		trishObj = createJsonObj(bookMapTrish, "trish");
		jsonArr.add(trishObj);
		
		BookingHist bkHistRob = new BookingHist();
		bkHistRob = hMap.get("rob");	
		bookMapRob = bkHistRob.gethMap();
		//displayHashMap(bookMapRob, "rob");
		
		JsonObject robObj = new JsonObject();
		robObj = createJsonObj(bookMapRob, "rob");
		jsonArr.add(robObj);
		
		BookingHist bkHistMum = new BookingHist();
		bkHistMum = hMap.get("mumtaz");	
		bookMapMum = bkHistMum.gethMap();
		//displayHashMap(bookMapMum, "mumtaz");
		JsonObject mumObj = new JsonObject();
		mumObj = createJsonObj(bookMapMum, "mumtaz");
		jsonArr.add(mumObj);
		
		 
		//System.out.println("**** bookeBy=" +   hMap.get("trish").getBookedBy() + "--");
		
		//Olyutil.printStrArray(strArr);
		//writeToFile(strArr, outputFileName1, sep);
		//writeToFile(strArr2, outputFileName2, sep);
		//displayData(strArr, headerArr, sep);
		 
		//displayHashMap( rtnMap);
		 
		//displayData(strArr,  headerArr, sep );
		 //jsonArr = parseData(strArr, headerArr, rtnMap);
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
		
		//System.out.println("** arr1Sz=" + strArr.size() + "-- SapArrSZ=" + strArr2.size() + "--");
		
		
		System.out.println("**** END");
	}
		
		
		/****************************************************************************************************************************************************/

		
		
		
	}

