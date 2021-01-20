package com.olympus.rest;
 


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyStore.Entry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 
@WebServlet("/ssbook")
public class SSbook2 extends HttpServlet {
	private final Logger logger = Logger.getLogger(SalesSupportBooking.class.getName()); // define logger
	static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";	
	
	static String hdrFile = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR_V2.txt";
	
	static String hdrFile2 = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR_SAP.txt";
	
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V4.sql";
	static String sqlFile2 = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_SAP_V3.sql";
	static String Jsonfile = "C:\\Java_Dev\\JSON\\salessupport.json";

	private static String dbName = "Commencement.accdb";
	//private static String dbPath = "//C://Java_Dev//props//accessDB//";
	private static String dbPath = "//C:\\Java_Dev\\props\\accessDB\\"+ "";
	 
	private static String dbPathName = dbPath + dbName;
	
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
			public static boolean dispMap(HashMap<String, Double> hashMap) {
				boolean status = true;
		
				TreeMap<String, Double> sorted = new TreeMap<>(hashMap); 
				Set<java.util.Map.Entry<String, Double>> mappings = sorted.entrySet(); 
				//System.out.println("HashMap after sorting by keys in ascending order "); 
				for(java.util.Map.Entry<String, Double> mapping : mappings){ 
					System.out.println("***!*** Key=" + mapping.getKey()  + "--");
					//System.out.println("***!*** " + mapping.getKey() + " ==> " + mapping.getValue(	)   + "--");
				}
			
				
				return(status);
				
			}
			
			
			/****************************************************************************************************************************************************/
			public static boolean displayHashMapAccessDB(HashMap<String, Double> hashMap) {
				boolean status = true;
			/*	
				Set<String> keys = hashMap.keySet();  //get all keys
				for(String key: keys) {

				  System.out.println("**----** Key=" + key + "-- Value=" + hashMap.get(key).getCreditApp() + "--");
				  System.out.println("**----** Key=" + key + "-- Value=" + hashMap.get(key).getEquipCost() + "--");
				  
				}
			*/	
				TreeMap<String, Double> sorted = new TreeMap<>(hashMap); 
				Set<java.util.Map.Entry<String, Double>> mappings = sorted.entrySet(); 
				//System.out.println("HashMap after sorting by keys in ascending order "); 
				for(java.util.Map.Entry<String, Double> mapping : mappings){ 
					/* if (mapping.getKey().equals("101 0017464-001")) {
						System.out.println("***!*** " + mapping.getKey() + " ==> " + mapping.getValue(	)   + "--");
					} */
					System.out.println("***!*** " + mapping.getKey() + " ==> " + mapping.getValue(	)   + "--");
				}
			
				
				return(status);
				
			}
	/*****************************************************************************************************************************************************/
	public JsonArray  parseData(ArrayList<String> strArr, ArrayList<String> headerArr, HashMap<Integer, SapObj> hMap) throws IOException  {
		HashMap<String, Double> TotalsMap = new HashMap<String, Double>();
		int keyIdx = 0;
		JsonArray jsonArr = new JsonArray();
		//ArrayList<String> headerArr = new ArrayList<String>();
		//String headerFile = "C:\\Java_Dev\\props\\headers\\age_rest_hdr.txt";
		String s = "";
		String contractID = "";
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
		
		TreeMap<Integer, SapObj> sorted = new TreeMap<>(hMap); 
		Set<java.util.Map.Entry<Integer,SapObj>> mappings = sorted.entrySet(); 
		//System.out.println("HashMap after sorting by keys in ascending order "); 
		//for(java.util.Map.Entry<Integer, SapObj> mapping : mappings){ 
			//System.out.println("***!!!*** " + mapping.getKey() + " ==> " + mapping.getValue().getCreditApp()   + "-- EC=" + mapping.getValue().getEquipCost() + "--"); 
		//}
	/*-------------------------------------------------------------------------------------------------------------------------------------------*/
		// Added code for Third Party Shipments-- 2021-02-08
		try {
			TotalsMap = getAccessDbDataNew();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("***MapSZ=" + TotalsMap.size());
		
		//displayHashMapAccessDB(TotalsMap);
	/*-------------------------------------------------------------------------------------------------------------------------------------------*/

		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);
			JsonObject obj = new JsonObject();
			String[] items = str.split("~");
			
			 
			
			if (items[0].equals("Contract ID")) {
				continue;
			}
			contractID = items[1];
			//System.out.println("**** ID=" + items[1] + "--");
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
						String fDate = Olyutil.formatDate(items[k],  "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd");
						
						//System.out.println("Line=" + i + " -- k=" + k + "/" + (sz - 1) + "-- Item=" + items[k] + "--");
						obj.addProperty(headerArr.get(k),   Olyutil.formatDate(items[k],  "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd"));
						 //if (fDate.equals("2020-09-30") && items[28].equals("Robert Patten")) {
							 //System.out.println("**** Str=" + str);
							 //System.out.println("Line=" + i + " -- k=" + k + "/" + (sz - 1) + "-- Item=" + items[k] + "--");
							 
							 //System.out.println("AppID=" + items[0] + "-- Date=" + fDate + "-- User_Booked=" + items[28]);
						 //}
						
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
			
			keyIdx = Olyutil.strToInt(items[0]);
			
			
			
		 
			
			if ( hMap.containsKey(keyIdx)) { 
					/*
					if (i >= 0 && i < 25) {
						//System.out.println("**--!!--** i=" + i + "-- Key=" + keyIdx);
					}
					*/
					 
					//System.out.println("**--BG--** i=" + i + "-- Key=" + keyIdx + "-- SZ=" +  hMap.size());
					
					//System.out.println("**--!!--** Key=" + keyIdx + "-- Value=" + hMap.get(keyIdx).getCreditApp() + "-- NumAssets=" + hMap.get(keyIdx).getNumAssets() + "--");
					//System.out.println("**--!!--** Key=" + keyIdx + "-- Value=" + hMap.get(keyIdx).getEquipCost() + "--");
					
					obj.addProperty("NumAssets", hMap.get(keyIdx).getNumAssets());
					obj.addProperty("NumQuotes", hMap.get(keyIdx).getNumQuotes());
					obj.addProperty("equipCostsSAP", hMap.get(keyIdx).getEquipCost());
					
					
			 } else {
				 obj.addProperty("NumAssets", 0);
					obj.addProperty("NumQuotes", 0);
					obj.addProperty("equipCostsSAP", 0.00);
			 }
			/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
			// Added Third Part Shipm3nt amounts 2021-01-08 
				double tpsAmount = 0.00;
				//System.out.println("***!!!!*** Check** ID=" + contractID  + "--");
				//System.out.println("** TotalsMapSZ=" + TotalsMap.size());
				//dispMap(TotalsMap);
				if ( TotalsMap.containsKey(contractID)) { 
					tpsAmount = TotalsMap.get(contractID);
					//System.out.println("**Found** ID=" + contractID + "-- Value=" + tpsAmount  + "--");
				}		
				obj.addProperty("ThirdPartyShipAmount", tpsAmount);
			/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/

			
			
			jsonArr.add(obj);
			i++;
		}
		//System.out.println("** jArr Size=" + jsonArr.size() + "--");
		
