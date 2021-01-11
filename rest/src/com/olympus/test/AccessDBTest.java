package com.olympus.test;

import java.sql.*;
/*
add to buildPath:

In case you don’t use Maven, you have to download UCanAccess distribution and add the following JAR files to the classpath:
ucanaccess-4.0.4.jar
hsqldb-2.3.1.jar
jackcess-2.1.11.jar
commons-lang-2.6.jar
commons-logging-1.1.3.jar
slf4j-simple-1.7.25.jar
slf4j-api-1.7.2.jar
*/

public class AccessDBTest {
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
	    	//String sqlQuery = "SELECT * FROM Comments";
	    	String sqlQuery = "SELECT * FROM Searchresults";
	       //String databaseURL = "jdbc:ucanaccess://e://Java//JavaSE//MsAccess//Contacts.accdb";
	    	
	    	String databaseURL = "jdbc:ucanaccess:" + dbPathName;
	    	System.out.println("** URL=" + databaseURL);
	        try (Connection connection = DriverManager.getConnection(databaseURL)) {
	             
	         
	             
	            Statement statement = connection.createStatement();
	            ResultSet result = statement.executeQuery(sqlQuery);
	             
	            while (result.next()) {
	                int id = result.getInt("Credit App");
	                String comment = result.getString("Comment");
	                
	                 
	                System.out.println(id + ", " + comment );
	            }
	             
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
