package com.olympus.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.olympus.olyutil.Olyutil;

@WebServlet("/tpsold")
public class ThirdPartyShipments extends HttpServlet {

	private final Logger logger = Logger.getLogger(SalesSupportBooking.class.getName()); // define logger
	private static String dbName = "Commencement.accdb";
	//private static String dbPath = "//C://Java_Dev//props//accessDB//";
	private static String dbPath = "//C:\\Java_Dev\\props\\accessDB\\"
			+ "";
	 
	private static String dbPathName = dbPath + dbName;
	
	/*****************************************************************************************************************************************************/
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
	public static boolean displayHashMap(HashMap<String, Double> hashMap) {
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
	/****************************************************************************************************************************************************/
	public static JsonArray HashMapToJSON(HashMap<String, Double> hashMap) {
		JsonArray jsonArr = new JsonArray();
		
		TreeMap<String, Double> sorted = new TreeMap<>(hashMap); 
		Set<java.util.Map.Entry<String, Double>> mappings = sorted.entrySet(); 
		//System.out.println("HashMap after sorting by keys in ascending order "); 
		for(java.util.Map.Entry<String, Double> mapping : mappings){ 
			JsonObject obj = new JsonObject();
			//System.out.println("***!*** " + mapping.getKey() + " ==> " + mapping.getValue()   + "--");
			
			obj.addProperty("ContractID", mapping.getKey());	
			
			
			String amt = decimalfmt(mapping.getValue(),  "###,##0.00");
			//System.out.println("*****AMT=" + amt + "--");
			double amount = Olyutil.strToDouble(amt);
			mapping.setValue( amount);
			obj.addProperty("Third_Party_Shipment_Amount", mapping.getValue());	
			//System.out.println("***!After*** " + mapping.getKey() + " ==> " + mapping.getValue()   + "--");
			
			//obj.addProperty(mapping.getKey(), mapping.getValue());	
			jsonArr.add(obj);
		}
		return(jsonArr);
		
	}
	
	/****************************************************************************************************************************************************/

	
	public static HashMap<String, Double> getAccessDbData() throws ClassNotFoundException {
		HashMap<String, Double> map = new HashMap<String, Double>();
		JsonArray jsonArr = new JsonArray();
		double amount = 0.00;
		String poNumberMod = "";
		String part1 = "";
		String part2 = "";
		String part3 = "";
	
		String sqlQuery = "SELECT * FROM Searchresults";
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
						} else {
							String[] parts = poNumber.split("-");
							  part2 = parts[0];
							  part3 = parts[1];
							//System.out.println("**-15<-** P2=" + part2 + "-- P3=" + part3 + "--");
						}						
						//System.out.println("**--** P1=" + part1 + "-- P2=" + part2 + "-- P3=" + part3 + "-- 		 PO=" + poNumber + "--");					
					} else {						
						//System.out.println("**poError: Less than 15 chars.");
						continue;
					}				
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return(map);
	}

	/*****************************************************************************************************************************************************/
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

	
	/****************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonArray jArr = new JsonArray();
		PrintWriter out = response.getWriter();
		HashMap<String, Double> TotalsMap = new HashMap<String, Double>();
		try {
			TotalsMap = getAccessDbData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jArr = HashMapToJSON(TotalsMap);
		//displayHashMap(TotalsMap);
		Olyutil.jsonWriterResponse(jArr, out);
		
	}
	
}
