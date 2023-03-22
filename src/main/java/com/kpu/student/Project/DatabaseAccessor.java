package com.kpu.student.Project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseAccessor {

	public static String retrievePasswordHash(String email) {
		//Gets the password hash associated with a certain email
		//Returns an empty string if there is no account associated with the email - Liam
		String pwHash = "";
		Connection c = DatabaseAccessor.connect();
		try {
			PreparedStatement getPW = c.prepareStatement(
					"SELECT passwordHash FROM Account WHERE email = ?");
			getPW.setString(1, email);
			ResultSet results = getPW.executeQuery();
			pwHash = results.getString("passwordHash");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pwHash;
	}
	
	public static Account retrieveAccountInfo(String email) {
		//Returns an Account object with all associated info
		//If the requested account is a patient account, it is returned as a PatientAccount
		//Likewise for doctor and staff accounts - Liam
		Account retrievedAccount = new Account("", "");
		Connection c = DatabaseAccessor.connect();
		try {
			//Execute MySQL statement to see if the provided email shows up in StaffAccount table
			//If it does, we know this account is a staff account
			PreparedStatement checkIfStaff = c.prepareStatement(
					"SELECT * FROM StaffAccount WHERE email = ?");
			checkIfStaff.setString(1, email);
			ResultSet staffResults = checkIfStaff.executeQuery();
			
			PreparedStatement checkIfDoctor = c.prepareStatement(
					"SELECT * FROM DoctorAccount WHERE email = ?");
			checkIfDoctor.setString(1, email);
			ResultSet doctorResults = checkIfDoctor.executeQuery();
			
			PreparedStatement checkIfPatient = c.prepareStatement(
					"SELECT * FROM PatientAccount WHERE email = ?");
			checkIfPatient.setString(1, email);
			ResultSet patientResults = checkIfPatient.executeQuery();
			
			if (staffResults.getString(1) != null) {
				//Check to see if provided email is in StaffAccount table - in other words, is this a staff account?
				//If it is, we'll return a StaffAccount object
				retrievedAccount = new StaffAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));				
				
			} else if (doctorResults.getString(1) != null) {
				retrievedAccount = new DoctorAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));
				((DoctorAccount) retrievedAccount).setProfile(doctorResults.getString("profile"));
				
			} else if (patientResults.getString(1) != null) {
				retrievedAccount = new PatientAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));
				((PatientAccount) retrievedAccount).setAddress(patientResults.getString("address"));
				((PatientAccount) retrievedAccount).setHealthNo(patientResults.getString("healthNo"));
				if (patientResults.getString("verifyingStaffMember") != null) {
					String verifyingStaffEmail = patientResults.getString("verifyingStaffMember");
					Account verifyingStaff = retrieveAccountInfo(verifyingStaffEmail);
					if (verifyingStaff instanceof StaffAccount) {
						((StaffAccount) verifyingStaff).verifyPatient((PatientAccount) retrievedAccount);
					}
				}
				
			}
			
			//Get information common to all account types - Liam
			PreparedStatement generalAccountInfo = c.prepareStatement(
					"SELECT * FROM Account WHERE email = ?");
			checkIfPatient.setString(1, email);
			ResultSet accountResults = generalAccountInfo.executeQuery();
			retrievedAccount.setFirstName(accountResults.getString("firstName"));
			retrievedAccount.setLastName(accountResults.getString("lastName"));
			retrievedAccount.setPhoneNo(accountResults.getString("phoneNo"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retrievedAccount;
	}
	
	public static ConfidentialRecord retrieveRecord (int recordID) {
		ConfidentialRecord retrievedRecord = new ConfidentialRecord(0, "", "");
		Connection c = DatabaseAccessor.connect();
		
		return retrievedRecord;
	}
	
	public static void addAccount (Account newAccount) {
		
		//Add row in Account table
		if (newAccount instanceof PatientAccount) {
			//If patient, add row in PatientAccount table
		} else if (newAccount instanceof DoctorAccount) {
			
		} else if (newAccount instanceof StaffAccount) {
			
		}
		
	}
	public static void addRecord(ConfidentialRecord newRecord) {
		
		//Add row in ConfidentialRecord table
		if (newRecord instanceof VisitRecord) {
			//If visit record, add row in VisitRecord table
		} else if (newRecord instanceof Prescription) {
			
		} else if (newRecord instanceof LabExam) {
			
		} else if (newRecord instanceof LabExamResult) {
			
		}
		
	}
	
	public static Connection connect() {

		Connection conn = null;
		try {
		    conn =
		       DriverManager.getConnection("jdbc:mysql://localhost/INFO2413DB?" +
		                                   "user=accessor&password=DB_Accessor");
		    //Create connection with database - Liam
		
		} catch (SQLException ex) {
			//Not sure how to handle these, maybe a log file?
			//We definitely can't just print them to console - Liam
			
		    //System.out.println("SQLException: " + ex.getMessage());
		    //System.out.println("SQLState: " + ex.getSQLState());
		    //System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
	
	public static void main(String[] args) { 
		
	}
}
