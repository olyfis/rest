package com.olympus.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

 
import com.olympus.olyutil.Olyutil;

	@WebServlet("/testsql2")
	public class TestSql2 extends HttpServlet {
	
		static Statement stmt = null;
		static Connection con = null;
		static ResultSet res  = null;
		static NodeList  node  = null;
		static String s = null;
		static private PreparedStatement statement;
		//static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
		static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";		
		
		static String hdrFile = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR_V2.txt";
		
		static String hdrFile2 = "C:\\Java_Dev\\props\\headers\\Qlik_SalesSupport_HDR_SAP.txt";
		
		static String sqlFile = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_V3.sql";
		static String sqlFile2 = "C:\\Java_Dev\\props\\sql\\qlikrest\\QlikSalesSupport_SAP_V3.sql";
		
		/*****************************************************************************************************************************************************/
		public static ArrayList<String> getDbData(String id, String sqlQueryFile, String booked, String qType) throws IOException {
			FileInputStream fis = null;
			FileReader fr = null;
			String s = new String();
			String sep = "";
	        StringBuffer sb = new StringBuffer();
	        ArrayList<String> strArr = new ArrayList<String>();
			try {
				fis = new FileInputStream(propFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Properties connectionProps = new Properties();
			connectionProps.load(fis);
			fr = new FileReader(new File(sqlQueryFile));	
			// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
			BufferedReader br = new BufferedReader(fr);
			while((s = br.readLine()) != null){
			      sb.append(s);       
			}
			br.close();
			//displayProps(connectionProps);
			String query = new String();
			query = sb.toString();	
			System.out.println( query);	
			System.out.println("*** idVal=" + id + "--");
			try {
				con = Olyutil.getConnection(connectionProps);
				if (con != null) {
					//System.out.println("Connected to the database");
					statement = con.prepareStatement(query);
					//System.out.println("***^^^*** contractID=" + contractID);
					if (Olyutil.isNullStr(id)) {
						//statement.setString(1, id);
					} else {
						
					}
					
					
			
					sep = ";";	 
					res = Olyutil.getResultSetPS(statement);		 	 
					strArr = Olyutil.resultSetArray(res, sep);			
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
		/*****************************************************************************************************************************************************/
		public static void displayData(ArrayList<String> strArr, int size, ArrayList<String> hdrArr) {
			
			int k = 0;
			for (String str : strArr) { // iterating ArrayList
 	 			
 				//System.out.println("**** Str=" + str);
 				String[] items = str.split(";");
 				int sz = items.length;
 				
 				//for (int i = 0; i < sz; i++) {
 				for (int i = 0; i <= size; i++) {
					System.out.println("****Row="  + k + "--  i=" + i + "-- " + hdrArr.get(i)  + "=" + items[i] + "-- SZ=" + sz);
 					 
 						//System.out.println(k + ";" + i + ";" + hdrArr.get(i)  + ";" + items[i] );
 					 
				}
				k++;
			}
			
		}
		
		/*****************************************************************************************************************************************************/

		
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//String idVal = "001-0000090-001";
			String idVal = "";
			ArrayList<String>strArr = new ArrayList<String>();
			ArrayList<String>strArr2 = new ArrayList<String>();
			Date date = Olyutil.getCurrentDate();
			String dateStamp = date.toString();
			ArrayList<String> hdrArr = new ArrayList<String>();
			ArrayList<String> hdrArr2 = new ArrayList<String>();
			hdrArr = Olyutil.readInputFile(hdrFile);
			hdrArr2 = Olyutil.readInputFile(hdrFile2);
			String dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
			System.out.println("*** Begin load:" + dateFmt);
			
			strArr = getDbData(idVal, sqlFile, "", "Asset");
			strArr2 = getDbData(idVal, sqlFile2, "", "Asset");
			String dateFmt2 = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
			
			
			System.out.println("*** End load:" + dateFmt2);
			
			int arrSZ = strArr.size();
			if (arrSZ > 0) {
				//Olyutil.printStrArray(strArr);			
				displayData(strArr2, 3, hdrArr2);
				
				
			} else {
				System.out.println("*** RTN Arr SZ=" + arrSZ + "--");
			}
			 
			
		}
		
}

