package com.olympus.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/*
add to buildPath:

In case you don�t use Maven, you have to download UCanAccess distribution and add the following JAR files to the classpath:
ucanaccess-4.0.4.jar
hsqldb-2.3.1.jar
jackcess-2.1.11.jar
commons-lang-2.6.jar
commons-logging-1.1.3.jar
slf4j-simple-1.7.25.jar
slf4j-api-1.7.2.jar
*/




public class AccessDBSearchResults {
	private static String dbName = "Commencement.accdb";
	private static String dbPath = "//D://Kettle//accessDB//";
	private static String dbPathName = dbPath + dbName;
	 
	/**
	 * This program demonstrates how to use UCanAccess JDBC driver to read/write
	 * a Microsoft Access database.
	 * @author www.codejava.net
	 *
	 */
	 
	    public static void main(String[] args) {
	     
	    	String sqlQuery = "SELECT * FROM Searchresults";
	       //String databaseURL = "jdbc:ucanaccess://e://Java//JavaSE//MsAccess//Contacts.accdb";
	    	
	    	String databaseURL = "jdbc:ucanaccess:" + dbPathName;
	    	System.out.println("** URL=" + databaseURL);
	        try (Connection connection = DriverManager.getConnection(databaseURL)) {
	             
	         
	             
	            Statement statement = connection.createStatement();
	            ResultSet result = statement.executeQuery(sqlQuery);
	             
	            while (result.next()) {
	                int id = result.getInt("ID");
	                //String internalID = result.getString("Internal ID");
	                
	                int internalID = result.getInt("Internal ID");
	                String poNumber = result.getString("PO Number");
	                 if (poNumber.length() != 15) {
	                	 System.out.println("ID:" + id + ", Internal ID:" + internalID  + ", PO" + poNumber);
	                 }
	            }
	             
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
