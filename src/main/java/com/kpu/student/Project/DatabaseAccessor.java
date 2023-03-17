package com.kpu.student.Project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseAccessor {

	public static String retrievePasswordHash(String email) {
		Connection c = DatabaseAccessor.connect();
		try {
			PreparedStatement getPW = c.prepareStatement(
					"GET password FROM Account WHERE ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "password";
	}
	public static Account retrieveAccountInfo(String email) {
		Account retrievedAccount = new Account("", "");
		
		return retrievedAccount;
	}
	public static ConfidentialRecord retrieveRecord (int recordID) {
		ConfidentialRecord retrievedRecord = new ConfidentialRecord(0, null, null);
		return retrievedRecord;
	}
	public static void addAccount (Account newAccount) {
		
	}
	public static void addRecord(ConfidentialRecord newRecord) {
		
	}
	
	public static Connection connect() {

		Connection conn = null;
		try {
		    conn =
		       DriverManager.getConnection("jdbc:mysql://localhost/INFO2413DB?" +
		                                   "user=accessor&password=DB_Accessor");
		    //Create connection with database to return - Liam
		
		} catch (SQLException ex) {
			//Not sure how to handle these, maybe a log file?
			//We definitely can't just print them to console - Liam
		    //System.out.println("SQLException: " + ex.getMessage());
		    //System.out.println("SQLState: " + ex.getSQLState());
		    //System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
}
