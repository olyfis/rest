package com.olympus.rest.aging; 

 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.olympus.olyutil.Olyutil;

//RUN: http://localhost:8181/qlik/agingdetail
@WebServlet("/agingdetails")
public class AgingDetail extends HttpServlet {
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static String s = null;
	static private PreparedStatement statement;
	static String ILpropFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\evergreenChk.sql";

	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(String propFile, String params, String sqlSrc) throws IOException {
		FileInputStream fis = null;
		FileReader fr = null;
		String s = new String();
		StringBuffer sb = new StringBuffer();
		ArrayList<String> strArr = new ArrayList<String>();
		try {
			fis = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		connectionProps.load(fis);
		fr = new FileReader(new File(sqlSrc));
		// be sure to not have line starting with "--" or "/*" or any other non
		// alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		br.close();
		// displayProps(connectionProps);
		String query = new String();
		query = sb.toString();
		// System.out.println(query);
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				// System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
				//statement.clearParameters();
				res = Olyutil.getResultSetPS(statement);
				strArr = Olyutil.resultSetArray(res, ";");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return strArr;
	}
	/****************************************************************************************************************************************************/
	public static Map<String, String> setContractHashEG(ArrayList<String> dataArr) {
		Map<String, String> myMap = new HashMap<String, String>();
		String key = "";
		String value = "";
		//int i = 0;
		for (String str : dataArr) {
			//System.out.println("**** i=" + i + "--  STR="  + str);
			String[] items = str.split(";");
			key = items[0];
			value = items[1];
			myMap.put(key, value);
			//i++;
		}
		return myMap;
	}
	/****************************************************************************************************************************************************/
	static public JsonArray buildJSON(ArrayList<String> strArr, Map<String, String> evergreenMap, ArrayList<String> hdrArr, String sep) {
		JsonArray jsonArr = new JsonArray();
		//Olyutil.printStrArray(strArr);
		int k = 0;
		for (String str : strArr) { // iterating ArrayList
			JsonObject obj = new JsonObject();
			//System.out.println("Line: " + k +  " -- DATA:" + str + "---");
			String[] items = str.split(sep);
			
			String key = items[0];
			/*
			if (evergreenMap.containsKey(key)) {
				
				String val = evergreenMap.get(key);
				//System.out.println("ID=" + items[0]  + " -- EG="  + val    +  "--");
			}
			*/
			int j = 0;

			// System.out.println("**** items=" + items.length + "-- Str=" + str);

			for (int i = 0; i < items.length; i++) {
				k = i;
				//System.out.println("**** i=" + i + "--  " + hdrArr.get(i).trim() + "-->" + items[i]  + "--  SZ=" + items.length);
				obj.addProperty(hdrArr.get(i).trim(), items[i]);
				// newStrArr.add(items[i]);
				// System.out.println(hdrArr.get(i).trim() + "-->" + items[i]);
			}
			if (items.length == 8) {

				obj.addProperty("Comments", "N/A");

			}
			
			
    		if (evergreenMap.containsKey(key)) {
				/*int n = items.length;
				if (n > 9) {
    				continue;
    			}
    			*/
    			String val = evergreenMap.get(key);
    			obj.addProperty(hdrArr.get(9).trim(), val);
    			//System.out.println("ID=" + items[0]  + " -- EG="  + val    +  "-- HDR=" + hdrArr.get(n).trim() + "-- n=" + n);
    		}
    		
    		jsonArr.add(obj);
    		k++;
    		/*
    		if (k > 0) {
    			return;
    		}
    		*/
		}
		
		return(jsonArr);
	}
	/****************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> evergreenMap = new HashMap<String, String>();
		PrintWriter out = response.getWriter();
		ArrayList<String> uasArr = new ArrayList<String>();
		ArrayList<String> agingArr = new ArrayList<String>();
		ArrayList<String> evergreenArr = new ArrayList<String>();
		String ageFile = "D:\\Kettle\\Production\\unappsuspense\\csvfiles\\dailyAge.csv";
		ArrayList<String> headerArr = new ArrayList<String>();
		JsonArray jsonArr = new JsonArray();
		String Jsonfile = "C:\\Java_Dev\\JSON\\unappsuspense\\dailyAging.json";
		String headerFile = "C:\\Java_Dev\\props\\headers\\unappsuspense\\agingHrd.txt";
		
		evergreenArr = getDbData(ILpropFile, "", sqlFile);
		evergreenMap = setContractHashEG(evergreenArr);
		//Olyutil.printStrArray(evergreenArr);
		
		headerArr = Olyutil.readInputFile(headerFile);
		agingArr = Olyutil.readInputFile(ageFile);
		//Olyutil.printStrArray(agingArr, "agingArr: ");
		
		//Olyutil.printStrArray(agingArr, "");
		jsonArr = buildJSON(agingArr, evergreenMap, headerArr, ";");

		Olyutil.jsonWriterResponse(jsonArr, out);
		Olyutil.jsonWriter(jsonArr, Jsonfile);
		
	}
	
	/****************************************************************************************************************************************************/
	
}