		return(jsonArr);
		
	}
	/****************************************************************************************************************************************************/
	public HashMap<Integer, SapObj>  parseSAPData(ArrayList<String> strArr, ArrayList<String> hdrArr) throws IOException  {
		HashMap<Integer, SapObj> map = new HashMap<Integer, SapObj>();
		
		int k = 0;
		int key = 0;
		for (String str : strArr) { // iterating ArrayList
			SapObj sapObj = new SapObj();
				//System.out.println("**** Str=" + str);
				String[] items = str.split(";");
				int sz = items.length;
				for (int i = 0; i < sz; i++) {
				//for (int i = 0; i <= size; i++) {
					/*
					if (k >= 0 && k < 147) {
						System.out.println("****!!!*** Row="  + k + "--  i=" + i + "-- " + hdrArr.get(i)  + "=" + items[i] + "-- SZ=" + sz);

					}
					 
						//System.out.println(k + ";" + i + ";" + hdrArr.get(i)  + ";" + items[i] );
					 */
			}
					//System.out.println("*** Insert:" + items[0] + "-- EC=" + items[3] + "--");
					key = Olyutil.strToInt(items[0]);
					sapObj.setCreditApp(key);
					sapObj.setNumAssets(Olyutil.strToInt(items[1]));
					sapObj.setNumQuotes(Olyutil.strToInt(items[2]));
					sapObj.setEquipCost(Olyutil.strToDouble(items[3]));
					map.put(key, sapObj);
				
			k++;
		}
		
	
		return(map);
	}
	/****************************************************************************************************************************************************/
	public static boolean displayHashMap(HashMap<Integer, SapObj> hashMap) {
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
	/****************************************************************************************************************************************************/
	// Call: String amt = decimalfmt(value,  "$###,##0.00");

	public  static String decimalfmt(double value, String fmt) {
		//System.out.println("**Val=" + value + "--");
		
		String rtn = "";
		DecimalFormat df = new DecimalFormat(fmt);
		rtn = df.format(value);	
		return(rtn);		
	}
	/****************************************************************************************************************************************************/
	/*****************************************************************************************************************************************************/
	
	
	/****************************************************************************************************************************************************/
	public static HashMap<String, Double> getAccessDbDataNew() throws ClassNotFoundException {
		HashMap<String, Double> map = new HashMap<String, Double>();
		JsonArray jsonArr = new JsonArray();
		double amount = 0.00;
		String poNumberMod = "";
		String poNumberOrig = "";
		String part1 = "";
		String part2 = "";
		String part3 = "";
		String newPO = "";
		String sqlQuery = "SELECT * FROM Searchresults";
		int recCount = 0;
		// String databaseURL =
		// "jdbc:ucanaccess://e://Java//JavaSE//MsAccess//Contacts.accdb";
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		String databaseURL = "jdbc:ucanaccess:" + dbPathName;
		// System.out.println("** URL=" + databaseURL);
		try (Connection connection = DriverManager.getConnection(databaseURL)) {

			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sqlQuery);

			while (result.next()) {
				JsonObject obj = new JsonObject();

				String poNumber = result.getString("PO Number");
				recCount++;
				poNumberOrig = poNumber;
				poNumber = poNumber.replace(".", "");
				if (poNumber.length() != 15) {
					if (poNumber.length() > 15) { // try to extract poNumber
						if (poNumber.startsWith("101") || poNumber.startsWith("102")) {
							String[] parts = poNumber.split("-");
							  part1 = parts[0];
							  part2 = parts[1];
							part2 = part2.replaceAll("\\s", "");
							String part3Tmp = parts[2];
							String[] p3 = part3Tmp.split(" ");
							  part3 =  p3[0];
							
							  if (part2.length() > 7) {
									part2 = part2.substring(1, 8);
									// System.out.println("**7** PO=" + part2 + "-- part2=" + part2 + "--");
								}
								if (part3.length() > 3) {
									
									//System.out.println("**3 Before** PO=" + poNumber + "-- part3=" + part3 + "--");
									part3 = part3.substring(1, 4);
									//System.out.println("**3** PO=" + poNumber + "-- part3=" + part3 + "--");
								}
								//System.out.println("**-15>-** P1=" + part1 + "-- P2=" + part2 + "-- P3=" + part3 + "--");
								
								newPO = part2 + "-" + part3;
						} else {
							String[] parts = poNumber.split("-");
							part2 = parts[0];
							part3 = parts[1];
							newPO = part2 + "-" + part3;
							  
							//System.out.println("**-15<-** P2=" + part2 + "-- P3=" + part3 + "--");
						}						
						//System.out.println("**--** P1=" + part1 + "-- P2=" + part2 + "-- P3=" + part3 + "-- 		 PO=" + poNumber + "--");					
					} else {						
						//System.out.println("**poError: Less than 15 chars.");
						continue;
						  
					}				
				} else { // poNumber == 15 chars
					//System.out.println("**!!!!!!!!!!!!!!!!!!!!!!!!!!! poNumber=" + poNumber + "--");
					poNumber = poNumber.replaceAll("\\s", "-");
					String[] parts = poNumber.split("-");
					part2 = parts[1];
					part3 = parts[2];
					newPO = part2 + "-" + part3;
					
				}
				
				//System.out.println("**!!!!!!!!!!!!!!!!!!!!!!!!!!!! NewPO=" + newPO + "--              PO=" + poNumber + "--");
				poNumber = newPO;
				//System.out.println("***** PO_NUMBER=" + poNumber + "--");
				double value = result.getFloat("Amount");
				//System.out.println("*****VALUE=" + value + "--");
				
				String amt = decimalfmt(value,  "###,##0.00");
				//System.out.println("*****AMT=" + amt + "--");
				amount = Olyutil.strToDouble(amt);
				//double amount = result.getFloat("Amount");
				//if (poNumber.equals("101 0017464-001")) {
					//System.out.println("*****PO" + poNumber + ", Amount:" + amount);
				//}
			 
			 
				
				//System.out.println("PO=" + poNumber + ", Amount:" + amount);
				
				
				if (map.containsKey(poNumber)) {
					double newAmount = 0.00;
					newAmount = map.get(poNumber) + amount;
					//if (poNumber.equals("101 0017464-001")) {
						//System.out.println("***** Updating PO" + poNumber + ", Amount:" + amount + ", NewAmount:" + newAmount );
					//}
					 //System.out.println("***** Updating PO-- " + poNumber + ", Amount:" + amount + ", NewAmount:" + newAmount );
					map.replace(poNumber, newAmount);
				} else { // key not in Hash
					 //System.out.println("***** Adding New PO=" + poNumber + "--  Amount:" + amount + "-- 		poOrig=" + poNumberOrig +"--");
					map.put(poNumber, amount);
				 
				}
				//System.out.println("****^^A^^^**** PO=" + poNumber + "-- NewPO=" + newPO  + "-- 		origPO=" + poNumberOrig + "-- P2=" + part2 + "-- P3=" + part3 +"--");
				
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		//System.out.println("***** Records read: " + recCount );
	
		return(map);
	}

	
	
	
	/****************************************************************************************************************************************************/

	@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

	JsonArray jsonArr = new JsonArray();
	HashMap<Integer, SapObj> rtnMap = new HashMap<Integer, SapObj>();

	PrintWriter out = response.getWriter();
	String dispatchJSP = null;

	String sep = null;
	ArrayList<String> strArr = new ArrayList<String>();
	ArrayList<String> strArr2 = new ArrayList<String>();
	sep = "~";

	String logFileName = "salessupport.log";
	String directoryName = "D:/Kettle/logfiles/salessupport";
	Handler fileHandler = OlyLog.setAppendLog(directoryName, logFileName, logger);
	String dateFmt = "";
	// System.out.println("%%%%%%%%%%%%%%%%%%%% in ContractEOT: doGet() ");
	
	/*
	 String outputFileName1 = "C:\\Java_Dev\\data\\ss.txt";
	 String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V1.sql";
	*/
	
	String outputFileName1 = "C:\\Java_Dev\\dataLog\\ss.txt";
	String outputFileName2 = "C:\\Java_Dev\\dataLog\\ssSap.txt";
	
	// String headerFile = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR_V2.txt";
	 //String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V2.sql";
	
	
	 

	ArrayList<String> headerArr = new ArrayList<String>();
	ArrayList<String> headerArr2 = new ArrayList<String>();
	headerArr = Olyutil.readInputFile(hdrFile);
	headerArr2 = Olyutil.readInputFile(hdrFile2);
	//Olyutil.printStrArray(headerArr);
	// ageArr = Olyutil.readInputFile(ageFile);

	Date date = Olyutil.getCurrentDate();
	String dateStamp = date.toString();

	// Olyutil.displayJsonArray(jsonArr);
	strArr = DButil.getDbData(propFile, sqlFile, sep);
	strArr2 = DButil.getDbData2(sqlFile2, "", "Asset", propFile, "" );
	//Olyutil.printStrArray(strArr);
	//writeToFile(strArr, outputFileName1, sep);
	//writeToFile(strArr2, outputFileName2, sep);
	//displayData(strArr, headerArr, sep);
	rtnMap = parseSAPData(strArr2, headerArr2);
	//displayHashMap( rtnMap);
	 
	//displayData(strArr2, 3, headerArr2 );
	 jsonArr = parseData(strArr, headerArr, rtnMap);
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
}
	
	
	/****************************************************************************************************************************************************/

	
	
	
}
