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

@WebServlet("/tps")
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
	public static HashMap<String, Double> getAccessDbData() throws ClassNotFoundException {
		HashMap<String, Double> map = new HashMap<String, Double>();
		JsonArray jsonArr = new JsonArray();
		double amount = 0.00;
		String poNumberMod = "";
		String newPart2 = "";
		String newPart3 = "";
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
				if (poNumber.length() > 15) {
					if (poNumber.startsWith("101") || poNumber.startsWith("102")) {

						// poNumber = poNumber.replaceAll("\\s", "");
						String first15Chars = "";
						first15Chars = poNumber.substring(0, 15);
						String[] parts1 = poNumber.split("/");
						// System.out.println("** PO=" + parts1[0] + "--");

						String[] parts = parts1[0].split("-");
						String part1 = parts[0];
						String part2 = parts[1];
						String part3a = parts[2];
						String[] parts2 = part3a.split(" ");

						String part3 = parts2[0];
						// System.out.println("** P2=" + part2 + "-- P3=" + part3 + "--");

						if (part2.length() > 7) {
							part2 = part2.substring(1, 7);
							// System.out.println("**7** PO=" + part2 + "-- part2=" + part2 + "--");
						}
						if (part3.length() > 3) {
							
							//System.out.println("**3 Before** PO=" + poNumber + "-- part3=" + part3 + "--");
							part3 = part3.substring(1, 4);
							 //System.out.println("**3** PO=" + poNumber + "-- part3=" + part3 + "--");
						}
						// System.out.println("** PO=" + poNumber + "-- newID=" + first15Chars + "--");
						//String newPO = part1 + "-" + part2 + "-" + part3;
						String newPO = part2 + "-" + part3;
						//System.out.println("** NewPO=" + newPO + "--              PO=" + poNumber + "--");
						poNumber = newPO;
						
					} else {
						// System.out.println("** PO=" + poNumber + "--");
						
						
						
					}					
				} else {
					//System.out.println("** PO=" + poNumber + "--");
					if (poNumber.startsWith("101") || poNumber.startsWith("102")) {
						if (poNumber.length() < 15) {
							System.out.println("** POError->to Few Chars=" + poNumber + "--");
						} else {
							poNumber = poNumber.substring(4, 15);
						} 				 
					}			
				}
				
				double value = result.getFloat("Amount");
				//System.out.println("*****VALUE=" + value + "--");
				
				String amt = decimalfmt(value,  "###,##0.00");
				//System.out.println("*****AMT=" + amt + "--");
				amount = Olyutil.strToDouble(amt);
				//double amount = result.getFloat("Amount");
				//if (poNumber.equals("101 0017464-001")) {
					//System.out.println("*****PO" + poNumber + ", Amount:" + amount);
				//}
			 
			 
				
				 //System.out.println("PO" + poNumber + ", Amount:" + amount);
				if (map.containsKey(poNumber)) {
					double newAmount = 0.00;
					newAmount = map.get(poNumber) + amount;
					//if (poNumber.equals("101 0017464-001")) {
						//System.out.println("***** Updating PO" + poNumber + ", Amount:" + amount + ", NewAmount:" + newAmount );
					//}
					//System.out.println("***** Updating PO" + poNumber + ", Amount:" + amount + ", NewAmount:" + newAmount );
					map.replace(poNumber, newAmount);
				} else { // key not in Hash
					//System.out.println("***** Adding New PO" + poNumber + ", Amount:" + amount);
					map.put(poNumber, amount);
				 
				}
				 
				
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	 
		return(map);
	}
	
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
