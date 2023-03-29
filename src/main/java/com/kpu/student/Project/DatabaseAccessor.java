package com.kpu.student.Project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;


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
			if (results.isBeforeFirst()) {
				results.first();
				pwHash = results.getString("passwordHash");
			}
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
			
			if (staffResults.isBeforeFirst()) {
				//Check to see if provided email is in StaffAccount table - in other words, is this a staff account?
				//If it is, we'll return a StaffAccount object
				retrievedAccount = new StaffAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));		
				
			} else if (doctorResults.isBeforeFirst()) {
				//Same as above, but for DoctorAccount
				retrievedAccount = new DoctorAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));
				doctorResults.first();
				
				((DoctorAccount) retrievedAccount).setProfile(doctorResults.getString("profile"));
				
			} else if (patientResults.isBeforeFirst()) {
				//Same as above, but for PatientAccount
				retrievedAccount = new PatientAccount(email, 
						DatabaseAccessor.retrievePasswordHash(email));
				patientResults.first();
				
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
					//Set verified to false
					((PatientAccount) retrievedAccount).setVerified(false);
					//Nobody verified them, so set verifiedBy to null
					((PatientAccount) retrievedAccount).setVerifiedBy(null);
				}
				
			}
			
			//Get information common to all account types - Liam
			PreparedStatement generalAccountInfo = c.prepareStatement(
					"SELECT * FROM Account WHERE email = ?");
			checkIfPatient.setString(1, email);
			ResultSet accountResults = generalAccountInfo.executeQuery();
			
			if (accountResults.isBeforeFirst()) {
				//Ensure the account actually exists first, just in case
				accountResults.first();
				retrievedAccount.setFirstName(accountResults.getString("firstName"));
				retrievedAccount.setLastName(accountResults.getString("lastName"));
				retrievedAccount.setPhoneNo(accountResults.getString("phoneNo"));
			} else {
				
			}
			
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
					"SELECT * FROM Prescription WHERE recordID = ?");
			checkIfPrescription.setInt(1, recordID);
			ResultSet prescriptionResults = checkIfPrescription.executeQuery();
			
			PreparedStatement checkIfExam = c.prepareStatement(
					"SELECT * FROM LabExam WHERE recordID = ?");
			checkIfExam.setInt(1, recordID);
			ResultSet examResults = checkIfExam.executeQuery();
			
			PreparedStatement checkIfExamData = c.prepareStatement(
					"SELECT * FROM LabExamResult WHERE recordID = ?");
			checkIfExamData.setInt(1, recordID);
			ResultSet examDataResults = checkIfExamData.executeQuery();
			
			PreparedStatement genericRecordData = c.prepareStatement(
					"SELECT * FROM ConfidentialRecord WHERE recordID = ?");
			checkIfExamData.setInt(1, recordID);
			ResultSet recordResults = genericRecordData.executeQuery();
			
			if (recordResults.isBeforeFirst()) {
				
				if (visitResults.isBeforeFirst()) {
					retrievedRecord = new VisitRecord(recordID);
					visitResults.first();
					((VisitRecord) retrievedRecord).setVisitDate(visitResults.getDate("date"));		
					
				} else if (prescriptionResults.isBeforeFirst()) {
					retrievedRecord = new Prescription(recordID);
					prescriptionResults.first();
					
					int relatedVisit = prescriptionResults.getInt("relatedVisitRecord");
					((Prescription) retrievedRecord).setDatePrescribed(
							((VisitRecord) DatabaseAccessor.retrieveRecord(relatedVisit)).getVisitDate());
					
					((Prescription) retrievedRecord).setMedicineDose(prescriptionResults.getString("medDose"));
					((Prescription) retrievedRecord).setMedicineName(prescriptionResults.getString("medName"));
					((Prescription) retrievedRecord).setMedicineQuantity(prescriptionResults.getString("medQuantity"));
					((Prescription) retrievedRecord).setRefillable(prescriptionResults.getBoolean("refillable"));
					
				} else if (examResults.isBeforeFirst()) {
					retrievedRecord = new LabExam(recordID);
					examResults.first();
					
					((LabExam) retrievedRecord).setExamItem(prescriptionResults.getString("examItem"));
					((LabExam) retrievedRecord).setExamDate(prescriptionResults.getDate("date"));
					
				} else if (examDataResults.isBeforeFirst()) {
					retrievedRecord = new LabExamResult(recordID);
					examDataResults.first();
					
					int upper = examDataResults.getInt("upperBound");
					int lower = examDataResults.getInt("lowerBound");
					int result = examDataResults.getInt("result");
					
					((LabExamResult) retrievedRecord).setRelatedExam(examDataResults.getInt("relatedLabExam"));
					((LabExamResult) retrievedRecord).setNormalUpperBound(upper);
					((LabExamResult) retrievedRecord).setNormalLowerBound(lower);
					((LabExamResult) retrievedRecord).setResult(result);
					((LabExamResult) retrievedRecord).setNormalResult(
							(lower <= result) && (result <= upper));
				}
				recordResults.first();
				retrievedRecord.setPrescribingDoctor(recordResults.getString("relatedDoctor"));
				retrievedRecord.setRelatedPatient(recordResults.getString("relatedPatient"));
				
			} else {
				
			}
			
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
			addToAccount.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (newAccount instanceof PatientAccount) {
			//If patient, add row in PatientAccount table
			try {
				addToPatient = c.prepareStatement(
						"INSERT INTO PatientAccount (email, verifyingStaffMember, address, healthNo) "
						+ "VALUES (?, ?, ?, ?)");
				addToPatient.setString(1, ((PatientAccount) newAccount).getEmail());
				if (((PatientAccount) newAccount).isVerified()) {
					addToPatient.setString(2, ((PatientAccount) newAccount).getVerifiedBy());
				} else {
					addToPatient.setNull(2, Types.VARCHAR);
				}
				addToPatient.setString(3, ((PatientAccount) newAccount).getAddress());
				addToPatient.setString(4, ((PatientAccount) newAccount).getHealthNo());
				addToPatient.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (newAccount instanceof DoctorAccount) {
			try {
				addToDoctor = c.prepareStatement(
						"INSERT INTO DoctorAccount (email, profile) "
						+ "VALUES (?, ?)");
				addToDoctor.setString(1, ((DoctorAccount) newAccount).getEmail());
				addToDoctor.setString(2, ((DoctorAccount) newAccount).getProfile());
				addToDoctor.executeUpdate();
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
				addToStaff.executeUpdate();
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
		Timestamp t = new Timestamp(Instant.now().toEpochMilli());
		PreparedStatement addToRecord, addToVisit, addToPrescription, addToExam, addToExamResult;
		
		//Add row in ConfidentialRecord table
		try {
			addToRecord = c.prepareStatement(
					"INSERT INTO ConfidentialRecord (relatedPatient, relatedDoctor, createdTimeStamp) "
					+ "VALUES (?, ?, ?)");
			addToRecord.setString(1, newRecord.getPrescribingDoctor());
			addToRecord.setString(2, newRecord.getRelatedPatient());
			addToRecord.setTimestamp(3, t);
			addToRecord.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
