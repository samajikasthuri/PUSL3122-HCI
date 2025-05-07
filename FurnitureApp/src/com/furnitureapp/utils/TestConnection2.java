package com.furnitureapp.utils;
import java.sql.Connection;

public class TestConnection2 {

	public static void main(String[] args) {
		 System.out.println("Attempting to connect to MySQL...");
	        
	        try (Connection conn = DBUtil.getConnection()) {
	            System.out.println("✅ Connection successful!");
	            System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
	            System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());
	        } catch (Exception e) {
	            System.err.println("❌ Connection failed: " + e.getMessage());
	            e.printStackTrace();
	        }

	}

}
