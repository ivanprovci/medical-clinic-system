package com.kpu.student.Project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


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
			c.close();
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
			
			//Same as above, but for DoctorAccount
			PreparedStatement checkIfDoctor = c.prepareStatement(
					"SELECT * FROM DoctorAccount WHERE email = ?");
			checkIfDoctor.setString(1, email);
			ResultSet doctorResults = checkIfDoctor.executeQuery();
			
			//Same as above, but for PatientAccount
			PreparedStatement checkIfPatient = c.prepareStatement(
					"SELECT * FROM PatientAccount WHERE email = ?");
			checkIfPatient.setString(1, email);
			ResultSet patientResults = checkIfPatient.executeQuery();
			
			if (staffResults.getString("email") != null) {
				//Check to see if provided email is in StaffAccount table - in other words, is this a staff account?
				//If it is, we'll return a StaffAccount object
				retrievedAccount = new StaffAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));				
				
			} else if (doctorResults.getString("email") != null) {
				//Same as above, but for DoctorAccount
				retrievedAccount = new DoctorAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));
				((DoctorAccount) retrievedAccount).setProfile(doctorResults.getString("profile"));
				
			} else if (patientResults.getString("email") != null) {
				//Same as above, but for PatientAccount
				retrievedAccount = new PatientAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));
				((PatientAccount) retrievedAccount).setAddress(patientResults.getString("address"));
				((PatientAccount) retrievedAccount).setHealthNo(patientResults.getString("healthNo"));
				if (patientResults.getString("verifyingStaffMember") != null) {
					//If the patient has a staff member that verified them
					//Set verified to true
					((PatientAccount) retrievedAccount).setVerified(true);
					//Get the email of the staff member who verified them
					((PatientAccount) retrievedAccount).setVerifiedBy(patientResults.getString("verifyingStaffMember"));
				} else {
					//If the patient is unverified
					((PatientAccount) retrievedAccount).setVerified(false);
					((PatientAccount) retrievedAccount).setVerifiedBy(null);
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
		
		//Close connection
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retrievedAccount;
	}
	
	public static ConfidentialRecord retrieveRecord (int recordID) {
		ConfidentialRecord retrievedRecord = new ConfidentialRecord(0, "", "");
		Connection c = DatabaseAccessor.connect();

		try {
			PreparedStatement checkIfVisit = c.prepareStatement(
					"SELECT * FROM VisitRecord WHERE recordID = ?");
			checkIfVisit.setInt(1, recordID);
			ResultSet visitResults = checkIfVisit.executeQuery();
			
			PreparedStatement checkIfPrescription = c.prepareStatement(
					"SELECT * FROM DoctorAccount WHERE email = ?");
			checkIfPrescription.setInt(1, recordID);
			ResultSet prescriptionResults = checkIfPrescription.executeQuery();
			
			PreparedStatement checkIfExam = c.prepareStatement(
					"SELECT * FROM PatientAccount WHERE email = ?");
			checkIfExam.setInt(1, recordID);
			ResultSet examResults = checkIfExam.executeQuery();
			
			PreparedStatement checkIfExamData = c.prepareStatement(
					"SELECT * FROM PatientAccount WHERE email = ?");
			checkIfExamData.setInt(1, recordID);
			ResultSet examDataResults = checkIfExamData.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Close connection
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retrievedRecord;
	}
	
	public static void addAccount (Account newAccount) {
		
		Connection c = DatabaseAccessor.connect();
		
		//Add row in Account table
		PreparedStatement addToAccount, addToPatient, addToDoctor, addToStaff;
		try {
			addToAccount = c.prepareStatement(
					"INSERT INTO Account (email, passwordHash, firstName, lastName, phoneNo) "
					+ "VALUES (?, ?, ?, ?, ?)");
			addToAccount.setString(1, newAccount.getEmail());
			addToAccount.setString(2, newAccount.getPassword());
			addToAccount.setString(3, newAccount.getFirstName());
			addToAccount.setString(4, newAccount.getLastName());
			addToAccount.setString(5, newAccount.getPhoneNo());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (newAccount instanceof PatientAccount) {
			//If patient, add row in PatientAccount table
			try {
				addToPatient = c.prepareStatement(
						"INSERT INTO PatientAccount (email, passwordHash, firstName, lastName, phoneNo) "
						+ "VALUES (?, ?, ?, ?, ?)");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (newAccount instanceof DoctorAccount) {
			try {
				addToDoctor = c.prepareStatement(
						"INSERT INTO DoctorAccount (email, passwordHash, firstName, lastName, phoneNo) "
						+ "VALUES (?, ?, ?, ?, ?)");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (newAccount instanceof StaffAccount) {
			try {
				addToStaff = c.prepareStatement(
						"INSERT INTO StaffAccount (email) "
						+ "VALUES (?)");
				addToStaff.setString(1, newAccount.getEmail());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//Close connection
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void addRecord(ConfidentialRecord newRecord) {
		
		Connection c = DatabaseAccessor.connect();
		Timestamp t = new Timestamp(0);
		
		//Add row in ConfidentialRecord table
		if (newRecord instanceof VisitRecord) {
			//If visit record, add row in VisitRecord table
		} else if (newRecord instanceof Prescription) {
			
		} else if (newRecord instanceof LabExam) {
			
		} else if (newRecord instanceof LabExamResult) {
			
		}
		
		//Close connection
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Connection connect() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection conn = null;
		try {
		    conn =
		       DriverManager.getConnection("jdbc:mysql://localhost/INFO2413DB?" +
		                                   "user=accessor&password=DB_Accessor");
		    //Create connection with database - Liam
		
		} catch (SQLException ex) {
			//Not sure how to handle these, maybe a log file?
			//We definitely can't just print them to console - Liam
			ex.printStackTrace();
		}
		return conn;
	}
}
